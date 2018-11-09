package com.sb9.foloke.sectorb9.game.entities.Weapons;
import com.sb9.foloke.sectorb9.game.entities.Ships.*;
import com.sb9.foloke.sectorb9.game.display.*;
import android.graphics.*;

abstract public class Weapon
{
	protected TurretSystem turret;
	protected Game game;
	public Weapon(TurretSystem turret,Game game)
	{
		this.turret=turret;
		this.game=game;
	}
	abstract public void tick();
	abstract public void shoot();
	abstract public void render(Canvas canvas);
	
	
}
