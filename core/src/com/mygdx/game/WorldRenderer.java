/*  Tyler Major 
 *  10/31/2018
 */
package com.mygdx.game;

import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Disposable;
import com.packtpub.libgdx.canyonbunny.util.Assets;
import com.packtpub.libgdx.canyonbunny.util.Constants;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class WorldRenderer implements Disposable
{
	private static final boolean DEBUG_DRAW_BOX2D_WORLD = true;
	private OrthographicCamera camera;
	private SpriteBatch batch;
	private WorldController worldController;
	private OrthographicCamera cameraGUI;
    private Box2DDebugRenderer b2debugRenderer;

	
	//Constructor
	public WorldRenderer (WorldController worldController) 
	{
		this.worldController = worldController;
		init();
	}
	
	//Method for initialiation
	private void init () 
	{
		batch = new SpriteBatch();
		camera = new OrthographicCamera(Constants.VIEWPORT_WIDTH,
		Constants.VIEWPORT_HEIGHT);
		camera.position.set(0, 0, 0);
		camera.update();
		cameraGUI = new OrthographicCamera(Constants.VIEWPORT_GUI_WIDTH,
				Constants.VIEWPORT_GUI_HEIGHT);
				cameraGUI.position.set(0, 0, 0);
				cameraGUI.setToOrtho(true); // flip y-axis
				cameraGUI.update();
		 b2debugRenderer = new Box2DDebugRenderer();

	}
	
	//method that will contain the logic to define in which order the
	//game objects are drawn over others.
	public void render ()
	{
		renderWorld(batch);
		renderGui(batch);
	}
	
	private void renderWorld (SpriteBatch batch)
	{
		worldController.cameraHelper.applyTo(camera);
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		worldController.level.render(batch);
		batch.end();
		if (DEBUG_DRAW_BOX2D_WORLD)
    	{
    		b2debugRenderer.render(worldController.b2world, camera.combined);
    	}
	}
	
	/*
	 * Whenever the screen size is changed, including
	 *	the event at the start of the program, resize
	 */
	public void resize (int width, int height) 
	{ 
		camera.viewportWidth = (Constants.VIEWPORT_HEIGHT
				/ (float)height) * (float)width;
				camera.update();
				cameraGUI.viewportHeight = Constants.VIEWPORT_GUI_HEIGHT;
				cameraGUI.viewportWidth = (Constants.VIEWPORT_GUI_HEIGHT
				/ (float)height) * (float)width;
				cameraGUI.position.set(cameraGUI.viewportWidth / 2,
				cameraGUI.viewportHeight / 2, 0);
				cameraGUI.update();
	}
	
	/*
	 * method to free the allocated memory when it is no longer
	 *	needed.
	 */
	@Override 
	public void dispose () 
	{ 
		batch.dispose();
	}	
	
	//Sets total score for collecting presents
	private void renderGuiScore(SpriteBatch batch) 
	{
		float x = -15;
		float y = -15;
		batch.draw(Assets.instance.gift.gift,
		x, y, 50, 50, 100, 100, 0.35f, -0.35f, 0);
		Assets.instance.fonts.defaultBig.draw(batch,
		"" + worldController.score,
		x + 75, y + 37);
	}
	
	//Sets GUI extra lives in top right
	private void renderGuiExtraLive(SpriteBatch batch)
	{
		float x = cameraGUI.viewportWidth - 50 -
		Constants.LIVES_START * 50;
		float y = -5;
		for (int i = 0; i < Constants.LIVES_START; i++) 
		{
			if (worldController.lives <= i)
			batch.setColor(0.5f, 0.5f, 0.5f, 0.5f);
			batch.draw(Assets.instance.body2.body2,
			x + i * 50, y, 50, 50, 120, 100, 0.35f, -0.65f, 0);
			batch.setColor(1, 1, 1, 1);
		}
	}
	
	//Shows frames per second in bottom right of GUI
	private void renderGuiFpsCounter(SpriteBatch batch) 
	{
		float x = cameraGUI.viewportWidth - 55;
		float y = cameraGUI.viewportHeight - 15;
		int fps = Gdx.graphics.getFramesPerSecond();
		BitmapFont fpsFont = Assets.instance.fonts.defaultNormal;
		if (fps >= 45)
		{
			// 45 or more FPS show up in green
			fpsFont.setColor(0, 1, 0, 1);
		} 
		else if (fps >= 30) 
		{
			// 30 or more FPS show up in yellow
			fpsFont.setColor(1, 1, 0, 1);
		} 
		else 
		{
			// less than 30 FPS show up in red
			fpsFont.setColor(1, 0, 0, 1);
		}
			fpsFont.draw(batch, "FPS: " + fps, x, y);
			fpsFont.setColor(1, 1, 1, 1); // white
		}
	
		//Group whole GUI together with score, extra lives and FPS
		private void renderGui (SpriteBatch batch) 
		{
			batch.setProjectionMatrix(cameraGUI.combined);
			batch.begin();
			// draw collected gold coins icon + text
			// (anchored to top left edge)
			renderGuiScore(batch);
			// draw extra lives icon + text (anchored to top right edge)
			renderGuiExtraLive(batch);
			// draw FPS text (anchored to bottom right edge)
			renderGuiFpsCounter(batch);
			batch.end();
		}
}
