package com.gis.block;

import org.apache.logging.log4j.Level;

import com.gis.core.GIS;

import cpw.mods.fml.common.FMLLog;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.BlockSand;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ResourceStructure {
	
	
	public ResourceStructure(World world, int x, int y, int z){
		placeStructure(world, x, y, z);
	}
	
	public void placeStructure(World world, int x, int y, int z) {
		
		for(int i = y; i < 8; i++) {
			world.setBlock(x, i, z, Blocks.sand);
			world.setBlock(x+1, i, z, Blocks.sand);
			world.setBlock(x+2, i, z, Blocks.sand);
			world.setBlock(x+3, i, z, Blocks.sand);
			world.setBlock(x+4, i, z, Blocks.sand);
			
			world.setBlock(x, i, z+1, Blocks.sand);
			world.setBlock(x+1, i, z+1, Blocks.sand);
			world.setBlock(x+2, i, z+1, Blocks.sand);
			world.setBlock(x+3, i, z+1, Blocks.sand);
			world.setBlock(x+4, i, z+1, Blocks.sand);

			world.setBlock(x, i, z+2, Blocks.sand);
			world.setBlock(x+1, i, z+2, Blocks.sand);
			world.setBlock(x+2, i, z+2, Blocks.sand);
			world.setBlock(x+3, i, z+2, Blocks.sand);
			world.setBlock(x+4, i, z+2, Blocks.sand);
			
			world.setBlock(x, i, z+3, Blocks.sand);
			world.setBlock(x+1, i, z+3, Blocks.sand);
			world.setBlock(x+2, i, z+3, Blocks.sand);
			world.setBlock(x+3, i, z+3, Blocks.sand);
			world.setBlock(x+4, i, z+3, Blocks.sand);
			
			world.setBlock(x, i, z+4, Blocks.sand);
			world.setBlock(x+1, i, z+4, Blocks.sand);
			world.setBlock(x+2, i, z+4, Blocks.sand);
			world.setBlock(x+3, i, z+4, Blocks.sand);
			world.setBlock(x+4, i, z+4, Blocks.sand);
		}
	}

}
