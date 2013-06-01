package com.garbri.proigo.core.objects;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.garbri.proigo.core.utilities.BoxProp;
import com.garbri.proigo.core.vehicles.Car;
import com.garbri.proigo.core.vehicles.Vehicle;

public class TeamSelectArea {
	
	
	private Vector2 center;
	
	public List<BoxProp> walls;
	
	private float gapFromOuterEdge = 1f;
	private float playerGapX = 8f;
	private float playerGapY = 8f;
	
	private float worldWidth;
	private float worldHeight;
	
	public int carsInBlueArea = 0;
	public int carsInRedArea = 0;
	
	private float blueTextPosition;
	private float redTextPosition;
	private float textY;
	
	
	public TeamSelectArea(World world, float worldWidth, float worldHeight, Vector2 center)
	{
		this.walls = new ArrayList<BoxProp>();
		
		this.center = center;
		
		
		this.worldHeight = worldHeight;
		this.worldWidth = worldWidth;
		
		createOuterWalls(world, worldWidth, worldHeight, center, gapFromOuterEdge);
		
		//make it even!
		createInnerWalls(world, worldWidth, worldHeight, center, gapFromOuterEdge);
		
		//Changethis!
		this.blueTextPosition = 2*10;
		this.redTextPosition = (((worldWidth/16)*12)+2)*10;
		
		this.textY = ((worldHeight/2)*10) + 50; 
		
		
		
		
	}
	
	private void createOuterWalls(World world, float worldWidth, float worldHeight, Vector2 center, float gapFromOuterEdge)
	{
		
	    //outer walls
	    BoxProp bottomWall = new BoxProp(world, worldWidth, 1, new Vector2 (worldWidth/2, gapFromOuterEdge)); //bottom Wall
	    
	    BoxProp leftWall = new BoxProp(world, 1, worldHeight/2, new Vector2 (gapFromOuterEdge, worldHeight/4));//left Wall
	    
	    BoxProp topWall = new BoxProp(world,  worldWidth, 1, new Vector2 (worldWidth/2,worldHeight/2));//top
	    
	    BoxProp rightWall = new BoxProp(world, 1, worldHeight/2, new Vector2 (worldWidth - gapFromOuterEdge, worldHeight/4));//left Wall
	    
	    walls.add(bottomWall);
	    walls.add(leftWall);
	    walls.add(topWall);
	    walls.add(rightWall);
	    
	}
	
	private void createInnerWalls(World world, float worldWidth, float worldHeight, Vector2 center, float gapFromOuterEdge)
	{
	    
	    BoxProp leftInnerWall = new BoxProp(world, 1, worldHeight/4, new Vector2 ((worldWidth/4), 3*(worldHeight/8)));//left Wall  
	    
	    BoxProp rightInnerWall = new BoxProp(world, 1, worldHeight/4, new Vector2 (3*(worldWidth/4), 3*(worldHeight/8)));//left Wall
	    
	    walls.add(leftInnerWall);
	    walls.add(rightInnerWall);

	}
	
	public Vector2 getStartPosition(int carNumber)
	{
		float carPosX;
		
		if(carNumber%2 == 0)
		{
			carPosX = this.center.x - (float)(((carNumber/2) + 1)*playerGapX);
		}
		else
		{
			carPosX = this.center.x + (float)(((carNumber/2) + 1)*playerGapX);
		}
		
		return new Vector2(carPosX, this.center.y - 8f);
	}
	
	public void checkTeams(List<Car> vehicles)
	{
		carsInBlueArea = 0;
		carsInRedArea = 0;
		
		for (Vehicle vehicle : vehicles) 
		{
			//Check for Blue
			if(checkVehicleInBlue(vehicle))
			{
				this.carsInBlueArea++;
			}
			else if(checkVehicleInRed(vehicle))
			{
				this.carsInRedArea++;
			}
		}
	
		
	}
	
	public void displayNumbersInTeam(BitmapFont font, SpriteBatch spriteBatch)
	{
		//Set for blue
		font.setColor(0f, 0f, 1f, 1.0f);
        font.draw(spriteBatch, String.valueOf(this.carsInBlueArea) + " TEAM BLUE", blueTextPosition, textY);
        
      //Set for blue
      		font.setColor(1f, 0f, 0f, 1.0f);
              font.draw(spriteBatch, String.valueOf(this.carsInRedArea) + " TEAM RED", redTextPosition, textY);
	}
	
	public boolean checkVehicleInBlue(Vehicle vehicle)
	{
		return (vehicle.body.getPosition().x <= worldWidth/4); 
	}
	
	public boolean checkVehicleInRed(Vehicle vehicle)
	{
		return (vehicle.body.getPosition().x >= 3*(worldWidth/4)); 
	}
	

}
