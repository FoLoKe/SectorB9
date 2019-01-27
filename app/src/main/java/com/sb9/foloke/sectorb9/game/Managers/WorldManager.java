package com.sb9.foloke.sectorb9.game.Managers;
import com.sb9.foloke.sectorb9.*;
import com.sb9.foloke.sectorb9.game.entities.*;
import com.sb9.foloke.sectorb9.game.entities.Buildings.*;
import java.util.*;
import com.sb9.foloke.sectorb9.game.display.*;
import android.graphics.*;
import java.io.*;

public class WorldManager
{
	MainActivity MA;
	EntityManager entityManager;
	Game game;
	public WorldManager(MainActivity MA,Game game)
	{
		this.MA=MA;
		this.game=game;
		this.entityManager=new EntityManager(game);
		
		
	}
	public void loadDebugWorld()
	{
		
		Random rand=new Random();
		
		entityManager.addObject(new FuelGenerator(1200,900,rand.nextInt(180),game));
		entityManager.addObject(new BigSmelter(1100,900,rand.nextInt(180),game));
		entityManager.addObject(new ModularLab(1000,900,rand.nextInt(180),game));
		entityManager.addObject(new Assembler(900,900,rand.nextInt(180),game));
		entityManager.addObject(new SolarPanel(800,900,rand.nextInt(180),game));
		entityManager.addObject(new Crusher(700,900,rand.nextInt(180),game));
		entityManager.addObject(new SmallCargoContainer(600,900,rand.nextInt(180),game));
		for (int i=0;i<50;i++)
			entityManager.addObject(new Asteroid(50*rand.nextInt(50)+25*rand.nextInt(20),100*rand.nextInt(20)+20*rand.nextInt(50),rand.nextInt(180),game));
	}
	
	public void updateWorld()
	{
		//Render Box
		for(Entity e : entityManager.getArray())
		{
			if(e.getActive())
			{
				if(e.getCollsionBox().intersect(MA.getGame().getCamera().getScreenRect()))
					e.setRenderable(true);
				else
					e.setRenderable(false);
			}
			else
				e.setRenderable(false);
		}
		
		//objects ticks
		entityManager.tick();
	}
	
	public void renderWorld(Canvas canvas)
	{
		entityManager.render(canvas);
		//debug information
		if(MA.getGame().drawDebugInf)
		for(Entity e: entityManager.getArray())
		{
			e.drawDebugBox(canvas);
		}
	}
	
	public void interactionCheck(float x,float y)
	{
		Player player=MA.getGame().getPlayer();
		for(Entity e: entityManager.getArray())
		{
			if(e.getCollisionBox().contains(x,y))
			{
				if(Math.sqrt(
					   (e.getCenterX()-player.getCenterX())*(e.getCenterX()-player.getCenterX())
					   +(e.getCenterY()-player.getCenterY())*(e.getCenterY()-player.getCenterY()))-32<player.getRadius())											 
				{								
					if(e instanceof StaticEntity)
					{
						MA.getGame().setPressedObject((StaticEntity)e);
						MA.uiInteraction.init(MA,MA.getViewFlipper(),(StaticEntity)e);
					}

				}							
			}
		}
	}
	
	public void addObject(Entity e)
	{
		entityManager.addObject(e);
	}
	
	public EntityManager getEntityManager()
	{
		return entityManager;
	}
	public void save(BufferedWriter w)
	{
		entityManager.save(w);
	}
	
	public void load(BufferedReader r)
	{
		entityManager.load(r);
	}
}
