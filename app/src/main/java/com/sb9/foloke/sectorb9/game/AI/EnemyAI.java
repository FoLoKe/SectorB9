package com.sb9.foloke.sectorb9.game.AI;

import android.graphics.PointF;

import com.sb9.foloke.sectorb9.game.Entities.DynamicEntity;
import com.sb9.foloke.sectorb9.game.Entities.EnemyShip;
import com.sb9.foloke.sectorb9.game.Entities.Entity;
import java.util.*;

public class EnemyAI {

    private final float distanceToTarget=200;
    private EnemyShip child;
	private final float sightRadius=500;
	private PointF pickedRandomPoint;
    public EnemyAI(EnemyShip child)
    {
        this.child=child;
		pickedRandomPoint=pickRandomPoint(2000,2000);
    }
    public void tick()
    {
        float distance=distanceTo(child.getGameManager().getPlayer().getCenterWorldLocation());
		if(distance<sightRadius)
		{
       	 	if(distance>distanceToTarget)
        		child.addMovement();
        	
        	if(child.rotationToPoint(child.getGameManager().getPlayer().getCenterWorldLocation(),distance/distanceToTarget)) 
            	if (distance < 200)
                	child.shoot();
		}
		else
		{
			//child.getGameManager().getGamePanel().debugText.setString(""+pickedRandomPoint);
			float rnddistance=distanceTo(pickedRandomPoint);
			if(rnddistance>distanceToTarget)
        		child.addMovement();
        	
        	if(child.rotationToPoint(pickedRandomPoint,rnddistance/distanceToTarget)) 
            	if (rnddistance < 200)
                	pickedRandomPoint=pickRandomPoint(2000,2000);
		}
    }

    private float distanceTo(PointF point)
    {
        point.set(child.getCenterX()-point.x,child.getCenterY()-point.y);
        return (float)Math.sqrt((point.x)*(point.x)+(point.y)*(point.y));
    }
	
	private PointF pickRandomPoint(int mx,int my)
	{
		Random rnd=new Random();
		return new PointF(rnd.nextInt(mx),rnd.nextInt(my));
	}
}
