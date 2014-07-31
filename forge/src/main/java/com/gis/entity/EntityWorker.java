package com.gis.entity;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.Level;

import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.ai.EntityAIMoveIndoors;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAIOpenDoor;
import net.minecraft.entity.ai.EntityAIRestrictOpenDoor;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.EntityAIWatchClosest2;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.world.World;






import net.minecraftforge.common.ISpecialArmor.ArmorProperties;

import com.gis.chore.ChoreMining;
import com.gis.core.Constants;
import com.gis.core.GIS;

import cpw.mods.fml.common.FMLLog;

public class EntityWorker extends EntityZombie {

	public int gisID;
	
	private Map<String, ItemStack> inventory = new HashMap<String, ItemStack>();
	private ItemStack tool;
	
	//AI stuff
	public ChoreMining miningChore;
	public boolean isInChoreMode = false;
	public boolean addedAI;
	private int counter = 0;
	private int miningCheckDistance = 200;
	
	//States
	public boolean isFollowing;
	public boolean isStaying;
	private boolean hasSpawned = false;
	
    public EntityWorker(World world) {
        super(world);
        
		setHealth(100);
		setSize(Constants.WIDTH_ADULT, Constants.HEIGHT_ADULT);
		tool = new ItemStack(Items.wooden_pickaxe);
		this.setCurrentItemOrArmor(0, new ItemStack(tool.getItem()));
		this.setCurrentItemOrArmor(1, new ItemStack(Items.leather_helmet));;
		this.setCurrentItemOrArmor(2, new ItemStack(Items.leather_chestplate));;
		this.setCurrentItemOrArmor(3, new ItemStack(Items.leather_leggings));;
		this.setCurrentItemOrArmor(4, new ItemStack(Items.leather_boots));

		FMLLog.getLogger().log(Level.INFO, "TOOL IS: "+tool.getDisplayName());
    }
    
    @Override
    public void onDeathUpdate() {
    	++this.deathTime;
    	if (this.deathTime == 20)
        {
    		this.setDead();
    		GIS.workersPlaced--;
        }
    }
 
    public EnumCreatureAttribute getCreatureAttribute() {
        return EnumCreatureAttribute.UNDEFINED;
    }

	public EntityVillager createChild(EntityAgeable var1) {
		return null;
	}

    
    //Main function
	@Override
	public void onUpdate() {
		super.onUpdate();
		if(!this.hasSpawned){
			GIS.workersPlaced++;	//Increment counter of workers spawned
			GIS.output-=2500;		//Base price to spawn a worker
			this.hasSpawned = true;
		}
		//Check if their AI has been added.
		if (!addedAI) {
			addAI();
			addedAI = true;
		}
		if(this.isDead) {
    		GIS.workersPlaced--;
		}
		GIS.output-=1;
		updateChores();
		
	}
	

	@Override
	public boolean isAIEnabled() {
		return true;
	}

	public void addAI() {
		this.tasks.taskEntries.clear();

		this.getNavigator().setBreakDoors(true);
		this.getNavigator().setAvoidsWater(true);

		this.tasks.addTask(0, new EntityAISwimming(this));
		this.tasks.addTask(1, new EntityAIAvoidEntity(this, EntityZombie.class, 8.0F, Constants.SPEED_RUN, 0.35F));
		this.tasks.addTask(2, new EntityAIMoveIndoors(this));
		this.tasks.addTask(3, new EntityAIRestrictOpenDoor(this));
		this.tasks.addTask(4, new EntityAIOpenDoor(this, true));
		this.tasks.addTask(5, new EntityAIMoveTowardsRestriction(this, Constants.SPEED_WALK));
		this.tasks.addTask(9, new EntityAIWatchClosest2(this, EntityPlayer.class, 3.0F, 1.0F));
		this.tasks.addTask(9, new EntityAIWatchClosest2(this, EntityWorker.class, 5.0F, 0.02F));
		this.tasks.addTask(9, new EntityAIWander(this, Constants.SPEED_WALK));
		this.tasks.addTask(10, new EntityAIWatchClosest(this, EntityLivingBase.class, 8.0F));
	}
	

	/**
	 * Handles running chore AI.
	 */
	private void updateChores()	{
		if (isInChoreMode) {
			if (miningChore.hasEnded) {
				isInChoreMode = false;
			}

			else if (miningChore.hasBegun) {
				miningChore.runChoreAI();
			}

			else {
				miningChore.beginChore();
			}
		} else {
			if (counter < Constants.MINUTE)
				counter++;
			else {
				isInChoreMode = MakeChore();
				miningCheckDistance += 200;
				counter = 0;
			}
		}
	}
	
	private boolean MakeChore() {
    	miningChore = new ChoreMining(this, 1, Constants.MINEABLE_BLOCKS[0], miningCheckDistance);
    	return true;
	}
	
    ///////////////////
    //Inventory Stuff//
	///////////////////

	/**
	 * Get's the entity's tool
	 */
    public Item getTool() {
    	if (tool != null) {
    		return tool.getItem();
    	}
    	return null;
    }
    
	/**
	 * Get's the entity's entire inventory map
	 */
    public Map getInventory() {
    	return inventory;
    }

	/**
	 * Get's the entity's inventory ItemStack from inputed key
	 * @param key		The ItemStack.getDisplayName() of the ItemStack we want to get
	 */
    public ItemStack getInventory(String key) {
    	return inventory.get(key);
    }
    
    /**
     * 
     * @param item		The itemstack we want to add to the inventory
     */
    public void addToInventory(ItemStack item) {
    	if (inventory.containsKey(""+item)) {
    		inventory.get(item.getDisplayName()).stackSize += item.stackSize;
    	} else {
    		inventory.put(item.getDisplayName(), item);
    	}
    }
    /**
     * 
     * @param item		The ItemStack type we want to remove
     * @param removeNum	The amount we want to remove
     * @return			An ItemStack containing the correct number of items
     */
    public ItemStack removeFromInventory(ItemStack item, int removeNum) {
    	String key = item.getDisplayName();
    	if (inventory.containsKey(key)) {
    		ItemStack invItem = getInventory(key);
			
    		//If the inventory has <= the amount we want to remove, return it
    		if (invItem.stackSize <= removeNum) {
    			inventory.remove(key);
    			return invItem;
    		//Otherwise, subtract the amount and return a new stack with of the amount
    		} else {
        		inventory.get(key).stackSize -= removeNum;
    			return new ItemStack(invItem.getItem(), removeNum);
    		}
    	}
    	return null;
    }
    /**
     * 
     * @param item		The ItemStack.getDisplayName of the ItemStack type we want to remove
     * @param removeNum	The amount we want to remove
     * @return			An ItemStack containing the correct number of items
     */
    public ItemStack removeFromInventory(String key, int removeNum) {
    	if (inventory.containsKey(key)) {
    		ItemStack invItem = getInventory(key);
			
    		//If the inventory has <= the amount we want to remove, return it
    		if (invItem.stackSize <= removeNum) {
    			inventory.remove(key);
    			return invItem;
    		//Otherwise, subtract the amount and return a new stack with of the amount
    		} else {
        		inventory.get(key).stackSize -= removeNum;
    			return new ItemStack(invItem.getItem(), removeNum);
    		}
    	}
    	return null;
    }
}

