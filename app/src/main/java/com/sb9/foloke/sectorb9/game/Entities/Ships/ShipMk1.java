package com.sb9.foloke.sectorb9.game.Entities.Ships;
import com.sb9.foloke.sectorb9.game.Entities.*;
import com.sb9.foloke.sectorb9.game.Assets.*;
import android.graphics.*;

public class ShipMk1 extends Ship
{
	public ShipMk1(DynamicEntity holder)
	{
		super(ShipAsset.player_mk1,ShipAsset.engine_mk1,null,holder);
		
		init(holder);
	}
	
	public void init(DynamicEntity holder)
	{
		
		pointOfengine.set(0,0);
		pointOfShooting.set(0,0);
		setCollisionObject();
		
		turrets=new TurretSystem[1];
		holder.setSpeed(3);
		holder.setRotationSpeed(5);
		turrets[0]=new TurretSystem(pointOfShooting,1,holder.getGameManager(),this);
	}
}
