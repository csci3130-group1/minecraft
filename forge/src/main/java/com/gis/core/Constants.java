package com.gis.core;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;

public class Constants {
	

	/**
	 * Array of blocks that CAN be mined by the mining chore.
	 */
	public static final Block[] MINEABLE_BLOCKS = 
	{
		Blocks.sand
	};
	
	public static final float HEIGHT_ADULT = 1.8F;
	public static final float WIDTH_ADULT = 0.6F;

	//Time stuff, # of ticks in a:
	public static final int	SECOND	= 20;
	public static final int	MINUTE	= 1200;
	public static final int	HOUR	= 72000;

	//Movement speeds.
	public static final float SPEED_SNEAK = 0.4F;
	public static final float SPEED_WALK = 0.6F;
	public static final float SPEED_RUN = 0.7F;
	public static final float SPEED_SPRINT = 0.8F;
	public static final float SPEED_HORSE_RUN = 2.1F;
}
