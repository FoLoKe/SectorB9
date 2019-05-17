package com.sb9.foloke.sectorb9.game.Entities.Ships;
import android.graphics.*;

import com.sb9.foloke.sectorb9.game.Managers.GameManager;
import com.sb9.foloke.sectorb9.game.Entities.Weapons.*;
import com.sb9.foloke.sectorb9.game.UI.CustomViews.*;
import com.sb9.foloke.sectorb9.game.Entities.*;
import com.sb9.foloke.sectorb9.game.Funtions.*;
//import java.util.*;

public class TurretSystem
{
	private	float[] initialPointOfTurret;
	private float[] actualPointOfTurret;
	private	float[] initialPointOfShooting;
	private float[] actualPointOfShooting;
	private int slotIndex;
	private int type;
	private Weapon weapon;
	private Ship parent;
	private float rotation;
	private float actualRotation;
	
	private Entity target;
	private boolean isStatic=false;
	public TurretSystem(PointF point, int type, GameManager gameManager, Ship ship)
	{
		//TODO: WEAPONS SYSTEM (ROCKETS AND CHOOSING THEM)
		this.type=type;
		this.parent=ship;
		initialPointOfTurret=new float[]{point.x,point.y};
		actualPointOfTurret=new float[]{point.x,point.y};
		initialPointOfShooting=new float[]{0,-20};
		actualPointOfShooting=new float[]{0,0};
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
		if(isStatic)
		{}
		else
		{
			
			if(target!=null)
			{
				rotationToPoint(target.getCenterWorldLocation());
				if(!target.getActive())
					target=null;
			}
		}
		if(rotation>360)
			rotation=0;
		if(rotation<0)
			rotation=360;
		actualRotation=getParent().getHolder().getWorldRotation()+rotation;
		if(actualRotation>360)
			actualRotation-=360;
		if(actualRotation<0)
			actualRotation+=360;
		
		parent.getHolder().getTransformMatrix().mapVectors(actualPointOfTurret,initialPointOfTurret);
		actualPointOfTurret[0]+=parent.getHolder().getCenterX();
		actualPointOfTurret[1]+=parent.getHolder().getCenterY();
		
		Matrix m=new Matrix();
		m.postRotate(actualRotation);
		m.mapVectors(actualPointOfShooting,initialPointOfShooting);
		
		actualPointOfShooting[0]+=actualPointOfTurret[0];
		actualPointOfShooting[1]+=actualPointOfTurret[1];
		
		weapon.tick();
	}
	public boolean rotationToPoint(PointF point)
	{
        PointF B=new PointF(actualPointOfTurret[0] -point.x,actualPointOfTurret[1] -point.y);
		float BLength=(float)Math.sqrt(B.x*B.x+B.y*B.y);
		B.set(B.x/BLength,B.y/BLength);
        float sinPhi = ((actualPointOfShooting[0]-point.x)*B.y - (actualPointOfShooting[1]-point.y)*B.x);


		
		if(sinPhi>-0.02&&sinPhi<0.02)
            return true;

		sinPhi*=100;

		if(Math.abs(sinPhi)<1)
		{
			rotation-=sinPhi/2;
		}
		else
		{
    	    if (sinPhi < 0)
          		rotation+=1;
        	if (sinPhi >=0)
				rotation-=1;
		}

        return false;
	}
	public void render(Canvas canvas)
	{
		if(!Options.drawDebugInfo.getBoolean())
			return;
		weapon.render(canvas);
		Paint p=new Paint();
		p.setColor(Color.RED);
		p.setStrokeWidth(3);
		canvas.drawLine(actualPointOfTurret[0],actualPointOfTurret[1],actualPointOfShooting[0],actualPointOfShooting[1],p);
	}

	public float[] getPointOfTurret()
	{
		return actualPointOfTurret;
	}
	public float getRotation()
	{
		return actualRotation;
	}
	public float[] getPointOfShooting()
	{
		return actualPointOfShooting;
	}
	public float[] getInitPointOfShooting()
	{
		return actualPointOfShooting;
	}

	public Ship getParent()
	{
		return parent;
	}
	
	public Weapon getWeapon()
	{
		return weapon;
	}
	
	public void setTarget(Entity e)
	{
		target=e;
	}

}
