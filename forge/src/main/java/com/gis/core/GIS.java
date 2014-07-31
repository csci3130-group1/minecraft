package com.gis.core;

import java.util.HashMap;
import java.util.Map;

import com.gis.block.*;
import com.gis.entity.*;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

@Mod(modid = GIS.MODID, version = GIS.VERSION, name = GIS.NAME)
public class GIS {
	/** An instance of the core GIS class. */
	@Instance("GIS")
	private static GIS instance;
		
    public static final String MODID = "gis";
    public static final String VERSION = "1.0";
    public static final String NAME = "Going it Solow";
    
    //EventManager worldGen = new EventManager();
    public static NaturalResourceBlock naturalResourceBlock;
    public static int resourcesMined;
    public static int workersPlaced;
    public static int output;
    public static int techLevel;

    
    /**Map of GIS ids and entity ids. Key = mcaId, Value = entityId.**/
	public Map<Integer, Integer> idsMap = new HashMap<Integer, Integer>();

	/**Map of GIS ids and their associated entity. Key = mcaId, Value = abstractEntity. */
	public Map<Integer, EntityWorker> entitiesMap = new HashMap<Integer, EntityWorker>();
    

  @EventHandler
    public void preinit(FMLPreInitializationEvent event)
    {
        
        naturalResourceBlock = new NaturalResourceBlock();
    	naturalResourceBlock.setBlockName("naturalResourceBlock").setCreativeTab(CreativeTabs.tabMisc).setStepSound(Block.soundTypeGlass);
    	GameRegistry.registerBlock(naturalResourceBlock, "naturalResourceBlock");
    	
    	workersPlaced = 0;
    	output = 99999;
    	techLevel = 1;
    	resourcesMined = 0;
    	
    
    }
        
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
    	FMLCommonHandler.instance().bus().register(new KeyHandler());
    }
    @EventHandler
    public void load(FMLInitializationEvent event)
    {
    	MinecraftForge.EVENT_BUS.register(RenderHandler.instance);

    	//Register worker entities
    	EntityRegistry.registerGlobalEntityID(EntityWorker.class, "Worker", EntityRegistry.findGlobalUniqueEntityId(), 1, 2);
		EntityRegistry.registerModEntity(EntityWorker.class, "Worker", 1, this, 50, 2, true);
		EntityRegistry.addSpawn(EntityWorker.class, 1, 10, 10, EnumCreatureType.ambient);


    }

    
	/**
	 * @return	An instance of GIS.
	 */
	public static GIS getInstance()
	{
		return instance;
	}
	
	/*
	public ModPropertiesList getModProperties()
	{
		//return (ModPropertiesList)modPropertiesManager.modPropertiesInstance;
	}
	*/
}
