package com.sb9.foloke.sectorb9.game.DataSheets;
import java.util.*;
import com.sb9.foloke.sectorb9.*;
import android.graphics.*;
import com.sb9.foloke.sectorb9.game.Assets.*;

public class ModulesDataSheet
{
	public enum type{NULL,HULL,ENGINE,SHIELD,WEAPON,GENERATOR,TURRET,GYROSCOPES}
	public static ArrayList<Module> modules;
	
	public static class Module
	{
		public int ID;
		public type moduleType;
		public int mass;
		public String name;
		public Bitmap image;
		public Module(int ID,String name,Bitmap image,type moduleType,int mass)
		{
			this.ID=ID;
			this.moduleType=moduleType;
			this.mass=mass;
			this.name=name;
			this.image=image;
		}
	}
	
	public static class HullModule extends Module
	{
		public GunMount[] gunMounts;
		public int HP;
		public static class GunMount
		{
			public boolean fixed;
			public PointF mountPoints;
			public GunMount(PointF mountPoints,boolean fixed)
			{
				this.mountPoints=mountPoints;
				this.fixed=fixed;
			}
		}
		public HullModule(int ID,String name,Bitmap image,type moduleType,int mass,int HP,GunMount[] gunMounts)
		{
			super(ID,name,image,moduleType,mass);
			this.gunMounts=gunMounts;
			this.HP=HP;
		}
	}
	
	public static class EngineModule extends Module
	{
		public float impulse;
		public EngineModule(int ID,String name,Bitmap image,type moduleType,int mass,float impulse)
		{
			super(ID,name,image,moduleType,mass);
			this.impulse=impulse;
		}
	}

    public static class GyrosModule extends Module
    {
        public float impulse;
        public GyrosModule(int ID,String name,Bitmap image,type moduleType,int mass,float impulse)
        {
            super(ID,name,image,moduleType,mass);
            this.impulse=impulse;
        }
    }
	
	public static class GeneratorModule extends Module
	{
		public float powerRate;
		public GeneratorModule(int ID,String name,Bitmap image,type moduleType,int mass,float powerRate)
		{
			super(ID,name,image,moduleType,mass);
			this.powerRate=powerRate;
		}
	}
	
	public static class TurretModule extends Module
	{
		public int type;
		public TurretModule(int ID,String name,Bitmap image,type moduleType,int mass,int type)
		{
			super(ID,name,image,moduleType,mass);
			this.type=type;
		}
	}
	
	public static class WeaponModule extends Module
	{
		public int type;
		public WeaponModule(int ID,String name,Bitmap image,type moduleType,int mass,int type)
		{
			super(ID,name,image,moduleType,mass);
			this.type=type;
		}
	}

	public  static class ShieldModule extends Module
    {
        public int type;
        public int SP;
        public ShieldModule(int ID,String name,Bitmap image,type moduleType,int mass,int type,int SP)
        {
            super(ID,name,image,moduleType,mass);
            this.type=type;
            this.SP=SP;
        }
    }
	public static void init(MainActivity MA)
	{
		modules=new ArrayList<>();
		modules.add(new Module(0,"null",ObjectsAsset.nullItem,type.NULL,0));
		modules.add(new HullModule(1,"small",ShipAsset.player_mk1,type.HULL,100,100,new HullModule.GunMount[]{new HullModule.GunMount(new PointF(0,-4),true)}));
		modules.add(new HullModule(2,"medium",ShipAsset.player_mk2,type.HULL,200,150,new HullModule.GunMount[]{new HullModule.GunMount(new PointF(-16,0),true),
									  																	 	new HullModule.GunMount(new PointF(16,0),true)}));
		modules.add(new HullModule(3,"large",ShipAsset.ship_mk3,type.HULL,500,400,new HullModule.GunMount[]{new HullModule.GunMount(new PointF(16,1),true),
									  																	 	new HullModule.GunMount(new PointF(-16,1),true),
																											new HullModule.GunMount(new PointF(8,9),true),
																											new HullModule.GunMount(new PointF(-8,9),true)}));
		modules.add(new EngineModule(4,"engine mk1",ShipAsset.engine_mk1,type.ENGINE,10,60));
		modules.add(new EngineModule(5,"engine mk2",ShipAsset.engine_mk2,type.ENGINE,50,120));
		modules.add(new EngineModule(6,"engine mk3",ShipAsset.engine_mk3,type.ENGINE,100,180));
		modules.add(new GeneratorModule(7,"generator mk1",ObjectsAsset.nullItem,type.GENERATOR,100,0.5f));

		modules.add(new WeaponModule(8,"empty",ObjectsAsset.nullItem,type.WEAPON,100,0));
		modules.add(new WeaponModule(9,"minigun",ObjectsAsset.nullItem,type.WEAPON,100,1));
		modules.add(new WeaponModule(10,"plasmgun",ObjectsAsset.nullItem,type.WEAPON,100,2));
		modules.add(new WeaponModule(11,"laser",ObjectsAsset.nullItem,type.WEAPON,100,3));
		modules.add(new TurretModule(12,"fixed turret",ObjectsAsset.nullItem,type.TURRET,0,1));
		modules.add(new TurretModule(13,"light turret",ObjectsAsset.nullItem,type.TURRET,0,2));
        modules.add(new ShieldModule(14,"shield mk1",ObjectsAsset.nullItem,type.SHIELD,0,1,100));
        modules.add(new GyrosModule(15,"gyros mk1",ObjectsAsset.nullItem,type.GYROSCOPES,25,200));
		modules.add(new GyrosModule(16,"gyros mk2",ObjectsAsset.nullItem,type.GYROSCOPES,50,250));
		modules.add(new GyrosModule(17,"gyros mk3",ObjectsAsset.nullItem,type.GYROSCOPES,75,400));
	}
	
	public static Module[] getOfType(type moduleType)
	{
		ArrayList<Module> list=new ArrayList<>();
			for(Module m:modules)
			if(m.moduleType==moduleType)
				list.add(m);
				
		return list.toArray(new Module[list.size()]);
	}
	
	public static Module getByID(int iD)
	{
		for(Module m:modules)
			if(m.ID==iD)
				return m;
		return modules.get(0);
	}
}
