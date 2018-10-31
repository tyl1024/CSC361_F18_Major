package com.packtpub.libgdx.canyonbunny.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Disposable;
import com.packtpub.libgdx.canyonbunny.util.Constants;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.utils.Array;

/**
 * Philip Deppen
 * Edited by Owen Burnham (Assignment 4)
 * Edited by Owen Burnham (Assignment 8)
 * Edited by Philip Deppen (Assignment 10)
 * Assets class for all the images
 */


public class Assets implements Disposable, AssetErrorListener
{
	
	public static final String TAG = Assets.class.getName();
	public static final Assets instance = new Assets();
	
	private AssetManager assetManager;
	
	// singleton: prevent instantiation from other classes
	private Assets () 
	{
		
	}
	
	//initializing assets
	public void init (AssetManager assetManager)
	{
		this.assetManager = assetManager;
		// set asset manager error handler
		assetManager.setErrorListener(this);
		// load texture atlas
		assetManager.load(Constants.TEXTURE_ATLAS_OBJECTS,
		TextureAtlas.class);
		// start loading assets and wait until finished
		assetManager.finishLoading();
		Gdx.app.debug(TAG, "# of assets loaded: "
		+ assetManager.getAssetNames().size);
		for (String a : assetManager.getAssetNames())
		Gdx.app.debug(TAG, "asset: " + a);
	}
	
	//free memory
	@Override
	public void dispose () 
	{
		assetManager.dispose();
	}
	
	//used if error finding asset filename
	public void error (String filename, Class type,Throwable throwable) 
	{
	Gdx.app.error(TAG, "Couldn't load asset '"
	+ filename + "'", (Exception)throwable);
	}
	
	//used if error finding asset
	@Override
	public void error(AssetDescriptor asset, Throwable throwable) {
	Gdx.app.error(TAG, "Couldn't load asset '" +
	asset.fileName + "'", (Exception)throwable);
	}
}

	

