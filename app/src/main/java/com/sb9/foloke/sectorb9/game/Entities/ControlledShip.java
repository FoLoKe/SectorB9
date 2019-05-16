package com.sb9.foloke.sectorb9.game.Entities;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;

import com.sb9.foloke.sectorb9.game.AI.AI;
import com.sb9.foloke.sectorb9.game.Assets.EffectsAsset;
import com.sb9.foloke.sectorb9.game.Assets.ShipAsset;
import com.sb9.foloke.sectorb9.game.Entities.Ships.Ship;

import com.sb9.foloke.sectorb9.game.Funtions.CustomCollisionObject;
import com.sb9.foloke.sectorb9.game.Managers.GameManager;

import static java.lang.Math.PI;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import com.sb9.foloke.sectorb9.game.Entities.Ships.*;
import com.sb9.foloke.sectorb9.game.AI.*;
import com.sb9.foloke.sectorb9.game.DataSheets.*;
import com.sb9.foloke.sectorb9.game.Funtions.*;

public class ControlledShip extends DynamicEntity {

    private Controller controller;
    private Ship ship;
	private static final int ID=10;

	public ControlledShip(int x, int y, int rotation, GameManager gameManager,Ship ship)
    {
        super(x,y,rotation,gameManager,ID);
        this.ship=ship;
		ship.init(this);
		
		movable=true;

		TEAM=2;
		
    }
	
	public Ship getShip()
	{
		return ship;
	}
	
	public Controller getController()
	{
		return controller;
	}
	
	public void setController(Controller c)
	{
		controller=c;
	}
	
    @Override
    public void render(Canvas canvas) {
		if(!active||!renderable)
			return;
        ship.render(canvas);
        super.render(canvas);
		
		if(controller!=null)
		controller.render(canvas);
		
		if(Options.drawDebugInfo.getBoolean())
			canvas.drawText(controller+"",getCenterX(),getCenterY(),debugPaint);

    }

    @Override
    public void tick() {
		if(!active)
			return;
        super.tick();
		
		if(controller!=null)
        controller.tick();
		lastDamage=null;
        ship.tick();
    }
	
	@Override
    public void calculateCollisionObject()
    {
        super.calculateCollisionObject();

        if(ship!=null)
			ship.calculateCollisionObject();
    }

    
   
    @Override
    public CustomCollisionObject getCollisionObject() {return ship.getCollisionObject();}

    public void shoot()
    {
        ship.shoot();
    }
	
	public void setTarget(Entity e)
	{
		if(ship!=null)
	    ship.target=e;
	}

	@Override
	protected String getSpecialSaveLine()
	{
		// TODO: Implement this method
		String line="(";
		line+=ship.getHull().ID+"-";
		//for(ModulesDataSheet:((ModulesDataSheet.HullModule)ModulesDataSheet.getByID(ship.getHull().ID)).gunMounts)
		for(int i=0;i<ship.getTurretsMods().length;i++)
		{
			line+=ship.getTurretsMods()[i].ID+"="+ship.getWeaponsMods()[i].ID+"-";
			
		}
		
		line+=ship.getEngine().ID+"-";
		line+=ship.getGenerator().ID+"-";
		line+=ship.getShields().ID+"-";
		line+=ship.getGyroscope().ID;
		if(controller instanceof AI)
		{
			line+="-"+((AI)controller).getAISaveLine();
		}
		line+=")";
		return line;
	}

	@Override
	protected void decodeSpecialSaveLine(String special)
	{
		// TODO: Implement this method
		special=special.replace("(","");
		special=special.replace(")","");
		String[] modsWords;
		modsWords = special.split("-");
		int i=0;
		ship.setHull((ModulesDataSheet.HullModule)ModulesDataSheet.getByID(Integer.parseInt(modsWords[i])));
		i++;
		ModulesDataSheet.TurretModule[] tms=new ModulesDataSheet.TurretModule[ship.getHull().gunMounts.length];
		ModulesDataSheet.WeaponModule[] wms=new ModulesDataSheet.WeaponModule[ship.getHull().gunMounts.length];
		for(int j=0;j<ship.getHull().gunMounts.length;j++)
		{
			String[] twLine=modsWords[i].split("=");
			tms[j]=(ModulesDataSheet.TurretModule)ModulesDataSheet.getByID(Integer.parseInt(twLine[0]));
			wms[j]=(ModulesDataSheet.WeaponModule)ModulesDataSheet.getByID(Integer.parseInt(twLine[1]));
			i++;
		}
		ship.setTurretsMods(tms);
		ship.setWeaponsMods(wms);
		
		ship.setEngine((ModulesDataSheet.EngineModule)ModulesDataSheet.getByID(Integer.parseInt(modsWords[i])));
		i++;
		ship.setGenerator((ModulesDataSheet.GeneratorModule)ModulesDataSheet.getByID(Integer.parseInt(modsWords[i])));
		i++;
		ship.setShields((ModulesDataSheet.ShieldModule)ModulesDataSheet.getByID(Integer.parseInt(modsWords[i])));
		i++;
		ship.setGyroscope((ModulesDataSheet.GyrosModule)ModulesDataSheet.getByID(Integer.parseInt(modsWords[i])));
		i++;
		if(controller instanceof AI)
		((AI)controller).decodeSaveLine(modsWords[i]);
		ship.init(this);
	}
	
	
	
}
