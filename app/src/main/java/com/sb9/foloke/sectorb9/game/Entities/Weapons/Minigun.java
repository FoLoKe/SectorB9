package com.sb9.foloke.sectorb9.game.Entities.Weapons;
import android.graphics.*;

import com.sb9.foloke.sectorb9.game.Assets.WeaponsAsset;
import com.sb9.foloke.sectorb9.game.Managers.GameManager;
import com.sb9.foloke.sectorb9.game.Funtions.*;
import com.sb9.foloke.sectorb9.game.Entities.Ships.*;
import com.sb9.foloke.sectorb9.game.Entities.*;


public class Minigun extends Weapon
{
	private Timer fireDelay=new Timer(0);
	private int fireRate= 200;
	public  ProjectilesPool projectiles;
	
	private float projectileSpeed=3;
	
	public Minigun(TurretSystem turret, GameManager gameManager)
	{
		super(turret, gameManager);
		name="minigun";
		damage=2;
		this.projectiles=new ProjectilesPool(WeaponsAsset.shell,projectileSpeed,300,damage,turret.getParent().getHolder(), gameManager);
	}
	public void shoot()
	{
		if(enabled)		
		if(fireDelay.getTick()<=1)
				if(turret.getParent().getHolder().getInventory().takeOneItemFromAllInventory(18,1))
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
