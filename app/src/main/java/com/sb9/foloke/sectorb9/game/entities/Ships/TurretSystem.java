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
	public TurretSystem(PointF point,int type,Game game,Ship ship)
	{
		//TODO: WEAPONS SYSTEM (LASERS ROCKETS AND CHOOSING THEM)
		this.type=type;
		this.parent=ship;
		pointOfShooting=point;
		switch (type)
		{
		case 1:
		weapon=new Minigun(this,game);
		break;
		case 2:
		weapon=new Plasmgun(this,game);
		break;
		case 3:
		weapon=new Laser(this,game);
		break;
		default:
				weapon=new Minigun(this,game);
				break;
		}
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
