package com.sb9.foloke.sectorb9.game.entities.Ships;
import com.sb9.foloke.sectorb9.game.entities.*;
import com.sb9.foloke.sectorb9.game.Assets.*;
import android.graphics.*;

public class ShipMk2 extends Ship
{
	public ShipMk2(DynamicEntity holder)
	{
		super(ShipAsset.player_mk2,ShipAsset.engine_mk2,ShipAsset.player_mk2_engine_shield,holder);
		pointOfengine.set(0,12);
		pointOfShooting.set(shipImage.getWidth()/2,0);
		
		collisionInitPoints=new PointF[4];
		collisionInitPoints[0]=new PointF(shipImage.getWidth()/2,-shipImage.getHeight()/2);
		collisionInitPoints[1]=new PointF(-shipImage.getWidth()/2,-shipImage.getHeight()/2);
		collisionInitPoints[2]=new PointF(-shipImage.getWidth()/2,shipImage.getHeight()/2);
		collisionInitPoints[3]=new PointF(shipImage.getWidth()/2,shipImage.getHeight()/2);
		setPoints(collisionInitPoints);
		turrets=new TurretSystem[2];
		turrets[0]=new TurretSystem(pointOfShooting,2,holder.getGame(),this);
		turrets[1]=new TurretSystem(new PointF(-shipImage.getWidth()/2,0),3,holder.getGame(),this);
	}
}
