package com.sb9.foloke.sectorb9.game.Entities.Ships;
import com.sb9.foloke.sectorb9.game.Entities.*;
import com.sb9.foloke.sectorb9.game.Assets.*;
import android.graphics.*;

public class ShipMk2 extends Ship
{
	public ShipMk2(DynamicEntity holder)
	{
		super(ShipAsset.player_mk2,ShipAsset.engine_mk2,ShipAsset.player_mk2_engine_shield,holder);
		pointOfengine.set(0,12);
		pointOfShooting.set(shipImage.getWidth()/2,0);
		
		setCollisionObject();
		turrets=new TurretSystem[2];
		turrets[0]=new TurretSystem(pointOfShooting,2,holder.getGameManager(),this);
		turrets[1]=new TurretSystem(new PointF(-shipImage.getWidth()/2,0),3,holder.getGameManager(),this);
	}
	public void init(DynamicEntity holder)
	{

		pointOfengine.set(0,12);
		pointOfShooting.set(shipImage.getWidth()/2,0);

		setCollisionObject();
		turrets=new TurretSystem[2];
		turrets[0]=new TurretSystem(pointOfShooting,2,holder.getGameManager(),this);
		turrets[1]=new TurretSystem(new PointF(-shipImage.getWidth()/2,0),3,holder.getGameManager(),this);

		
		holder.setSpeed(5);
		holder.setRotationSpeed(1);
		
	}
}
