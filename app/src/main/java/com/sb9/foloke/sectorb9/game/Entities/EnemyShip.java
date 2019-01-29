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
        ship.calculateCollisionObject(transformMatrix);

    }

    private void impulse(Entity e)
    {
        float trotation=360-(float)Math.toDegrees(Math.PI+Math.atan2(-e.getCenterX()+x,-e.getCenterY()+y));
        float mathRotation=(float)(PI/180*trotation);
        this.dy = -(float) (0.2*speed * cos(mathRotation));
        this.dx = (float) (0.2*speed * sin(mathRotation));
    }

    @Override
    public void onCollide(Entity e) {
        if (e instanceof DynamicEntity)
            ((DynamicEntity)e).impulse(new PointF(0,0),dx,dy);
        impulse(e);
    }

    @Override
    public CustomCollisionObject getCollisionObject() {

        ///return from ship
        return ship.getCollisonObject();
    }

    public void shoot()
    {
        ship.shoot();
    }
}
