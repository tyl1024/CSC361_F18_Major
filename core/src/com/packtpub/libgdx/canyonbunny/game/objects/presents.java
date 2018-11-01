/**
 * Tyler Major
 */
package com.packtpub.libgdx.canyonbunny.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.packtpub.libgdx.canyonbunny.util.Assets;
import com.badlogic.gdx.math.MathUtils;

public class presents extends AbstractGameObject 
{
	private TextureRegion presents;
	
	public boolean collected;
	
	public presents() 
	{
		init();
	}
	
	/**

	 * Sets the present animation
	 */
	private void init() 
	{
		dimension.set(0.5f, 0.5f);
		
		stateTime = MathUtils.random(0.0f, 1.0f);
				
		// set bounding box for collision detection
		bounds.set(0, 0, dimension.x, dimension.y);
		
		collected = false;
	}
	
	/**
	 * Render presents
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
	 * getScore when presents collected
	 */
	public int getScore() 
	{
		return 25;
	}
	
}
