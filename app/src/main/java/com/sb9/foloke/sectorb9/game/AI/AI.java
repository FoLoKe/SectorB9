package com.sb9.foloke.sectorb9.game.AI;

import android.graphics.*;
import com.sb9.foloke.sectorb9.game.Entities.*;
import com.sb9.foloke.sectorb9.game.Entities.Buildings.*;
import com.sb9.foloke.sectorb9.game.Funtions.*;
import java.util.*;
import com.sb9.foloke.sectorb9.game.Assets.*;

public class AI extends Controller {

    protected final float acceptableDistance=100;
	protected final float acceptableShootDistance=200;
    protected ControlledShip child;
	protected final float sightRadius=300;
	//private PointF pickedRandomPoint;
	//protected PointF wayPoint=new PointF(0,0);
	protected Paint debugPathPaint=new Paint();
	protected Paint debugOrderPaint=new Paint();
	
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
	protected Entity targetToRetreatFrom;
	protected PointF destination;
	
	
    public AI(ControlledShip child)
    {
        this.child=child;
		debugPathPaint.setColor(Color.GREEN);
		
		debugPathPaint.setStrokeWidth(10);
		debugPathPaint.setStyle(Paint.Style.FILL);
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
			case PEACEFUL:
				
				break;
			case RETREAT:
				orderRetreat();
				break;
			
		}
	}
	
	///DEBUG
	public void render(Canvas canvas)
	{
		
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
				canvas.drawBitmap(AIAsset.aggressive,child.getX(),child.getCenterY()+32,null);
				break;
				
			case DEFENSIVE:
				canvas.drawBitmap(AIAsset.defensive,child.getX(),child.getCenterY()+32,null);
				
				break;

			case PEACEFUL:
				canvas.drawBitmap(AIAsset.peaceful,child.getX(),child.getCenterY()+32,null);
				break;
				
			case RETREAT:
				canvas.drawBitmap(AIAsset.retreatful,child.getX(),child.getCenterY()+32,null);
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
		
		canvas.drawCircle(child.getX()-10,child.getY()-10,5,debugOrderPaint);
		
		
		
		if(!Options.drawDebugInfo.getBoolean())
			return;
		canvas.drawLine(child.getCenterX(),child.getCenterY(),destination.x,destination.y,debugPathPaint);
		canvas.drawCircle(child.getX()-20,child.getY()-10,4,debugPreBehaviourOrderPaint);
		
		
	}

	
	
	//orders
	private void orderRetreat()
	{
		if(targetToRetreatFrom==null)
		{
			Entity e=findEnemy();
			if(e!=null)
			{
				targetToRetreatFrom=e;	
				preBehaviourOrder=currentOrder;
				currentOrder=order.MOVETO;
			}
		}
		else
		{
			if(isInSightRadius(targetToRetreatFrom))
			{
				PointF vector=new PointF(targetToRetreatFrom.getCenterX()-child.getCenterX(),targetToRetreatFrom.getCenterY()-child.getCenterY());
				float lengh=(float)Math.sqrt(vector.x*vector.x+vector.y*vector.y);
				vector.set(vector.x/lengh*sightRadius,vector.y/lengh*sightRadius);
				destination=new PointF(child.getCenterX()-vector.x,child.getCenterY()-vector.y);
			}
			else
				targetToRetreatFrom=null;
			
		}

		if(targetToRetreatFrom==null||!targetToRetreatFrom.getActive())
		{
			currentOrder=preBehaviourOrder;
		}
	}
	
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
			Damage damage=child.getLastDamage();
			if(damage!=null)
			{
				targetToAttack=damage.instignator;
				child.setLastDamage(null);
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

	public String getAISaveLine()
	{
		String saveString="";
		saveString+=currentBehaviour+"=";
		saveString+=preBehaviourOrder+"=";
		saveString+=currentOrder+"=";
		
		if(targetToAttack!=null)
			saveString+=targetToAttack.getCenterX()+":"+targetToAttack.getCenterY()+"=";
		else
			saveString+=(-1f)+":"+(-1f)+"=";
			
		if(targetToFollow!=null)
			saveString+=targetToFollow.getCenterX()+":"+targetToFollow.getCenterY()+"=";
		else
			saveString+=(-1f)+":"+(-1f)+"=";
			
		if(targetToRetreatFrom!=null)
			saveString+=targetToRetreatFrom.getCenterX()+":"+targetToRetreatFrom.getCenterY()+"=";
		else
			saveString+=(-1f)+":"+(-1f)+"=";
			
		if(targetToMine!=null)
			saveString+=targetToMine.getCenterX()+":"+targetToMine.getCenterY()+"=";
		else
			saveString+=(-1f)+":"+(-1f)+"=";
			
		if(targetToRepairYourself!=null)
			saveString+=targetToRepairYourself.getCenterX()+":"+targetToRepairYourself.getCenterY()+"=";
		else
			saveString+=(-1f)+":"+(-1f)+"=";
			
		return saveString;
	}
	
	public void decodeSaveLine(String saveLine)
	{
		///TODO: AFTER LOAD TARGETS SETS, LOAD SYSTEM IN MAINTHREAD ISTEAD OF UI THREAD
		String aiWords[]=saveLine.split("=");
		PointF loadedTargetToAttackPoint=new PointF();
		PointF loadedTargetToFollowPoint=new PointF();
		PointF loadedTargetToRetreatFromPoint=new PointF();
		PointF loadedTargetToMinePoint=new PointF();
		PointF loadedTargetToRepairYouselfPoint=new PointF();
		
		if(aiWords.length>0)
		{
			currentBehaviour=behaviour.valueOf(aiWords[0]);
			preBehaviourOrder=order.valueOf(aiWords[1]);
			currentOrder=order.valueOf(aiWords[2]);
		}
		
		String pointWords[]=aiWords[3].split(":");
		loadedTargetToAttackPoint.set(Float.parseFloat(pointWords[0]),Float.parseFloat(pointWords[1]));
		pointWords=aiWords[4].split(":");
		loadedTargetToFollowPoint.set(Float.parseFloat(pointWords[0]),Float.parseFloat(pointWords[1]));
		pointWords=aiWords[5].split(":");
		loadedTargetToRetreatFromPoint.set(Float.parseFloat(pointWords[0]),Float.parseFloat(pointWords[1]));
		pointWords=aiWords[6].split(":");
		loadedTargetToMinePoint.set(Float.parseFloat(pointWords[0]),Float.parseFloat(pointWords[1]));
		pointWords=aiWords[7].split(":");
		loadedTargetToRepairYouselfPoint.set(Float.parseFloat(pointWords[0]),Float.parseFloat(pointWords[1]));
		
		for(Entity e:child.getGameManager().getEntityManager().getArray())
		{
			if(e.getCenterX()==loadedTargetToAttackPoint.x&&e.getCenterY()==loadedTargetToAttackPoint.y)
				targetToAttack=e;
			if(e.getCenterX()==loadedTargetToFollowPoint.x&&e.getCenterY()==loadedTargetToFollowPoint.y)
				targetToFollow=e;
			if(e.getCenterX()==loadedTargetToRetreatFromPoint.x&&e.getCenterY()==loadedTargetToRetreatFromPoint.y)
				targetToRetreatFrom=e;
			if(e.getCenterX()==loadedTargetToMinePoint.x&&e.getCenterY()==loadedTargetToMinePoint.y)
				targetToMine=e;
			if(e.getCenterX()==loadedTargetToRepairYouselfPoint.x&&e.getCenterY()==loadedTargetToRepairYouselfPoint.y)
				targetToMine=e;
		}
	}
}
