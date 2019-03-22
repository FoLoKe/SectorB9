package com.sb9.foloke.sectorb9.game.Entities.Ships;
import android.graphics.*;

import com.sb9.foloke.sectorb9.game.Assets.EffectsAsset;
import com.sb9.foloke.sectorb9.game.Assets.ShipAsset;
import com.sb9.foloke.sectorb9.game.Entities.DynamicEntity;
import com.sb9.foloke.sectorb9.game.Funtions.*;
import com.sb9.foloke.sectorb9.game.ParticleSystem.*;
import com.sb9.foloke.sectorb9.game.Entities.Weapons.*;
import com.sb9.foloke.sectorb9.game.Assets.*;

public class Ship
{
	protected Bitmap shipImage,engineImage,engineShieldImage=null;
	protected PointF pointOfengine;
	protected PointF[] pointOfShooting;
	protected PointF pointOfEngineSmoke;
	protected DynamicEntity holder;
	
	protected float sidewayImpulse;
    protected float maxHP;
    protected float maxSH;
    protected int shieldSize=1;
    protected float bacwardImpulse=1;
	protected float mass=1;
    protected float frontImpulse=1;
	private CustomCollisionObject collisonObject;

	

	protected TurretSystem turrets[];
	private ParticleSystem engineSmoke;
	
	public Ship(Bitmap shipImage,Bitmap engineImage,Bitmap engineShieldImage,DynamicEntity holder)
	{
		this.shipImage=shipImage;
		this.engineImage=engineImage;
		this.engineShieldImage=engineShieldImage;
		this.holder=holder;
		this.pointOfengine=new PointF(0,0);
		this.engineSmoke=new ParticleSystem(EffectsAsset.yellow_pixel,holder.getWorldLocation().x,holder.getWorldLocation().y,1f,new PointF(0.2f,0),true,120,holder.getGameManager());
		engineSmoke.setAccuracy(new Point(16,1));
		pointOfEngineSmoke=new PointF(0,shipImage.getHeight()/2);
	}
	
	
	public void init(DynamicEntity e)
	{}

	protected void setOptionToDynamic()
    {
        
        holder.setSidewayImpulse(sidewayImpulse);
        holder.setMaxHP(maxHP);
        holder.setMaxSH(maxSH);
        holder.setShieldSize(shieldSize);
        holder.setBackwardImpulse(bacwardImpulse);
        holder.setFrontImpulse(frontImpulse);
		holder.setMass(mass);
    }

	protected void setCollisionObject()
	{
		collisonObject=new CustomCollisionObject(shipImage.getWidth(),shipImage.getHeight(),holder.getGameManager());
	}
	public void render(Canvas canvas)
	{
        canvas.save();
		canvas.rotate(holder.getWorldRotation(),holder.getCenterX(),holder.getCenterY());
		if(holder.getMoveable())
		canvas.drawBitmap(engineImage,holder.getCenterX()-engineImage.getWidth()/2+pointOfengine.x,holder.getCenterY()-engineImage.getHeight()/2+pointOfengine.y-5+(holder.getAcceleration())*5,null);
		canvas.drawBitmap(shipImage,holder.getCenterX()-shipImage.getWidth()/2,holder.getCenterY()-shipImage.getHeight()/2,null);

		canvas.restore();

		for(TurretSystem t:turrets)
		t.render(canvas);
		
		if(holder.getAcceleration()>0)
		{
			float mathRotation=(float)(Math.PI/180*(holder.getWorldRotation()));
			PointF tpointOfEngineSmoke =new PointF((float)((pointOfEngineSmoke.x )* Math.cos(mathRotation) - pointOfEngineSmoke.y * Math.sin(mathRotation))
												,(float)(pointOfEngineSmoke.x * Math.sin(mathRotation) + pointOfEngineSmoke.y * Math.cos(mathRotation)));

			engineSmoke.draw(holder.getCenterX()+tpointOfEngineSmoke.x,holder.getCenterY()+tpointOfEngineSmoke.y,holder.getWorldRotation(), (pointOfEngineSmoke));
		}
		engineSmoke.render(canvas);
		
		if(Options.drawDebugInfo.getBoolean())
			collisonObject.render(canvas);
		
	}
	
	public void tick()
	{
		for(TurretSystem t:turrets)
			t.tick();
		engineSmoke.tick();
	}

	public CustomCollisionObject getCollisonObject() {
		return collisonObject;
	}

	public void calculateCollisionObject()
	{
		collisonObject.calculateCollisionObject(holder.getCenterX(),holder.getCenterY());
	}
	
	public void shoot()
	{
		for(TurretSystem t: turrets)
		{
			t.shoot();
		}
	}
	public DynamicEntity getHolder()
	{
		return holder;
	}

	public Weapon[] getWeapons()
	{
		Weapon[] w=new Weapon[turrets.length];
		for(int i=0;i<turrets.length;i++)
		{
			w[i]=turrets[i].getWeapon();
		}
		return w;
	}
}
