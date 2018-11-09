package com.sb9.foloke.sectorb9.game.entities.Ships;
import com.sb9.foloke.sectorb9.game.entities.*;
import com.sb9.foloke.sectorb9.game.Assets.*;
import android.graphics.*;

public class ShipMk2 extends Ship
{
	public ShipMk2(ImageAssets asset,DynamicEntity holder)
	{
		super(asset.player_mk2,asset.engine_mk2,asset.player_mk2_engine_shield,holder);
		pointOfengine.set(0,12);
		pointOfShooting.set(shipImage.getWidth()/2,0);
		
		collisionInitPoints=new PointF[4];
		collisionInitPoints[0]=new PointF(shipImage.getWidth()/2,-shipImage.getHeight()/2);
		collisionInitPoints[1]=new PointF(-shipImage.getWidth()/2,-shipImage.getHeight()/2);
		collisionInitPoints[2]=new PointF(-shipImage.getWidth()/2,shipImage.getHeight()/2);
		collisionInitPoints[3]=new PointF(shipImage.getWidth()/2,shipImage.getHeight()/2);
		setPoints(collisionInitPoints);
		turrets=new TurretSystem[2];
		turrets[0]=new TurretSystem(pointOfShooting,holder.getGame(),this);
		turrets[1]=new TurretSystem(new PointF(-shipImage.getWidth()/2,0),holder.getGame(),this);
	}
}
