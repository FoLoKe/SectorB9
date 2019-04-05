package com.sb9.foloke.sectorb9.game.AI;

import android.graphics.PointF;

import com.sb9.foloke.sectorb9.game.Entities.DynamicEntity;
import com.sb9.foloke.sectorb9.game.Entities.EnemyShip;
import com.sb9.foloke.sectorb9.game.Entities.Entity;
import java.util.*;
import android.graphics.*;
import com.sb9.foloke.sectorb9.game.Managers.*;
import com.sb9.foloke.sectorb9.game.Entities.*;
import com.sb9.foloke.sectorb9.game.Funtions.*;

public class EnemyAI {

    private final float acceptableDistance=200;
    private EnemyShip child;
	private final float sightRadius=500;
	//private PointF pickedRandomPoint;
	private PointF wayPoint=new PointF(0,0);
	private Entity enemy;
	
    public EnemyAI(EnemyShip child)
    {
        this.child=child;
		wayPoint=pickRandomPoint((int)child.getGameManager().getGamePanel().getWorldSize(),(int)child.getGameManager().getGamePanel().getWorldSize());
    }
	
    public void tick()
    {
        if(child.getTeam()!=child.getGameManager().getPlayer().getTeam())
		{
			if(isInSightRadius(child.getGameManager().getPlayer())&&child.getGameManager().getPlayer().getActive())
			{
				wayPoint=child.getGameManager().getPlayer().getCenterWorldLocation();
       	 		if(!isInAcceptableRadius(wayPoint))
        			taskAddMovement(0.9f);
        	
        		if(taskRotateToPoint(wayPoint)) 
            		if (isInAcceptableRadius(wayPoint))
                		//wayPoint=wayPoint;//
						child.shoot();
			}
			else
			{
        		if(taskRotateToPoint(wayPoint) )
				{
					if(!isInAcceptableRadius(wayPoint))
						taskAddMovement(0.5f);
            		else
                		wayPoint=pickRandomPoint((int)child.getGameManager().getGamePanel().getWorldSize(),(int)child.getGameManager().getGamePanel().getWorldSize());
				}
			}
		}
		else
		{
			if(findEnemy()!=null)
			{
				wayPoint=enemy.getCenterWorldLocation();
				if(!isInAcceptableRadius(wayPoint))
					taskAddMovement(0.9f);

				if(taskRotateToPoint(wayPoint)) 
					if (isInAcceptableRadius(wayPoint))
						child.shoot();
			}
			else
			{
				wayPoint=child.getGameManager().getPlayer().getCenterWorldLocation();
				if(!isInAcceptableRadius(wayPoint))
					taskAddMovement(0.9f);
					taskRotateToPoint(wayPoint);
			}
		}
    }
	private Entity findEnemy()
	{
		for(Entity e:child.getGameManager().getWorldManager().getEntityManager().getArray())
		{
			if(e.getTeam()!=child.getTeam()&&e.getTeam()!=0)
			if(isInSightRadius(e)&&e.getActive())
			{
				enemy=e;
				return e;
			}
		}
		return null;
	}
	private boolean taskRotateToPoint(PointF p)
	{
		return child.rotationToPoint(p);
	}
	
	public void taskAddMovement(float acceleration)
	{
		child.addMovement(acceleration);
	}
	
	public boolean isInAcceptableRadius(PointF p)
	{
		return distanceTo(p)<acceptableDistance;
	}
	
	public boolean isInSightRadius(PointF p)
	{
		return distanceTo(p)<sightRadius;
	}
	
	public boolean isInSightRadius(Entity e)
	{
		return distanceTo(e.getCenterWorldLocation())<sightRadius;
	}
	
	public void render(Canvas c)
	{
		if(!Options.drawDebugInfo.drawDebugInfo.getBoolean())
			return;
		Paint p=new Paint();
		
		if(!isInAcceptableRadius(wayPoint))
			p.setColor(Color.GREEN);
			else
				p.setColor(Color.RED);
		c.drawLine(child.getCenterX(),child.getCenterY(),wayPoint.x,wayPoint.y,p);
	}
    private float distanceTo(PointF p)
    {
        PointF point=new PointF(child.getCenterX()-p.x,child.getCenterY()-p.y);
        return (float)Math.sqrt((point.x)*(point.x)+(point.y)*(point.y));
    }
	
	private PointF pickRandomPoint(int mx,int my)
	{
		Random rnd=new Random();
		return new PointF(rnd.nextInt(mx),rnd.nextInt(my));
	}
}
