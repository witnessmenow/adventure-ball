package com.garbri.proigo.core.collision;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.garbri.proigo.core.collision.CollisionInfo.CollisionObjectType;

public class CollisionHelper implements ContactListener{

	private Sound ballBounce;
	public CollisionInfo lastCarToTouchBall;
	
	public CollisionHelper(Sound ball)
	{
		this.ballBounce = ball;
	}
	
	
	
	@Override
	public void beginContact(Contact contact) {
		
		Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();
        
        CollisionInfo bodyAInfo = getCollisionInfoFromFixture(fixtureA);
    	CollisionInfo bodyBInfo = getCollisionInfoFromFixture(fixtureB);

        
        if(bodyAInfo != null && bodyBInfo != null)
        {
        	
        	if(bodyAInfo.type == CollisionObjectType.ball)
        	{
        		ballIsDetected(bodyBInfo);
        	}
        	
        	if(bodyBInfo.type == CollisionObjectType.ball)
        	{
        		ballIsDetected(bodyAInfo);
        	}
        	
        	if (bodyAInfo != null)
        	{
        		Gdx.app.log("beginContact", "Fixture A: " + bodyAInfo.text);
        	}
        }
        
        
        
        Gdx.app.log("beginContact", "between " + fixtureA.toString() + " and " + fixtureB.toString());
		
	}
	
	private void ballIsDetected(CollisionInfo otherBody)
	{
		this.ballBounce.play(1.0f);
		
		Gdx.app.log("Collision Helper", "Ball Bounce");
		
		if(otherBody.type == CollisionObjectType.car)
		{
			this.lastCarToTouchBall = otherBody;
		}
	}
	
	private CollisionInfo getCollisionInfoFromFixture(Fixture fix)
	{	
		CollisionInfo colInfo = null;
		
		if(fix != null)
        {
			Body body = fix.getBody();
			
			if(body != null)
			{
				colInfo = (CollisionInfo) body.getUserData();
			}
        }
		
		return colInfo;
	}

	@Override
	public void endContact(Contact contact) {
		
		//We currently do not use anything that waits for the end of contact
		
//		Fixture fixtureA = contact.getFixtureA();
//        Fixture fixtureB = contact.getFixtureB();
//        
//        Gdx.app.log("endContact", "between " + fixtureA.toString() + " and " + fixtureB.toString());
//		
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
