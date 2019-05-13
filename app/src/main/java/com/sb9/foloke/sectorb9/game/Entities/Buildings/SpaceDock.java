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
	
	public void setToProduce(ModulesDataSheet.HullModule hull, ModulesDataSheet.EngineModule engine, ModulesDataSheet.GeneratorModule generator, ModulesDataSheet.ShieldModule shields, ModulesDataSheet.GyrosModule gyroscope,
							 ModulesDataSheet.TurretModule[] turrets, ModulesDataSheet.WeaponModule[] weapons)
	{
		Ship ship=new Ship(hull,engine,generator,shields,gyroscope,turrets,weapons);
		toSpawn=new ControlledShip((int)this.x,(int)this.y,0,gameManager,ship);
		toSpawn.setController(new AI(toSpawn));
		ship.init(toSpawn);
		inProduction=true;
	}
}
