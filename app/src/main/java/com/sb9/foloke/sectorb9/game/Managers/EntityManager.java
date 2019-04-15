package com.sb9.foloke.sectorb9.game.Managers;
import java.util.*;

import com.sb9.foloke.sectorb9.game.Entities.Asteroid;
import com.sb9.foloke.sectorb9.game.Entities.Entity;
import com.sb9.foloke.sectorb9.game.Entities.SmallCargoContainer;


import android.graphics.*;

import java.io.*;
import com.sb9.foloke.sectorb9.game.Entities.Buildings.*;
import com.sb9.foloke.sectorb9.game.Entities.*;
import com.sb9.foloke.sectorb9.game.DataSheets.*;
import com.sb9.foloke.sectorb9.game.UI.CustomViews.*;


public class EntityManager
{
	private ArrayList<Entity> entityArray;
    private ArrayList<Entity> entityToAdd;
	private GameManager gameManager;
	private boolean reloadFlag=false;
	EntityManager(GameManager gameManager)
	{
        GameLog.update("EntityManager: preparing manager",0);
		this.gameManager = gameManager;

		entityArray=new ArrayList<>();
        entityToAdd=new ArrayList<>();
        GameLog.update("EntityManager: READY",0);
	}

	public void addObject(Entity entity)
	{

		entityToAdd.add(entity);
		
	}

	public void render(Canvas canvas)
	{
		for(Entity e : entityArray)
		{
			e.render(canvas);
		}
	}

	public void tick()
	{
		if(reloadFlag)
		{
			entityArray.clear();
			//entityToAdd.clear();
			reloadFlag=false;
			GameLog.update("EntityManager: reloaded",0);
		}
	    if(entityToAdd.size()>0)
		{
	    	entityArray.addAll(entityToAdd);
			GameLog.update("EntityManager: added objects:"+entityToAdd.size(),0);
			entityToAdd.clear();
		}
	    
	   Iterator<Entity> it=entityArray.iterator();

		
		while(it.hasNext())
		{
			Entity e =  it.next();
			
			e.tick();
			if(e.toRemove)
			{
			    it.remove();
				GameLog.update("EntityManager: removed "+e,0);
			}
			
			if(e instanceof DynamicEntity)
				((DynamicEntity)e).collidedWith.clear();
		}
	}

	public ArrayList<Entity> getArray()
	{
		return entityArray;
	}

	public void save(BufferedWriter writer)
	{
		try
		{
			for (Entity e:entityArray)
			{
				if (!(e instanceof Player))
					writer.write(e.getSaveString());
			}
		}
		catch (IOException e)
		{GameLog.update(e.toString(),1);}
	}

	public void load(BufferedReader reader)
	{
		try
		{
			entityArray.clear();
			String s;
			reader.readLine();
			
			while((s=reader.readLine())!=null)
			{
				String[] words = s.split("\\s"); 
				int tID=Integer.parseInt(words[0]);
				Entity e=createObject(tID);
				e.loadFromStrings(words);
				addObject(e);
			}
		}
		catch(Throwable t)
		{
			GameLog.update("EntityManager: "+t.toString(),1);
		}
	}

	public Entity createObject(int tID)
	{
		Entity e;
		switch(tID)
		{
			case 1:
				{
					e=new SmallCargoContainer(0,0,0, gameManager);
					break;
				}
			case 2:
			{
				e=new Crusher(0,0,0, gameManager);
				break;
			}
			case 3:
			{
				e=new SolarPanel(0,0,0, gameManager);
				break;
			}
			case 4:
			{
				e=new FuelGenerator(0,0,0, gameManager);
				break;
			}
			case 5:
			{
				e=new BigSmelter(0,0,0, gameManager);
				break;
			}
			case 6:
			{
				e=new Assembler(0,0,0, gameManager);
				break;
			}
			case 7:
			{
				e=new ModularLab(0,0,0, gameManager);
				break;
			}
			case 8:
			{
				e=new Asteroid(0,0,0, gameManager,0);
				break;
			}
            case 9:
            {
                e=new DroppedItems(0,0,0,gameManager);
                break;
            }
			case 10:
			{
				e=new ControlledShip(0,0,0,gameManager,2);
				break;
			}
			case 12:
				{
					e=new SpaceDock(0,0,0,gameManager);
					break;
				}
			default:
				{
					e=new NullObject( gameManager);
					break;
				}
		}
		
		return e;
	}

	Entity createBuildable(int ID,Entity initiator)
	{
		if(ObjectsDataSheet.findById(ID).buildable)
			if(initiator.getInventory().contains(ObjectsDataSheet.findById(ID).resToBuild[0],1))
				if(initiator.getInventory().takeOneItemFromAllInventory(ObjectsDataSheet.findById(ID).resToBuild[0],1))
					return new Buildable(ID,initiator.getTeam(),gameManager);
				else
					GameLog.update("EntityManager: no resources",0);
				
		return null;
	}
	
	public void reload()
    {
        reloadFlag=true;
		GameLog.update("EntityManager: reload call",0);
    }
	
	public Entity findRespawnPoint(int Team)
	{
		Iterator<Entity> it=entityArray.iterator();


		while(it.hasNext())
		{
			Entity e =  it.next();
		
			if(e instanceof SpaceDock)
				if(e.getTeam()==Team)
					return e;
		}
		return null;
	}
}
