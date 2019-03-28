package com.sb9.foloke.sectorb9.game.Managers;
import com.sb9.foloke.sectorb9.*;
import com.sb9.foloke.sectorb9.game.Entities.*;
import com.sb9.foloke.sectorb9.game.Entities.Buildings.*;

import java.util.*;

import android.graphics.*;
import java.io.*;
import com.sb9.foloke.sectorb9.game.Funtions.*;
import com.sb9.foloke.sectorb9.game.UI.*;

public class WorldManager
{
	private MainActivity MA;
	private EntityManager entityManager;
	private GameManager gameManager;
	private CustomCollisionObject interObject;
	//background
	private Bitmap background;

	////current sector
	private int sectorX=0,sectorY=0;

	public WorldManager(MainActivity MA, GameManager gameManager)
	{
		this.MA=MA;
		this.gameManager = gameManager;
		this.entityManager=new EntityManager(gameManager);
		interObject=new CustomCollisionObject(2,2,gameManager);
		
	}
	
	public void loadEmptyWorld()
	{
		sectorX=1;
		sectorY=1;
		BitmapFactory.Options bitmapOptions=new BitmapFactory.Options();
		bitmapOptions.inScaled=false;
		background=Bitmap.createBitmap(BitmapFactory.decodeResource(MA.getResources(),R.drawable.galactic_outflow,bitmapOptions));

		entityManager.addObject(gameManager.getPlayer());
		
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
        entityManager.addObject(gameManager.getPlayer());
		entityManager.addObject(new EnemyShip(700,900,0,gameManager));
		for (int i=0;i<50;i++)
			entityManager.addObject(new Asteroid(50*rand.nextInt(50)+25*rand.nextInt(20),100*rand.nextInt(20)+20*rand.nextInt(50),rand.nextInt(180), gameManager,rand.nextInt(10)));
	}
	
	public void updateWorld()
	{
		int i=0;
		for(Entity e : entityManager.getArray())
		{
			if(e.getActive())
			{

				if((gameManager.getCamera().getScreenRect().contains(e.getRenderBox()))||(RectF.intersects(gameManager.getCamera().getScreenRect(),e.getRenderBox())))
				{
					e.setRenderable(true);
					i++;
				}
				else
					e.setRenderable(false);
			}
			else
				e.setRenderable(false);
		}
		
		//objects ticks
		gameManager.gamePanel.debugText.setString("objects in render:"+ i);
		entityManager.tick();
	}
	
	public void renderWorld(Canvas canvas)
	{
		//if(background!=null)
		//canvas.drawBitmap(background,0,0,null);
		canvas.drawColor(Color.BLACK);
		entityManager.render(canvas);
	}
	
	public void interactionCheck(float x,float y)
	{
		interObject.calculateCollisionObject(x,y);
		Player player=gameManager.getPlayer();
		for(Entity e: entityManager.getArray())
		{
			if(e.getCollisionObject().intersects(interObject))
			{
				if(Math.sqrt(
					   (e.getCenterX()-player.getCenterX())*(e.getCenterX()-player.getCenterX())
					   +(e.getCenterY()-player.getCenterY())*(e.getCenterY()-player.getCenterY()))-32<player.getRadius())											 
				{								
					if(e instanceof StaticEntity)
					{
						gameManager.setPressedObject((StaticEntity)e);
						InteractionUI.init(MA,MA.getViewFlipper(),(StaticEntity)e);
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

	public GameManager getGameManager()
	{
		return gameManager;
	}

	public void save(BufferedWriter w)
	{
		entityManager.save(w);
	}
	
	public void load(BufferedReader r)
	{
		entityManager.load(r);
		entityManager.addObject(gameManager.getPlayer());
	}

	public Point getSector()
	{
		return new Point(sectorX,sectorY);
	}

	public void setSector(int x,int y)
	{
		sectorX=x;
		sectorY=y;
	}
	
    public void warpToSector(int x,int y)
    {
		gameManager.saveGame();
		sectorX=x;
		sectorY=y;
        if(gameManager.loadSector(x,y))
        {
           WorldGenerator.makeRandomSector(this);
			gameManager.saveGame();
        }
    }
	
	public void spawnDestroyed(Entity e)
	{
        double a =System.nanoTime();
		entityManager.addObject(new DroppedItems(e));
		//entityManager.deleteObject(e);
        double b =System.nanoTime();
        gameManager.getGamePanel().textDebug3.setString(""+(b-a));
	}
}
