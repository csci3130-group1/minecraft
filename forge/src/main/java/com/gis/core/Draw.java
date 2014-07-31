package com.gis.core;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.ScaledResolution;


public class Draw {
private static Minecraft mc = Minecraft.getMinecraft();

public static void renderToHud(String text, int x, int y) {
	if ((mc.inGameHasFocus || (mc.currentScreen != null && (mc.currentScreen instanceof GuiChat))) &&!mc.gameSettings.showDebugInfo)
	{
		ScaledResolution res = new ScaledResolution(Draw.mc.gameSettings, Draw.mc.displayWidth, Draw.mc.displayHeight);
		FontRenderer fontRender = mc.fontRenderer;
		int width = res.getScaledWidth();
		int height = res.getScaledHeight();
		
		int color = 0xffffff;
		mc.fontRenderer.FONT_HEIGHT = 8;
		mc.fontRenderer.drawStringWithShadow(text, x, y, color);
	}
}
}