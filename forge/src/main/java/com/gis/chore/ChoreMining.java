package com.gis.chore;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Comparator;
import java.util.PriorityQueue;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import org.apache.logging.log4j.Level;

import com.gis.core.Constants;
import com.gis.core.GIS;
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
	public boolean hasNextBlock;

	/** The X coordinates that the active mining chore stated at.*/
	public double startX;

	/** The Y coordinates that the active mining chore stated at.*/
	public double startY;

	/** The Z coordinates that the active mining chore stated at.*/
	public double startZ;
	
	/** The queue for the next block positions **/
	PriorityQueue<Point3D> pathQueue;
	
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
	 * @param 	oreType		(Passive only) The type of ore that should be searched for.
	 * @param 	distance	(Active only) The distance that the entity should mine.
	 */
	public ChoreMining(EntityWorker entity, int mode, Block block, int distance) {
		super(entity);

		this.inPassiveMode = mode == 0 ? true : false;
		this.searchBlock = block;
		this.maxDistance = distance;
		this.delayInterval = getDelayForToolType(entity.getTool());
		
		//Check if we can do anything, if not, end
		pathQueue = getNearestBlocks();
		hasNextBlock = getNextBlock(pathQueue);
		if (!hasNextBlock) {
			endForNoBlocks();
			return;
		}
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
		runActiveAI();
	}

	@Override
	public String getChoreName() {
		return "Mining";
	}
	
	public EntityWorker getOwner() {
		return owner;
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
		int returnAmount = 25;
		if (owner.getTool() != null) {
			final ToolMaterial material = ToolMaterial.valueOf(((ItemPickaxe)tool).getToolMaterialName());

			switch (material) {
			case WOOD: 		returnAmount = 40; break;
			case STONE: 	returnAmount = 30; break;
			case IRON: 		returnAmount = 25; break;
			case EMERALD: 	returnAmount = 10; break;
			case GOLD: 		returnAmount = 5; break;
			default: 		break;
			}
		}
		return returnAmount;
	}

	/**
	 * Runs the active mining AI.
	 * Finds the nearest block if we have no 
	 */

	private void runActiveAI() {
		//doLookTowardsHeading();
		
		if (!hasNextBlock) {
			pathQueue = getNearestBlocks();
			hasNextBlock = getNextBlock(pathQueue);
		} else {
			//If someone else has already mined this block, try to find a new block
			if ((owner.worldObj.getBlock((int)nextX, (int)nextY, (int)nextZ) == Blocks.air)) {
				hasNextBlock = getNextBlock(pathQueue);
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
					hasNextBlock = getNextBlock(pathQueue); //Check for the next block
				}
			}
			//Not within 2.5 blocks of target so not close enough to hit it
			else {
				doSetPathToNextBlock();
			}
		}
	}

	/**
	 * Gets a queue of the blocks, ordered from nearest to most distant
	 */
	private PriorityQueue<Point3D> getNearestBlocks() {
		final double lastDistance = 100D;
		Point3D nearestPoint = null;
		
		PriorityQueue<Point3D> pointQueue = new PriorityQueue<Point3D>(10, new Comparator<Point3D>() {
	    	public int compare(Point3D point1, Point3D point2) {
	    		//owner = getOwner();
	    		double p1 = LogicHelper.getDistanceToXYZ(owner.posX, owner.posY, owner.posZ, point1.dPosX, point1.dPosY, point1.dPosZ);
	            double p2 = LogicHelper.getDistanceToXYZ(owner.posX, owner.posY, owner.posZ, point2.dPosX, point2.dPosY, point2.dPosZ);
	            return Double.compare(p1, p2);
	        }
	    });
		for (final Point3D point : LogicHelper.getNearbyBlocks_StartAtBottom(owner, searchBlock, maxDistance)) {
			pointQueue.add(point);
		}
		return pointQueue;
	}
	
	/**
	 * Sets next block to be the first block in the queue and returns whether it worked or not
	 */
	private boolean getNextBlock(PriorityQueue<Point3D> blockQueue) {
		//If we haven't found any blocks, we have none left in the area, give up?
		if (blockQueue.isEmpty()) {
			endForNoBlocks();
			return false;
		}
		final Point3D nextBlockPosition = blockQueue.remove();
		nextX = nextBlockPosition.iPosX;
		nextY = nextBlockPosition.iPosY;
		nextZ = nextBlockPosition.iPosZ;
		return true;
	}

	/**
	 * Checks if the owner has a pick
	 */
	private boolean hasPick() {
		return owner.getTool() != null;
	}

	/**
	 * Make the owner look in the right direction
	 */
	private void doLookTowardsHeading()	{
		if (owner.worldObj.isRemote) {
			owner.setRotationYawHead(heading);
		}
	}

	/**
	 * Make the owner's built-in navigator move the owner to the next position
	 */
	private void doSetPathToNextBlock() {
		if (!owner.worldObj.isRemote && owner.getNavigator().noPath()) {
			owner.getNavigator().setPath(owner.getNavigator().getPathToXYZ((int)nextX, (int)nextY, (int)nextZ), Constants.SPEED_WALK);
		}
	}

	/**
	 * Make the owner harvest the inputted block, collecting its resources
	 */
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

		try {
			ItemStack stackToAdd = null; 

			if (yieldBlock != null) {
				stackToAdd = new ItemStack(yieldBlock, yieldAmount, yieldMeta);
			} else {
				stackToAdd = new ItemStack(yieldItem, yieldAmount, yieldMeta);
			}

			//stackToAdd.damageItem(yieldMeta, owner); damage tool
			owner.addToInventory(stackToAdd);
			FMLLog.getLogger().log(Level.INFO, "Block mined");
			GIS.resourcesMined+=1;
			GIS.output += GIS.techLevel * 1000;

		}

		catch (NullPointerException e) {
			FMLLog.getLogger().log(Level.INFO, "\n\nUnable to mine block at " + nextX + ", " + nextY + ", " + nextZ+"\n\n");
		}
		owner.worldObj.setBlock((int)nextX, (int)nextY, (int)nextZ, Blocks.air);
	}

	/**
	 * Chore ending methods
	 */
	@Override
	public void endChore() {
		FMLLog.getLogger().log(Level.WARN, "END FOR NO PARTICULAR REASON\n");
		hasEnded = true;
		owner.addAI();
	}

	private void endForNoBlocks() {
		FMLLog.getLogger().log(Level.WARN, "END FOR NO BLOCKS FOUND\n");
		endChore();
	}

	private void endForFinished() {
		FMLLog.getLogger().log(Level.WARN, "END FOR MINING CHORE FINISHED\n");
		endChore();
	}
}
