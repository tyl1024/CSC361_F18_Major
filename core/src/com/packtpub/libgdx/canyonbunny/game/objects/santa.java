package com.packtpub.libgdx.canyonbunny.game.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.packtpub.libgdx.canyonbunny.game.objects.*;
import com.packtpub.libgdx.canyonbunny.util.Assets;
import com.packtpub.libgdx.canyonbunny.util.Constants;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.graphics.g2d.Animation;

/**
 * @author Tyler Major
 * This class is for the santa object.
 * It is the player's character and consists of only
 * one image, but has to account for jumping, falling,
 * and picking up objects
 */
public class santa extends AbstractGameObject
{
	public static final String TAG = santa.class.getName();
	
	private final float JUMP_TIME_MAX = 0.3f;
	private final float JUMP_TIME_MIN = 0.1f;
	private final float JUMP_TIME_OFFSET_FLYING = 
			JUMP_TIME_MAX - 0.018f;
	

		
	// different directions views
	public enum VIEW_DIRECTION { LEFT, RIGHT }
	
	// different jump states
	public enum JUMP_STATE 
	{
		GROUNDED, FALLING, JUMP_RISING, JUMP_FALLING
	}
	
	private TextureRegion body;
	
	public VIEW_DIRECTION viewDirection;
	public float timeJumping;
	public JUMP_STATE jumpState;
	public boolean hasFlakePowerup;
	public float timeLeftFlakePowerup;
	
	
	/**
	 * initializes the santa object
	 */
	public santa () 
	{
		init();
	}
	
	/**
	 * Edited by Tyler
	 * Initializes the santa game object by setting
	 * its physics values, a starting view direction, and 
	 * jump state.  Also deactivates the feather power-up 
	 * effect.
	 */
	public void init () 
	{
		dimension.set(1, 1);
		body = Assets.instance.santa.body;
		
		
		// Center image on game object
		origin.set(dimension.x / 2, dimension.y / 2);
		// Bounding box for collision detection
		bounds.set(0, 0, dimension.x, dimension.y);
		//bounding box fo collision
		bounds.set(0,0,dimension.x,dimension.y);

		// View direction
		viewDirection = VIEW_DIRECTION.RIGHT;
		// Jump state
		jumpState = JUMP_STATE.FALLING;
		timeJumping = 0;
		// Power-ups
		hasFlakePowerup = false;
		timeLeftFlakePowerup = 0;
		
	}
	
	/**
	 * Allows us to make santa jump.
	 * The state handling in the code will decide whether
	 * jumping is currently possible and whether it is a 
	 * single or a multi jump.
	 * @param jumpKeyPressed
	 * 
	 * Tyler Major added pg326-327 code to BunnyHead
	 * The changes in the code for santa trigger the sound effects for the jumped and
     * jumped-in-mid-air events at the right time. The jumpWithFeather sound is played
     *  using a different play() method of the AudioManager class. It is also provided with
     *  a random pitch value in the range from 1.0 to 1.1, which adds a little change in the
     *  frequency, rendering the rapidly repeated sound effect more interesting.
	 */
	public void setJumping (boolean jumpKeyPressed) 
	{
		switch (jumpState)	
		{
		case GROUNDED: // Character is standind on a platform
			if (jumpKeyPressed)
			{
				// Start counting jump time from the beginning
				timeJumping = 0;
				jumpState = JUMP_STATE.JUMP_RISING;
			}
			break;
		case JUMP_RISING: // Rising in the air
			if (!jumpKeyPressed)
				jumpState = JUMP_STATE.JUMP_FALLING;
			break;
		case FALLING: // Falling down
		case JUMP_FALLING: // Falling down after jump
			if (jumpKeyPressed && hasFlakePowerup)
			{
				timeJumping = JUMP_TIME_OFFSET_FLYING;
				jumpState = JUMP_STATE.JUMP_RISING;
			}
			break;
		}
	}
	
	
	/**
	 * Allows us to toggle the feather power-up effect
	 * @param pickedUp
	 */
	public void setFlakePowerup (boolean pickedUp) 
	{
		hasFlakePowerup = pickedUp;
		if (pickedUp)
		{
			timeLeftFlakePowerup = 
					Constants.ITEM_SNOWFLAKE_POWERUP_DURATION;
		}
	}

	
	/**
	 * Finds out whether the power-up is still active 
	 * @return if power-up is still still active
	 */
	public boolean hasFlakePowerup () 
	{
		return hasFlakePowerup && timeLeftFlakePowerup > 0;
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
		
		if (timeLeftFlakePowerup > 0)
		{
			timeLeftFlakePowerup -= deltaTime;
			if (timeLeftFlakePowerup < 0)
			{
				// disable power-up
				timeLeftFlakePowerup = 0;
				setFlakePowerup(false);
			}
		}
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
		
		// Set special color when game object has a feather power-up
		if (hasFlakePowerup)
		{
			batch.setColor(1.0f, 0.8f, 0.0f, 1.0f);
		}
		
		// Draw image
		reg = body;
		batch.draw(reg.getTexture(), position.x, position.y, origin.x, 
				origin.y, dimension.x, dimension.y, scale.x, scale.y, rotation, 
				reg.getRegionX(), reg.getRegionY(), reg.getRegionWidth(), 
				reg.getRegionHeight(), viewDirection == VIEW_DIRECTION.LEFT, 
				false);
		
		// Reset color to white
		batch.setColor(1, 1, 1, 1);
	}
}
