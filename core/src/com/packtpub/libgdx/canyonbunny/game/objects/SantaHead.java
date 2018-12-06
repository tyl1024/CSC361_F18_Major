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
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.packtpub.libgdx.canyonbunny.util.CharacterSkin;
import com.packtpub.libgdx.canyonbunny.util.GamePreferences;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;

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
	private final float JUMP_TIME_MAX = 0.3f;
	private final float JUMP_TIME_MIN = 0.1f;
	private final float JUMP_TIME_OFFSET_FLYING =
	JUMP_TIME_MAX - 0.018f;
	
	public VIEW_DIRECTION viewDirection;
	public float timeJumping;
	public JUMP_STATE jumpState;
	public boolean hasFlakePowerup;
	public float timeLeftFlakePowerup;
	public ParticleEffect dustParticles = new ParticleEffect();

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
		regHead = Assets.instance.body2.body2;
		// Center image on game object
		origin.set(dimension.x / 2, dimension.y / 2);
		// View direction
		viewDirection = VIEW_DIRECTION.RIGHT;
		dimension.set(1, 1);	
		// Bounding box for collision detection
		bounds.set(0, 0, dimension.x, dimension.y);
		// Set physics values
		terminalVelocity.set(3.0f, 4.0f);
		friction.set(12.0f, 0.0f);
		acceleration.set(0.0f, -25.0f);
		// View direction
		viewDirection = VIEW_DIRECTION.RIGHT;
		// Jump state
		jumpState = JUMP_STATE.FALLING;
		timeJumping = 0;
		// Power-ups
		hasFlakePowerup = false;
		timeLeftFlakePowerup = 0;
		// Particles
		dustParticles.load(Gdx.files.internal("particles/dustParticle.pfx"),
				Gdx.files.internal("particles"));

	}
	
	public void setJumping (boolean jumpKeyPressed)
	{
		switch (jumpState) 
		{
		
		case GROUNDED: // Character is standing on a platform
		if (velocity.x != 0)
		{
				dustParticles.setPosition(position.x + dimension.x/2, position.y);
				dustParticles.start();
		}
		if (jumpKeyPressed)
		{
			// Start counting jump time from the beginning
			timeJumping = 0;
			jumpState = JUMP_STATE.JUMP_RISING;
			body.setLinearVelocity(new Vector2(0,5.4f));
			
		}
		break;
		case JUMP_RISING: // Rising in the air
		if (!jumpKeyPressed)
			jumpState = JUMP_STATE.JUMP_FALLING;

		break;
		case FALLING:// Falling down
		case JUMP_FALLING: // Falling down after jump
			if (jumpKeyPressed && hasFlakePowerup) 
			{
					timeJumping = JUMP_TIME_OFFSET_FLYING;
					jumpState = JUMP_STATE.JUMP_RISING;
			}
				break;
		}
	
	}	
			
	public void setFlakePowerup (boolean pickedUp) 
	{
		hasFlakePowerup = pickedUp;
		if (pickedUp) {
		timeLeftFlakePowerup =
		Constants.ITEM_SNOWFLAKE_POWERUP_DURATION;
		}
		}
		public boolean hasFlakePowerup () 
		{
			return hasFlakePowerup && timeLeftFlakePowerup > 0;
		}
	
	

	@Override
	public void update (float deltaTime) 
	{
			super.update(deltaTime);
			if (velocity.x != 0) 
			{
				viewDirection = velocity.x < 0 ? VIEW_DIRECTION.LEFT :
				VIEW_DIRECTION.RIGHT;
			}
			if (timeLeftFlakePowerup > 0) 
			{
				timeLeftFlakePowerup -= deltaTime;
			}
			if (timeLeftFlakePowerup < 0) 
			{
			// disable power-up
				timeLeftFlakePowerup = 0;
				setFlakePowerup(false);
			}
			dustParticles.update(deltaTime);
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
		// Draw Particles
		dustParticles.draw(batch);
		
		// Set special color when game object has a feather power-up
		if (hasFlakePowerup) 
		{
			batch.setColor(1.0f, 0.8f, 0.0f, 1.0f);
		}
		
		// Apply Skin Color
		batch.setColor(
		CharacterSkin.values()[GamePreferences.instance.charSkin]
		.getColor());
		
		// Draw image
		reg = regHead;
		batch.draw(reg.getTexture(), position.x, position.y, origin.x,
		origin.y, dimension.x, dimension.y, scale.x , scale.y, rotation,
		reg.getRegionX(), reg.getRegionY(), reg.getRegionWidth(),
		reg.getRegionHeight(), viewDirection == VIEW_DIRECTION.LEFT,
		false);
		
		// Draw Santa
		//batch.draw(Assets.instance.santa.body2,0.9f,1.5f,2.6f,3.8f);
		
		// Reset color to white
		batch.setColor(1, 1, 1, 1);
	}
	
	@Override
	protected void updateMotionY (float deltaTime)
	{
		switch (jumpState)
		{
			case GROUNDED:
			jumpState = JUMP_STATE.FALLING;
			break;
			case JUMP_RISING:
			// Keep track of jump time
			timeJumping += deltaTime;
			// Jump time left?
			if (timeJumping <= JUMP_TIME_MAX) 
			{
				// Still jumping
				velocity.y = terminalVelocity.y;
			}
			break;
			case FALLING:
			break;
			case JUMP_FALLING:
				// Add delta times to track jump time
				timeJumping += deltaTime;
			// Jump to minimal height if jump key was pressed too short
			if (timeJumping > 0 && timeJumping <= JUMP_TIME_MIN)
			{
				// Still jumping
				velocity.y = terminalVelocity.y;
			}
		}
			if (jumpState != JUMP_STATE.GROUNDED)
			{
				dustParticles.allowCompletion();
				super.updateMotionY(deltaTime);
			}
	}

}