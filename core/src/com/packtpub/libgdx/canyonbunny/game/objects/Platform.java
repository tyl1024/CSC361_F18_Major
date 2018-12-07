package com.packtpub.libgdx.canyonbunny.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.packtpub.libgdx.canyonbunny.*;
import com.packtpub.libgdx.canyonbunny.util.Assets;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

/**
 * Platforms for sant
 * 
 * Updated 10/31/2018 @author: Tyler Major
 * 
 * The float variables were added and changes were made in init()
 * These changes make sure that the following floating mechanism works properly
 * Starting value of float direction is set to up
 * The cycle is then randomly picked between 0 and half the max cycle time
 * Using a random cycle time gives the floating effect a more natural look
 */

public class Platform extends AbstractGameObject
	{
		private TextureRegion regMiddle;
		private int length;
		private final float FLOAT_CYCLE_TIME = 2.0f;
		private final float FLOAT_AMPLITUDE = 0.25f;
		private float floatCycleTimeLeft;
		private boolean floatingDownwards;
		private Vector2 floatTargetPosition;
		
		public Platform()
		{
			init();
		}
		
		private void init () 
		{
			dimension.set(1.0f, 1.9f);
			regMiddle = Assets.instance.Platform.flat;
			// Start length of this rock
			setLength(1);
			floatingDownwards = false;
			floatCycleTimeLeft = MathUtils.random(0,
			FLOAT_CYCLE_TIME / 2);
			floatTargetPosition = null;
		}
		
		public void setLength (int length)
		{
			this.length = length;
			// Update bounding box for collision detection
			bounds.set(0, 0, dimension.x * length, dimension.y);
		}
		
		public void increaseLength (int amount) 
		{
			setLength(length + amount);
		}

		@Override
		public void render (SpriteBatch batch)
		{
			TextureRegion reg = null;
			float relX = 0;
			float relY = 0;
			// Draw middle
			relX = 0;
			reg = regMiddle;
			for (int i = 0; i < length; i++)
			{
				batch.draw(reg.getTexture(), position.x + relX, position.y
				+ relY, origin.x, origin.y, dimension.x, dimension.y,
				scale.x + .02f, scale.y, rotation, reg.getRegionX(), reg.getRegionY(),
				reg.getRegionWidth(), reg.getRegionHeight(), false, false);
				relX += dimension.x;
			}
			
		}
		
		@Override
		public void update (float deltaTime)
		{
			super.update(deltaTime);
			floatCycleTimeLeft -= deltaTime;
			if (floatCycleTimeLeft<= 0)
			{
				floatCycleTimeLeft = FLOAT_CYCLE_TIME;
				floatingDownwards = !floatingDownwards;
				body.setLinearVelocity(0, FLOAT_AMPLITUDE
				* (floatingDownwards ? -1 : 1));
			} 
			else
			{
				body.setLinearVelocity(body.getLinearVelocity().scl(0.98f));
		}
		}
}
