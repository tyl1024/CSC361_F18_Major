/**
 * Group 2
 */
package com.packtpub.libgdx.canyonbunny.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.packtpub.libgdx.canyonbunny.*;
import com.packtpub.libgdx.canyonbunny.util.Assets;

/**
 * A cloud object will be one of 3 available cloud images
 */
public class Clouds extends AbstractGameObject {
	
	private float length;
	
	private Array<TextureRegion> regClouds;
	private Array<Cloud> clouds;
	
	/**
	 * Made by Philip Deppen (Assignment 4)
	 * Nested Cloud class
	 */
	private class Cloud extends AbstractGameObject {
		private TextureRegion regCloud;
		
		public Cloud() {}
		
		public void setRegion (TextureRegion region) {
			regCloud = region;
		}
		
		/**
		 * Made by Philip Deppen (Assignment 4)
		 * renders a Cloud image
		 */
		@Override
		public void render(SpriteBatch batch) {
			TextureRegion reg = regCloud;
	         batch.draw(reg.getTexture(), position.x + origin.x,
					   position.y + origin.y, origin.x, origin.y, dimension.x,
					   dimension.y, scale.x, scale.y, rotation, reg.getRegionX(),
					   reg.getRegionY(), reg.getRegionWidth(), reg.getRegionHeight(),
					   false, false);
	         
		}
	}
	
	/**
	 * Made by Philip Deppen (Assignment 4)
	 * Clouds constructor
	 */
	public Clouds (float length) {
		this.length = length;
		init();
	}
	
	/**
	 * Made by Philip Deppen (Assignment 4)
	 * initializes objects
	 */
	private void init() {
		dimension.set(3.0f, 1.5f);
		regClouds = new Array<TextureRegion>();
		regClouds.add(Assets.instance.levelDecoration.cloud01);
		regClouds.add(Assets.instance.levelDecoration.cloud02);
		regClouds.add(Assets.instance.levelDecoration.cloud03);
		
		int distFac = 5;
		int numClouds = (int) (length / distFac);
		clouds = new Array<Cloud> (2 * numClouds);
		for (int i = 0; i < numClouds; i++) {
			Cloud cloud = spawnCloud();
			cloud.position.x = i * distFac;
			clouds.add(cloud);
		}
	}
	
	/**
	 * Made by Philip Deppen (Assignment 4)
	 * Edited by Philip Deppen (Assignment 7, p.274)
	 * picks a random cloud image and creates it
	 */
	private Cloud spawnCloud() 
	{
		Cloud cloud = new Cloud();
		cloud.dimension.set(dimension);
		
		// select random cloud image
		cloud.setRegion(regClouds.random());
		// position
		Vector2 pos = new Vector2();
		pos.x = length + 10; // position after end of level
		pos.y += 1.75; // base position
		pos.y += MathUtils.random(0.0f, 0.2f) * (MathUtils.randomBoolean() ? 1: -1); // random additional position
		cloud.position.set(pos);
		
		// speed 
		Vector2 speed = new Vector2();
		speed.x += 0.5f; // base speed
		// random additional speed
		speed.x += MathUtils.random(0.0f, 0.75f);	
		cloud.terminalVelocity.set(speed);
		speed.x *= -1; // move left
		cloud.velocity.set(speed);
		
		return cloud;
	}
	
	/**
	 * Made by Philip Deppen (Assignment 4)
	 * render method inherited from AbstractGameObject
	 */
	@Override
	public void render (SpriteBatch batch) 
	{
		for (Cloud cloud: clouds)
			cloud.render(batch);
	}
	
	/**
	 * Made by Philip Deppen (Assignment 7, p.274-275)
	 * spawnCloud() will create a new cloud/
	 * This method iterates through all existing clouds to let the physics move them.
	 * The cloud's position is checked to see if it moved off screen.
	 */
	@Override
	public void update(float deltaTime)
	{
		for (int i = clouds.size - 1; i >= 0; i--)
		{
			Cloud cloud = clouds.get(i);
			cloud.update(deltaTime);
			if (cloud.position.x < -10) 
			{
				// cloud moved outside of world
				// destroy and new cloud at end of level
				clouds.removeIndex(i);
				clouds.add(spawnCloud());
			}
		}
	}
}
