package com.packtpub.libgdx.canyonbunny.util;
/*  Tyler Major
 *  pg 113 
 *  10//2018
 */
/**
 * Holds variables that will always hold the same
 * value
 */
public class Constants 
{
	// Visible game world is 5 meters wide
	public static final float VIEWPORT_WIDTH = 5.0f;
	
	// Visible game world is 5 meters tall
	public static final float VIEWPORT_HEIGHT = 5.0f;
	
	// GUI Width
	public static final float VIEWPORT_GUI_WIDTH = 800.0f;
	
	// GUI Height
	public static final float VIEWPORT_GUI_HEIGHT = 480.0f;
	
	// Location of description file for texture atlas
	public static final String TEXTURE_ATLAS_OBJECTS = "images/canyonbunny.pack.atlas";
	
	// Location of image file for level 01
	public static final String LEVEL_01 = "levels/level-01.png";
	
	// Amount of extra lives at level start
	public static final int LIVES_START = 3;
	
	// Added this from pg220. Delay after game over
		public static final float TIME_DELAY_GAME_OVER = 3;
		
	// Added this from pg220. Delay after finish
	public static final float TIME_DELAY_GAME_FINISHED = 3;
		
	// Added this from p206. Sets time limit for feather power up
	public static final float ITEM_SNOWFLAKE_POWERUP_DURATION = 9;
	
}
