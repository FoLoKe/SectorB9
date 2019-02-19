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
		randomSectorHostile(WM.getEntityManager(),WM.getGameManager());
	}
	
	public static void randomSectorPeaceful(EntityManager entityManager,GameManager gameManager)
    {
       	entityManager.reload();
		entityManager.addObject(gameManager.getPlayer());
        Random rand=new Random();
       
       
        for (int i=0;i<50;i++)
			entityManager.addObject(new Asteroid(50*rand.nextInt(50)+25*rand.nextInt(20),100*rand.nextInt(20)+20*rand.nextInt(50),rand.nextInt(180), gameManager,rand.nextInt(10)));
    }
	
	public static void randomSectorHostile(EntityManager entityManager,GameManager gameManager)
    {
       	entityManager.reload();
        Random rand=new Random();
		entityManager.addObject(gameManager.getPlayer());
		for(int i=0;i<3;i++)
			entityManager.addObject(new EnemyShip(50*rand.nextInt(50)+25*rand.nextInt(20),1000,0,gameManager));
		for(int i=0;i<10;i++)
			entityManager.addObject(new Asteroid(50*rand.nextInt(50)+25*rand.nextInt(20),100*rand.nextInt(20)+20*rand.nextInt(50),rand.nextInt(180), gameManager,rand.nextInt(10)));
    }
	
}
