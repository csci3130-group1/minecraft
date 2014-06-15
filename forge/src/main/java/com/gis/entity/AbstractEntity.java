package com.gis.entity;

import java.util.HashMap;
import java.util.Map;

import com.gis.chore.ChoreMining;
import com.gis.core.Constants;
import com.gis.core.GIS;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public abstract class AbstractEntity extends AbstractSerializableEntity { //implements Serializable, ITickableEntity

	public Map<String, ItemStack> inventory = new HashMap<String, ItemStack>();
	public int gisID;
	public ChoreMining  miningChore = new ChoreMining(this);
	protected Item tool;
	
	//States
	public boolean isFollowing;
	public boolean isStaying;
	
	public AbstractEntity(World world)
	{
		super(world);

		//Get the appropriate GIS id for the person.
		if (!world.isRemote)
		{
			for (final Map.Entry<Integer, Integer> mapEntry : GIS.getInstance().idsMap.entrySet())
			{
				if (mapEntry.getKey() > gisID)
				{
					gisID = mapEntry.getKey();
				}
			}

			gisID++;

			//Put the ID in the list.
			GIS.getInstance().idsMap.put(gisID, getEntityId());
		}

		//getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(MCA.getInstance().getModProperties().villagerBaseHealth);
		//setHealth(GIS.getInstance().getModProperties().villagerBaseHealth);
		setHealth(20);
		setSize(Constants.WIDTH_ADULT, Constants.HEIGHT_ADULT);
	}
	

	/**
	 * Adds appropriate AI to the entity.
	 */
	public abstract void addAI();
	
	
    ///////////////////
    //Inventory Stuff//
	///////////////////

	/**
	 * Get's the entity's tool
	 */
    public Item getTool() {
    	return tool;
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
