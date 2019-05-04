package com.sb9.foloke.sectorb9.game.Entities.Ships;
import android.graphics.*;

import com.sb9.foloke.sectorb9.game.Assets.EffectsAsset;
import com.sb9.foloke.sectorb9.game.Assets.ShipAsset;
import com.sb9.foloke.sectorb9.game.Entities.DynamicEntity;
import com.sb9.foloke.sectorb9.game.Funtions.*;
import com.sb9.foloke.sectorb9.game.ParticleSystem.*;
import com.sb9.foloke.sectorb9.game.Entities.Weapons.*;
import com.sb9.foloke.sectorb9.game.Assets.*;
import com.sb9.foloke.sectorb9.game.DataSheets.*;
import com.sb9.foloke.sectorb9.game.Entities.*;

public class Ship
{
	protected Bitmap shipImage,engineImage,engineShieldImage=null;
	protected PointF pointOfengine;
	protected PointF[] pointOfShooting;
	protected PointF pointOfEngineSmoke;
	protected DynamicEntity holder;
	
	protected float sidewayImpulse=100;
    protected float maxHP=100;
    protected float maxSH=100;
    protected int shieldSize=1;
    protected float backwardImpulse=1;
	protected float mass=0;
    protected float frontImpulse=1;
	private CustomCollisionObject collisonObject;
	public Entity target=null;
	

	protected TurretSystem turrets[];
	private ParticleSystem engineSmoke;
	
	
	private ModulesDataSheet.HullModule hull;
	private ModulesDataSheet.EngineModule engine;
	private ModulesDataSheet.GeneratorModule generator;
	private ModulesDataSheet.Module shields;
	private ModulesDataSheet.TurretModule turretsMods[];
	private ModulesDataSheet.WeaponModule weapons[];
	
	public Ship(int hullID,DynamicEntity holder)
	{
		this.hull=(ModulesDataSheet.HullModule)ModulesDataSheet.getByID(hullID);
		this.engine=(ModulesDataSheet.EngineModule)ModulesDataSheet.getOfType(ModulesDataSheet.type.ENGINE)[0];
		turretsMods=new ModulesDataSheet.TurretModule[1];
		turretsMods[0]=(ModulesDataSheet.TurretModule)ModulesDataSheet.getOfType(ModulesDataSheet.type.TURRET)[0];
		weapons=new ModulesDataSheet.WeaponModule[1];
		weapons[0]=(ModulesDataSheet.WeaponModule)ModulesDataSheet.getOfType(ModulesDataSheet.type.WEAPON)[0];
		init(holder);
	}
	
	public Ship(ModulesDataSheet.HullModule hull,ModulesDataSheet.EngineModule engine,ModulesDataSheet.GeneratorModule generator,ModulesDataSheet.Module shields,
	ModulesDataSheet.TurretModule turretsMods[],ModulesDataSheet.WeaponModule weapons[])
	{
		this.hull=hull;
		this.engine=engine;
		this.generator=generator;
		this.shields=shields;
		this.turretsMods=turretsMods;
		this.weapons=weapons;
		
		
		
	}
	
	public void init(DynamicEntity e)
	{
		this.holder=e;
		mass+=hull.mass;
		this.shipImage=hull.image;
		
		mass+=engine.mass;
		this.engineImage=ShipAsset.engine_mk1;
		this.engineShieldImage=ShipAsset.engine_mk1;
		
		this.pointOfengine=new PointF(0,0);
		this.engineSmoke=new ParticleSystem(EffectsAsset.yellow_pixel,holder.getWorldLocation().x,holder.getWorldLocation().y,1f,new PointF(0.2f,0),true,120,holder.getGameManager());
		engineSmoke.setAccuracy(new Point(16,1));
		pointOfEngineSmoke=new PointF(0,shipImage.getHeight()/2);

		
		turrets=new TurretSystem[hull.gunMounts.length];
		for(int i=0;i<hull.gunMounts.length;i++)
		{
			//mass+=turretsMods[i].mass;
			//mass+=weapons[i].mass;
			turrets[i]=new TurretSystem(hull.gunMounts[i].mountPoints,weapons[i].type,holder.getGameManager(),this);
		}
		
		collisonObject=new CustomCollisionObject(shipImage.getHeight(),shipImage.getWidth(),holder.getGameManager());
		setOptionToDynamic();
	}

	protected void setOptionToDynamic()
    {
        
        holder.setSidewayImpulse(sidewayImpulse);
        holder.setMaxHP(maxHP);
        holder.setMaxSH(maxSH);
        holder.setShieldSize(shieldSize);
        holder.setBackwardImpulse(backwardImpulse);
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
		canvas.drawBitmap(engineImage,holder.getCenterX()-engineImage.getWidth()/2+pointOfengine.x,holder.getCenterY()-engineImage.getHeight()/2+pointOfengine.y-5+(holder.getAcceleration()*2),null);
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
		{
			t.setTarget(target);
			t.tick();
		}
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
