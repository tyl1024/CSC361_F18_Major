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
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;


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
	
	
	public AssetSanta body2;
	public AssetPlatform Platform;
	public AssetSnowflake Snowflake;
	public AssetPresent gift;
	public AssetLevelDecoration levelDecoration;
	public AssetLevelDecoration waterOverlay;
	
	
	public AssetSounds sounds;
	public AssetMusic music;
	public class AssetSounds 
	{
		public final Sound jump;
		public final Sound jumpWithFeather;
		public final Sound pickupCoin;
		public final Sound pickupFeather;
		public final Sound liveLost;
		public AssetSounds (AssetManager am) 
		{
			jump = am.get("sounds/jump3.wav", Sound.class);
			jumpWithFeather = am.get("sounds/jump_with_feather.wav",
					Sound.class);
			pickupCoin = am.get("sounds/pickup_coin.wav", Sound.class);
			pickupFeather = am.get("sounds/pickup_feather.wav",
					Sound.class);
			liveLost = am.get("sounds/live_lost2.wav", Sound.class);
					}
		}
		public class AssetMusic 
		{
			public final Music song01;
			public AssetMusic (AssetManager am) 
			{
				song01 = am.get("music/backgroundMusic.mp3",Music.class);
			}
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
		// load sounds
		assetManager.load("sounds/jump3.wav", Sound.class);
		assetManager.load("sounds/jump_with_feather.wav", Sound.class);
		assetManager.load("sounds/pickup_coin.wav", Sound.class);
		assetManager.load("sounds/pickup_feather.wav", Sound.class);
		assetManager.load("sounds/live_lost2.wav", Sound.class);
		// load music
		assetManager.load("music/backgroundMusic.mp3",
		Music.class);
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
				fonts = new AssetFonts();
				// create game resource objects
				body2 = new AssetSanta(atlas);
				Platform = new AssetPlatform(atlas);
				Snowflake = new AssetSnowflake(atlas);
				gift = new AssetPresent(atlas);
				levelDecoration = new AssetLevelDecoration(atlas);
				
				sounds = new AssetSounds(assetManager);
				music = new AssetMusic(assetManager);
				
}
	

	
	//free memory
	@Override
	public void dispose () 
	{
		assetManager.dispose();
		fonts.defaultSmall.dispose();
		fonts.defaultNormal.dispose();
		fonts.defaultBig.dispose();
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
		public final AtlasRegion body2;
		public AssetSanta (TextureAtlas atlas) 
		{
			body2 = atlas.findRegion("santa");
		}
	}
	
	//two different types of platforms for santa to move on
	public class AssetPlatform
	{
		public final AtlasRegion flat;
		public AssetPlatform (TextureAtlas atlas)
		{
			flat = atlas.findRegion("snowPlatform3");
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
		public final AtlasRegion mountainLeft;
		public final AtlasRegion mountainRight;
		public final AtlasRegion waterOverlay;
		public final AtlasRegion goal;
		public final AtlasRegion cloud01;
		public final AtlasRegion cloud02;
		public final AtlasRegion cloud03;


		
		
		public AssetLevelDecoration (TextureAtlas atlas) 
		{
			cloud01 = atlas.findRegion("clouds");
			cloud02 = atlas.findRegion("clouds");
			cloud03 = atlas.findRegion("clouds");
			mountainLeft = atlas.findRegion("mountain_left");
			mountainRight = atlas.findRegion("mountain_right");			
			waterOverlay = atlas.findRegion("water_overlay");
			goal = atlas.findRegion("goal");
			
		}
	}

public AssetFonts fonts;
public class AssetFonts
	{
		public final BitmapFont defaultSmall;
		public final BitmapFont defaultNormal;
		public final BitmapFont defaultBig;
		public AssetFonts () 
		{
			// create three fonts using Libgdx's 15px bitmap font
			defaultSmall = new BitmapFont(
			Gdx.files.internal("fonts/arial-15.fnt"), true);
			defaultNormal = new BitmapFont(
			Gdx.files.internal("fonts/arial-15.fnt"), true);
			defaultBig = new BitmapFont(
			Gdx.files.internal("fonts/arial-15.fnt"), true);
			// set font sizes
			defaultSmall.getData().setScale(0.75f);
			defaultNormal.getData().setScale(1.0f);
			defaultBig.getData().setScale(2.0f);
			// enable linear texture filtering for smooth fonts
			defaultSmall.getRegion().getTexture().setFilter(
			TextureFilter.Linear, TextureFilter.Linear);
			defaultNormal.getRegion().getTexture().setFilter(
			TextureFilter.Linear, TextureFilter.Linear);
			defaultBig.getRegion().getTexture().setFilter(
			TextureFilter.Linear, TextureFilter.Linear);
		}
}

}


	

