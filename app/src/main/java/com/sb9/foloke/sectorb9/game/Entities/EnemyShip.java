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

public class EnemyShip extends DynamicEntity {

    private EnemyAI AI;
    private Ship ship;

    public EnemyShip(int x, int y, int rotation, GameManager gameManager)
    {
        super(x,y,rotation,gameManager,-1);
        this.ship=new ShipMk1(this);
        AI=new EnemyAI(this);
	
		TEAM=2;
    }

    @Override
    public void render(Canvas canvas) {
		super.render(canvas);
        ship.render(canvas);

    }

    @Override
    public void tick() {
        super.tick();
        AI.tick();

        ship.tick();
    }
	
	@Override
    public void calculateCollisionObject()
    {
        super.calculateCollisionObject();

        if(ship!=null)
			ship.calculateCollisionObject(transformMatrix);
    }

    
   
    @Override
    public CustomCollisionObject getCollisionObject() {return ship.getCollisonObject();}

    public void shoot()
    {
        ship.shoot();
    }
}
