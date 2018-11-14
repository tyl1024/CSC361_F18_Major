package com.packtpub.libgdx.canyonbunny.game.objects;

import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.packtpub.libgdx.canyonbunny.game.objects.*;
import com.packtpub.libgdx.canyonbunny.game.objects.SantaHead.VIEW_DIRECTION;
import com.packtpub.libgdx.canyonbunny.util.Assets;
import com.packtpub.libgdx.canyonbunny.util.Constants;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.graphics.g2d.Animation;

/**
 * @author Owen Burnham (Assignment 5)
 * edited by Owen Burnham (Assignment 6)
 * edited by Owen Burnham (Assignment 7)
 * This class is for the bunny head object.
 * It is the player's character and consists of only
 * one image, but has to account for jumping, falling,
 * and picking up objects
 */
public class SantaHead extends AbstractGameObject
{
	public static final String TAG = SantaHead.class.getName();
	public VIEW_DIRECTION viewDirection;

	
	
	
	// different directions views
	public enum VIEW_DIRECTION { LEFT, RIGHT }
	
	// different jump states
	public enum JUMP_STATE 
	{
		GROUNDED, FALLING, JUMP_RISING, JUMP_FALLING
	}
	
	private TextureRegion regHead;
	
	/**
	 * initializes the bunny head object
	 */
	public SantaHead () 
	{
		init();
	}
	
	/**
	 * Edited by Owen Burnham (Assignment 7)
	 * Initializes the bunny head game object by setting
	 * its physics values, a starting view direction, and 
	 * jump state.  Also deactivates the feather power-up 
	 * effect.
	 */
	public void init () 
	{
		dimension.set(1, 1);
		regHead = Assets.instance.santa.body;
		// Center image on game object
		origin.set(dimension.x / 2, dimension.y / 2);
		// View direction
		viewDirection = VIEW_DIRECTION.RIGHT;

	}
	
	

	
	@Override
	/**
	 * Edited by Owen Burnham (Assignment 7)
	 * Handles the switching of the viewing direction accoriding
	 * to the current move direction.  The time remaining of the 
	 * power-up effect is also checked.  If the time is up, the
	 * feather power-up effect is disabled.
	 * 
	 * Tyler Major updated for Assignment 12
	 * Changes animation state if feather is picked up
	 */
	public void update (float deltaTime)
	{
		super.update(deltaTime);
	}
	
	
	
	@Override
	/**
	 * Edited by Owen Burnham (Assignment 7)
	 * Edited by Owen Burnham (Assignment 6)
	 * Handles the drawing of the image for the bunny head
	 * game object.  Image will be tinted orange if the feather 
	 * power-up effect is active.
	 * 
	 * Updated by Tyler Major on 10/20/2018 with pg_391
	 *  In the render() method, the color-tinting effect has been removed for
		new animations. If an animation other than the standard one (animNormal) is
		detected, the correcting values to the width and heightwill be applied for rendering.
		Since the standard animation is of a different dimension than the other animations,
		the other ones will look off-centered without the correcting values.
	 */
	public void render (SpriteBatch batch) 
	{
		TextureRegion reg = null;
		
		// Draw Santa
		batch.draw(Assets.instance.santa.body,0.9f,1.5f,2.6f,3.8f);
		
		// Reset color to white
		batch.setColor(1, 1, 1, 1);
	}
}
