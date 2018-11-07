/*  Tyler Major 
 *  10/31/2018
 */
package com.mygdx.game;

import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Disposable;
import com.packtpub.libgdx.canyonbunny.util.Assets;
import com.packtpub.libgdx.canyonbunny.util.Constants;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class WorldRenderer implements Disposable
{
	private OrthographicCamera camera;
	private SpriteBatch batch;
	private WorldController worldController;
	private OrthographicCamera cameraGUI;
	
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
	}
	
	/*
	 * Whenever the screen size is changed, including
	 *	the event at the start of the program, resize
	 */
	public void resize (int width, int height) 
	{ 
		camera.viewportWidth = (Constants.VIEWPORT_HEIGHT / height) *
				width;
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
	
	//used to display GUI score from collecting presents in top left of GUI
	private void renderGuiScore (SpriteBatch batch)
	{
		float x = -15;
		float y = -15;
		batch.draw(Assets.instance.present.gift,
		x, y, 50, 50, 100, 100, 0.35f, -0.35f, 0);
		Assets.instance.fonts.defaultBig.draw(batch,
		"" + worldController.score,
		x + 75, y + 37);
	}
	
	//displays 3 lives in top right of GUI
	private void renderGuiExtraLive (SpriteBatch batch) 
	{
		float x = cameraGUI.viewportWidth - 50 -
		Constants.LIVES_START * 50;
		float y = -15;
		for (int i = 0; i < Constants.LIVES_START; i++)
		{
			if (worldController.lives <= i)
			batch.setColor(0.5f, 0.5f, 0.5f, 0.5f);
			batch.draw(Assets.instance.santa.body,
			x + i * 50, y, 50, 50, 120, 100, 0.35f, -0.35f, 0);
			batch.setColor(1, 1, 1, 1);
		}
	}
	
	//shows FPS in bottom right of GUI screen
	private void renderGuiFpsCounter (SpriteBatch batch) 
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
	
		//renders the entire GUI with FPS, lives and score
		private void renderGui (SpriteBatch batch)
		{
			batch.setProjectionMatrix(cameraGUI.combined);
			batch.begin();
			// draw collected presents
			// (anchored to top left edge)
			renderGuiScore(batch);
			renderGuiFlakePowerup(batch);
			// draw extra lives icon + text (anchored to top right edge)
			renderGuiExtraLive(batch);
			// draw FPS text (anchored to bottom right edge)
			renderGuiFpsCounter(batch);
			
			renderGuiGameOverMessage(batch);
			batch.end();
		}
		
		//Tyler added this from page220. This function adds the text that says Game Over when lives run out
		private void renderGuiGameOverMessage (SpriteBatch batch) {
			float x = cameraGUI.viewportWidth / 2;
			float y = cameraGUI.viewportHeight / 2;
			if (worldController.isGameOver()) {
			BitmapFont fontGameOver = Assets.instance.fonts.defaultBig;
			fontGameOver.setColor(1, 0.75f, 0.25f, 1);
			fontGameOver.draw(batch, "GAME OVER", x, y, 0, Align.center, false);		// need to fix alignment center
			fontGameOver.setColor(1, 1, 1, 1);
			}
		}
		
		//Tyler added this code from page 224 in book.
		//This method first checks whether there is still time left for the feather power-up effect
		//to end. Only if this is the case, a feather icon is drawn in the top-left corner under the
		//gold coin icon. A small number is drawn next to it that displays the rounded time
		//that is still left until the effect vanishes.
		private void renderGuiFlakePowerup (SpriteBatch batch) {
			float x = -15;
			float y = 30;
			float timeLeftFeatherPowerup =
			worldController.level.body.timeLeftFlakePowerup;
			if (timeLeftFeatherPowerup > 0) {
			// Start icon fade in/out if the left power-up time
			// is less than 4 seconds. The fade interval is set
			// to 5 changes per second.
			if (timeLeftFeatherPowerup < 4) {
			if (((int)(timeLeftFeatherPowerup * 5) % 2) != 0) {
			batch.setColor(1, 1, 1, 0.5f);
			}
			}
			batch.draw(Assets.instance.snowflake.flake,
			x, y, 50, 50, 100, 100, 0.35f, -0.35f, 0);
			batch.setColor(1, 1, 1, 1);
			Assets.instance.fonts.defaultSmall.draw(batch,
			"" + (int)timeLeftFeatherPowerup, x + 60, y + 57);
			}
			}
		
	}
