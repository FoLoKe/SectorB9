package com.sb9.foloke.sectorb9.game.entities.Ships;
import android.graphics.*;
import com.sb9.foloke.sectorb9.game.funtions.*;
import com.sb9.foloke.sectorb9.game.display.*;
import com.sb9.foloke.sectorb9.game.entities.*;
import com.sb9.foloke.sectorb9.game.entities.Weapons.*;
//import java.util.*;

public class TurretSystem
{
	private PointF pointOfShooting;
	private int slotIndex;
	private int type;
	private Weapon weapon;
	private Ship parent;
	public TurretSystem(PointF point,Game game,Ship ship)
	{
		//TODO: WEAPONS SYSTEM (LASERS ROCKETS AND CHOOSING THEM)
		
		this.parent=ship;
		pointOfShooting=point;
		
		weapon=new Laser(this,game);
	}
	public void shoot()
	{
		weapon.shoot();
	}
	public void tick()
	{
		weapon.tick();
	}
	public void render(Canvas canvas)
	{
		weapon.render(canvas);
	}
	public PointF getPointOfShooting()
	{
		return pointOfShooting;
	}
	public Ship getParent()
	{
		return parent;
	}
}
