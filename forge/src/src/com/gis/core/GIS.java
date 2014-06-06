package com.gis.core;



import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;

@Mod(modid = GIS.MODID, version = GIS.VERSION)
public class GIS
{
    public static final String MODID = "examplemod";
    public static final String VERSION = "1.0";
    
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
		// some example code
        System.out.println("DIRT BLOCK >> "+Blocks.dirt.getUnlocalizedName());
    }
    @EventHandler
    public void load(FMLInitializationEvent event)
    {
    	Block modBlock = new TestBlock(Material.fire)
    		.setHardness(0.5F)
    		.setStepSound(Block.soundTypePiston)
    		.setBlockName("modBlock")
    		.setCreativeTab(CreativeTabs.tabBlock);
    	GameRegistry.registerBlock(modBlock, "modBlock");
    }
}
