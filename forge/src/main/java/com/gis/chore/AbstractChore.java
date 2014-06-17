package com.gis.chore;

import java.io.Serializable;

import com.gis.core.GIS;
import com.gis.entity.AbstractEntity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;

/**
 * Defines a chore that can be run by an AbstractEntity.
 */
public abstract class AbstractChore implements Serializable {
	/**The entity performing this chore.*/
	public AbstractEntity owner;
	
	/**Has beginChore() been ran?*/
	public boolean hasBegun;

	/**Has endChore() been ran?*/
	public boolean hasEnded;
	
	/**
	 * Constructor
	 * 
	 * @param 	entity	The entity that will be performing the chore.
	 */
	public AbstractChore(AbstractEntity entity)
	{
		owner = entity;
	}

	/**
	 * Initializes the chore and allows it to properly begin.
	 */
	public abstract void beginChore();

	/**
	 * Keeps the chore running.
	 */
	public abstract void runChoreAI();

	/**
	 * Gets the name of the chore that is being performed.
	 * 
	 * @return	The name of the chore being performed.
	 */
	public abstract String getChoreName();

	/**
	 * Ends the chore.
	 */
	public abstract void endChore();
	
	/**
	 * Writes the chore to NBT.
	 * 
	 * @param 	nbt	The NBTTagCompound to write the chore to.
	 */
	public abstract void writeChoreToNBT(NBTTagCompound nbt);

	/**
	 * Reads the chore from NBT.
	 * 
	 * @param 	nbt	The NBTTagCompound to read the chore from.
	 */
	public abstract void readChoreFromNBT(NBTTagCompound nbt);
	
	/**
	 * Calculates delay amount depending on the tool provided.
	 * 
	 * @param 	tool	The item that is the chore's tool.
	 * 
	 * @return	Delay time in ticks depending on tool type.
	 */
	protected abstract int getDelayForToolType(Item tool);
}
