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
	private Bitmap image;
	private PointF finalPoint;
	private Line2D line;
	private float lenght;
	private Random rnd=new Random();
	public Laser(TurretSystem turret, GameManager gameManager)
	{
		super(turret, gameManager);
		image=Bitmap.createScaledBitmap(EffectsAsset.yellow_pixel, 2, (int)(100), false);
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
		// TODO: Implement this method
	}

	@Override
	public void render(Canvas canvas)
	{
		if(active)
		{
			float mathRotation=(float)(Math.PI/180*(turret.getParent().getHolder().getWorldRotation()));
			PointF tpointOfShooting =new PointF((float)(turret.getPointOfShooting().x * Math.cos(mathRotation) - turret.getPointOfShooting().y * Math.sin(mathRotation))
											,(float)(turret.getPointOfShooting().x * Math.sin(mathRotation) + turret.getPointOfShooting().y * Math.cos(mathRotation)));
			tpointOfShooting.set(turret.getParent().getHolder().getCenterX()+tpointOfShooting.x,turret.getParent().getHolder().getCenterY()+tpointOfShooting.y);
		
			PointF tfpointOfShooting =new PointF((float)(turret.getPointOfShooting().x * Math.cos(mathRotation) - (turret.getPointOfShooting().y-lenght) * Math.sin(mathRotation))
												,(float)(turret.getPointOfShooting().x * Math.sin(mathRotation) + (turret.getPointOfShooting().y-lenght) * Math.cos(mathRotation)));
			tfpointOfShooting.set(turret.getParent().getHolder().getCenterX()+tfpointOfShooting.x,turret.getParent().getHolder().getCenterY()+tfpointOfShooting.y);
			Paint tpaint=new Paint();
			tpaint.setColor(Color.RED);
			line.set(tpointOfShooting.x,tpointOfShooting.y,tfpointOfShooting.x,tfpointOfShooting.y);
			for (Entity e: gameManager.getEntities())
				if(e!=turret.getParent().getHolder())
				if(e.getActive())
					//TODO: BUG
					if((e.getCollisionObject().intersect(line)))
						{
							//TODO: CHECK IF THERE TWO COLLISIONS
							e.applyDamage(1);
							laserDamageEffect.draw(line.getPoint().x,line.getPoint().y,rnd.nextInt(360),new PointF(0,0));
							line.set(tpointOfShooting.x,tpointOfShooting.y,line.getPoint().x,line.getPoint().y);
							break;
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
}
