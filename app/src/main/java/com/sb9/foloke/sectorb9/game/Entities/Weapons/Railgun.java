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
import com.sb9.foloke.sectorb9.game.UI.CustomViews.*;
import android.graphics.*;

public class Railgun extends Weapon
{
    private ParticleSystem laserDamageEffect;
    private boolean active=false;
    private PointF hitPoint;
    private Line2D line;
    private float lenght = 1000;
    private Random rnd=new Random();
    private float[] initLineEnd;
    private float[] lineEnd=new float[2];
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
        initLineEnd=new float[]{0,-lenght};
        damage=5;

        line=new Line2D(0,0,1,1);
        line.setThickness(3);
        line.setColor(Color.BLUE);
        laserDamageEffect=new ParticleSystem(EffectsAsset.yellow_pixel,0,0,1f,new PointF(1,1),true,120, gameManager);
        laserDamageEffect.setAccuracy(new Point(10,10));
    }

    @Override
    public void tick()
    {
		
		
		Matrix m=new Matrix();
		m.postRotate(turret.getRotation());
			
        m.mapVectors(lineEnd,initLineEnd);

		lineEnd[0]+=turret.getPointOfShooting()[0];
		lineEnd[1]+=turret.getPointOfShooting()[1];
        line.set(turret.getPointOfShooting()[0],
                turret.getPointOfShooting()[1],
                lineEnd[0],
                lineEnd[1]);
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
                        line.set(turret.getPointOfShooting()[0],
                                turret.getInitPointOfShooting()[1],hitPoint.x, hitPoint.y);
                    }
                    line.render(canvas);
                }

            }
        }
        catch (Exception e)
        {
            GameLog.update("Railgun: " +e.toString(),1);
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
        float x=turret.getPointOfShooting()[0]-p.x;
        float y=turret.getPointOfShooting()[1]-p.y;
        return (float)Math.sqrt(x*x+y*y);
    }
}
