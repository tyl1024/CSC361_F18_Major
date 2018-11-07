/*  Tyler Major 
 *  10/31/2018
 */
package com.packtpub.libgdx.canyonbunny;

import com.badlogic.gdx.ApplicationListener;
import com.packtpub.libgdx.canyonbunny.util.Assets;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.WorldController;
import com.mygdx.game.WorldRenderer;
import com.badlogic.gdx.assets.AssetManager;

public class CanyonBunnyMain implements ApplicationListener 
{
	private static final String TAG = CanyonBunnyMain.class.getName();
	private com.mygdx.game.WorldController worldController;
	private WorldRenderer worldRenderer;
	private boolean paused;
	
	SpriteBatch batch;
	Texture img;
	

	
	/*
	 * Creates worldController and WorldRenderer methods
	 * 
	 */
	@Override 
	public void create () 
	{ 
		// Set Libgdx log level to DEBUG
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
		// Load assets
		Assets.instance.init(new AssetManager());
		// Initialize controller and renderer
		worldController = new WorldController();
		worldRenderer = new WorldRenderer(worldController);
		// Game world is active on start
		paused = false;
		//Assets.instance.init(new AssetManager());
		
		
	}
	
	//Used to continuously update and render the game
	@Override 
	public void render () 
	{
		// Do not update game world when paused.
		if (!paused) 
		{
			// Update game world by the time that has passed
			// since last rendered frame.
			worldController.update(Gdx.graphics.getDeltaTime());
		}
		// Clears the screen
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		batch = new SpriteBatch();
		img = new Texture("images/winterBackground.png");
		
		batch.begin();
		batch.draw(img,0,0,Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		batch.end();
		
		// Render game world to screen
		worldRenderer.render();
		
	}
	
	//Calls worldRenderer to resize
	@Override 
	public void resize (int width, int height) 
	{
		worldRenderer.resize(width, height);
	}
	
	@Override 
	public void pause () 
	{
		paused = true;
	}
	
	@Override 
	public void resume () 
	{ 
		Assets.instance.init(new AssetManager());
		paused = false;
	}
	
	//Frees up memory
	@Override 
	public void dispose () 
	{
		worldRenderer.dispose();
		Assets.instance.dispose();
		img.dispose();
	}
}
