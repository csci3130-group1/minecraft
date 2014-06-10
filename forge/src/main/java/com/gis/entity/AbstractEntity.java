package com.gis.entity;

import java.util.Map;

import com.gis.core.Constants;
import com.gis.core.GIS;

import net.minecraft.world.World;

public abstract class AbstractEntity extends AbstractSerializableEntity {

	public int gisID;
	
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

	
}
