package com.sb9.foloke.sectorb9.game.AI;
import com.sb9.foloke.sectorb9.game.Entities.*;
import com.sb9.foloke.sectorb9.game.UI.CustomViews.*;
import android.graphics.*;

public class MinerAI extends AI
{
	private Entity resourceSpot;
	private boolean stoped=false;
	private boolean invEmpty=true;
	private Entity container;
	private String debugState="";
	
	public MinerAI(ControlledShip e)
	{
		super(e);
	}

	@Override
	public void render(Canvas c)
	{
		// TODO: Implement this method
		super.render(c);
		c.drawText(debugState,child.getCenterX(),child.getCenterY(),p);
	}
	
	
	
	@Override
	public void tick()
	{
		if(invEmpty)
		{
			findAndCollectDebris();
			
			if(resourceSpot==null||!resourceSpot.getActive())
			{
				findResources();
			}
		
			if(resourceSpot!=null&&resourceSpot.getActive())
			{
				if(stoped)
					taskGoForRes();
				else
					taskFullStop();
			}
			else
			{
				if(stoped)
				{
					if(taskRotateToPoint(wayPoint) )
					{
						if(!isInAcceptableRadius(wayPoint))
						{
							taskAddMovement(0.5f);
							debugState="moving towards random point";
						}
						else
							wayPoint=pickRandomPoint((int)child.getGameManager().getGamePanel().getWorldSize(),(int)child.getGameManager().getGamePanel().getWorldSize());
					}
				}
				else
				{
					taskFullStop();
				}
			}
		}
		else
		{
			if(container!=null&&container.getActive())
			{
				wayPoint=container.getCenterWorldLocation();
				if(stoped)
				{
					if(taskRotateToPoint(wayPoint)) 
					{
						if (isInAcceptableRadius(wayPoint))
						{
							if(child.getSpeed()<0.01)
								dumpInventory();
							else
								taskStop();
						}
						else
						{
							taskAddMovement(0.9f);
							debugState="moving towards container";
						}
					}
						
				}
				else
					taskFullStop();
			}
			else
			{
				taskStop();
				container=findContainer();
				debugState="searching for cargo container";
			}
		}
		// TODO: Implement this method
	}
	
	private void taskFullStop()
	{
		taskStop();
		if(child.getSpeed()<1f)
			stoped=true;
		else
			stoped=false;
			
		debugState="stoping";
		
	}
	private Entity findResources()
	{
		for(Entity e:child.getGameManager().getWorldManager().getEntityManager().getArray())
		{
			if(e instanceof Asteroid)
				if(isInSightRadius(e)&&e.getActive())
				{
					stoped=false;
					resourceSpot=e;
					return e;
				}
		}
		return null;
	}

	@Override
	protected PointF pickRandomPoint(int mx, int my)
	{
		// TODO: Implement this method
		stoped=false;
		return super.pickRandomPoint(mx, my);
	}
	
	
	private void taskGoForRes()
	{
		wayPoint=resourceSpot.getCenterWorldLocation();
		
		if(taskRotateToPoint(wayPoint)) 
			if (isInAcceptableRadius(wayPoint))
			{
				taskStop();
				child.shoot();
				debugState="mining";
			}
			else
			{
				taskAddMovement(0.9f);
				debugState="moving to asteroid";
			}
	}
	
	private void findAndCollectDebris()
	{
		for(Entity e:child.getGameManager().getWorldManager().getEntityManager().getArray())
		{
			if(e instanceof DroppedItems)
				if(isInSightRadius(e)&&e.getActive())
				{
					invEmpty=taskTakeResouces(e);
				}
		}
		
	}
	
	protected boolean taskTakeResouces(Entity e)
	{
		boolean collected=false;
		if (e instanceof DroppedItems)
		{
			collected = child.getInventory().collectFromInventory(e);
		}
		return collected;
	}
	
	private Entity findContainer()
	{
		for(Entity e:child.getGameManager().getWorldManager().getEntityManager().getArray())
		{
			if(e instanceof CargoContainer)
				if(e.getActive())
				{
					stoped=false;
					container=e;
					return e;
				}
		}
		return null;
	}
	
	private void dumpInventory(){
		if(isInSightRadius(container))
		invEmpty=container.getInventory().collectFromInventory(child);
	}
}
