package com.packtpub.libgdx.canyonbunny.game.objects;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.packtpub.libgdx.canyonbunny.game.objects.AbstractGameObject;
import com.packtpub.libgdx.canyonbunny.util.Assets;
import com.badlogic.gdx.math.Vector2;

/** @author: Tyler Major
 * pg 171 and 172 creates the mountain class 
 * Code consists of 3 mountains that have their own layer.
 * A tint, color and offset can be specified for each layer.
 */


public class Mountains extends AbstractGameObject 
{
	private TextureRegion regMountainLeft;
	private TextureRegion regMountainRight;
	private int length;
	
	public Mountains(int length) 
	{
      this.length = length;
	  init();
	}
	
	/**
	 * initializes the mountain object
	 */
	private void init() 
	{
		dimension.set(10, 2);
		regMountainLeft = Assets.instance.levelDecoration.mountainBackground;
		regMountainRight = Assets.instance.levelDecoration.mountainBackground;
		// shift mountain and extend length
		origin.x = -dimension.x * 2;
		length += dimension.x * 2;
	}
	
		/**
		 * @param batch
		 * @param offsetX
		 * @param offsetY
		 * @param tintColor
		 * @param parallaxSpeedX
		 * draw the mountain based on the given parameters
		 */
		private void drawMountain (SpriteBatch batch, float offsetX, float offsetY, float tintColor, float parallaxSpeedX) 
		{
			TextureRegion reg = null;
			batch.setColor(tintColor, tintColor, tintColor, 1);
			float xRel = dimension.x * offsetX;
			float yRel = dimension.y * offsetY;
			// mountains span the whole level
			int mountainLength = 0;
			mountainLength += MathUtils.ceil(length / (2 * dimension.x) * (1 - parallaxSpeedX));
			mountainLength += MathUtils.ceil(0.5f + offsetX);
			for (int i = 0; i < mountainLength; i++) 
			{
				// mountain left
				reg = regMountainLeft;
				batch.draw(reg.getTexture(), origin.x + xRel + position.x * parallaxSpeedX, position.y +
				origin.y + yRel, origin.x, origin.y, dimension.x, dimension.y,
				scale.x, scale.y, rotation, reg.getRegionX(), reg.getRegionY(),
				reg.getRegionWidth(), reg.getRegionHeight(), false, false);
				xRel += dimension.x;
				// mountain right
				reg = regMountainRight;
				batch.draw(reg.getTexture(),origin.x + xRel + position.x *parallaxSpeedX, position.y +
				origin.y + yRel, origin.x, origin.y, dimension.x, dimension.y,
				scale.x, scale.y, rotation, reg.getRegionX(), reg.getRegionY(),
				reg.getRegionWidth(), reg.getRegionHeight(), false, false);
				xRel += dimension.x;
		     }
		// reset color to white
		batch.setColor(1, 1, 1, 1);
		}
		
		@Override
		/**
		 * Renders mountains accordingly
		 */
		public void render(SpriteBatch batch)
		{
		// 80% distant mountains (dark gray)
		drawMountain(batch, 0.5f, 0.5f, 0.5f, 0.8f);
		// 50% distant mountains (gray)
		drawMountain(batch, 0.25f, 0.25f, 0.7f, 0.5f);
		// 30% distant mountains (light gray)
		drawMountain(batch, 0.0f, 0.0f, 0.9f, 0.3f);
		}
		
		/**
		 * @param camPosition
		 * Updates the scroll position based on the current 
		 * position of the camera
		 */
		public void updateScrollPosition (Vector2 camPosition)
		{
			position.set(camPosition.x, position.y);
		}

		
}
	

