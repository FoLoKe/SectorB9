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

public class EnemyShip extends DynamicEntity {

    private EnemyAI AI;
    private Ship ship;

    public EnemyShip(int x, int y, int rotation, String name, GameManager gameManager)
    {
        super(x,y,rotation, ShipAsset.cursor,name,gameManager,-1);
        this.ship=new ShipMk1(this);
        AI=new EnemyAI(this);
    }

    @Override
    public void render(Canvas canvas) {
        ship.render(canvas);

    }

    @Override
    public void tick() {
        super.tick();
        AI.tick();

        ship.tick();
        ship.calculateCollisionObject();

    }

    @Override
    public CustomCollisionObject getCustomCollisionObject() {

        ///return from ship
        return super.getCustomCollisionObject();
    }

    public void shoot()
    {
        ship.shoot();
    }
}
