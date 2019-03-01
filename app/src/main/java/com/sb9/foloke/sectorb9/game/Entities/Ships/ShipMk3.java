package com.sb9.foloke.sectorb9.game.Entities.Ships;
import com.sb9.foloke.sectorb9.game.Entities.*;
import com.sb9.foloke.sectorb9.game.Assets.*;
import android.graphics.*;

public class ShipMk3 extends Ship
{
	public ShipMk3(DynamicEntity holder)
	{
		super(ShipAsset.ship_mk3,ShipAsset.engine_mk3,ShipAsset.player_mk2_engine_shield,holder);
        speed=3;
        rotSpeed=1;
        maxHP=300;
        maxSH=150;
        shieldSize=2;
        accel=0.001f;
        decel=0.001f;
		init(holder);
	}

	public void init(DynamicEntity holder)
	{

		pointOfengine.set(0,0);
		setCollisionObject();
		pointOfShooting=new PointF[4];
		pointOfShooting[0]=new PointF(-16,1);
        pointOfShooting[1]=new PointF(16,1);
        pointOfShooting[2]=new PointF(8,8);
        pointOfShooting[3]=new PointF(-8,9);
		turrets=new TurretSystem[4];
		turrets[0]=new TurretSystem(pointOfShooting[0],1,holder.getGameManager(),this);
		turrets[1]=new TurretSystem(pointOfShooting[1],1,holder.getGameManager(),this);
		turrets[2]=new TurretSystem(pointOfShooting[2],4,holder.getGameManager(),this);
		turrets[3]=new TurretSystem(pointOfShooting[3],4,holder.getGameManager(),this);

        setOptionToDynamic();
	}
}

