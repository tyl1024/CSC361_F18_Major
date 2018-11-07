/*  Tyler Major 
 *  10/31/2018
 */
package com.packtpub.libgdx.canyonbunny.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
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
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.packtpub.libgdx.canyonbunny.util.Constants;


/**
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
	
	
	public AssetSanta santa;
	public AssetPlatform Platform;
	public AssetSnowflake snowflake;
	public AssetPresent present;
	public AssetLevelDecoration levelDecoration;
	public AssetLevelDecoration waterOverlay;


	
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
	
		
		
		TextureAtlas atlas =
				assetManager.get(Constants.TEXTURE_ATLAS_OBJECTS);
		
				// enable texture filtering for pixel smoothing
				for (Texture t : atlas.getTextures()) 
				{
					t.setFilter(TextureFilter.Linear, TextureFilter.Linear);
				}
				// create game resource objects
				santa = new AssetSanta(atlas);
				Platform = new AssetPlatform(atlas);
				snowflake = new AssetSnowflake(atlas);
				present = new AssetPresent(atlas);
				levelDecoration = new AssetLevelDecoration(atlas);
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
	public void error(AssetDescriptor asset, Throwable throwable)
	{
		Gdx.app.error(TAG, "Couldn't load asset '" +
		asset.fileName + "'", (Exception)throwable);
	}

	//santa image
	public class AssetSanta 
	{
		public final AtlasRegion body;
		public AssetSanta (TextureAtlas atlas) 
		{
			body = atlas.findRegion("santa");
		}
	}
	
	//two different types of platforms for santa to move on
	public class AssetPlatform
	{
		public final AtlasRegion flat;
		public final AtlasRegion edge;
		public AssetPlatform (TextureAtlas atlas)
		{
			flat = atlas.findRegion("snowPlatform1");
			edge = atlas.findRegion("snowPlatform2");
		}
	}
		
	//present that santa will collect
	public class AssetPresent 
	{
		public final AtlasRegion gift;
		public AssetPresent (TextureAtlas atlas) 
		{
			gift = atlas.findRegion("present");
		}
	}
	
	//snowflake to grant invincibility for 8 seconds
	public class AssetSnowflake 
	{
		public final AtlasRegion flake;
		public AssetSnowflake (TextureAtlas atlas) 
		{
			flake = atlas.findRegion("snowflake");
		}
	}
	
	
public class AssetLevelDecoration 
{
		public final AtlasRegion mountainBackground;
		public final AtlasRegion waterOverlay;
		
		public AssetLevelDecoration (TextureAtlas atlas) 
		{
			mountainBackground = atlas.findRegion("mountainBackground");
			waterOverlay = atlas.findRegion("waterOverlay");
		}
	}
}


	

