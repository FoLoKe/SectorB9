package com.sb9.foloke.sectorb9.game.Entities.Ships;
import android.graphics.*;

import com.sb9.foloke.sectorb9.game.Managers.GameManager;
import com.sb9.foloke.sectorb9.game.Entities.Weapons.*;
//import java.util.*;

public class TurretSystem
{
	private PointF pointOfShooting;
	private int slotIndex;
	private int type;
	private Weapon weapon;
	private Ship parent;

	public TurretSystem(PointF point, int type, GameManager gameManager, Ship ship)
	{
		//TODO: WEAPONS SYSTEM (ROCKETS AND CHOOSING THEM)
		this.type=type;
		this.parent=ship;
		pointOfShooting=point;
		switch (type)
		{
		case 1:
		    weapon=new Minigun(this, gameManager);
		    break;
		case 2:
		    weapon=new Plasmgun(this, gameManager);
		    break;
		case 3:
		    weapon=new Laser(this, gameManager);
		    break;
		case 4:
		    weapon=new Railgun(this, gameManager);
		    break;
		default:
		    weapon=new Minigun(this, gameManager);
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
