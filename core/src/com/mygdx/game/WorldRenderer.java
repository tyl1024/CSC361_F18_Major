/*  Tyler Major 
 *  10/31/2018
 */
package com.mygdx.game;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.TreeMap;

import javax.swing.JOptionPane;

import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.Map;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Disposable;
import com.packtpub.libgdx.canyonbunny.game.objects.Presents;
import com.packtpub.libgdx.canyonbunny.util.Assets;
import com.packtpub.libgdx.canyonbunny.util.Constants;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.packtpub.libgdx.canyonbunny.util.GamePreferences;

public class WorldRenderer implements Disposable
{
	private static final boolean DEBUG_DRAW_BOX2D_WORLD = false;
	private OrthographicCamera camera;
	private SpriteBatch batch;
	private WorldController worldController;
	private OrthographicCamera cameraGUI;
    private Box2DDebugRenderer b2debugRenderer;
	static String fileName = "../core/assets/images/myFile.txt";

	
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
		Texture img = new Texture("images/winterBackground.png");
		batch.begin();
		batch.draw(img,0,0,Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		batch.end();
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
	
	public void textWriter() throws IOException 
	{

			  try{
			     

			      FileWriter fileWriter = new FileWriter(fileName, true);

			      BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
			      bufferedWriter.write("High Score: " + worldController.score + "\n");
			      sortScores();
			      bufferedWriter.close();

			      System.out.println("Done");
			  } catch(IOException e) {
			      System.out.println("COULD NOT LOG!!");
			  }
	}
	
	public void sortScores() throws NumberFormatException, IOException
	{
		// instantiate your sorted collection
		TreeMap<Integer, String> highestScores = new TreeMap<Integer, String>();

		// setup a file reader
		BufferedReader reader = new BufferedReader(
		                        new FileReader(new File("../core/assets/images/myFile.txt")));

		String line = null;
		while ((line = reader.readLine()) != null) 
		{ // read your file line by line
		    String[] playerScores = line.split(": ");
		    // populate your collection with score-player mappings
		    highestScores.put(Integer.valueOf(playerScores[1]), playerScores[0]);
		}

		// iterate in descending order
		for (Integer score : highestScores.descendingKeySet()) 
		{
		    System.out.println(highestScores.get(score) + ": " + score);
		}
		reader.close();

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
		if (worldController.lives>= 0
				&&worldController.livesVisual>worldController.lives)
		{
				int i = worldController.lives;
				float alphaColor = Math.max(0, worldController.livesVisual
				- worldController.lives - 0.5f);
				float alphaScale = 0.35f * (2 + worldController.lives
				- worldController.livesVisual) * 2;
				float alphaRotate = -45 * alphaColor;
				batch.setColor(1.0f, 0.7f, 0.7f, alphaColor);
				batch.draw(Assets.instance.body2.body2,
				x + i * 50, y, 50, 50, 120, 100, alphaScale, -alphaScale,
				alphaRotate);
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
	
	//prompts user with game over message once out of lives
	private void renderGuiGameOverMessage (SpriteBatch batch) 
	{
		float x = cameraGUI.viewportWidth / 2;
		float y = cameraGUI.viewportHeight / 2;
		if (worldController.isGameOver()) 
		{
			BitmapFont fontGameOver = Assets.instance.fonts.defaultBig;
			fontGameOver.setColor(1, 0.75f, 0.25f, 1);
			fontGameOver.draw(batch, "GAME OVER", x, y, 0, Align.center, false);		// need to fix alignment center
			fontGameOver.setColor(1, 1, 1, 1);
		}
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
			if (GamePreferences.instance.showFpsCounter)
			renderGuiFpsCounter(batch);
			// draw FPS text (anchored to bottom right edge)
			renderGuiFpsCounter(batch);
			// draw game over text
			renderGuiGameOverMessage(batch);
			
			try {
				textWriter();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			batch.end();
			
		}
}
