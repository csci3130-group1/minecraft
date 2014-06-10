package com.gis.entity;

import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.passive.EntityAmbientCreature;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.world.World;
 
public class EntityWorker extends EntityCow
{
    public EntityWorker(World world)
    {
        super(world);
    }
 
    public EnumCreatureAttribute getCreatureAttribute()
    {
        return EnumCreatureAttribute.UNDEAD;
    }

	public EntityCow createChild(EntityAgeable var1) {
		return null;
	}
}
