package com.gis.core;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.Minecraft;

public class RenderHandler {
public static RenderHandler instance = new RenderHandler();
private static Minecraft mc = Minecraft.getMinecraft();

@SubscribeEvent
public void RenderGameOverlayEvent(net.minecraftforge.client.event.RenderGameOverlayEvent event) {

// render everything onto the screen
if (event.type == net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType.TEXT) {

Integer test = GIS.resourcesPlaced;

Draw.renderToHud("Resources remaining: " + test.toString());

}
}
}

