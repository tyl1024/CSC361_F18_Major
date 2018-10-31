package com.packtpub.libgdx.canyonbunny.game;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;
import com.mygdx.game.WorldController;
import com.packtpub.libgdx.canyonbunny.util.Constants;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class WorldRenderer implements Disposable
{
	private OrthographicCamera camera;
	private SpriteBatch batch;
	private WorldController worldController;
	
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
	}
	
	//method that will contain the logic to define in which order the
	//game objects are drawn over others.
	public void render ()
	{
		renderTestObjects();
	}
	
	// Testing objects rendering
	private void renderTestObjects() 
	{
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		for(Sprite sprite : worldController.testSprites) 
		{
			sprite.draw(batch);
		}
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
}