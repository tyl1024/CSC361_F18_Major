package com.packtpub.libgdx.canyonbunny.util;

import com.badlogic.gdx.graphics.Color;
/**
 * 
 * @author Owen Burnham (Assignment 6)
 * Abstracts all the selectable character skins
 */
public enum CharacterSkin 
{
	WHITE("White", 1.0f, 1.0f, 1.0f),
	RED("Red", 255.0f, 0.0f, 0.0f),
	BLUE("Blue", 0.0f, 0.0f, 255.0f);
	
	private String name;
	private Color color = new Color();
	private CharacterSkin (String name, float r, float g, float b) 
	{
		this.name = name;
		color.set(r, g, b, 1.0f);
	}
	
	/**
	 * Made by Owen Burnham (Assignment 6)
	 * All character skins are defined using a name
	 * that is used for display and RGB color values
	 */
	@Override
	public String toString () 
	{
		return name;
	}
	
	/**
	 * Made by Owen Burnham (Assignment 6)
	 * @return the color value used to describe a color
	 */
	public Color getColor () 
	{
		return color;
	}

}
