package com.sb9.foloke.sectorb9.game.Entities.Weapons;
//import java.util.;
import com.sb9.foloke.sectorb9.game.Assets.WeaponsAsset;
import com.sb9.foloke.sectorb9.game.Managers.GameManager;
import com.sb9.foloke.sectorb9.game.Funtions.*;
import com.sb9.foloke.sectorb9.game.Entities.Ships.*;

import android.graphics.*;
import com.sb9.foloke.sectorb9.game.Entities.*;

public class Plasmgun extends Weapon
{
	private Timer fireDelay=new Timer(0);
	private int fireRate= 60;
	public  ProjectilesPool projectiles;
	
	public Plasmgun(TurretSystem turret, GameManager gameManager)
	{
		super(turret, gameManager);
		name="plasmgun";
		damage=20;
		this.projectiles=new ProjectilesPool(WeaponsAsset.plasm,5,60,damage,turret.getParent().getHolder(), gameManager);
	}
	public void shoot()
	{
		if(enabled)
		if(fireDelay.getTick()<=1)
		{
			
			projectiles.shoot(new PointF(turret.getPointOfShooting()[0],turret.getPointOfShooting()[1]),turret.getRotation());
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
