/*  Tyler Major 
 *  10/31/2018
 */
package com.mygdx.game;


import com.badlogic.gdx.Application.ApplicationType;
import com.packtpub.libgdx.canyonbunny.Level;
import com.packtpub.libgdx.canyonbunny.game.objects.santa;
import com.packtpub.libgdx.canyonbunny.game.objects.snowflake;
import com.packtpub.libgdx.canyonbunny.game.objects.presents;
import com.packtpub.libgdx.canyonbunny.game.objects.platform;
import com.packtpub.libgdx.canyonbunny.game.objects.platform;
import com.packtpub.libgdx.canyonbunny.game.objects.presents;
import com.packtpub.libgdx.canyonbunny.game.objects.santa;
import com.packtpub.libgdx.canyonbunny.game.objects.snowflake;
import com.packtpub.libgdx.canyonbunny.game.objects.santa.JUMP_STATE;
import com.packtpub.libgdx.canyonbunny.util.Constants;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Disposable;
import com.packtpub.libgdx.canyonbunny.util.Assets;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

public class WorldController extends InputAdapter implements Disposable
	{
	
		
		public CameraHelper cameraHelper;
		
		private static final String TAG =
		WorldController.class.getName();
		
		private boolean goalReached;
		public float scoreVisual; 
		public float livesVisual;
		public Level level;
		public int lives;
		public int score;
		private float timeLeftGameOverDelay;
		// Rectangles for collision detection
		private Rectangle r1 = new Rectangle();
		private Rectangle r2 = new Rectangle();
		public World b2world;
		
		private void initLevel () 
		{
			score = 0;
			scoreVisual = score;
			goalReached = false;
			level = new Level (Constants.LEVEL_01);
			cameraHelper.setTarget(level.body);
		}
		
		
		/**
		 * Creates an instance of WorldController
		 * The constructor
		 */
		public WorldController () 
		{ 
			init();
		}
		
		/**
		 * internal init (useful when reseting an object) 
		 */
		private void init () 
		{ 
			Gdx.input.setInputProcessor(this);
			cameraHelper = new CameraHelper();
			lives = Constants.LIVES_START;
			initLevel();
		}
		
		
		
		//Setting pixamp and coloring and drawing rectangle
		private Pixmap createProceduralPixmap (int width, int height)
		{
			Pixmap pixmap = new Pixmap(width, height, Format.RGBA8888);
			// Fill square with red color at 50% opacity
			pixmap.setColor(1, 0, 0, 0.5f);
			pixmap.fill();
			// Draw a yellow-colored X shape on square
			pixmap.setColor(1, 1, 0, 1);
			pixmap.drawLine(0, 0, width, height);
			pixmap.drawLine(width, 0, 0, height);
			// Draw a cyan-colored border around square
			pixmap.setColor(0, 1, 1, 1);
			pixmap.drawRectangle(0, 0, width, height);
			return pixmap;
		}

		/**
		 * contains game logic
		 * called several hundred times per sec
		 * @param deltaTime
		 */
		public void update(float deltaTime) 
		{
			handleDebugInput(deltaTime);
			if (isGameOver() || goalReached)
			{
				timeLeftGameOverDelay -= deltaTime;
				if (timeLeftGameOverDelay < 0)
				{
					backToMenu();	
					return;
				}
			}
			else 
			{
				handleInputGame(deltaTime);
			}
			if (!isGameOver() && isPlayerInWater()) {
				
				lives--;
				if (isGameOver())
					timeLeftGameOverDelay = Constants.TIME_DELAY_GAME_OVER;
				else
					initLevel();
			}
			level.update(deltaTime);
			testCollisions();
			cameraHelper.update(deltaTime);
		}
		
		/**
		 * Made by Philip Deppen (Assignment 5)
		 * Edited by Philip Deppen (Assignment 9, p.352)
		 * iterates through all the game objects and tests whether
		 * there is a collision between the bunny head and another game object
		 */
		private void testCollisions() 
		{
			r1.set(level.body.position.x, level.body.position.y,
				   level.body.bounds.width, level.body.bounds.height);
			
			// Test collision: Bunny Head <-> Rocks
		     for (platform platform : level.platform) 
		     {
		       r2.set(platform.position.x, platform.position.y, platform.bounds.width,
		    		      platform.bounds.height);
		       if (!r1.overlaps(r2)) continue;
	       			onCollisionSantaWithPlatform(platform);
		       // IMPORTANT: must do all collisions for valid
		       // edge testing on rocks.
		     }
		     
		     // Test collision: Bunny Head <-> Gold Coins
		     for (presents present : level.gift) 
		     {
		       if (present.collected) continue;
		       		r2.set(present.position.x, present.position.y,
		       			   present.bounds.width, present.bounds.height);
		       
		       if (!r1.overlaps(r2)) continue;
		       onCollisionSantaWithPresents(present);
		       break;
		     }
		     // Test collision: Bunny Head <-> Feathers
		     for (snowflake flake : level.flake) 
		     {
		       if (flake.collected) continue;
		       r2.set(flake.position.x, flake.position.y,
		    		   flake.bounds.width, flake.bounds.height);
		       
		       if (!r1.overlaps(r2)) continue;
		       onCollisionSantaWithSnowflake(flake);
		       break;
		     }
		     
		     // Test collision: Bunny Head <-> Goal
		     if (!goalReached)
		     {
		    	 	r2.set(level.goal.bounds);
		    	 	r2.x += level.goal.position.x;
		    	 	r2.y += level.goal.position.y;
		    	 	
		    	 	if (r1.overlaps(r2))
		    	 		onCollisionSantaWithGoal();
		     }
		}
		
		//Keys to move around the sprites
		private void handleDebugInput (float deltaTime)
		{
			
			
			// Camera Controls (move)
			float camMoveSpeed = 5 * deltaTime;
			float camMoveSpeedAccelerationFactor = 5;
			if (Gdx.input.isKeyPressed(Keys.SHIFT_LEFT)) camMoveSpeed *=
			camMoveSpeedAccelerationFactor;
			if (Gdx.input.isKeyPressed(Keys.LEFT)) moveCamera(-camMoveSpeed,
			0);
			if (Gdx.input.isKeyPressed(Keys.RIGHT)) moveCamera(camMoveSpeed,
			0);
			if (Gdx.input.isKeyPressed(Keys.UP)) moveCamera(0, camMoveSpeed);
			if (Gdx.input.isKeyPressed(Keys.DOWN)) moveCamera(0,
			-camMoveSpeed);
			if (Gdx.input.isKeyPressed(Keys.BACKSPACE))
			cameraHelper.setPosition(0, 0);
			// Camera Controls (zoom)
			float camZoomSpeed = 1 * deltaTime;
			float camZoomSpeedAccelerationFactor = 5;
			if (Gdx.input.isKeyPressed(Keys.SHIFT_LEFT)) camZoomSpeed *=
			camZoomSpeedAccelerationFactor;
			if (Gdx.input.isKeyPressed(Keys.COMMA))
			cameraHelper.addZoom(camZoomSpeed);
			if (Gdx.input.isKeyPressed(Keys.PERIOD)) cameraHelper.addZoom(
			-camZoomSpeed);
			if (Gdx.input.isKeyPressed(Keys.SLASH)) cameraHelper.setZoom(1);
		}
		
			private void moveCamera (float x, float y)
			{
				x += cameraHelper.getPosition().x;
				y += cameraHelper.getPosition().y;
				cameraHelper.setPosition(x, y);
			}
			
			/**
			 * Made by Philip Deppen (Assignment 5)
			 * allows player to be controllable with left and right arrow keys
			 */
			private void handleInputGame (float deltaTime) 
			{
			   if (cameraHelper.hasTarget(level.body)) 
			   {
				   // Player Movement
				   if (Gdx.input.isKeyPressed(Keys.LEFT)) 
				   {
					   level.body.velocity.x = -level.body.terminalVelocity.x;
				   } 
				   else if (Gdx.input.isKeyPressed(Keys.RIGHT)) 
				   {
					   level.body.velocity.x = level.body.terminalVelocity.x;
				   } 
				   else 
				   {
					   // Execute auto-forward movement on non-desktop platform
					   if (Gdx.app.getType() != ApplicationType.Desktop) 
					   {
						   level.body.velocity.x = level.body.terminalVelocity.x;
					   }
				   }
				   // Bunny Jump
				   if (Gdx.input.isTouched() || Gdx.input.isKeyPressed(Keys.SPACE)) 
				   {
			         level.body.setJumping(true);
			       } else 
			       {
			         level.body.setJumping(false);
			       }
			   }
		   	}
		
		//keys to reset sprites and space bar selects sprites
		public boolean keyUp(int keycode) 
		{
			// Reset game world
			if (keycode == Keys.R) 
			{
				init();
				Gdx.app.debug(TAG, "Game world resetted");
			}
			return false;
		}

		@Override
		public void dispose() 
		{
			// TODO Auto-generated method stub
			
		}
		
		/**
		 * Made by Philip Deppen (Assignment 5)
		 */
		public boolean isGameOver()
		{
			return lives < 0;
		}
		
		/**
		 * Made by Philip Deppen (Assignment 5)
		 */
		public boolean isPlayerInWater()
		{
			return level.body.position.y < -5;
		}
		
		/**
		 * Made by Philip Deppen (Assignment 5)
		 * flags the gold coin as being collected so that it will disappear
		 */
		private void onCollisionSantaWithPresents(presents gift) 
		{
			gift.collected = true;
			score += gift.getScore();
			Gdx.app.log(TAG, "Present collected");
		}
		
		/**
		 * Made by Philip Deppen (Assignment 5)
		 * handles collisions between the bunny and rock objects
		 * called when a collision is detected
		 */
		private void onCollisionSantaWithPlatform(platform platform) 
		{
			santa body = level.body;
			float heightDifference = Math.abs(body.position.y - (platform.position.y + platform.bounds.height));
			
			if (heightDifference > 0.25f) 
			{
				boolean hitRightEdge = body.position.x > (platform.position.x + platform.bounds.width / 2.0f);
				if (hitRightEdge) 
				{
					body.position.x = platform.position.x + platform.bounds.width;
				}
				else 
				{
					body.position.x = platform.position.x - platform.bounds.width;
				}
				return;
			}
			
			switch (body.jumpState) 
			{
				case GROUNDED:
					break;
				case FALLING:
				case JUMP_FALLING:
					body.position.y = platform.position.y + body.bounds.height  + body.origin.y;
					body.jumpState = JUMP_STATE.GROUNDED;
					break;
				case JUMP_RISING:
					 body.position.y = platform.position.y + body.bounds.height + body.origin.y;
					break;
			}
		}
		

		/**
		 * Made by Philip Deppen (Assignment 5)
		 * flags the feather as being collected and also activates/refreshes the power-up effect
		 */
		private void onCollisionSantaWithSnowflake(snowflake flake) 
		{
			flake.collected = true;
			score += flake.getScore();
			level.body.setFlakePowerup(true);
			Gdx.app.log(TAG, "Snowflake collected");
		}
		
		/**
		 * Created by Owen Burnham (Assignment 6)
		 * This method allows us to save a reference to 
		 * the game instance, which will enable us to switch 
		 * to another screen
		 */
		private void backToMenu()
		{
			// switch to menu screen
		//	game.setScreen(new MenuScreen(game));
			return;
		}
		
		/**
		 * Made by Philip Deppen (Assignment 9, p.351)
		 * handles the event when the player passes the goal-level object
		 */
		private void onCollisionSantaWithGoal() 
		{
			goalReached = true;
			timeLeftGameOverDelay = Constants.TIME_DELAY_GAME_FINISHED;
			Vector2 centerPosBunnyHead = new Vector2(level.body.position);
			centerPosBunnyHead.x += level.body.bounds.width;
		}
		
		
}