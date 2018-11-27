/*  Tyler Major 
 *  10/31/2018
 */
package com.packtpub.libgdx.canyonbunny;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.packtpub.libgdx.canyonbunny.screens.MenuScreen;
import com.packtpub.libgdx.canyonbunny.util.Assets;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.WorldController;
import com.mygdx.game.WorldRenderer;
import com.badlogic.gdx.assets.AssetManager;

public class CanyonBunnyMain extends Game 
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
	public void create () 
	{ 
		// Set Libgdx log level to DEBUG
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
		// Load assets
		Assets.instance.init(new AssetManager());
		paused = false;
		//Assets.instance.init(new AssetManager());
		// Start game at menu screen
		setScreen(new MenuScreen(this));
	
		
		
	}
}