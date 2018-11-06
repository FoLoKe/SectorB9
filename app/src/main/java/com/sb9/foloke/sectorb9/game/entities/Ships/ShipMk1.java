package com.sb9.foloke.sectorb9.game.entities.Ships;
import com.sb9.foloke.sectorb9.game.entities.*;
import com.sb9.foloke.sectorb9.game.Assets.*;
import android.graphics.*;

public class ShipMk1 extends Ship
{
	public ShipMk1(ImageAssets shipsAsset,Entity holder)
	{
		super(shipsAsset.player_mk1,shipsAsset.engine_mk1,null,holder);
		pointOfengine.set(0,0);
		pointOfShooting.set(0,0);
		collisionInitPoints=new PointF[3];
		collisionInitPoints[0]=new PointF(0,shipImage.getHeight()/2);
		collisionInitPoints[1]=new PointF(-shipImage.getWidth()/2,shipImage.getHeight()/2);
		collisionInitPoints[2]=new PointF(shipImage.getWidth()/2,shipImage.getHeight()/2);
		setPoints(collisionInitPoints);
	}
}
