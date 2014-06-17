package com.gis.chore;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import com.gis.core.Constants;
import com.gis.core.GIS;
import com.gis.entity.AbstractEntity;
import com.gis.logic.LogicHelper;
import com.gis.logic.Point3D;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;


/**
 * The mining chore handles mining tunnels and searching for ores.
 */
public class ChoreMining extends AbstractChore {
	/** Is the chore in passive mode?*/
	public boolean inPassiveMode;

	/** Does the owner have coordinates they should be moving to? (Active only)*/
	public boolean hasNextPath;

	/** The X coordinates that the active mining chore stated at.*/
	public double startX;

	/** The Y coordinates that the active mining chore stated at.*/
	public double startY;

	/** The Z coordinates that the active mining chore stated at.*/
	public double startZ;

	/** The X coordinates that the owner should be moving to. (Active only)*/
	public double nextX;

	/** The Y coordinates that the owner should be moving to. (Active only)*/
	public double nextY;

	/** The Z coordinates that the owner should be moving to. (Active only)*/
	public double nextZ;

	/** The X coordinates of the nearest valid block. (Passive only)*/
	public int nearestX;

	/** The Y coordinates of the nearest valid block. (Passive only)*/
	public int nearestY;

	/** The Z coordinates of the nearest valid block. (Passive only)*/
	public int nearestZ;

	/** The amount of time it takes for a block to be broken when mining.*/
	public int delayInterval;

	/** The amount of time the owner has been swinging the pick.*/
	public int delayCounter;

	/** The amount of time that needs to pass for the owner to notify the player that ore is nearby.*/
	public int notifyInterval;

	/** The amount of time that has passed since the player was notified of nearby ore.*/
	public int notifyCounter;

	/** The distance from the owner's current point to the ore they have found.*/
	public int distanceToOre;

	/** The direction the owner is facing. (Active only)*/
	public int heading;

	/** How far from the start position the owner will continue active mining.*/
	public int maxDistance;

	/** The index of the chore entry being used. */
	public int entryIndex;

	/** The ID of the block that a passive miner is looking for.*/
	public transient Block searchBlock;

	/**
	 * Constructor
	 * 
	 * @param 	entity	The entity performing the chore.
	 */
	public ChoreMining(AbstractEntity entity)
	{
		super(entity);
	}

	/**
	 * Constructor
	 * 
	 * @param 	entity		The entity that should be performing this chore.
	 * @param 	mode		0 = passive mode, 1 = active mode.
	 * @param	block		The block this chore should be trying to mine
	 * @param	entryIndex	The index of the chore entry being used (???)
	 * @param 	direction	The direction the entity should mine in. 0 = forward, 1 = backward, 2 = left, 3 = right/
	 * @param 	oreType		(Passive only) The type of ore that should be searched for.
	 * @param 	distance	(Active only) The distance that the entity should mine.
	 */
	public ChoreMining(AbstractEntity entity, int mode, Block block, int entryIndex, int direction, int distance) {
		super(entity);
		this.entryIndex = entryIndex;

		this.inPassiveMode = mode == 0 ? true : false;
		this.searchBlock = block;
		this.maxDistance = distance;
		this.heading = direction;
		//this.heading = LogicHelper.getHeadingRelativeToPlayerAndSpecifiedDirection(entity.worldObj.getPlayerEntityByName(entity.lastInteractingPlayer), direction);
		this.delayInterval = getDelayForToolType(entity.getTool());
		this.notifyInterval = Constants.SECOND * 10;
	}

	@Override
	public void beginChore() {
		//If the chore isn't in passive mode, set it to start at the owner position
		if (!inPassiveMode) {
			startX = owner.posX;
			startY = owner.posY;
			startZ = owner.posZ; 
			owner.isFollowing = false;
			owner.isStaying = false;
		}
		//Prep the owner for actions and flag the chore as starting
		owner.getNavigator().clearPathEntity();
		owner.tasks.taskEntries.clear();
		hasBegun = true;
	}

	@Override
	public void runChoreAI() {
		if (inPassiveMode) {
			runPassiveAI();
		}

		else {
			runActiveAI();
		}
	}

	@Override
	public String getChoreName() {
		return "Mining";
	}

	@Override
	public void endChore() {
		hasEnded = true;

		/*if (owner.worldObj.isRemote)
		{
			GIS.packetPipeline.sendPacketToServer(new Packet(EnumPacketType.AddAI, owner.getEntityId()));
		}

		else
		{
			GIS.packetPipeline.sendPacketToAllPlayers(new Packet(EnumPacketType.SetChore, owner.getEntityId(), this));
			GIS.packetPipeline.sendPacketToAllPlayers(new Packet(EnumPacketType.AddAI, owner.getEntityId()));
		}*/

		owner.addAI();
	}

	@Override
	public void writeChoreToNBT(NBTTagCompound nbt) {
		//Loop through each field in this class and write to NBT.
		for (final Field field : this.getClass().getFields()) {
			try {
				if (field.getModifiers() != Modifier.TRANSIENT) {
					if (field.getType().toString().contains("int")) {
						nbt.setInteger(field.getName(), Integer.parseInt(field.get(owner.miningChore).toString()));
					}

					else if (field.getType().toString().contains("double")) {
						nbt.setDouble(field.getName(), Double.parseDouble(field.get(owner.miningChore).toString()));
					}

					else if (field.getType().toString().contains("float")) {
						nbt.setFloat(field.getName(), Float.parseFloat(field.get(owner.miningChore).toString()));
					}

					else if (field.getType().toString().contains("String")) {
						nbt.setString(field.getName(), field.get(owner.miningChore).toString());
					}

					else if (field.getType().toString().contains("boolean")) {
						nbt.setBoolean(field.getName(), Boolean.parseBoolean(field.get(owner.miningChore).toString()));
					}
				}
			}

			catch (IllegalAccessException e) {
				//GIS.getInstance().getLogger().log(e);
				System.out.println("Exception in writing to NBT: "+e);
				continue;
			}
		}
	}

	@Override
	public void readChoreFromNBT(NBTTagCompound nbt) {
		//Loop through each field in this class and read from NBT.
		for (final Field field : this.getClass().getFields()) {
			try {
				if (field.getModifiers() != Modifier.TRANSIENT) {
					if (field.getType().toString().contains("int")) {
						field.set(owner.miningChore, nbt.getInteger(field.getName()));
					}

					else if (field.getType().toString().contains("double")) {
						field.set(owner.miningChore, nbt.getDouble(field.getName()));
					}

					else if (field.getType().toString().contains("float")) {
						field.set(owner.miningChore, nbt.getFloat(field.getName()));
					}

					else if (field.getType().toString().contains("String")) {
						field.set(owner.miningChore, nbt.getString(field.getName()));
					}

					else if (field.getType().toString().contains("boolean")) {
						field.set(owner.miningChore, nbt.getBoolean(field.getName()));
					}
				}
			}

			catch (IllegalAccessException e)
			{
				//GIS.getInstance().getLogger().log(e);
				System.out.println("Exception in reading from NBT: "+e);
				continue;
			}
		}
	}

	@Override
	protected int getDelayForToolType(Item tool) {
		if (owner.getTool() != null) {
			final ToolMaterial material = ToolMaterial.valueOf(((ItemPickaxe)tool).getToolMaterialName());
			int returnAmount = 0;

			switch (material) {
			case WOOD: 		returnAmount = 40; break;
			case STONE: 	returnAmount = 30; break;
			case IRON: 		returnAmount = 25; break;
			case EMERALD: 	returnAmount = 10; break;
			case GOLD: 		returnAmount = 5; break;
			default: 		returnAmount = 25; break;
			}
		}
		return 25;
	}

	/**
	 * Runs the passive mining AI.
	 */
	private void runPassiveAI()	{
		if (hasPick()) {
			if (notifyCounter >= notifyInterval) {
				final Point3D nearestBlock = getNearestBlockCoordinates();

				if (nearestBlock != null) {
					nearestX = nearestBlock.iPosX;
					nearestY = nearestBlock.iPosY;
					nearestZ = nearestBlock.iPosZ;

					distanceToOre = Math.round((float)LogicHelper.getDistanceToXYZ(owner.posX, owner.posY, owner.posZ, nearestX, nearestY, nearestZ));
					//doOreDistanceNotification();
				}
				notifyCounter = 0;
			}
			//Logic for finding a block is not ready to run
			else {
				notifyCounter++;
			}
		}
		//No longer carrying a pick
		else {
			endChore();
		}
	}

	/**
	 * Runs the active mining AI.
	 */
	private void runActiveAI() {
		doLookTowardsHeading();

		//Check if the coordinates for the next block to mine have been assigned.
		if (hasNextPath) {
			if (LogicHelper.getDistanceToXYZ(startX, startY, startZ, nextX, nextY, nextZ) > maxDistance) {
				endForFinished();
				return;
			}
			else {
				if (isNextBlockInvalid()) {
					endForNoBlocks();
					return;
				}
				//Close enough to block to hit it
				if (LogicHelper.getDistanceToXYZ(owner.posX, owner.posY, owner.posZ, nextX, nextY, nextZ) <= 2.5) {
					//If we're not finished mining the block, based on the delayInterval, swing the pick
					if (delayCounter < delayInterval) {
						owner.swingItem();
						delayCounter++;
					}
					//If we're finished mining it, do actions and reset delaycounter for next time
					else {
						final Block nextBlock = owner.worldObj.getBlock((int)nextX, (int)nextY, (int)nextZ);

						if (nextBlock != Blocks.air) {
							doHarvestBlock(nextBlock);
						}

						delayCounter = 0;
						hasNextPath = false;
					}
				}
				//Not within 2.5 blocks of target so not close enough to hit it
				else {
					doSetPathToNextBlock();
				}
			}
		}
		//No path, find one
		else {
			doSetNextPath();
		}
	}
	
	private Point3D getNearestBlockCoordinates() {
		final double lastDistance = 100D;
		Point3D nearestPoint = null;

		for (final Point3D point : LogicHelper.getNearbyBlocks_StartAtBottom(owner, searchBlock, 20)) {
			final double thisDistance = LogicHelper.getDistanceToXYZ(owner.posX, owner.posY, owner.posZ, point.dPosX, point.dPosY, point.dPosZ);

			if (thisDistance < lastDistance) {
				nearestPoint = point;
			}
		}

		return nearestPoint;
	}
	
	/*private boolean isNextBlockInvalid() 
	{
		final Block block = owner.worldObj.getBlock((int)nextX, (int)nextY, (int)nextZ);

		for (final Block invalidBlock : Constants.UNMINEABLE_BLOCKS)
		{
			if (block == invalidBlock)
			{
				return true;
			}
		}

		return false;
	}*/
	//Reversal of the invalid block method, to return true if the block is NOT one of our mineable blocks
	private boolean isNextBlockInvalid() {
		final Block block = owner.worldObj.getBlock((int)nextX, (int)nextY, (int)nextZ);

		for (final Block validBlock : Constants.MINEABLE_BLOCKS) {
			if (block == validBlock) {
				return false;
			}
		}

		return true;
	}

	private boolean hasPick() {
		return owner.getTool() != null;
	}

	//Unused in single player I think
	/*
	private void doOreDistanceNotification()
	{
		if (getChoreXp() < 20.0F)
		{
			if (distanceToOre > 5 && !owner.worldObj.isRemote)
			{
				owner.say(GIS.getInstance().getLanguageLoader().getString("notify.child.chore.status.mining.orefound", null, owner, false));
			}

			else if (distanceToOre <= 5 && !owner.worldObj.isRemote)
			{
				owner.say(GIS.getInstance().getLanguageLoader().getString("notify.child.chore.status.mining.orenearby", null, owner, false));
			}
		}

		else
		{
			if (!owner.worldObj.isRemote)
			{
				owner.say(GIS.getInstance().getLanguageLoader().getString("notify.child.chore.status.mining.oredistance", null, owner, false));
			}
		}
	}*/

	private void doLookTowardsHeading()	{
		//if (owner.worldObj.isRemote) { - not important since we're always singleplayer
			owner.setRotationYawHead(heading);
		//}
	}

	private void doSetNextPath() {
		int scanDistance = 0;

		while (scanDistance != maxDistance) {
			nextY = owner.posY;
			
			//Pick the next block to look at based on owner's heading
			switch (heading) {
				case 0:    nextX = owner.posX; nextZ = owner.posZ + scanDistance; break; 
				case 180:  nextX = owner.posX; nextZ = owner.posZ - scanDistance; break; 
				case -90:  nextX = owner.posX + scanDistance; nextZ = owner.posZ; break;
				case 90:   nextX = owner.posX - scanDistance; nextZ = owner.posZ; break;
				default: break;
			}
			//If the block we found or the block below it is air, try again, otherwise we're done
			if (owner.worldObj.getBlock((int)nextX, (int)nextY, (int)nextZ) == Blocks.air) {
				if (owner.worldObj.getBlock((int)nextX, (int)nextY + 1, (int)nextZ) == Blocks.air) {
					hasNextPath = false;
					scanDistance++;
				}

				else {
					nextY = owner.posY + 1;
					hasNextPath = true;
					break;
				}
			}

			else {
				hasNextPath = true;
				break;
			}
		}

		if (scanDistance == maxDistance) {
			endForNoBlocks();
		}
	}

	private void doHarvestBlock(Block nextBlock) {
		Block yieldBlock = nextBlock;
		int yieldMeta = 0; //Meta is used for special blocks like dyes, we should be fine with just 0, i.e. no meta
		Item yieldItem = nextBlock.getItemDropped(yieldMeta, null, 1);
		int yieldAmount = 1;
		
		//Set it so we only yield a block if we don't have an item to yield
		//Probably fine to do this instead of using the api??
		if (yieldItem != null) {
			yieldBlock = null;
		}
		
		
		
		/*for (MineableOre entry : ChoreRegistry.getMiningOreEntries()) {
			if (nextBlock == entry.getOreBlock()) {
				if (entry.getYieldsBlock()) {
					yieldBlock = entry.getOreBlockYield();
					yieldItem = null;
				}

				else {
					yieldBlock = null;
					yieldItem = entry.getOreItemYield();
				}

				//yieldAmount = LogicHelper.getNumberInRange(entry.getMinimumReturn(), entry.getMaximumReturn());
				break;
			}
		}*/

		try {
			ItemStack stackToAdd = null; 

			if (yieldBlock != null) {
				stackToAdd = new ItemStack(yieldBlock, yieldAmount, yieldMeta);
			}

			else {
				stackToAdd = new ItemStack(yieldItem, yieldAmount, yieldMeta);
			}

			//stackToAdd.damageItem(yieldMeta, owner);
			owner.addToInventory(stackToAdd);
		}

		catch (NullPointerException e) {
			//GIS.getInstance().getLogger().log("Unable to mine block at " + nextX + ", " + nextY + ", " + nextZ);
			System.out.println("Unable to mine block at " + nextX + ", " + nextY + ", " + nextZ);
		}

		owner.worldObj.setBlock((int)nextX, (int)nextY, (int)nextZ, Blocks.air);
	}

	private void doSetPathToNextBlock() {
		if (!owner.worldObj.isRemote && owner.getNavigator().noPath()) {
			owner.getNavigator().setPath(owner.getNavigator().getPathToXYZ((int)nextX, (int)nextY, (int)nextZ), Constants.SPEED_WALK);
		}
	}

	private void endForNoBlocks() {
		endChore();
	}

	private void endForFinished() {
		endChore();
	}
}
