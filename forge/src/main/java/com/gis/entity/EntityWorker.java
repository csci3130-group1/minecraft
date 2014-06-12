package com.gis.entity;

import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.world.World;
 
public class EntityWorker extends EntityVillager
{
    public EntityWorker(World world)
    {
        super(world);
    }
 
    public EnumCreatureAttribute getCreatureAttribute()
    {
        return EnumCreatureAttribute.UNDEFINED;
    }

	public EntityVillager createChild(EntityAgeable var1) {
		return null;
	}
}

