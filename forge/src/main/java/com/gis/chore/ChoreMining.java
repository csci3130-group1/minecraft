package com.gis.chore;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.apache.logging.log4j.Level;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import com.gis.core.Constants;
import com.gis.entity.EntityWorker;
import com.gis.logic.LogicHelper;
import com.gis.logic.Point3D;

import cpw.mods.fml.common.FMLLog;


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
	
	/** The Point3D of the block*/
	private Point3D nextBlockPosition;

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
	public ChoreMining(EntityWorker entity)
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
	public ChoreMining(EntityWorker entity, int mode, Block block, int entryIndex, int direction, int distance) {
		super(entity);
		this.entryIndex = entryIndex;

		this.inPassiveMode = mode == 0 ? true : false;
		this.searchBlock = block;
		this.maxDistance = distance;
		this.heading = direction;
		//this.heading = LogicHelper.getHeadingRelativeToPlayerAndSpecifiedDirection(entity.worldObj.getPlayerEntityByName(entity.lastInteractingPlayer), direction);

		Point3D nearestBlock = getNearestBlockCoordinates();
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
		/*if (inPassiveMode) {
			runPassiveAI();
		}
		else {
			runActiveAI();
		}*/
		runActiveAI();
	}

	@Override
	public String getChoreName() {
		return "Mining";
	}

	@Override
	public void endChore() {
		FMLLog.getLogger().log(Level.WARN, "\n\nEND FOR NO PARTICULAR REASON\n\n");
		hasEnded = true;
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
				FMLLog.getLogger().log(Level.INFO, "Exception in writing to NBT: "+e);
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
				FMLLog.getLogger().log(Level.INFO, "Exception in reading from NBT: "+e);
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
	//Not needed?
	/*
	private void runPassiveAI()	{
		FMLLog.getLogger().log(Level.INFO, "RUNING PASSIVE AI");
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
	*/

	/**
	 * Runs the active mining AI.
	 * Finds the nearest block if we have no 
	 */

	private void runActiveAI() {
		doLookTowardsHeading();
		
		if (!hasNextPath) {
			final Point3D nextBlockPosition = getNearestBlockCoordinates();
			//If we haven't found any blocks, we have none left in the area, give up?
			if (nextBlockPosition == null) {
				endForNoBlocks();
				return;
			}
			nextX = nextBlockPosition.iPosX;
			nextY = nextBlockPosition.iPosY;
			nextZ = nextBlockPosition.iPosZ;
			FMLLog.getLogger().log(Level.INFO, "PATH SET: GO TO "+nextBlockPosition);
			hasNextPath = true;
		} else {
			//If someone else has already mined this block, try to find a new block
			if (owner.worldObj.getBlock((int)nextX, (int)nextY, (int)nextZ) == Blocks.air) {
				hasNextPath = false;
				return;
			}
			//Check if the block is too far (probably irrelevant and should be removed
			if (LogicHelper.getDistanceToXYZ(startX, startY, startZ, nextX, nextY, nextZ) > maxDistance) {
				endForFinished();
				return;
			} else {
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
		//else {
		//	doSetNextPath();
		//}
	}
	
	private Point3D getNearestBlockCoordinates() {
		final double lastDistance = 100D;
		Point3D nearestPoint = null;

		for (final Point3D point : LogicHelper.getNearbyBlocks_StartAtBottom(owner, searchBlock, 200)) {
			final double thisDistance = LogicHelper.getDistanceToXYZ(owner.posX, owner.posY, owner.posZ, point.dPosX, point.dPosY, point.dPosZ);

			if (thisDistance < lastDistance) {
				nearestPoint = point;
			}
		}
		return nearestPoint;
	}
	
	private boolean hasPick() {
		return true;
		//return owner.getTool() != null;
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
		if (owner.worldObj.isRemote) {
			owner.setRotationYawHead(heading);
		}
	}

	//Unnecessary I think
	/*private void doSetNextPath() {
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
	}*/

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
			} else {
				stackToAdd = new ItemStack(yieldItem, yieldAmount, yieldMeta);
			}

			//stackToAdd.damageItem(yieldMeta, owner); damage tool
			owner.addToInventory(stackToAdd);
		}

		catch (NullPointerException e) {
			FMLLog.getLogger().log(Level.INFO, "\n\nUnable to mine block at " + nextX + ", " + nextY + ", " + nextZ+"\n\n");
		}
		owner.worldObj.setBlock((int)nextX, (int)nextY, (int)nextZ, Blocks.air);
	}

	private void doSetPathToNextBlock() {
		if (!owner.worldObj.isRemote && owner.getNavigator().noPath()) {
			owner.getNavigator().setPath(owner.getNavigator().getPathToXYZ((int)nextX, (int)nextY, (int)nextZ), Constants.SPEED_WALK);
		}
	}

	private void endForNoBlocks() {
		FMLLog.getLogger().log(Level.WARN, "\n\nEND FOR NO BLOCKS FOUND\n\n");
		endChore();
	}

	private void endForFinished() {
		FMLLog.getLogger().log(Level.WARN, "\n\nEND FOR MINING CHORE FINISHED\n\n");
		endChore();
	}
}
