package com.sb9.foloke.sectorb9.game.AI;

import android.graphics.PointF;

import com.sb9.foloke.sectorb9.game.Entities.DynamicEntity;
import com.sb9.foloke.sectorb9.game.Entities.EnemyShip;
import com.sb9.foloke.sectorb9.game.Entities.Entity;

public class EnemyAI {

    private final float distanceToTarget=200;
    private EnemyShip child;

    public EnemyAI(EnemyShip child)
    {
        this.child=child;
    }
    public void tick()
    {
        float distance=distanceTo(child.getGameManager().getPlayer().getCenterWorldLocation());
        if (distance>distanceToTarget)
        child.addMovement();
        child.getGameManager().getGamePanel().debugText.setString(distanceTo(child.getGameManager().getPlayer().getCenterWorldLocation())+" ");
        if(child.rotationToPoint(child.getGameManager().getPlayer().getCenterWorldLocation(),distance/distanceToTarget)) {

            if (distance < 200)
                child.shoot();


        }
    }

    private float distanceTo(PointF point)
    {
        point.set(child.getCenterX()-point.x,child.getCenterY()-point.y);
        return (float)Math.sqrt((point.x)*(point.x)+(point.y)*(point.y));
    }
}
