package com.sb9.foloke.sectorb9.game.AI;

import android.graphics.PointF;

import com.sb9.foloke.sectorb9.game.Entities.DynamicEntity;
import com.sb9.foloke.sectorb9.game.Entities.ControlledShip;
import com.sb9.foloke.sectorb9.game.Entities.Entity;
import java.util.*;
import android.graphics.*;
import com.sb9.foloke.sectorb9.game.Managers.*;
import com.sb9.foloke.sectorb9.game.Entities.*;
import com.sb9.foloke.sectorb9.game.Funtions.*;

public abstract class AI {

    protected final float acceptableDistance=200;
    protected ControlledShip child;
	protected final float sightRadius=500;
	//private PointF pickedRandomPoint;
	protected PointF wayPoint=new PointF(0,0);
	protected Entity enemy;
	protected Paint p=new Paint();
	
    public AI(ControlledShip child)
    {
        this.child=child;
		p.setColor(Color.RED);
		wayPoint=pickRandomPoint((int)child.getGameManager().getGamePanel().getWorldSize(),(int)child.getGameManager().getGamePanel().getWorldSize());
    }
	
    public abstract void tick();

	
	protected Entity findEnemy()
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
	protected boolean taskRotateToPoint(PointF p)
	{
		return child.rotationToPoint(p);
	}
	
	protected void taskAddMovement(float acceleration)
	{
		child.addMovement(acceleration);
		child.setMoveable(true);
	}
	protected void taskStop()
	{
		child.setMoveable(false);
	}
	
	
	
	protected boolean isInAcceptableRadius(PointF p)
	{
		return distanceTo(p)<acceptableDistance;
	}
	
	protected boolean isInSightRadius(PointF p)
	{
		return distanceTo(p)<sightRadius;
	}
	
	protected boolean isInSightRadius(Entity e)
	{
		return distanceTo(e.getCenterWorldLocation())<sightRadius;
	}
	
	public void render(Canvas c)
	{
		if(!Options.drawDebugInfo.drawDebugInfo.getBoolean())
			return;
		
		
		if(!isInAcceptableRadius(wayPoint))
			p.setColor(Color.GREEN);
			else
				p.setColor(Color.RED);
		c.drawLine(child.getCenterX(),child.getCenterY(),wayPoint.x,wayPoint.y,p);
	}
	
    protected float distanceTo(PointF p)
    {
        PointF point=new PointF(child.getCenterX()-p.x,child.getCenterY()-p.y);
        return (float)Math.sqrt((point.x)*(point.x)+(point.y)*(point.y));
    }
	
	protected PointF pickRandomPoint(int mx,int my)
	{
		Random rnd=new Random();
		return new PointF(rnd.nextInt(mx),rnd.nextInt(my));
	}
}
