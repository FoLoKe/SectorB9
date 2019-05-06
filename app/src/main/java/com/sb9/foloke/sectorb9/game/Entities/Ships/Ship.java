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
	private Bitmap shipImage,engineImage;
	private PointF pointOfEngine;
	private PointF pointOfEngineSmoke;
	private DynamicEntity holder;
	private float sidewaysImpulse=0;
    private float maxHP=1;
    private float maxSP=0;
    private int shieldSize=1;
    private float backwardImpulse=0;
	private float mass=0;
    private float frontImpulse=0;
	private CustomCollisionObject collisionObject;
	public Entity target=null;
	

	private TurretSystem[] turrets;
	private ParticleSystem engineSmoke;
	
	
	private ModulesDataSheet.HullModule hull;
	private ModulesDataSheet.EngineModule engine;
	private ModulesDataSheet.GeneratorModule generator;
	private ModulesDataSheet.ShieldModule shields;
	private ModulesDataSheet.TurretModule[] turretsMods;
	private ModulesDataSheet.WeaponModule[] weapons;
	private ModulesDataSheet.GyrosModule gyroscope;


	
	public Ship(ModulesDataSheet.HullModule hull, ModulesDataSheet.EngineModule engine, ModulesDataSheet.GeneratorModule generator, ModulesDataSheet.ShieldModule shields, ModulesDataSheet.GyrosModule gyroscope,
	ModulesDataSheet.TurretModule[] turretsMods, ModulesDataSheet.WeaponModule[] weapons)
	{
		this.hull=hull;
		this.engine=engine;
		this.generator=generator;
		this.shields=shields;
		this.turretsMods=turretsMods;
		this.weapons=weapons;
		this.gyroscope=gyroscope;
        this.collisionObject=new CustomCollisionObject(1,1,null);

	}
	
	public void init(DynamicEntity e)
	{
		holder=e;
        mass=0;

		//HULL
		shipImage=hull.image;
        mass+=hull.mass;
        maxHP=hull.HP;

		//ENGINE
        engineImage=engine.image;
        mass+=engine.mass;
        frontImpulse=engine.impulse;
        backwardImpulse=engine.impulse;

        //GYROS
        sidewaysImpulse=gyroscope.impulse;
        mass+=gyroscope.mass;

        //TURRETS AND WEAPONS
        turrets=new TurretSystem[hull.gunMounts.length];
		for(int i=0;i<hull.gunMounts.length;i++)
		{
			mass+=turretsMods[i].mass;
			mass+=weapons[i].mass;
			turrets[i]=new TurretSystem(hull.gunMounts[i].mountPoints,weapons[i].type,holder.getGameManager(),this);
		}

        //SHIELDS
        maxSP=shields.SP;
		shieldSize=(int)hull.image.getHeight()/32;
		
        //ENGINE SMOKE
        pointOfEngine=new PointF(0,0);
        engineSmoke=new ParticleSystem(EffectsAsset.yellow_pixel,holder.getWorldLocation().x,holder.getWorldLocation().y,1f,new PointF(0.2f,0),true,120,holder.getGameManager());
        engineSmoke.setAccuracy(new Point(16,1));
        pointOfEngineSmoke=new PointF(0,shipImage.getHeight()/2f);

        //COLLISION
		collisionObject=new CustomCollisionObject(shipImage.getHeight(),shipImage.getWidth(),holder.getGameManager());
		setOptionToDynamic();
	}

	private void setOptionToDynamic()
    {
        holder.setSidewaysImpulse(sidewaysImpulse);
        holder.setMaxHP(maxHP);
        holder.setMaxSH(maxSP);
        holder.setShieldSize(shieldSize);
        holder.setBackwardImpulse(backwardImpulse);
        holder.setFrontImpulse(frontImpulse);
		holder.setMass(mass);
    }

	public void render(Canvas canvas)
	{
        canvas.save();
		canvas.rotate(holder.getWorldRotation(),holder.getCenterX(),holder.getCenterY());
		if(holder.getMovable())
		canvas.drawBitmap(engineImage,holder.getCenterX()-engineImage.getWidth()/2f+pointOfEngine.x,holder.getCenterY()-engineImage.getHeight()/2f+pointOfEngine.y-5+(holder.getAcceleration()*2),null);
		canvas.drawBitmap(shipImage,holder.getCenterX()-shipImage.getWidth()/2f,holder.getCenterY()-shipImage.getHeight()/2f,null);

		canvas.restore();

		for(TurretSystem t:turrets)
		t.render(canvas);
		
		if(holder.getAcceleration()>0)
		{
			float mathRotation=(float)(Math.PI/180*(holder.getWorldRotation()));
			PointF tPointOfEngineSmoke =new PointF((float)((pointOfEngineSmoke.x )* Math.cos(mathRotation) - pointOfEngineSmoke.y * Math.sin(mathRotation))
												,(float)(pointOfEngineSmoke.x * Math.sin(mathRotation) + pointOfEngineSmoke.y * Math.cos(mathRotation)));

			engineSmoke.draw(holder.getCenterX()+tPointOfEngineSmoke.x,holder.getCenterY()+tPointOfEngineSmoke.y,holder.getWorldRotation(), (pointOfEngineSmoke));
		}
		engineSmoke.render(canvas);
		
		if(Options.drawDebugInfo.getBoolean())
			collisionObject.render(canvas);
		
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

	public CustomCollisionObject getCollisionObject() {
		return collisionObject;
	}

	public void calculateCollisionObject()
	{
		collisionObject.calculateCollisionObject(holder.getCenterX(),holder.getCenterY());
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

	public static Ship createSimple()
    {
        Ship ship;
        ModulesDataSheet.HullModule h=(ModulesDataSheet.HullModule)ModulesDataSheet.getOfType(ModulesDataSheet.type.HULL)[0];
        ModulesDataSheet.EngineModule e=(ModulesDataSheet.EngineModule)ModulesDataSheet.getOfType(ModulesDataSheet.type.ENGINE)[0];
        ModulesDataSheet.GeneratorModule g=(ModulesDataSheet.GeneratorModule)ModulesDataSheet.getOfType(ModulesDataSheet.type.GENERATOR)[0];
        ModulesDataSheet.ShieldModule s=(ModulesDataSheet.ShieldModule)ModulesDataSheet.getOfType(ModulesDataSheet.type.SHIELD)[0];
        ModulesDataSheet.GyrosModule gy=(ModulesDataSheet.GyrosModule)ModulesDataSheet.getOfType(ModulesDataSheet.type.GYROSCOPES)[0];

        ModulesDataSheet.TurretModule[] t=new ModulesDataSheet.TurretModule[h.gunMounts.length];
        for(int i=0;i<t.length;i++)
            t[i]= (ModulesDataSheet.TurretModule) ModulesDataSheet.getOfType(ModulesDataSheet.type.TURRET)[0];

        ModulesDataSheet.WeaponModule[] w=new ModulesDataSheet.WeaponModule[h.gunMounts.length];
        for(int i=0;i<w.length;i++)
            w[i]= (ModulesDataSheet.WeaponModule) ModulesDataSheet.getOfType(ModulesDataSheet.type.WEAPON)[0];

        ship=new Ship(h,e,g,s,gy,t,w);
        return ship;
    }
}
