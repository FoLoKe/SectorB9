package com.sb9.foloke.sectorb9.game.entities.Weapons;
import android.graphics.*;
import com.sb9.foloke.sectorb9.game.entities.Ships.*;
import com.sb9.foloke.sectorb9.game.display.*;
import com.sb9.foloke.sectorb9.game.funtions.*;
import com.sb9.foloke.sectorb9.game.entities.*;
import com.sb9.foloke.sectorb9.game.ParticleSystem.*;
import java.util.*;

public class Laser extends Weapon
{
	public ParticleSystem laserDamageEffect;
	private boolean active=false;
	private Bitmap image;
	private PointF finalPoint;
	private Line2D line;
	private float lenght;
	Random rnd=new Random();
	public Laser(TurretSystem turret,Game game)
	{
		super(turret,game);
		image=Bitmap.createScaledBitmap(game.shipAsset.yellow_pixel, 2, (int)(100), false);
		lenght=100;
		line=new Line2D(0,0,1,1);
		line.setThickness(3);
		laserDamageEffect=new ParticleSystem(game.shipAsset.yellow_pixel,turret.getParent().getHolder().getWorldLocation().x,turret.getParent().getHolder().getWorldLocation().y,1f,new PointF(1,1),game);
		laserDamageEffect.setAccuracy(new Point(10,10));
	}

	@Override
	public void tick()
	{
		active=false;
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
		//projectiles.shoot(tpointOfShooting,turret.getParent().getHolder().getWorldRotation());
		// TODO: Implement this method
		Paint tpaint=new Paint();
		tpaint.setColor(Color.RED);
		line.set(tpointOfShooting.x,tpointOfShooting.y,tfpointOfShooting.x,tfpointOfShooting.y);
		for (Entity e:game.getEntities())
		if(e.getActive())
		if(line.intersect(e.getCollisionBox()))
		{
			//TODO: CHECK IF THER TWO COLLISIONS
			e.applyDamage(1);
			laserDamageEffect.draw(line.getPoint().x,line.getPoint().y,rnd.nextInt(360),new PointF(0,0));
			line.set(tpointOfShooting.x,tpointOfShooting.y,line.getPoint().x,line.getPoint().y);
			break;
		}
		line.render(canvas);
		
		//canvas.drawLine(,,tpointOfShooting.x,tpointOfShooting.y,tpaint);
		//canvas.save();
		//canvas.rotate(turret.getParent().getHolder().getWorldRotation()+180,tpointOfShooting.x,tpointOfShooting.y);
		//canvas.drawBitmap(image,tpointOfShooting.x-1,tpointOfShooting.y,null);
		//canvas.restore();
		}
		laserDamageEffect.render(canvas);
	}

	@Override
	public void shoot()
	{
		active=true;
		// TODO: Implement this method
	}



	
}
