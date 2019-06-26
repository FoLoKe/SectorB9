package com.sb9.foloke.sectorb9.game.Entities.Buildings;
import com.sb9.foloke.sectorb9.game.Entities.*;
import com.sb9.foloke.sectorb9.game.Managers.*;
import android.graphics.*;
import com.sb9.foloke.sectorb9.game.DataSheets.*;
import com.sb9.foloke.sectorb9.game.Entities.Ships.*;
import com.sb9.foloke.sectorb9.game.AI.*;

public class SpaceDock extends StaticEntity
{
	final static int ID=12;
	private boolean inProduction=false;
	private ControlledShip toSpawn;

	public ModulesDataSheet.HullModule selectedHull;
	public ModulesDataSheet.EngineModule selectedEngine;
	public ModulesDataSheet.TurretModule[] selectedTurrets;
	public ModulesDataSheet.WeaponModule[] selectedWeapons;
	public ModulesDataSheet.GeneratorModule selectedGenerator;
	public ModulesDataSheet.ShieldModule selectedShield;
	public ModulesDataSheet.GyrosModule selectedGyroscopes;
	
	public SpaceDock(float x,float y,float rotation,GameManager gm)
	{
		super(x,y,rotation,gm,ID);
	}

	@Override
	public void render(Canvas canvas)
	{
		if(!renderable)
			return;
		super.render(canvas);
		canvas.save();
		canvas.rotate(rotation,getCenterX(),getCenterY());
		canvas.drawBitmap(image,x,y,null);
		canvas.restore();
	}

	@Override
	public void tick()
	{
		if(inProduction)
		{
			gameManager.getEntityManager().addObject(toSpawn);
			inProduction=false;
		}
		super.tick();
	}
	
	public void setToProduce()
	{
		Ship ship=new Ship(selectedHull,selectedEngine,selectedGenerator,selectedShield,selectedGyroscopes,selectedTurrets,selectedWeapons);
		toSpawn=new ControlledShip((int)this.x,(int)this.y,0,gameManager,ship);
		toSpawn.setController(new AI(toSpawn));
		toSpawn.setTeam(TEAM);
		ship.init(toSpawn);
		inProduction=true;
	}
}
