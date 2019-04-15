package com.sb9.foloke.sectorb9.game.Funtions;
import com.sb9.foloke.sectorb9.game.Managers.*;
import java.util.*;
import com.sb9.foloke.sectorb9.game.Entities.*;
import android.graphics.*;

public class WorldGenerator
{
	public static void makeRandomSector(WorldManager WM)
	{
		Random rnd=new Random();
		switch(rnd.nextInt(1))
		{
			case 0:
				randomSectorPeaceful(WM.getEntityManager(),WM.getGameManager());
				break;
			case 1:
				randomSectorHostile(WM.getEntityManager(),WM.getGameManager());
				break;
		}

	}
	
	public static void randomSectorPeaceful(EntityManager entityManager,GameManager gameManager)
    {
       	entityManager.reload();
		
        Random rand=new Random();
       
       
        for (int i=0;i<50;i++)
			entityManager.addObject(new Asteroid(50*rand.nextInt(50)+25*rand.nextInt(20),100*rand.nextInt(20)+20*rand.nextInt(50),rand.nextInt(180), gameManager,rand.nextInt(3)));
    }
	
	public static void randomSectorHostile(EntityManager entityManager,GameManager gameManager)
    {
       	entityManager.reload();
        Random rand=new Random();
		
		for(int i=0;i<3;i++)
			entityManager.addObject(new ControlledShip(50*rand.nextInt(50)+25*rand.nextInt(20),1000,0,gameManager,rand.nextInt(1)));
		for(int i=0;i<500;i++)
			entityManager.addObject(new Asteroid(50*rand.nextInt(50)+25*rand.nextInt(20),100*rand.nextInt(20)+20*rand.nextInt(50),rand.nextInt(180), gameManager,rand.nextInt(3)));
    }
	
	public static void makeTestingBox(GameManager gameManager)
	{
		EntityManager entityManager=gameManager.getEntityManager();
		entityManager.reload();
        Random rand=new Random();
		gameManager.getGamePanel().setWorldSize(250);
		entityManager.addObject(gameManager.getPlayer());
		for(int i=0;i<3;i++)
			entityManager.addObject(new ControlledShip(rand.nextInt(100),rand.nextInt(100),0,gameManager,rand.nextInt(1)));
		for(int i=0;i<2;i++)
			entityManager.addObject(new Asteroid(rand.nextInt(50),rand.nextInt(180),0, gameManager,rand.nextInt(3)));
	}
	
	public static void makeSector(GameManager gameManager,MapManager.Sector s)
	{
		EntityManager entityManager=gameManager.getEntityManager();
		entityManager.reload();
        Random rand=new Random();
		float WS=gameManager.getGamePanel().getWorldSize();
		switch(s.type)
		{
			case (EMPTY):
				
				break;
			case ASTEROID_BELT:
				Matrix m=new Matrix();
				m.postRotate(45);
				for(int i=0;i<100;i++)
				{
					float[] y=new float[]{WS/3+rand.nextInt((int)WS/3),rand.nextInt((int)WS)};
					m.mapPoints(y);
					entityManager.addObject(new Asteroid(y[0],y[1],0, gameManager,rand.nextInt(3)));
				}
				break;
			case ASTEROID_CLUSTER:
				for(int i=0;i<100;i++)
					entityManager.addObject(new Asteroid(WS/3+rand.nextInt((int)WS/3),WS/3+rand.nextInt((int)WS/3),0, gameManager,rand.nextInt(3)));
				break;
		}
		
		switch(s.agr)
		{
			case HOSTILE:
			for(int i=0;i<5;i++)
				entityManager.addObject(new ControlledShip(rand.nextInt((int)WS),rand.nextInt((int)WS),0,gameManager,0));
			break;
			case PEACEFUL:
				for(int i=0;i<5;i++)
					entityManager.addObject(new ControlledShip(rand.nextInt((int)WS),rand.nextInt((int)WS),0,gameManager,1));
				break;
		}
		
		
	}
	
}
