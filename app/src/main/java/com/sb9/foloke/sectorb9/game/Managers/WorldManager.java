package com.sb9.foloke.sectorb9.game.Managers;
import com.sb9.foloke.sectorb9.*;
import com.sb9.foloke.sectorb9.game.Entities.*;
import com.sb9.foloke.sectorb9.game.Entities.Buildings.*;

import java.util.*;

import android.graphics.*;
import java.io.*;

public class WorldManager
{
	private MainActivity MA;
	private EntityManager entityManager;
	private GameManager gameManager;

	//background
	private Bitmap background;

	////current sector
	private int sectorX=0,sectorY=0;


	public WorldManager(MainActivity MA, GameManager gameManager)
	{
		this.MA=MA;
		this.gameManager = gameManager;
		this.entityManager=new EntityManager(gameManager);
		
		
	}
	public void loadDebugWorld()
	{
		sectorX=2;
		sectorY=9;
		BitmapFactory.Options bitmapOptions=new BitmapFactory.Options();
		bitmapOptions.inScaled=false;
		background=Bitmap.createBitmap(BitmapFactory.decodeResource(MA.getResources(),R.drawable.galactic_outflow,bitmapOptions));

		Random rand=new Random();
		
		entityManager.addObject(new FuelGenerator(1200,900,rand.nextInt(180), gameManager));
		entityManager.addObject(new BigSmelter(1100,900,rand.nextInt(180), gameManager));
		entityManager.addObject(new ModularLab(1000,900,rand.nextInt(180), gameManager));
		entityManager.addObject(new Assembler(900,900,rand.nextInt(180), gameManager));
		entityManager.addObject(new SolarPanel(800,900,rand.nextInt(180), gameManager));
		entityManager.addObject(new Crusher(700,900,rand.nextInt(180), gameManager));
		entityManager.addObject(new SmallCargoContainer(600,900,rand.nextInt(180), gameManager));
		for (int i=0;i<50;i++)
			entityManager.addObject(new Asteroid(50*rand.nextInt(50)+25*rand.nextInt(20),100*rand.nextInt(20)+20*rand.nextInt(50),rand.nextInt(180), gameManager));
	}
	
	public void updateWorld()
	{
		//Render Box
		for(Entity e : entityManager.getArray())
		{
			if(e.getActive())
			{
				if(e.getCollsionBox().intersect(gameManager.getCamera().getScreenRect()))
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
		canvas.drawBitmap(background,0,0,null);
		entityManager.render(canvas);
		//debug information
		if(gameManager.drawDebugInfo)
		for(Entity e: entityManager.getArray())
		{
			e.drawDebugBox(canvas);
		}

	}
	
	public void interactionCheck(float x,float y)
	{
		Player player=gameManager.getPlayer();
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
						gameManager.setPressedObject((StaticEntity)e);
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

	public Point getSector()
	{
		return new Point(sectorX,sectorY);
	}

	public void randomSector()
    {

       entityManager.reload();
        Random rand=new Random();

        Entity e=entityManager.createObject(rand.nextInt(6));
        e.setWorldLocation(new PointF(900,900));
        entityManager.addObject(e);
        for (int i=0;i<50;i++)
            entityManager.addObject(new Asteroid(50*rand.nextInt(50)+25*rand.nextInt(20),100*rand.nextInt(20)+20*rand.nextInt(50),rand.nextInt(180), gameManager));
    }

    public void warpToSector(int x,int y)
    {
        MA.saveFile("sector-"+sectorX+"-"+sectorY);
       sectorX=x;
       sectorY=y;
        if(MA.loadFile("sector-"+x+"-"+y)==1)
        {
           randomSector();
           MA.saveFile("sector-"+x+"-"+y);
        }
    }
}
