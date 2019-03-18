package com.sb9.foloke.sectorb9.game.Entities.Ships;
import com.sb9.foloke.sectorb9.game.Entities.*;
import com.sb9.foloke.sectorb9.game.Assets.*;
import android.graphics.*;

public class ShipMk2 extends Ship
{
	public ShipMk2(DynamicEntity holder)
	{
		super(ShipAsset.player_mk2,ShipAsset.engine_mk2,ShipAsset.player_mk2_engine_shield,holder);
        
        sidewayImpulse=30;
        maxHP=100;
        maxSH=50;
        shieldSize=2;
        frontImpulse=0.05f;
        bacwardImpulse=0.05f;
		mass=25;
		init(holder);
	}

	public void init(DynamicEntity holder)
	{

		pointOfengine.set(0,12);
		pointOfShooting=new PointF[2];
        pointOfShooting[0]=new PointF(shipImage.getWidth()/2,0);
        pointOfShooting[1]=new PointF(-shipImage.getWidth()/2,0);
		setCollisionObject();
		turrets=new TurretSystem[2];
		turrets[0]=new TurretSystem(pointOfShooting[0],2,holder.getGameManager(),this);
		turrets[1]=new TurretSystem(pointOfShooting[1],3,holder.getGameManager(),this);

		
		setOptionToDynamic();
		super.init(holder);
	}
}
