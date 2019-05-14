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
import com.sb9.foloke.sectorb9.game.Entities.Buildings.*;

public class AI extends Controller {

    protected final float acceptableDistance=100;
	protected final float acceptableShootDistance=200;
    protected ControlledShip child;
	protected final float sightRadius=300;
	//private PointF pickedRandomPoint;
	//protected PointF wayPoint=new PointF(0,0);
	protected Paint debugPathPaint=new Paint();
	protected Paint debugOrderPaint=new Paint();
	protected Paint debugBehaviourPaint=new Paint();
	protected Paint debugPreBehaviourOrderPaint=new Paint();
	
	
	
	public enum behaviour{AGGRESSIVE,DEFENSIVE,PEACEFUL,RETREAT}
	public enum order{MOVETO,ATTACK,FOLLOW,MINE,STAY,REPAIR,PATROL}
	
	protected behaviour currentBehaviour=behaviour.PEACEFUL;
	protected order currentOrder=order.STAY;
	protected order preBehaviourOrder=order.STAY;
	
	protected Entity targetToFollow;
	protected Entity targetToMine;
	protected Entity targetToAttack;
	protected Entity targetToRepairYourself;
	protected PointF destination;
	
	
    public AI(ControlledShip child)
    {
        this.child=child;
		debugPathPaint.setColor(Color.GREEN);
		destination=pickRandomPoint((int)child.getGameManager().getGamePanel().getWorldSize(),(int)child.getGameManager().getGamePanel().getWorldSize());
    }
	
    public void tick()
	{
		switch(currentOrder)
		{
			case STAY:
				orderStay();
				break;
			case MOVETO:
				orderMoveTo();
				break;
			case ATTACK:
				orderAttack();
				break;
			case MINE:
				orderMine();
				break;
			case FOLLOW:
				orderFollow();
				break;
			case REPAIR:
				orderRepair();
				break;
			case PATROL:
				orderPatrol();
				break;
				
		}
		
		switch(currentBehaviour)
		{
			case AGGRESSIVE:
				orderAgressive();
				break;
			case DEFENSIVE:
				orderDefensive();
				break;
		}
	}
	
	///DEBUG
	public void render(Canvas canvas)
	{
		if(!Options.drawDebugInfo.getBoolean())
			return;
		canvas.drawLine(child.getCenterX(),child.getCenterY(),destination.x,destination.y,debugPathPaint);
		switch(currentOrder)
		{
			case STAY:
				debugOrderPaint.setColor(Color.YELLOW);
				
				break;
			case MOVETO:
				debugOrderPaint.setColor(Color.BLUE);
				break;
				
			case ATTACK:
				debugOrderPaint.setColor(Color.RED);
				break;
				
			case FOLLOW:
				debugOrderPaint.setColor(Color.GREEN);
				break;
				
			case REPAIR:
				debugOrderPaint.setColor(Color.MAGENTA);
				break;

			case MINE:
				debugOrderPaint.setColor(Color.GRAY);
				break;	
				
			case PATROL:
				debugOrderPaint.setColor(Color.WHITE);
				break;	
		}
		
		switch(currentBehaviour)
		{
			case AGGRESSIVE:
				debugBehaviourPaint.setColor(Color.RED);
				break;
				
			case DEFENSIVE:
				debugBehaviourPaint.setColor(Color.YELLOW);
				break;

			case PEACEFUL:
				debugBehaviourPaint.setColor(Color.GREEN);
				break;
				
			case RETREAT:
				debugBehaviourPaint.setColor(Color.WHITE);
				break;

		}
		switch(preBehaviourOrder)
		{
			case STAY:
				debugPreBehaviourOrderPaint.setColor(Color.YELLOW);

				break;
			case MOVETO:
				debugPreBehaviourOrderPaint.setColor(Color.BLUE);
				break;

			case ATTACK:
				debugPreBehaviourOrderPaint.setColor(Color.RED);
				break;

			case FOLLOW:
				debugPreBehaviourOrderPaint.setColor(Color.GREEN);
				break;

			case REPAIR:
				debugPreBehaviourOrderPaint.setColor(Color.MAGENTA);
				break;

			case MINE:
				debugPreBehaviourOrderPaint.setColor(Color.GRAY);
				break;	

			case PATROL:
				debugPreBehaviourOrderPaint.setColor(Color.WHITE);
				break;	

		}
		//state
		canvas.drawCircle(child.getX()-10,child.getY(),5,debugBehaviourPaint);
		canvas.drawCircle(child.getX()-10,child.getY()-10,5,debugOrderPaint);
		canvas.drawCircle(child.getX()-20,child.getY()-10,4,debugPreBehaviourOrderPaint);
	}

	
	
	//orders
	private void orderAgressive()
	{
		if(targetToAttack==null)
		{
			Entity e=findEnemy();
			if(e!=null)
			{
				targetToAttack=e;	
			}
		}
			
		if(currentOrder!=order.ATTACK&&targetToAttack!=null)
		{
			preBehaviourOrder=currentOrder;
			currentOrder=order.ATTACK;
		}
		
		if(targetToAttack==null)
		{
			currentOrder=preBehaviourOrder;
		}
		
	}
	
	private void orderDefensive()
	{
		if(targetToAttack==null)
		{
			Entity e=findEnemy();
			if(e!=null)
			{
				targetToAttack=e;	
			}
		}

		if(currentOrder!=order.ATTACK&&targetToAttack!=null)
		{
			preBehaviourOrder=currentOrder;
			currentOrder=order.ATTACK;
		}

		if(targetToAttack==null)
		{
			currentOrder=preBehaviourOrder;
		}
	}
	
	private void orderPatrol()
	{
		if(taskRotateToPoint(destination))
		{
			if(!isInAcceptableRadius(destination))
				taskAddMovement(1);
			else
				destination=pickRandomPoint((int)child.getGameManager().getGamePanel().getWorldSize(),(int)child.getGameManager().getGamePanel().getWorldSize());
		}
		else
			taskStop();
	}
	
	private void orderRepair()
	{
		if(targetToRepairYourself!=null)
		{
			if(targetToRepairYourself.getActive())
			{
				destination=targetToRepairYourself.getCenterWorldLocation();

				if(taskRotateToPoint(destination))
				{
					if(!isInAcceptableShootRadius(destination))
						taskAddMovement(1);
					
				}
				else
					taskStop();

				return;
			}
		}

		targetToRepairYourself=findRepair();
	}
	
	private void orderFollow()
	{
		if(targetToFollow!=null)
		{
			if(targetToFollow.getActive())
			{
				destination=targetToFollow.getCenterWorldLocation();

				if(taskRotateToPoint(destination))
				{
					if(!isInAcceptableShootRadius(destination))
						taskAddMovement(1);
					
				}
				else
					taskStop();

				return;
			}
		}

		currentOrder=order.STAY;
	}
	
	public void orderMine()
	{
		if(targetToMine!=null)
		{
			if(targetToMine.getActive())
			{
				destination=targetToMine.getCenterWorldLocation();

				if(taskRotateToPoint(destination))
				{
					if(!isInAcceptableShootRadius(destination))
						taskAddMovement(1);
					else
						child.shoot();
				}
				else
					taskStop();

				return;
			}
		}
	
			targetToMine=findMinable();
		
	}
	
	protected void orderAttack()
	{
		if(targetToAttack!=null)
		{
			if(targetToAttack.getActive())
			{
				destination=targetToAttack.getCenterWorldLocation();
				
				if(taskRotateToPoint(destination))
				{
					if(!isInAcceptableShootRadius(destination))
						taskAddMovement(1);
					else
						child.shoot();
				}
				else
					taskStop();
				
				return;
			}
			else
				targetToAttack=null;
		}
		
		
	}
	
	private void orderMoveTo()
	{
		if(taskRotateToPoint(destination))
		{
			if(!isInAcceptableRadius(destination))
				taskAddMovement(1);
			else
				currentOrder=order.STAY;
		}
	}
	
	private void orderStay()
	{
		taskStop();
	}
	
	//tasks
	protected Entity findEnemy()
	{
		for(Entity e:child.getGameManager().getWorldManager().getEntityManager().getArray())
		{
			if(e.getTeam()!=child.getTeam()&&e.getTeam()!=0)
			if(isInSightRadius(e)&&e.getActive())
			{
				//enemy=e;
				return e;
			}
		}
		return null;
	}
	
	private Entity findMinable()
	{
		Entity asteroid=null;
		for(Entity e:child.getGameManager().getWorldManager().getEntityManager().getArray())
		{
			if(e instanceof Asteroid)
				if(e.getActive())
				{
					//enemy=e;
					asteroid=e;
					break;
				}
		}

		for(Entity e:child.getGameManager().getWorldManager().getEntityManager().getArray())
		{
			if(e instanceof Asteroid)
				if(e.getActive()&&distanceTo(e.getCenterWorldLocation())<distanceTo(asteroid.getCenterWorldLocation()))
				{
					//enemy=e;
					asteroid=e;
					
				}
		}
		
		
		return asteroid;
	}
	
	private Entity findRepair()
	{
		Entity asteroid=null;
		for(Entity e:child.getGameManager().getWorldManager().getEntityManager().getArray())
		{
			if(e instanceof SpaceDock)
				if(e.getActive())
				{
					//enemy=e;
					asteroid=e;
					break;
				}
		}

		for(Entity e:child.getGameManager().getWorldManager().getEntityManager().getArray())
		{
			if(e instanceof SpaceDock)
				if(e.getActive()&&distanceTo(e.getCenterWorldLocation())<distanceTo(asteroid.getCenterWorldLocation()))
				{
					//enemy=e;
					asteroid=e;

				}
		}


		return asteroid;
	}
	
	protected boolean taskRotateToPoint(PointF p)
	{
		return child.rotationToPoint(p);
	}
	
	protected void taskAddMovement(float acceleration)
	{
		child.addMovement(acceleration);
		child.setMovable(true);
	}
	protected void taskStop()
	{
		child.setMovable(false);
	}
	
	protected boolean isInAcceptableRadius(PointF p)
	{
		return distanceTo(p)<acceptableDistance;
	}
	
	protected boolean isInAcceptableShootRadius(PointF p)
	{
		return distanceTo(p)<acceptableShootDistance;
	}
	
	protected boolean isInSightRadius(PointF p)
	{
		return distanceTo(p)<sightRadius;
	}
	
	protected boolean isInSightRadius(Entity e)
	{
		return distanceTo(e.getCenterWorldLocation())<sightRadius;
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
	
	public void setCurrentOrder(order currentOrder)
	{
		this.currentOrder = currentOrder;
		this.preBehaviourOrder=currentOrder;
	}

	public order getCurrentOrder()
	{
		return currentOrder;
	}

	public void setCurrentBehaviour(behaviour currentBehaviour)
	{
		this.currentBehaviour = currentBehaviour;
	}

	public behaviour getCurrentBehaviour()
	{
		return currentBehaviour;
	}
	public void setTargetToAttack(Entity targetToAttack)
	{
		this.targetToAttack = targetToAttack;
	}

	public Entity getTargetToAttack()
	{
		return targetToAttack;
	}
	
	public void setDestination(PointF destination)
	{
		this.destination = destination;
	}

	public PointF getDestination()
	{
		return destination;
	}
	
	public void setTargetToFollow(Entity targetToFollow)
	{
		this.targetToFollow = targetToFollow;
	}

	public Entity getTargetToFollow()
	{
		return targetToFollow;
	}

	
}
