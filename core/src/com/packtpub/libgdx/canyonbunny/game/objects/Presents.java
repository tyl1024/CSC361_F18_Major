/**
 * Philip Deppen
 * Group 2
 */
package com.packtpub.libgdx.canyonbunny.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.packtpub.libgdx.canyonbunny.util.Assets;
import com.badlogic.gdx.math.MathUtils;

public class Presents extends AbstractGameObject 
{
	private TextureRegion regPresent;
	
	public boolean collected;
	
	/**
	 * Made by Philip Deppen (Assignment 5)
	 */
	public Presents() 
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
		
		
		batch.draw(Assets.instance.gift.gift, position.x, position.y + 4.2f, 1.5f,1.5f);
		//batch.draw(Assets.instance.gift.gift, 6.5f, 1.5f,1.5f,1.5f);
		
	}
	
	/**
	 * Made by Philip Deppen (Assignment 5)
	 */
	public int getScore() 
	{
		return 50;
	}
	
}
