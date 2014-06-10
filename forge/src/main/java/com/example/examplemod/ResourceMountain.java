package com.example.examplemod;

import net.minecraft.init.Blocks;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;

@Mod(modid = ExampleMod.MODID, version = ExampleMod.VERSION)
public class ResourceMountain implements IWorldGenerator
{
    public static final String MODID = "nrmountain";
    public static final String VERSION = "1.0";
    
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
		// some example code
        System.out.println("creating natural resource mountians!");
    }
    
    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world) {
        world.setBlock(chunkX*16 + random.nextInt(16), 100, chunkZ*16 + random.nextInt(16), 5);
        // block X location, block Y location, block Z location, block id (in this case, wooden planks)
    }
}
