package com.sb9.foloke.sectorb9.game.Entities;
import android.graphics.*;
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
	}
	@Override
	public void render(Canvas canvas)
	{
		// TODO: Implement this method
		//super.render(canvas);
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
		// TODO: Implement this method
		super.tick();
	}

	@Override
	public void applyDamage(float damage)
	{
		// TODO: Implement this method
		//super.applyDamage(damage);
		progress+=damage;
		uIhp.set(progress);
		if(progress>=maxProgress)
		{
			Entity e=gameManager.getWorldManager().getEntityManager().createObject(idOfObject);
			gameManager.getWorldManager().getEntityManager().addObject(e);
			e.setCenterX(getCenterX());
			e.setCenterY(getCenterY());
			e.calculateCollisionObject();
			e.setTeam(tteam);
			active=false;
			renderable=false;
			opened=false;
			inventoryMaxCapacity=0;
			gameManager.getWorldManager().getEntityManager().deleteObject(this);
		}
	}
	
}
