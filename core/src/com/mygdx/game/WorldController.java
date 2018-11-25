/**
 * Tyler Major
 */
package com.mygdx.game;

import com.badlogic.gdx.Application.ApplicationType;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.packtpub.libgdx.canyonbunny.game.objects.Platform;
import com.packtpub.libgdx.canyonbunny.game.objects.Presents;
import com.packtpub.libgdx.canyonbunny.game.objects.SantaHead;
import com.packtpub.libgdx.canyonbunny.game.objects.SantaHead.JUMP_STATE;
import com.packtpub.libgdx.canyonbunny.game.objects.Snowflake;
import com.packtpub.libgdx.canyonbunny.util.Constants;
import com.packtpub.libgdx.canyonbunny.*;


public class WorldController extends InputAdapter
{
	public Level level;
	public int lives;
	public int score;
	public World b2world;
	private float timeLeftGameOverDelay;

	
	private static final String TAG = 
		WorldController.class.getName();
	
	public CameraHelper cameraHelper;
	
	/**
	 * constructor 
	 */
	public WorldController() 
	{
		init();
	}
	
	/**
	 * internal init (useful when reseting an object) 
	 */
	public void init() 
	{
		Gdx.input.setInputProcessor(this);
		cameraHelper = new CameraHelper();
		lives = Constants.LIVES_START;
		initLevel();
	}
	
	
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
		handleInputGame(deltaTime);
		
		if (!isGameOver() && isPlayerInWater())
		{
			
			lives--;
			if (isGameOver())
				timeLeftGameOverDelay = Constants.TIME_DELAY_GAME_OVER;
			else
				initLevel();
		}
		
		level.update(deltaTime);
		b2world.step(deltaTime, 8, 3);
		cameraHelper.update(deltaTime);
	}
	
	private void handleDebugInput (float deltaTime)
	{
		if (Gdx.app.getType() != ApplicationType.Desktop) 
			return;
				
		// Camera Controls (move)
		float camMoveSpeed = 5 * deltaTime;
		float camMoveSpeedAccelerationFactor = 5;
		if (Gdx.input.isKeyPressed(Keys.SHIFT_LEFT)) 
			camMoveSpeed *= camMoveSpeedAccelerationFactor;
		if (Gdx.input.isKeyPressed(Keys.LEFT))
			moveCamera(-camMoveSpeed, 0);
		if (Gdx.input.isKeyPressed(Keys.RIGHT)) 
			moveCamera(camMoveSpeed, 0);
		if (Gdx.input.isKeyPressed(Keys.UP))
			moveCamera(0, camMoveSpeed);
		if (Gdx.input.isKeyPressed(Keys.DOWN))
			moveCamera(0, -camMoveSpeed);
		if (Gdx.input.isKeyPressed(Keys.BACKSPACE))
			cameraHelper.setPosition(0, 0);
		
		// Camera Controls (zoom)
		float camZoomSpeed = 1 * deltaTime;
		float camZoomSpeedAccelerationFactor = 5;
		if (Gdx.input.isKeyPressed(Keys.SHIFT_LEFT))
			camZoomSpeed *= camZoomSpeedAccelerationFactor;
		if (Gdx.input.isKeyPressed(Keys.COMMA))
			cameraHelper.addZoom(camZoomSpeed);
		if (Gdx.input.isKeyPressed(Keys.PERIOD)) 
			cameraHelper.addZoom(-camZoomSpeed);
		if (Gdx.input.isKeyPressed(Keys.SLASH))
			cameraHelper.setZoom(1);
	}
	
	private void moveCamera (float x, float y) 
	{
		x += cameraHelper.getPosition().x;
		y += cameraHelper.getPosition().y;
		cameraHelper.setPosition(x, y);
	}
	
	
	/**
	 * Restarts world
	 */
	@Override
	public boolean keyUp (int keycode) 
	{
		// Reset game world
		if (keycode == Keys.R) 
		{
			init();
			Gdx.app.debug(TAG, "Game world resetted");
		}
		
		return false;
	}
	
	/**
	 * Initializes level
	 */
	private void initLevel() 
	{
		score = 0;
		level = new Level (Constants.LEVEL_01);
		cameraHelper.setTarget(level.body2);
		initPhysics();
	}
		
	public void initPhysics()
    {
        if (b2world != null) 
            b2world.dispose();
        b2world = new World(new Vector2(0, -9.81f), true);
        b2world.setContactListener(new physicsTest(this));
        
        Vector2 origin = new Vector2();
        
        // creating the rock body
        for (Platform platform : level.platform)
        {
            BodyDef bodyDef = new BodyDef();
            bodyDef.type = BodyType.KinematicBody;
            bodyDef.position.set(platform.position);
            Body body = b2world.createBody(bodyDef);
            // set user data
            body.setUserData(platform);
            platform.body = body;
            PolygonShape polygonShape = new PolygonShape();
            origin.x = platform.bounds.width / 2.0f;
            origin.y = platform.bounds.height / 2.0f;
            polygonShape.setAsBox(platform.bounds.width / 2.0f,
                    platform.bounds.height / 2.0f,origin, 0);
            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.friction = 0.5f;
            fixtureDef.shape = polygonShape;
            body.createFixture(fixtureDef);
            polygonShape.dispose();
        }
        
        // creating the presents body
        for (Presents gift : level.gift)
        {
            BodyDef bodyDef = new BodyDef();
            bodyDef.type = BodyType.KinematicBody;
            bodyDef.position.set(gift.position);
            Body body = b2world.createBody(bodyDef);
            body.setUserData(gift);
            
            gift.body = body;
            PolygonShape polygonShape = new PolygonShape();
            origin.x = gift.bounds.width / 2.0f;
            origin.y = gift.bounds.height / 2.0f;
            polygonShape.setAsBox(gift.bounds.width / 2.0f,
                    gift.bounds.height / 2.0f,origin, 0);
            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.shape = polygonShape;
            fixtureDef.isSensor = true;
            body.createFixture(fixtureDef);
            polygonShape.dispose();
        }
       
        // creating the snowflake body
        for (Snowflake flake : level.flake)
        {
            BodyDef bodyDef = new BodyDef();
            bodyDef.type = BodyType.KinematicBody;
            bodyDef.position.set(flake.position);
            Body body = b2world.createBody(bodyDef);
            // set user data
            body.setUserData(flake);
            
            flake.body = body;
            PolygonShape polygonShape = new PolygonShape();
            origin.x = flake.bounds.width / 2.0f;
            origin.y = flake.bounds.height / 2.0f;
            polygonShape.setAsBox(flake.bounds.width / 2.0f,
                    flake.bounds.height / 2.0f,origin, 0);
            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.shape = polygonShape;
            fixtureDef.isSensor = true;
            body.createFixture(fixtureDef);
            polygonShape.dispose();
        }
        
        // creating the boy body
        SantaHead body2 = Level.body2;
    
            BodyDef bodyDef = new BodyDef();
            bodyDef.type = BodyType.DynamicBody;
            bodyDef.position.set(body2.position);
            Body body = b2world.createBody(bodyDef);
            body.setUserData(body2);
            body2.body = body;
            PolygonShape polygonShape = new PolygonShape();
            origin.x = body2.bounds.width / 2.0f;
            origin.y = body2.bounds.height / 2.0f;
            polygonShape.setAsBox(body2.bounds.width / 2.0f,
                    body2.bounds.height / 2.0f,origin, 0);
            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.shape = polygonShape;
            body.createFixture(fixtureDef);
            polygonShape.dispose();
         
    }
	/* handle input game from book//
	 * 
	 * if a
	 * 	Level.body2.body.setLinearVelocity(new Vector2(-3,0));
	 */
	/**
	 * Made by Philip Deppen (Assignment 5)
	 * allows player to be controllable with left and right arrow keys
	 */
	private void handleInputGame (float deltaTime) 
	{
	   if (cameraHelper.hasTarget(level.body2)) 
	   {
		   // Player Movement
		   if (Gdx.input.isKeyPressed(Keys.A)) 
		   {
			   level.body2.body.setLinearVelocity(new Vector2(-4,0));
			   level.body2.velocity.x = -5.0f;
		   } 
		   else if (Gdx.input.isKeyPressed(Keys.D)) 
		   {
			   level.body2.body.setLinearVelocity(new Vector2(4,0));
			   level.body2.velocity.x = 5.0f;
		   } 
		   else 
		   {
			   // Execute auto-forward movement on non-desktop platform
			   if (Gdx.app.getType() != ApplicationType.Desktop) 
			   {
				   //level.bunnyHead.velocity.x = level.bunnyHead.terminalVelocity.x;
			   }
		   }
		   // Bunny Jump
		   if (Gdx.input.isTouched() || Gdx.input.isKeyPressed(Keys.SPACE)) 
		   {
	         level.body2.setJumping(true);
	       } 
		   else 
	       {
	         level.body2.setJumping(false);
	       }
	   }
   	}	
	
	//Checks if there are still lives available
	public boolean isGameOver() 
	{
		return lives < 0;
	}
	
	//checks to see if player comes in contact with water
	public boolean isPlayerInWater() 
	{
		return level.body2.position.y < -3;
	}
}