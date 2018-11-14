package com.packtpub.libgdx.canyonbunny;


import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.packtpub.libgdx.canyonbunny.Level.BLOCK_TYPE;
import com.packtpub.libgdx.canyonbunny.game.objects.AbstractGameObject;
import com.packtpub.libgdx.canyonbunny.game.objects.Mountains;
import com.packtpub.libgdx.canyonbunny.game.objects.Platform;
import com.packtpub.libgdx.canyonbunny.game.objects.SantaHead;
import com.packtpub.libgdx.canyonbunny.game.objects.WaterOverlay;
import com.packtpub.libgdx.canyonbunny.util.Assets.AssetSanta;




/* @author: Tyler Major
 * pg 177-178 level code.. also 179-180...also 181-182
 * This class is the loader that will read and interpret the image data
 * Adds mountains in level class
 */
public class Level 
{
		public static final String TAG = Level.class.getName();
		public enum BLOCK_TYPE {
		EMPTY(0, 0, 0), // black
		PLATFORM(0, 255, 0), // green
		PLAYER_SPAWNPOINT(255, 255, 255), // white
		ITEM_SNOWFLAKE(255, 0, 255), // purple
		ITEM_PRESENT(255, 255, 0); // yellow
		private int color;
		
	private BLOCK_TYPE (int r, int g, int b) 
	{
		color = r << 24 | g << 16 | b << 8 | 0xff;
	}
		public boolean sameColor (int color) 
		{
			return this.color == color;
		}
		public int getColor () 
		{
		return color;
		}
	}
		// objects
		public Array<Platform> platform;
		public SantaHead body;
		
		// decoration
		public Mountains mountains;
		public WaterOverlay waterOverlay;
		
		public Level (String filename) 
		{
			init(filename);
		}
		
		private void init (String filename)
		{
			
			// objects
			platform = new Array<Platform>();
			//santa body
			body = null;
			// load image file that represents the level data
			Pixmap pixmap = new Pixmap(Gdx.files.internal(filename));
			// scan pixels from top-left to bottom-right
			
			int lastPixel = -1;
			for (int pixelY = 0; pixelY < pixmap.getHeight(); pixelY++) 
			{
			for (int pixelX = 0; pixelX < pixmap.getWidth(); pixelX++) 
			{
			AbstractGameObject obj = null;
			float offsetHeight = 0;
			// height grows from bottom to top
			float baseHeight = pixmap.getHeight() - pixelY;
			// get color of current pixel as 32-bit RGBA value
			int currentPixel = pixmap.getPixel(pixelX, pixelY);
			// find matching color value to identify block type at (x,y)
			// point and create the corresponding game object if there is
			// a match
			// empty space
			if (BLOCK_TYPE.EMPTY.sameColor(currentPixel)) 
			{
			// do nothing
			}
			// rock
			else if (BLOCK_TYPE.PLATFORM.sameColor(currentPixel)) 
			{
				if (lastPixel != currentPixel) 
				{
					obj = new Platform();
					float heightIncreaseFactor = 0.25f;
					offsetHeight = -2.5f;
					obj.position.set(pixelX, baseHeight * obj.dimension.y
					* heightIncreaseFactor + offsetHeight);
					platform.add((Platform)obj);
				} 
				else 
				{
					platform.get(platform.size - 1).increaseLength(1);
				}
			}
			// player spawn point
			else if
			(BLOCK_TYPE.PLAYER_SPAWNPOINT.sameColor(currentPixel)) 
			{
				obj = new SantaHead();
				offsetHeight = -3.0f;
				obj.position.set(pixelX,baseHeight * obj.dimension.y +
						offsetHeight);
						body = (SantaHead)obj;
			}
			// feather
			else if (BLOCK_TYPE.ITEM_SNOWFLAKE.sameColor(currentPixel)) 
			{
			}
			// gold coin
			else if (BLOCK_TYPE.ITEM_PRESENT.sameColor(currentPixel)) 
			{
			}
			// unknown object/pixel color
			else {
			int r = 0xff & (currentPixel >>> 24); //red color channel
			int g = 0xff & (currentPixel >>> 16); //green color channel
			int b = 0xff & (currentPixel >>> 8); //blue color channel
			int a = 0xff & currentPixel; //alpha channel
			Gdx.app.error(TAG, "Unknown object at x<" + pixelX + "> y<"
			+ pixelY + ">: r<" + r+ "> g<" + g + "> b<" + b + "> a<" + a + ">");
			}
				lastPixel = currentPixel;
			}
			}
			// decoration
			mountains = new Mountains(pixmap.getWidth());
			mountains.position.set(-1, -1);
			waterOverlay = new WaterOverlay(pixmap.getWidth());
			waterOverlay.position.set(0, -3.75f);
			// free memory
			pixmap.dispose();
			Gdx.app.debug(TAG, "level '" + filename + "' loaded");
		}
		
		public void render (SpriteBatch batch) 
		{
			// Draw Mountains
			mountains.render(batch);
			// Draw Rocks
			for (Platform platform : platform)
			platform.render(batch);
			//Draw Santa at beginning
			body.render(batch);
			// Draw Water Overlay
			waterOverlay.render(batch);
			
			
		}
}
		
			

		
