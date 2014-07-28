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
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

@Mod(modid = GIS.MODID, version = GIS.VERSION)
public class GIS {
	/** An instance of the core GIS class. */
	@Instance("GIS")
	private static GIS instance;
		
    public static final String MODID = "gis";
    public static final String VERSION = "1.0";
    
    EventManager worldGen = new EventManager();
    public static Block resourceBlock;
    
    /**Map of GIS ids and entity ids. Key = mcaId, Value = entityId.**/
	public Map<Integer, Integer> idsMap = new HashMap<Integer, Integer>();

	/**Map of GIS ids and their associated entity. Key = mcaId, Value = abstractEntity. */
	public Map<Integer, EntityWorker> entitiesMap = new HashMap<Integer, EntityWorker>();
    

	
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
    	
    }
    @EventHandler
    public void load(FMLInitializationEvent event)
    {
    	
    	resourceBlock = new NaturalResourceBlock();
    	GameRegistry.registerBlock(resourceBlock, "naturalResourceBlock");
    	
    	
    	GameRegistry.registerWorldGenerator(worldGen, 0);
    	
    	//Insantiate worker entities
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
