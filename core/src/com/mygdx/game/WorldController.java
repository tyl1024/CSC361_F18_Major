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
import com.packtpub.libgdx.canyonbunny.game.objects.Platform;
import com.packtpub.libgdx.canyonbunny.game.objects.SantaHead;
import com.packtpub.libgdx.canyonbunny.game.objects.SantaHead.JUMP_STATE;
import com.packtpub.libgdx.canyonbunny.util.Constants;
import com.packtpub.libgdx.canyonbunny.*;


public class WorldController extends InputAdapter
{
	public Level level;
	public int lives;
	public int score;
	
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
		level.update(deltaTime);
		testCollisions();
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
	}
	
	private void onCollisionSantaHeadWithPlatform (Platform platform) 
	{
		SantaHead bunnyHead = level.body;
		float heightDifference = Math.abs(bunnyHead.position.y
		- ( platform.position.y + platform.bounds.height));
		if (heightDifference > 0.25f) 
		{
			boolean hitRightEdge = bunnyHead.position.x > (
					platform.position.x + platform.bounds.width / 2.0f);
		if (hitRightEdge) 
		{
			bunnyHead.position.x = platform.position.x + platform.bounds.width;
		} 
		else 
		{
			bunnyHead.position.x = platform.position.x -
			bunnyHead.bounds.width;
		}
		return;
		}
		switch (bunnyHead.jumpState) 
		{
			case GROUNDED:
				break;
			case FALLING:
			case JUMP_FALLING:
			bunnyHead.position.y = platform.position.y +
			bunnyHead.bounds.height + bunnyHead.origin.y;
			bunnyHead.jumpState = JUMP_STATE.GROUNDED;
			break;
		case JUMP_RISING:
			bunnyHead.position.y = platform.position.y +
			bunnyHead.bounds.height + bunnyHead.origin.y;
			break;
		}
	}
	
	
	// Rectangles for collision detection
	private Rectangle r1 = new Rectangle();
	private Rectangle r2 = new Rectangle();
	//private void onCollisionSantaHeadWithPlatform(Platform platform) {};
	//private void onCollisionBunnyWithGoldCoin(GoldCoin goldcoin) {};
	//private void onCollisionBunnyWithFeather(Feather feather) {};
	private void testCollisions ()
	{
		r1.set(level.body.position.x, level.body.position.y,
		level.body.bounds.width, level.body.bounds.height);
		// Test collision: Bunny Head <-> Rocks
		for (Platform platform : level.platform) {
		r2.set(platform.position.x, platform.position.y, platform.bounds.width,
		platform.bounds.height);
		if (!r1.overlaps(r2)) continue;
		onCollisionSantaHeadWithPlatform(platform);
		// IMPORTANT: must do all collisions for valid
		// edge testing on rocks.
	}
	
	}
}