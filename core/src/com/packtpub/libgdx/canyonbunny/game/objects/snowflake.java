package com.packtpub.libgdx.canyonbunny.game.objects;

/*
 * Tyler Major pg 200_201
 * Chapter 6
 * Updated 9/20/2018
 * 
 */
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.packtpub.libgdx.canyonbunny.util.Assets;

/* @author: Tyler Major
 * Feather class is very similar to presents class.
 * It consists of only one image and is a collectible item that will turn invisible when
 * it is collected by the player's character
 */
public class snowflake extends AbstractGameObject 
{
	private TextureRegion regSnowFlake;
	public boolean collected;
	
	//snowflake constructor
	public snowflake ()
	{
		init();
	}
	
// Tyler Major: Initializes feather and sets bounding boc for collision
private void init () 
{
	dimension.set(0.5f, 0.5f);
	regSnowFlake = Assets.instance.snowflake.flake;
	// Set bounding box for collision detection
	bounds.set(0, 0, dimension.x, dimension.y);
	collected = false;
}

//This function draws the snowflake
public void render (SpriteBatch batch) 
{
	if (collected) return;
		TextureRegion reg = null;
		reg = regSnowFlake;
		batch.draw(reg.getTexture(), position.x, position.y,
				origin.x, origin.y, dimension.x, dimension.y, scale.x, scale.y,
				rotation, reg.getRegionX(), reg.getRegionY(),
				reg.getRegionWidth(), reg.getRegionHeight(), false, false);
}

// returns a score
public int getScore() 
{
	return 25;
}

}
