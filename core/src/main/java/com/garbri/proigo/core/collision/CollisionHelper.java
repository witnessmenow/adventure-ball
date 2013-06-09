package com.garbri.proigo.core.collision;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

public class CollisionHelper implements ContactListener{

	@Override
	public void beginContact(Contact contact) {
		
		Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();
        
        Body bodyA = fixtureA.getBody(); 
        
        if(bodyA != null)
        {
        	CollisionInfo fixAInfo = (CollisionInfo) bodyA.getUserData();
        
        	if (fixAInfo != null)
        	{
        		Gdx.app.log("beginContact", "Fixture A: " + fixAInfo.text);
        	}
        }
        
        
        
        Gdx.app.log("beginContact", "between " + fixtureA.toString() + " and " + fixtureB.toString());
		
	}

	@Override
	public void endContact(Contact contact) {
		
		Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();
        
        Gdx.app.log("endContact", "between " + fixtureA.toString() + " and " + fixtureB.toString());
		
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		// TODO Auto-generated method stub
		
	}
	

}
