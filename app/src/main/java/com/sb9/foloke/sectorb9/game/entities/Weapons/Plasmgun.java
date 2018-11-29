package com.sb9.foloke.sectorb9.game.entities.Weapons;
//import java.util.;
import com.sb9.foloke.sectorb9.game.funtions.*;
import com.sb9.foloke.sectorb9.game.entities.Ships.*;
import com.sb9.foloke.sectorb9.game.display.*;
import android.graphics.*;
import com.sb9.foloke.sectorb9.game.entities.*;

public class Plasmgun extends Weapon
{
	private Timer fireDelay=new Timer(0);
	private int fireRate= 60;
	public  ProjectilesPool projectiles;

	public Plasmgun(TurretSystem turret,Game game)
	{
		super(turret,game);
		this.projectiles=new ProjectilesPool(game.weaponAsset.plasm,5,60,game);
	}
	public void shoot()
	{
		if(fireDelay.getTick()<=1)
		{
			float mathRotation=(float)(Math.PI/180*turret.getParent().getHolder().getWorldRotation());
			PointF tpointOfShooting =new PointF((float)(turret.getPointOfShooting().x * Math.cos(mathRotation) - turret.getPointOfShooting().y * Math.sin(mathRotation))
												,(float)(turret.getPointOfShooting().x * Math.sin(mathRotation) + turret.getPointOfShooting().y * Math.cos(mathRotation)));
			tpointOfShooting.set(turret.getParent().getHolder().getCenterX()+tpointOfShooting.x,turret.getParent().getHolder().getCenterY()+tpointOfShooting.y);
			projectiles.shoot(tpointOfShooting,turret.getParent().getHolder().getWorldRotation());
			fireDelay.setTimer(60f/fireRate);
			//boolean shootFlag=false;
		}
	}
	public void tick()
	{
		fireDelay.tick();
		for(Projectile p: projectiles.getArray())
			p.tick();
	}
	public void render(Canvas canvas)
	{
		for(Projectile p: projectiles.getArray())
			p.render(canvas);
	}
}