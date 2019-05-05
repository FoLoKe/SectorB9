package com.sb9.foloke.sectorb9.game.Entities;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;

import com.sb9.foloke.sectorb9.game.AI.AI;
import com.sb9.foloke.sectorb9.game.Assets.EffectsAsset;
import com.sb9.foloke.sectorb9.game.Assets.ShipAsset;
import com.sb9.foloke.sectorb9.game.Entities.Ships.Ship;

import com.sb9.foloke.sectorb9.game.Funtions.CustomCollisionObject;
import com.sb9.foloke.sectorb9.game.Managers.GameManager;

import static java.lang.Math.PI;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import com.sb9.foloke.sectorb9.game.Entities.Ships.*;
import com.sb9.foloke.sectorb9.game.AI.*;
import com.sb9.foloke.sectorb9.game.DataSheets.*;

public class ControlledShip extends DynamicEntity {

    private AI AI;
    private Ship ship;
	private static final int ID=10;

	public ControlledShip(int x, int y, int rotation, GameManager gameManager,int AIType,Ship ship)
    {
        super(x,y,rotation,gameManager,ID);
        this.ship=ship;
		switch (AIType)
		{
			case 0:
        		AI=new CombatAI(this);
				break;
			case 1:
				AI=new MinerAI(this);
				break;
			default:
				AI=new CombatAI(this);
				break;
		}
		movable=true;

		TEAM=2;
    }
	
    @Override
    public void render(Canvas canvas) {
		if(!active||!renderable)
			return;
        ship.render(canvas);
        super.render(canvas);
		AI.render(canvas);

    }

    @Override
    public void tick() {
		if(!active)
			return;
        super.tick();
        AI.tick();

        ship.tick();
    }
	
	@Override
    public void calculateCollisionObject()
    {
        super.calculateCollisionObject();

        if(ship!=null)
			ship.calculateCollisionObject();
    }

    
   
    @Override
    public CustomCollisionObject getCollisionObject() {return ship.getCollisionObject();}

    public void shoot()
    {
        ship.shoot();
    }
	
	public void serTarget(Entity e)
	{
		ship.target=e;
	}
}
