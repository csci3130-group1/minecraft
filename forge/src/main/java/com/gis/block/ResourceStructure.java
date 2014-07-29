package com.gis.block;

import org.apache.logging.log4j.Level;

import com.gis.core.GIS;

import cpw.mods.fml.common.FMLLog;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFalling;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ResourceStructure extends NaturalResourceBlock {
	
	
	public ResourceStructure(){
		super();
	}
	
	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack itemstack) {
		super.onBlockPlacedBy(world, x, y, z, entity, itemstack);
		
		for(int i = y; i < 8; i++) {
			world.setBlock(x, i, z, GIS.getInstance().naturalResourceBlock);
			world.setBlock(x+1, i, z, GIS.getInstance().naturalResourceBlock);
			world.setBlock(x+2, i, z, GIS.getInstance().naturalResourceBlock);
			world.setBlock(x+3, i, z, GIS.getInstance().naturalResourceBlock);
			world.setBlock(x+4, i, z, GIS.getInstance().naturalResourceBlock);
			
			world.setBlock(x, i, z+1, GIS.getInstance().naturalResourceBlock);
			world.setBlock(x+1, i, z+1, GIS.getInstance().naturalResourceBlock);
			world.setBlock(x+2, i, z+1, GIS.getInstance().naturalResourceBlock);
			world.setBlock(x+3, i, z+1, GIS.getInstance().naturalResourceBlock);
			world.setBlock(x+4, i, z+1, GIS.getInstance().naturalResourceBlock);

			world.setBlock(x, i, z+2, GIS.getInstance().naturalResourceBlock);
			world.setBlock(x+1, i, z+2, GIS.getInstance().naturalResourceBlock);
			world.setBlock(x+2, i, z+2, GIS.getInstance().naturalResourceBlock);
			world.setBlock(x+3, i, z+2, GIS.getInstance().naturalResourceBlock);
			world.setBlock(x+4, i, z+2, GIS.getInstance().naturalResourceBlock);
			
			world.setBlock(x, i, z+3, GIS.getInstance().naturalResourceBlock);
			world.setBlock(x+1, i, z+3, GIS.getInstance().naturalResourceBlock);
			world.setBlock(x+2, i, z+3, GIS.getInstance().naturalResourceBlock);
			world.setBlock(x+3, i, z+3, GIS.getInstance().naturalResourceBlock);
			world.setBlock(x+4, i, z+3, GIS.getInstance().naturalResourceBlock);
			
			world.setBlock(x, i, z+4, GIS.getInstance().naturalResourceBlock);
			world.setBlock(x+1, i, z+4, GIS.getInstance().naturalResourceBlock);
			world.setBlock(x+2, i, z+4, GIS.getInstance().naturalResourceBlock);
			world.setBlock(x+3, i, z+4, GIS.getInstance().naturalResourceBlock);
			world.setBlock(x+4, i, z+4, GIS.getInstance().naturalResourceBlock);
		}




		FMLLog.getLogger().log(Level.WARN, GIS.resourcesPlaced);


	}

}
