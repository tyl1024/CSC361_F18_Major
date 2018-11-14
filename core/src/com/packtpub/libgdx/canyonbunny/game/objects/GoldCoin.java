/**
 * Philip Deppen
 * Group 2
 */
package com.packtpub.libgdx.canyonbunny.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.packtpub.libgdx.canyonbunny.util.Assets;
import com.badlogic.gdx.math.MathUtils;

public class GoldCoin extends AbstractGameObject {
	private TextureRegion regGoldCoin;
	
	public boolean collected;
	
	/**
	 * Made by Philip Deppen (Assignment 5)
	 */
	public GoldCoin() 
	{
		init();
	}
	
	/**
	 * Made by Philip Deppen (Assignment 5)
	 * Edited by Philip Deppen (Assignment 10, p.385)
	 * Sets the gold coin animation
	 */
	private void init() 
	{
		dimension.set(0.5f, 0.5f);
		
		setAnimation(Assets.instance.goldCoin.animGoldCoin);
		stateTime = MathUtils.random(0.0f, 1.0f);
				
		// set bounding box for collision detection
		bounds.set(0, 0, dimension.x, dimension.y);
		
		collected = false;
	}
	
	/**
	 * Made by Philip Deppen (Assignment 5)
	 * Edited by Philip Deppen (Assignment 10, p.386)
	 */
	public void render (SpriteBatch batch) 
	{
		if (collected) return;
		
		TextureRegion reg = null;
		reg = (TextureRegion) animation.getKeyFrame (stateTime, true);
		
		batch.draw(reg.getTexture(), position.x, position.y,
				   origin.x, origin.y, dimension.x, dimension.y, scale.x, scale.y,
				   rotation, reg.getRegionX(), reg.getRegionY(),
				   reg.getRegionWidth(), reg.getRegionHeight(), false, false);
		
	}
	
	/**
	 * Made by Philip Deppen (Assignment 5)
	 */
	public int getScore() 
	{
		return 100;
	}
	
}
