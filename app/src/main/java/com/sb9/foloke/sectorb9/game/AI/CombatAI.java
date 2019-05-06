package com.sb9.foloke.sectorb9.game.AI;
import com.sb9.foloke.sectorb9.game.Entities.*;
//import org.w3c.dom.*;

public class CombatAI extends AI
{
	public CombatAI(ControlledShip e)
	{
		super(e);
	}

	@Override
	public void tick()
	{
		// TODO: Implement this method
		if(child.getTeam()!=child.getGameManager().getPlayer().getTeam())
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

	
}
