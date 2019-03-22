package com.sb9.foloke.sectorb9.game.Entities;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;

import com.sb9.foloke.sectorb9.game.AI.EnemyAI;
import com.sb9.foloke.sectorb9.game.Assets.EffectsAsset;
import com.sb9.foloke.sectorb9.game.Assets.ShipAsset;
import com.sb9.foloke.sectorb9.game.Entities.Ships.Ship;
import com.sb9.foloke.sectorb9.game.Entities.Ships.ShipMk1;
import com.sb9.foloke.sectorb9.game.Funtions.CustomCollisionObject;
import com.sb9.foloke.sectorb9.game.Managers.GameManager;

import static java.lang.Math.PI;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import com.sb9.foloke.sectorb9.game.Entities.Ships.*;

public class EnemyShip extends DynamicEntity {

    private EnemyAI AI;
    private Ship ship;
	private static final int ID=10;
    public EnemyShip(int x, int y, int rotation, GameManager gameManager)
    {
        super(x,y,rotation,gameManager,ID);
        this.ship=new ShipMk2(this);
        AI=new EnemyAI(this);
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
    public CustomCollisionObject getCollisionObject() {return ship.getCollisonObject();}

    public void shoot()
    {
        ship.shoot();
    }
}
