/*  Tyler Major 
 *  10/31/2018
 */
package com.mygdx.game;


import com.badlogic.gdx.Application.ApplicationType;
import com.packtpub.libgdx.canyonbunny.Level;
import com.packtpub.libgdx.canyonbunny.game.objects.platform;
import com.packtpub.libgdx.canyonbunny.util.Constants;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
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
		
		
		
		public Level level;
		public int lives;
		public int score;
		private void initLevel () 
		{
			score = 0;
			level = new Level(Constants.LEVEL_01);
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
		public void update (float deltaTime) 
		{ 
		
			handleDebugInput(deltaTime);
			cameraHelper.update(deltaTime);
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
		public void dispose() {
			// TODO Auto-generated method stub
			
		}
}