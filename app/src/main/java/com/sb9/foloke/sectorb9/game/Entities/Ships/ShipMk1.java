package com.sb9.foloke.sectorb9.game.Entities.Ships;
import com.sb9.foloke.sectorb9.game.Entities.*;
import com.sb9.foloke.sectorb9.game.Assets.*;
import android.graphics.*;

public class ShipMk1 extends Ship
{

	public ShipMk1(DynamicEntity holder)
	{
		super(ShipAsset.player_mk1,ShipAsset.engine_mk1,null,holder);
        
        sidewayImpulse=10;
        maxHP=50;
        maxSH=25;
        shieldSize=1;
		mass=4;
        frontImpulse=mass*0.055f;
        backwardImpulse=mass*0.055f;
		
		init(holder);

	}
	
	public void init(DynamicEntity holder)
	{
		
		pointOfengine.set(0,0);
		pointOfShooting=new PointF[1];
		pointOfShooting[0]=new PointF(0,-4);
		setCollisionObject();
		
		turrets=new TurretSystem[1];
        turrets[0]=new TurretSystem(pointOfShooting[0],3,holder.getGameManager(),this);
        setOptionToDynamic();
		super.init(holder);
	}
}
