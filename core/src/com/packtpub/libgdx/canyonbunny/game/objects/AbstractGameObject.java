package com.packtpub.libgdx.canyonbunny.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.graphics.g2d.Animation;

/**
 * @author Tyler Majoe
 * This class will hold all the common attributes
 * and fucntionalities that each of our game objects
 * inherit from
 */
public abstract class AbstractGameObject 
{
	public Vector2 position;
	public Vector2 dimension;
	public Vector2 origin;
	public Vector2 scale;
	public float rotation;
	public Vector2 velocity;
	public Vector2 terminalVelocity;
	public Vector2 friction;
	
	public Vector2 acceleration;
	public Rectangle bounds;
	
	public Body body;
	public float stateTime;
	public Animation<TextureRegion> animation;
	
	/**
	 * Makes instances of all the variables for the 
	 * abstract game object
	 */
	public AbstractGameObject() 
	{
		position = new Vector2();
		dimension = new Vector2(1, 1);
		origin = new Vector2();
		scale = new Vector2(1, 1);
		rotation = 0;
	}
	
	/**
	 * updates objects accordingly in relevance 
	 * to the delta time
	 * updates motion for both x and y
	 */
	public void update(float deltaTime) 
	{
		
	}
	
	/**
	 * @param batch
	 * objects are rendered accordingly
	 */
	public abstract void render(SpriteBatch batch);
	
}
