package com.packtpub.libgdx.canyonbunny.screens;

import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

public class highScoreMenu 
{
	public Preferences getHighScore() throws BackingStoreException
	{
		int score = 0;
		Preferences prefs = (Preferences) Gdx.app.getPreferences("My Preferences");
	
	    
		((com.badlogic.gdx.Preferences) prefs).putString("name", "Donald Duck");
		String name = ((com.badlogic.gdx.Preferences) prefs).getString("name", "No name stored");
	
		prefs.putBoolean("soundOn", true);
		prefs.putInt("highscore", 10);
		
		prefs.flush();
		return prefs;
	}
}
