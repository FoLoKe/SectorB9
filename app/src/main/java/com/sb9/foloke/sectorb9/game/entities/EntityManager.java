package com.sb9.foloke.sectorb9.game.entities;
import java.util.*;
import com.sb9.foloke.sectorb9.game.display.*;
import android.graphics.*;
import android.net.wifi.aware.*;
import java.io.*;
import com.sb9.foloke.sectorb9.game.entities.Buildings.*;


public class EntityManager
{
	private ArrayList<Entity> entityArray;
	private Game game;
	public EntityManager(Game game)
	{
		this.game=game;
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
			e.save(writer);
		}
	}
	public void load(BufferedReader reader)
	{
		try
		{
			entityArray.clear();
			String s;
			
			while((s=reader.readLine())!=null)
			{
			String[] words = s.split("\\s"); 
			int tID=Integer.parseInt(words[0]);
			Entity e=createObject(tID);
			e.load(words);
			addObject(e);
			e.calculateCollisionObject();
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
			case 2:
			{
				e=new Crusher(0,0,0,game);
				break;
			}
			case 3:
			{
				e=new SolarPanel(0,0,0,game);
				break;
			}
			case 4:
			{
				e=new FuelGenerator(0,0,0,game);
				break;
			}
			case 5:
			{
				e=new BigSmelter(0,0,0,game);
				break;
			}
			case 6:
			{
				e=new Assembler(0,0,0,game);
				break;
			}
			case 7:
			{
				e=new ModularLab(0,0,0,game);
				break;
			}
			case 8:
			{
				e=new Asteroid(0,0,0,game);
				break;
			}
			default:
				{
					e=new SmallCargoContainer(0,0,0,game);
					break;
				}
		}
		
		return e;
	}
}
