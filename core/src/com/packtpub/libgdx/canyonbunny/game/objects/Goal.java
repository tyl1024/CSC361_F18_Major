/**
 * Group 2
 * Made by Philip Deppen Assignemnt 9
 */
package com.packtpub.libgdx.canyonbunny.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.packtpub.libgdx.canyonbunny.util.Assets;

public class Goal extends AbstractGameObject
{
	private TextureRegion regGoal;
	
	/**
	 * Made by Philip Deppen (Assignment 9, p.341)
	 * Goal Constructor
	 */
	public Goal () 
	{
		init();
	}
	
	/**
	 * Made by Philip Deppen (Assignment 9, p.341)
	 * initializes class
	 * objects in infinitely tall compared to the other game objects
	 * so that the player will always collide with it
	 */
	private void init() 
	{
		dimension.set(3.0f, 3.0f);
		regGoal = Assets.instance.levelDecoration.goal;
		
		origin.set(dimension.x / 2.0f, 0.0f);
	}
	
	/**
	 * Made by Philip Deppen (Assignment 9, p.341)
	 * renders the goal object
	 */
	public void render (SpriteBatch batch) 
	{
		TextureRegion reg = null;
		
		reg = regGoal;
		batch.draw(Assets.instance.levelDecoration.goal,position.x -1.5f,position.y + 1.5f, 5.5f, 5.5f);
	}
	
}
