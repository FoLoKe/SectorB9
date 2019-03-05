package com.sb9.foloke.sectorb9.game.Entities.Weapons;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.PointF;

import com.sb9.foloke.sectorb9.game.Assets.EffectsAsset;
import com.sb9.foloke.sectorb9.game.Assets.UIAsset;
import com.sb9.foloke.sectorb9.game.Entities.Entity;
import com.sb9.foloke.sectorb9.game.Entities.Ships.TurretSystem;
import com.sb9.foloke.sectorb9.game.Funtions.Line2D;
import com.sb9.foloke.sectorb9.game.Managers.GameManager;
import com.sb9.foloke.sectorb9.game.ParticleSystem.ParticleSystem;
import com.sb9.foloke.sectorb9.game.UI.ProgressBarUI;

import java.util.Random;

public class Railgun extends Weapon
{
    private ParticleSystem laserDamageEffect;
    private boolean active=false;
    private PointF hitPoint;
    private Line2D line;
    private float lenght = 1000;
    private Random rnd=new Random();
    private float[] initShootVector;
    private float[] tempShootVector=new float[4];
    private Entity hittedEntity;
    private float maxLoad=10;
    private float deload=0.2f;
    private boolean ready=false;
    private float load=0;
    private float loadGain=0.1f;
    private ProgressBarUI heatBar;

    public Railgun(TurretSystem turret, GameManager gameManager)
    {
        super(turret, gameManager);
		name="railgun";
        this.heatBar=new ProgressBarUI(this,20,2,-10,-2, UIAsset.hpBackground,UIAsset.hpLine,UIAsset.progressBarBorder,load/maxLoad*100);
        initShootVector=new float[]{turret.getPointOfShooting().x,
                turret.getPointOfShooting().y,
                turret.getPointOfShooting().x,
                turret.getPointOfShooting().y-lenght};
        damage=5;

        line=new Line2D(0,0,1,1);
        line.setThickness(3);
        line.setColor(Color.BLUE);
        laserDamageEffect=new ParticleSystem(EffectsAsset.yellow_pixel,turret.getParent().getHolder().getWorldLocation().x,turret.getParent().getHolder().getWorldLocation().y,1f,new PointF(1,1),true,120, gameManager);
        laserDamageEffect.setAccuracy(new Point(10,10));
    }

    @Override
    public void tick()
    {
        turret.getParent().getHolder().getTransformMatrix().mapVectors(tempShootVector,initShootVector);

        line.set(tempShootVector[0]+turret.getParent().getHolder().getCenterX(),
                tempShootVector[1]+turret.getParent().getHolder().getCenterY(),
                tempShootVector[2]+turret.getParent().getHolder().getCenterX(),
                tempShootVector[3]+turret.getParent().getHolder().getCenterY());
        laserDamageEffect.tick();
        heatBar.setWorldLocation(line.getFirstPoint().x,line.getFirstPoint().y);
       loadRail();
    }

    private void loadRail() {
        heatBar.set(load / maxLoad * 100);

        if (active)
        {
            if(!ready) {
                if (load >= maxLoad)
                    ready = true;
                else
                    load += loadGain;
            }
            else
            {

            }
        }
        else
        {
            ready=false;
            if(load>0)
            load-=deload;
        }

    }

    @Override
    public void render(Canvas canvas)
    {

        try {
            heatBar.render(canvas);
            if (active) {
                if (ready) {
                    hittedEntity = null;
                    float distance = lenght * 2;
                    float tDistance;


                    for (Entity e : gameManager.getEntities())
                        if (e.getCollidable())
                            if (e != turret.getParent().getHolder())
                                if (e.getActive())
                                    if (e.getTeam() != turret.getParent().getHolder().getTeam())
                                        if (line.intersect(e.getCollisionObject())) {
                                            tDistance = distanceTo(line.getPoint());

                                            if (tDistance < distance) {
                                                distance = tDistance;
                                                hittedEntity = e;
                                                hitPoint = line.getPoint();
                                            }
                                        }
                    if (hittedEntity != null) {

                        hittedEntity.applyDamage(damage);
                        laserDamageEffect.draw(hitPoint.x, hitPoint.y, rnd.nextInt(360), new PointF(0, 0));
                        line.set(tempShootVector[0] + turret.getParent().getHolder().getCenterX(),
                                tempShootVector[1] + turret.getParent().getHolder().getCenterY(), hitPoint.x, hitPoint.y);
                    }
                    line.render(canvas);
                }

            }
        }
        catch (Exception e)
        {
            gameManager.getMainActivity().makeToast(e+"");
        }

        laserDamageEffect.render(canvas);
        active=false;
    }

    @Override
    public void shoot()
    {
		if(enabled)
        active=true;
    }

    private float distanceTo(PointF p)
    {
        float x=tempShootVector[0] + turret.getParent().getHolder().getCenterX()-p.x;
        float y=tempShootVector[1] + turret.getParent().getHolder().getCenterY()-p.y;
        return (float)Math.sqrt(x*x+y*y);
    }
}
