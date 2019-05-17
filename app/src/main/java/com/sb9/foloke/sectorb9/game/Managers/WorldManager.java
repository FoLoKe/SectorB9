package com.sb9.foloke.sectorb9.game.Managers;
import com.sb9.foloke.sectorb9.*;
import com.sb9.foloke.sectorb9.game.Entities.*;
import com.sb9.foloke.sectorb9.game.Entities.Buildings.*;

import java.util.*;

import android.graphics.*;
import java.io.*;

import com.sb9.foloke.sectorb9.game.Entities.Ships.Ship;
import com.sb9.foloke.sectorb9.game.Funtions.*;
import com.sb9.foloke.sectorb9.game.UI.*;
import com.sb9.foloke.sectorb9.game.UI.CustomViews.GameLog;

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

	WorldManager(MainActivity MA, GameManager gameManager)
	{
        GameLog.update("WorldManager: preparing manager",0);
		this.MA=MA;
		this.gameManager = gameManager;
		this.entityManager=new EntityManager(gameManager);
		interObject=new CustomCollisionObject(2,2,gameManager);
        GameLog.update("WorldManager: READY",0);
	}
	
	void loadEmptyWorld()
	{
		sectorX=1;
		sectorY=1;
		BitmapFactory.Options bitmapOptions=new BitmapFactory.Options();
		bitmapOptions.inScaled=false;
		background=Bitmap.createBitmap(BitmapFactory.decodeResource(MA.getResources(),R.drawable.galactic_outflow,bitmapOptions));

		//entityManager.addObject(gameManager.getPlayer());
		
		}
	
	void updateWorld()
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
		
		entityManager.tick();
	}
	
	void renderWorld(Canvas canvas)
	{
		//if(background!=null)
		//canvas.drawBitmap(background,0,0,null);
		canvas.drawColor(Color.BLACK);
		entityManager.render(canvas);
	}
	
	void interactionCheck(float x,float y)
	{
		interObject.calculateCollisionObject(x,y);
	
		for(Entity e: entityManager.getArray())
		{
			if(e.getCollisionObject().intersects(interObject))
			{
//				if(Math.sqrt(
//					   (e.getCenterX()-player.getCenterX())*(e.getCenterX()-player.getCenterX())
//					   +(e.getCenterY()-player.getCenterY())*(e.getCenterY()-player.getCenterY()))-32<PlayerController.interactionRadius)											 
//				{								
					gameManager.interactionTouch(e,e.getCenterWorldLocation());
					return;
				
			}
		}
		gameManager.interactionTouch(null,new PointF(x,y));
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

	Point getSector()
	{
		return new Point(sectorX,sectorY);
	}

	void setSector(int x,int y)
	{
		sectorX=x;
		sectorY=y;
	}
	
    void warpToSector(int x,int y)
    {
		gameManager.saveGame();
		sectorX=x;
		sectorY=y;
        if(!gameManager.loadSector(x,y))
        {
           WorldGenerator.makeSector(gameManager,gameManager.getMapManager().getSector(x,y));
			gameManager.saveGame();
        }
		gameManager.getMapManager().getSector(x,y).explored=true;
    }
	
	void spawnDestroyed(Entity e)
	{
        
		entityManager.addObject(new DroppedItems(e));
        
        
	}
}
