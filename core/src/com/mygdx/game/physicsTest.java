package com.mygdx.game;

import com.badlogic.gdx.Application.ApplicationType;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.mygdx.game.WorldController;
import com.packtpub.libgdx.canyonbunny.Level;
import com.packtpub.libgdx.canyonbunny.game.objects.AbstractGameObject;
import com.packtpub.libgdx.canyonbunny.game.objects.Platform;
import com.packtpub.libgdx.canyonbunny.game.objects.Presents;
import com.packtpub.libgdx.canyonbunny.game.objects.SantaHead;
import com.packtpub.libgdx.canyonbunny.game.objects.SantaHead.JUMP_STATE;
import com.packtpub.libgdx.canyonbunny.game.objects.Snowflake;
import com.packtpub.libgdx.canyonbunny.util.Assets;
import com.packtpub.libgdx.canyonbunny.util.AudioManager;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;


/**
 * This class takes into account all of the collisions
 */
public class physicsTest implements ContactListener
{
    private WorldController controller;
    private ObjectMap<Short, ObjectMap<Short, ContactListener>> listeners;
    
    public physicsTest(WorldController w)
    {
        controller = w;
        listeners = new ObjectMap<Short, ObjectMap<Short, ContactListener>>();
    }
    /**
     * Determines which objects are colliding and calls the 
     * appropriate method
     * @param contact
     */
    private void processContact(Contact contact)
    {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();
        AbstractGameObject objA = (AbstractGameObject) fixtureA.getBody().getUserData();
        AbstractGameObject objB = (AbstractGameObject) fixtureB.getBody().getUserData();
        if (((objA instanceof SantaHead) && (objB instanceof Presents)) || ((objA instanceof Presents) && (objB instanceof SantaHead)))
        {
            processPresentContact(fixtureA, fixtureB);
        }
        
        if (((objA instanceof SantaHead) && (objB instanceof Platform)) || ((objA instanceof Platform) && (objB instanceof SantaHead)))
        {
            processRockContact(fixtureA,fixtureB);
        }
        
        if (((objA instanceof SantaHead) && (objB instanceof Snowflake)) || ((objA instanceof Snowflake) && (objB instanceof SantaHead)))
        {
            processSnowflakeContact(fixtureA,fixtureB);
        }
        
        //if ((objA instanceof Boy) && (objB instanceof Ghost))
        //{
        //    processGhostContact(fixtureA,fixtureB);
        //}
    
    }
    
    /**
     * Handles collision between boy and candy corn
     * @param bod2Fixture
     * @param presentFixture
     */
    private void processPresentContact(Fixture body2Fixture, Fixture presentFixture)
    {
        SantaHead body2 = (SantaHead) presentFixture.getBody().getUserData();
        Presents gift = (Presents) body2Fixture.getBody().getUserData();
        gift.collected = true;
        AudioManager.instance.play(Assets.instance.sounds.pickupCoin);
        controller.score += gift.getScore();
        Gdx.app.log("CollisionHandler", "Presents collected");
    }
    
    /**
     * Handles collision between santa and rock
     * @param bodyFixture
     * @param platformFixture
     */
    private void processRockContact(Fixture bodyFixture, Fixture platformFixture)
    {
        SantaHead body2 = (SantaHead) platformFixture.getBody().getUserData();
        Platform platform = (Platform) bodyFixture.getBody().getUserData();
        body2 = Level.body2;
        float heightDifference = Math.abs(body2.position.y - (  platform.position.y + platform.bounds.height));
         
         if (heightDifference > 0.25f) 
         {
             boolean hitRightEdge = body2.position.x > (platform.position.x + platform.bounds.width / 2.0f);
             if (hitRightEdge) 
             {
                 body2.position.x = platform.position.x + platform.bounds.width;
             }
             else 
             {
                 body2.position.x = platform.position.x - body2.bounds.width;
             }
             return;
         }
         
         switch (body2.jumpState) 
         {
             case GROUNDED:
                 break;
             case FALLING:
             case JUMP_FALLING:
                 body2.position.y = platform.position.y + body2.bounds.height  + body2.origin.y;
                 body2.jumpState = JUMP_STATE.GROUNDED;
                 break;
             case JUMP_RISING:
                  body2.position.y = platform.position.y + body2.bounds.height + body2.origin.y;
                 break;
         }

    }
    
    /**
     * Handles collision between boy and pumpkin
     * @param boyFixture
     * @param candyCornFixture
     */
    private void processSnowflakeContact(Fixture body2Fixture, Fixture snowflakeFixture)
    {
        SantaHead boy = (SantaHead) snowflakeFixture.getBody().getUserData();
        Snowflake flake = (Snowflake) body2Fixture.getBody().getUserData();
        flake.collected = true;
        AudioManager.instance.play(Assets.instance.sounds.pickupFeather);
        controller.score += flake.getScore();
        controller.level.body2.setFlakePowerup(true);
        Gdx.app.log("CollisionHandler", "Powerup Flake collected");
    }

    
    @Override
    public void beginContact(Contact contact) 
    {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();
        processContact(contact);

        Gdx.app.log("CollisionHandler-begin A", "begin");

        ContactListener listener = getListener(fixtureA.getFilterData().categoryBits, fixtureB.getFilterData().categoryBits);
        if (listener != null)
        {
            listener.beginContact(contact);
     }    
    }
    
    private ContactListener getListener(short categoryA, short categoryB)
	   {
		ObjectMap<Short, ContactListener> listenerCollection = listeners.get(categoryA);
		if (listenerCollection == null)
		{
		    return null;
		}
		return listenerCollection.get(categoryB);
    }

    
    public void endContact(Contact contact) 
    {
    	Fixture fixtureA = contact.getFixtureA();
    	Fixture fixtureB = contact.getFixtureB();

    	Gdx.app.log("CollisionHandler-end A", "end");

    	 Gdx.app.log("CollisionHandler-end A", fixtureA.getBody().getLinearVelocity().x+" : "+fixtureA.getBody().getLinearVelocity().y);
    	 Gdx.app.log("CollisionHandler-end B", fixtureB.getBody().getLinearVelocity().x+" : "+fixtureB.getBody().getLinearVelocity().y);
    	ContactListener listener = getListener(fixtureA.getFilterData().categoryBits, fixtureB.getFilterData().categoryBits);
    	if (listener != null)
    	{
    	    listener.endContact(contact);
    	}
        
    }
    
    @Override
    public void preSolve(Contact contact, Manifold oldManifold) 
    {
        //Gdx.app.log("CollisionHandler-preSolve A", "preSolve");
        //Fixture fixtureA = contact.getFixtureA();
        //Fixture fixtureB = contact.getFixtureB();
        //ContactListener listener = getListener(fixtureA.getFilterData().categoryBits, fixtureB.getFilterData().categoryBits);
        //if (listener != null)
        //{
        //    listener.preSolve(contact, oldManifold);
        //}
        
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) 
    {
    	this.processContact(contact); 
    }
    
}
