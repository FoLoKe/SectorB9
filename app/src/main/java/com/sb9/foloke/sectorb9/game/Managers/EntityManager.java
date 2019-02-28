package com.sb9.foloke.sectorb9.game.Managers;
import java.util.*;

import com.sb9.foloke.sectorb9.game.Entities.Asteroid;
import com.sb9.foloke.sectorb9.game.Entities.Entity;
import com.sb9.foloke.sectorb9.game.Entities.SmallCargoContainer;


import android.graphics.*;

import java.io.*;
import com.sb9.foloke.sectorb9.game.Entities.Buildings.*;
import com.sb9.foloke.sectorb9.game.Entities.*;


public class EntityManager
{
	private ArrayList<Entity> entityArray;
	private GameManager gameManager;
	public EntityManager(GameManager gameManager)
	{
		this.gameManager = gameManager;

		entityArray=new ArrayList<Entity>();
	}

	public void addObject(Entity entity)
	{
		entityArray.add(entity);
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
		for(Entity e: entityArray)
		{
			e.tick();
		}
	}

	public ArrayList<Entity> getArray()
	{
		return entityArray;
	}

	public void deleteObject(Entity entity)
	{
		entityArray.remove(entity);
	}

	public Entity getObject(int index)
	{
		return entityArray.get(index);
	}

	public void save(BufferedWriter writer)
	{
		for(Entity e:entityArray)
		{
			if(!(e instanceof Player))
			e.save(writer);
		}
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
				e.load(words);
				addObject(e);
			}
		}
		catch(Throwable t)
		{
			System.out.println(t);
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
			case 10:
			{
				e=new EnemyShip(0,0,0,gameManager);
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
	public Entity createBuildable(int ID,Entity initiator)
	{
		return new Buildable(ID,initiator.getTeam(),gameManager);
	}
	
	public void reload()
    {
        entityArray.clear();
    }
}
