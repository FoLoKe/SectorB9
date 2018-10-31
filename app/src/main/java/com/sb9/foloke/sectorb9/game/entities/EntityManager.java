package com.sb9.foloke.sectorb9.game.entities;
import java.util.*;
import com.sb9.foloke.sectorb9.game.display.*;
import android.graphics.*;
import android.net.wifi.aware.*;


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
}
