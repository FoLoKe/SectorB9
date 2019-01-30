package com.sb9.foloke.sectorb9.game.Entities.Weapons;
import android.graphics.*;

import com.sb9.foloke.sectorb9.game.Assets.EffectsAsset;
import com.sb9.foloke.sectorb9.game.Managers.GameManager;
import com.sb9.foloke.sectorb9.game.Entities.Ships.*;
import com.sb9.foloke.sectorb9.game.Funtions.*;
import com.sb9.foloke.sectorb9.game.Entities.*;
import com.sb9.foloke.sectorb9.game.ParticleSystem.*;
import java.util.*;

public class Laser extends Weapon
{
	private ParticleSystem laserDamageEffect;
	private boolean active=false;
	//private Bitmap image;
	private PointF hitPoint;
	private Line2D line;
	private float lenght=200;
	private Random rnd=new Random();
	private float[] initShootVector;
	private float[] tempShootVector=new float[4];
	private Entity hitedEntity;
	public Laser(TurretSystem turret, GameManager gameManager)
	{
		super(turret, gameManager);
		initShootVector=new float[]{turret.getPointOfShooting().x,
                turret.getPointOfShooting().y,
                turret.getPointOfShooting().x,
                turret.getPointOfShooting().y-lenght};
		//image=Bitmap.createScaledBitmap(EffectsAsset.yellow_pixel, 2, (int)(100), false);
		lenght=100;
		line=new Line2D(0,0,1,1);
		line.setThickness(3);
		laserDamageEffect=new ParticleSystem(EffectsAsset.yellow_pixel,turret.getParent().getHolder().getWorldLocation().x,turret.getParent().getHolder().getWorldLocation().y,1f,new PointF(1,1),true,120, gameManager);
		laserDamageEffect.setAccuracy(new Point(10,10));
	}

	@Override
	public void tick()
	{
		laserDamageEffect.tick();
	}

	@Override
	public void render(Canvas canvas)
	{
		if(active)
		{
            hitedEntity=null;
            float distance=lenght;
            float tDistance;

		    turret.getParent().getHolder().getTransformMatrix().mapVectors(tempShootVector,initShootVector);

			line.set(tempShootVector[0]+turret.getParent().getHolder().getCenterX(),
                    tempShootVector[1]+turret.getParent().getHolder().getCenterY(),
                    tempShootVector[2]+turret.getParent().getHolder().getCenterX(),
                    tempShootVector[3]+turret.getParent().getHolder().getCenterY());
			for (Entity e: gameManager.getEntities())
				if(e!=turret.getParent().getHolder())
				    if(e.getActive())
					    for(Line2D CE : e.getCollisionObject().getCollisionlines())
						{
						    if(CE.lineLine(line)) {
                                tDistance = distanceTo(line.getPoint());
                                turret.getParent().getHolder().getGameManager().getGamePanel().debugText.setString("" + distance);
                                if (tDistance < distance) {
                                    distance = tDistance;
                                    hitedEntity = e;
                                    hitPoint = line.getPoint();
                                }
                            }
						}
            if (hitedEntity!=null) {
                hitedEntity.applyDamage(1);
                laserDamageEffect.draw(hitPoint.x, hitPoint.y, rnd.nextInt(360), new PointF(0, 0));
                line.set(tempShootVector[0] + turret.getParent().getHolder().getCenterX(),
                        tempShootVector[1] + turret.getParent().getHolder().getCenterY(),hitPoint.x, hitPoint.y);
            }
			line.render(canvas);
		}
		laserDamageEffect.render(canvas);
		active=false;
	}

	@Override
	public void shoot()
	{
		active=true;
	}

	private float distanceTo(PointF p)
    {
        float x=tempShootVector[0] + turret.getParent().getHolder().getCenterX()-p.x;
        float y=tempShootVector[1] + turret.getParent().getHolder().getCenterY()-p.y;
        return (float)Math.sqrt(x*x+y*y);
    }
}
