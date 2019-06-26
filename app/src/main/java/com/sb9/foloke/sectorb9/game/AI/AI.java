package com.sb9.foloke.sectorb9.game.AI;

import android.graphics.*;
import com.sb9.foloke.sectorb9.game.Entities.*;
import com.sb9.foloke.sectorb9.game.Entities.Buildings.*;
import com.sb9.foloke.sectorb9.game.Funtions.*;
import java.util.*;
import com.sb9.foloke.sectorb9.game.Assets.*;
import android.graphics.drawable.*;

public class AI extends Controller {

    private final float acceptableDistance=100;
    private final float acceptableShootDistance=200;
    private ControlledShip child;
    private final float sightRadius=300;
    private Paint debugPathPaint=new Paint();
    
    private Paint debugPreBehaviourOrderPaint=new Paint();

	public enum behaviour{AGGRESSIVE,DEFENSIVE,PEACEFUL,RETREAT}
	public enum order{MOVE,ATTACK,FOLLOW,MINE,STAY,REPAIR,PATROL}

    private behaviour currentBehaviour=behaviour.PEACEFUL;
    private order currentOrder=order.STAY;
    private order preBehaviourOrder=order.STAY;

    private Entity targetToFollow;
    private Entity targetToMine;
    private Entity targetToAttack;
    private Entity targetToRepairYourself;
    private Entity targetToRetreatFrom;
    private PointF destination;

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
			case MOVE:
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
				orderAggressive();
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

	private void drawSymbol(Canvas canvas,PointF point)
	{
		
		Paint paint=new Paint();
		paint.setColor(Color.GREEN);
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth(2);
		
		float radius=20;
		
		float fullInterval=6.28f*radius/4*3/4;
		float blankInterval=6.28f*radius/4*1/4;
		
		float[] intervals=new float[]{fullInterval,blankInterval};
		DashPathEffect effect=new DashPathEffect(intervals,-blankInterval/2);
		
		paint.setPathEffect(effect);
		canvas.drawCircle(point.x,point.y,radius,paint);
	}
	
	public void render(Canvas canvas)
	{
		
		switch(currentOrder)
		{
			case STAY:
				break;
				
			case MOVE:
				if(destination!=null)
					drawSymbol(canvas,destination);
				break;
				
			case ATTACK:
				if(targetToAttack!=null)
					drawSymbol(canvas,targetToAttack.getCenterWorldLocation());
				break;
				
			case FOLLOW:
				if(targetToFollow!=null)
				{
					Paint pathPaint=new Paint();
					pathPaint.setColor(Color.parseColor("#5500ff00"));
					pathPaint.setStrokeWidth(5);
					drawSymbol(canvas,targetToFollow.getCenterWorldLocation());
					canvas.drawLine(child.getCenterX(),child.getCenterY(),targetToFollow.getCenterX(),targetToFollow.getCenterY(),pathPaint);
				}
				break;
				
			case REPAIR:
				if(targetToRepairYourself!=null)
					drawSymbol(canvas,targetToRepairYourself.getCenterWorldLocation());
				break;

			case MINE:
				if(targetToMine!=null)
					drawSymbol(canvas,targetToMine.getCenterWorldLocation());
				break;	
				
			case PATROL:
				if(destination!=null)
					drawSymbol(canvas,destination);
				
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
			case MOVE:
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

		
		
		if(!Options.drawDebugInfo.getBoolean())
			return;
		canvas.drawLine(child.getCenterX(),child.getCenterY(),destination.x,destination.y,debugPathPaint);
		canvas.drawCircle(child.getX()-20,child.getY()-10,4,debugPreBehaviourOrderPaint);
	}

	private void orderRetreat()
	{
		if(targetToRetreatFrom==null)
		{
			Entity e=findEnemy();
			if(e!=null)
			{
				targetToRetreatFrom=e;	
				preBehaviourOrder=currentOrder;
				currentOrder=order.MOVE;
			}
		}
		else
		{
			if(isInSightRadius(targetToRetreatFrom))
			{
				PointF vector=new PointF(targetToRetreatFrom.getCenterX()-child.getCenterX(),targetToRetreatFrom.getCenterY()-child.getCenterY());
				float length=(float)Math.sqrt(vector.x*vector.x+vector.y*vector.y);
				vector.set(vector.x/length*sightRadius,vector.y/length*sightRadius);
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
	
	private void orderAggressive()
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
	
	private void orderMine()
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
	
			targetToMine=findAsteroid();
		
	}
	
	private void orderAttack()
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
		else
		{
			taskStop();
		}
	}
	
	private void orderStay()
	{
		taskStop();
	}

	private Entity findEnemy()
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
	
	private Entity findAsteroid()
	{
		Entity asteroid=null;
		for(Entity e:child.getGameManager().getWorldManager().getEntityManager().getArray())
		{
			if(e instanceof Asteroid)
				if(e.getActive())
				{
					asteroid=e;
					break;
				}
		}

		for(Entity e:child.getGameManager().getWorldManager().getEntityManager().getArray())
		{
			if(e instanceof Asteroid)
				if(e.getActive()&&distanceTo(e.getCenterWorldLocation())<distanceTo(asteroid.getCenterWorldLocation()))
				{
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
	
	private boolean taskRotateToPoint(PointF p)
	{
		return child.rotationToPoint(p);
	}
	
	private void taskAddMovement(float acceleration)
	{
		child.addMovement(acceleration);
		child.setMovable(true);
	}

	private void taskStop()
	{
		child.setMovable(false);
	}
	
	private boolean isInAcceptableRadius(PointF p)
	{
		return distanceTo(p)<acceptableDistance;
	}
	
	private boolean isInAcceptableShootRadius(PointF p)
	{
		return distanceTo(p)<acceptableShootDistance;
	}
	
	private boolean isInSightRadius(PointF p)
	{
		return distanceTo(p)<sightRadius;
	}
	
	private boolean isInSightRadius(Entity e)
	{
		return distanceTo(e.getCenterWorldLocation())<sightRadius;
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
		String[] aiWords=saveLine.split("=");
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
		
		String[] pointWords=aiWords[3].split(":");
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
