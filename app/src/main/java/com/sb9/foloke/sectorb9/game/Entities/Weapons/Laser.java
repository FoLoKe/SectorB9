package com.sb9.foloke.sectorb9.game.Entities.Weapons;
import android.graphics.*;

import com.sb9.foloke.sectorb9.game.Assets.EffectsAsset;
import com.sb9.foloke.sectorb9.game.Managers.GameManager;
import com.sb9.foloke.sectorb9.game.Entities.Ships.*;
import com.sb9.foloke.sectorb9.game.Funtions.*;
import com.sb9.foloke.sectorb9.game.Entities.*;
import com.sb9.foloke.sectorb9.game.ParticleSystem.*;
import java.util.Random;
import com.sb9.foloke.sectorb9.game.UI.*;
import com.sb9.foloke.sectorb9.game.Assets.*;


public class Laser extends Weapon
{
	private ParticleSystem laserDamageEffect;
	private boolean active=false;

	private PointF hitPoint;
	private Line2D line;
	private float lenght=200;
	private Random rnd=new Random();
	private float[] initShootVector;
	private float[] tempShootVector=new float[4];
	private Entity hitedEntity;
	private float maxHeat=10;
	private float cooling=0.2f;
	private boolean overheated=false;
	private float heat=0;
	private float heatGain=0.1f;
	
	private	ProgressBarUI heatBar;
	
	public Laser(TurretSystem turret, GameManager gameManager)
	{
		super(turret, gameManager);
		name="laser";
		this.heatBar=new ProgressBarUI(this,20,2,-10,-2,UIAsset.hpBackground,UIAsset.hpLine,UIAsset.progressBarBorder,heat/maxHeat*100);
		initShootVector=new float[]{turret.getPointOfShooting().x,
                turret.getPointOfShooting().y,
                turret.getPointOfShooting().x,
                turret.getPointOfShooting().y-lenght};
		damage=0.1f;
		lenght=100;
		line=new Line2D(0,0,1,1);
		line.setThickness(3);
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
		calculateHeat();
	}

	private void calculateHeat()
	{
		heatBar.set(heat/maxHeat*100);
		if(active)
		{
		
		if(heat>=maxHeat)
			overheated=true;
		if(overheated)
		{
			heat-=cooling;
			if(heat<=0)
			{
				heat=0;
				overheated=false;
			}
		}
		}
		else
		{
			heat-=cooling;
			if(heat<=0)
			{
				heat=0;
				overheated=false;
			}
		}
	}
	
	@Override
	public void render(Canvas canvas)
	{
		
		heatBar.render(canvas);
		if(active)
		{
		if(!overheated)
		{
			heat+=heatGain;
            hitedEntity=null;
            float distance=lenght*2;
            float tDistance;

		    
			for (Entity e: gameManager.getEntities())
			if(e.getCollidable())
				if(e!=turret.getParent().getHolder())
				    if(e.getActive())
						if(e.getTeam()!=turret.getParent().getHolder().getTeam())
						if(line.intersect(e.getCollisionObject()))
						{
                        	tDistance = distanceTo(line.getPoint());
                            if (tDistance < distance) 
							{
                                distance = tDistance;
                                hitedEntity = e;
                                hitPoint = line.getPoint();
                            } 
						}
            if (hitedEntity!=null) 
			{
                hitedEntity.applyDamage(damage);
                laserDamageEffect.draw(hitPoint.x, hitPoint.y, rnd.nextInt(360), new PointF(0, 0));
                line.set(tempShootVector[0] + turret.getParent().getHolder().getCenterX(),
                        tempShootVector[1] + turret.getParent().getHolder().getCenterY(),hitPoint.x, hitPoint.y);
            }
			line.render(canvas);
		}
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
