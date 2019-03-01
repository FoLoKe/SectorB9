package com.sb9.foloke.sectorb9.game.Entities.Ships;
import com.sb9.foloke.sectorb9.game.Entities.*;
import com.sb9.foloke.sectorb9.game.Assets.*;
import android.graphics.*;

public class ShipMk3 extends Ship
{
	public ShipMk3(DynamicEntity holder)
	{
		super(ShipAsset.ship_mk3,ShipAsset.engine_mk3,ShipAsset.player_mk2_engine_shield,holder);
		init(holder);
	}
	public void init(DynamicEntity holder)
	{

		pointOfengine.set(0,0);
		pointOfShooting.set(shipImage.getWidth()/2,0);

		setCollisionObject();
		turrets=new TurretSystem[4];
		turrets[0]=new TurretSystem(new PointF(-16,1),2,holder.getGameManager(),this);
		turrets[1]=new TurretSystem(new PointF(16,1),2,holder.getGameManager(),this);
		turrets[2]=new TurretSystem(new PointF(8,8),1,holder.getGameManager(),this);
		turrets[3]=new TurretSystem(new PointF(-8,8),1,holder.getGameManager(),this);
		holder.setSpeed(4);
		holder.setRotationSpeed(1);

	}
}

