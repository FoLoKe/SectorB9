package com.sb9.foloke.sectorb9.game.Managers;
import android.graphics.*;
import com.sb9.foloke.sectorb9.*;
import com.sb9.foloke.sectorb9.game.Entities.*;
import com.sb9.foloke.sectorb9.game.Funtions.*;
import com.sb9.foloke.sectorb9.game.UI.CustomViews.*;
import java.io.*;

public class WorldManager
{
	private EntityManager entityManager;
	private GameManager gameManager;
	private int sectorX=0,sectorY=0;

	WorldManager(GameManager gameManager)
	{
        GameLog.update("WorldManager: preparing manager",0);
		this.gameManager = gameManager;
		this.entityManager=new EntityManager(gameManager);
		
        GameLog.update("WorldManager: READY",0);
	}
	
	void loadEmptyWorld()
	{
		sectorX=1;
		sectorY=1;
		BitmapFactory.Options bitmapOptions=new BitmapFactory.Options();
		bitmapOptions.inScaled=false;
	}
	
	void updateWorld()
	{

		for(Entity e : entityManager.getArray())
		{
			if(e.getActive())
			{

				if((gameManager.getCamera().getScreenRect().contains(e.getRenderBox()))||(RectF.intersects(gameManager.getCamera().getScreenRect(),e.getRenderBox())))
				{
					e.setRenderable(true);

				}
				else
					e.setRenderable(false);
			}
			else
				e.setRenderable(false);
		}

		entityManager.tick();
	}
	
	void renderWorld(Canvas canvas)
	{
		//if(background!=null)
		//canvas.drawBitmap(background,0,0,null);
		canvas.drawColor(Color.BLACK);
		entityManager.render(canvas);
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
        if(!GameManager.loadSector(x,y))
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
