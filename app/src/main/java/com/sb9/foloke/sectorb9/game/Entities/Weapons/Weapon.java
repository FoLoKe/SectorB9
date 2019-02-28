package com.sb9.foloke.sectorb9.game.Entities.Weapons;
import com.sb9.foloke.sectorb9.game.Managers.GameManager;
import com.sb9.foloke.sectorb9.game.Entities.Ships.*;

import android.graphics.*;

abstract public class Weapon
{
	protected TurretSystem turret;
	protected GameManager gameManager;
	protected float damage=0;
	public Weapon(TurretSystem turret, GameManager gameManager)
	{
		this.turret=turret;
		this.gameManager = gameManager;
	}
	abstract public void tick();
	abstract public void shoot();
	abstract public void render(Canvas canvas);
	
	
}
