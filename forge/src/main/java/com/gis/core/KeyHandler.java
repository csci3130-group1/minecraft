package com.gis.core;

import org.apache.logging.log4j.Level;
import org.lwjgl.input.Keyboard;

import com.gis.block.ResourceStructure;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent.KeyInputEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerEvent;

public class KeyHandler // note that before we extended KeyHandler, but that class no longer exists
{
	/** Key index for easy handling */
	public static final int CUSTOM_INV = 0;
	/** Key descriptions; use a language file to localize the description later */
	private static final String[] desc = {"gis.key.desc"};
	
	/** Default key values */
	private static final int[] keyValues = {Keyboard.KEY_P};
	private final KeyBinding[] keys;
	//public EntityClientPlayerMP player = Minecraft.getMinecraft().thePlayer;

	
	public KeyHandler() {
	keys = new KeyBinding[desc.length];

	
	for (int i = 0; i < desc.length; ++i) {
		keys[i] = new KeyBinding(desc[i], keyValues[i], "key.tutorial.category");
		ClientRegistry.registerKeyBinding(keys[i]);
	}
}
	
	/**
	* KeyInputEvent is in the FML package, so we must register to the FML event bus
	*/
	@SubscribeEvent
	public void onKeyInput(KeyInputEvent event) {
		// FMLClientHandler.instance().getClient().inGameHasFocus
		if (!FMLClientHandler.instance().isGUIOpen(GuiChat.class)) {
		if (keys[CUSTOM_INV].isPressed()) {
			FMLLog.getLogger().log(Level.INFO, "Key pressed");
		}
		}
	}
	
	@SubscribeEvent
	public void onPlayerJoin(PlayerLoggedInEvent event) {
		FMLLog.getLogger().log(Level.INFO, "Player joined");
		
		int x = event.player.getPlayerCoordinates().posX;
		FMLLog.getLogger().log(Level.INFO, x);

		int z = event.player.getPlayerCoordinates().posZ + 10;
		FMLLog.getLogger().log(Level.INFO, z);
		World world = event.player.worldObj;

		if(!world.isRemote) {
			FMLLog.getLogger().log(Level.INFO, "client is remote");
			for(int j = 0; j <=5; j++) {
				x=x+10;
				new ResourceStructure(world, x, 4, z);
			}
			
		}		
	}
}