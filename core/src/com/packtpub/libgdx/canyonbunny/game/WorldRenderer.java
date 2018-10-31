package com.packtpub.libgdx.canyonbunny.game;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;
import com.packtpub.libgdx.canyonbunny.util.Constants;

public class WorldRenderer implements Disposable
{
	private OrthographicCamera camera;
	private SpriteBatch batch;
	private WorldController worldController;
	
	public WorldRenderer (WorldController worldController) 
	{
		
	}
	
	//Method for initialiation
	private void init () 
	{
		
	}
	
	//method that will contain the logic to define in which order the
	//game objects are drawn over others.
	public void render ()
	{
		
	}
	
	/*
	 * Whenever the screen size is changed, including
	 *	the event at the start of the program, resize
	 */
	public void resize (int width, int height) 
	{ 
		
	}
	
	/*
	 * method to free the allocated memory when it is no longer
	 *	needed.
	 */
	@Override 
	public void dispose () 
	{ 
		
	}
}