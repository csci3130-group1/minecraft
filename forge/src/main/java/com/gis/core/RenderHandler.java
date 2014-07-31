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

Integer resRem = GIS.resourcesMined;
Integer workers = GIS.workersPlaced / 2;
Integer output = GIS.output;
Integer techLevel = GIS.techLevel;

Draw.renderToHud("Workers: " + workers.toString(), 0, 0);
Draw.renderToHud("Mined: " + resRem.toString(), 0, 10);
Draw.renderToHud("Tech: " + techLevel.toString(), 0, 20);
Draw.renderToHud("Output: " + output.toString(), 0, 30);



}
}
}

