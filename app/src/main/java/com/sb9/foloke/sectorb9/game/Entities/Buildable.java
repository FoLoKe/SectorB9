package com.sb9.foloke.sectorb9.game.Entities;
import android.graphics.*;

import com.sb9.foloke.sectorb9.game.DataSheets.BuildingsDataSheet;
import com.sb9.foloke.sectorb9.game.Funtions.CustomCollisionObject;
import com.sb9.foloke.sectorb9.game.Managers.*;

public class Buildable extends StaticEntity
{
	private final static int ID=11;
	private int idOfObject;
	private float progress=0,maxProgress=100;
	//todo healing stuf
	int tteam=0;
	
	public Buildable(int id,int team,GameManager GM)
	{
		super(0,0,0,GM,ID);
		idOfObject=id;
		uIhp.set(progress);
		tteam=team;
        collisionObject=new CustomCollisionObject(BuildingsDataSheet.findById(idOfObject).image.getWidth(),BuildingsDataSheet.findById(idOfObject).image.getHeight(),gameManager);
        calculateCollisionObject();
	}
	@Override
	public void render(Canvas canvas)
	{
		if(active)
		{
			if(!renderable)
				return;
			super.render(canvas);
			canvas.save();
			canvas.rotate(rotation,getCenterX(),getCenterY());
			canvas.drawBitmap(image,x,y,null);
			canvas.restore();
			if(gameManager.drawDebugInfo)
				drawDebugCollision(canvas);
				
			if(progress<maxProgress)
			{
				uIhp.render(canvas);
			}
		}
	}

	@Override
	public void tick()
	{
		super.tick();
	}

	@Override
	public void applyDamage(float damage)
	{
		progress+=damage;
		uIhp.set(progress);
		if(progress>=maxProgress)
		{
			Entity e=gameManager.getWorldManager().getEntityManager().createObject(idOfObject);
			gameManager.getWorldManager().getEntityManager().addObject(e);
			e.setCenterX(getCenterX());
			e.setCenterY(getCenterY());
			e.setWorldRotation(rotation);
			e.calculateCollisionObject();
			e.setTeam(tteam);
			active=false;
			renderable=false;
			opened=false;
			inventoryMaxCapacity=0;
			toRemove=true;
		}
	}
	
}
