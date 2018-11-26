/*  Tyler Major 
 *  10/31/2018
 */

package com.mygdx.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import com.badlogic.gdx.tools.texturepacker.TexturePacker.Settings;
import com.packtpub.libgdx.canyonbunny.CanyonBunnyMain;


public class DesktopLauncher 
{
	private static boolean rebuildAtlas = false;
	private static boolean drawDebugOutline = false;
	public static void main (String[] arg) 
	{
		if (rebuildAtlas) 
		{
			Settings settings = new Settings();
			settings.maxWidth = 1024;
			settings.maxHeight = 1024;
			settings.duplicatePadding = false;
			settings.debug = drawDebugOutline;

			TexturePacker.process(settings, "assets-raw/images", "../core/assets/images", "canyonbunny.pack");
			TexturePacker.process(settings, "assets-raw/images-ui", "../core/assets/images-ui", "canyonbunny-ui.pack");
		}
		
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "CanyonBunny";
		config.width = 800;
		config.height = 480;
		new LwjglApplication(new CanyonBunnyMain(), config);
	}
}
