package com.sb9.foloke.sectorb9.game.Entities;
import com.sb9.foloke.sectorb9.game.Managers.GameManager;
import com.sb9.foloke.sectorb9.game.DataSheets.BuildingsDataSheet;

import java.util.*;
import android.graphics.*;

public class DroppedItems extends StaticEntity
{
	private final static int ID=9;
	
	//Game game;
	//protected HashMap<Integer,Integer> inventory;
	public DroppedItems(Entity e) {

        super(e.getX(), e.getY(), e.getWorldRotation(), e.getGameManager(), ID);

        inventory = e.getInventory().copy(this);
        renderable = true;
        active = true;


    }

    public DroppedItems(float x,float y,float r,GameManager gm)
    {

        super(x,y,r,gm,ID);
        renderable=true;
        active=true;


    }

	public void render(Canvas canvas)
	{
		if(!renderable||!active)
			return;
		canvas.save();
		
		canvas.rotate(rotation,getCenterX(),getCenterY());
		canvas.drawBitmap(image,x,y,null);
		canvas.restore();
		
	}

	@Override
	public void tick()
	{
		super.tick();
		if(!renderable||!active)
			return;
		this.calculateCollisionObject();
		
		if(inventory.count()==0)
		{
			//active=false;
			//renderable=false;
			//opened=false;
		inventoryMaxCapacity=0;
		toRemove=true;
		}
		
		

	}

	@Override
	public void applyDamage(float damage)
	{

		//super.applyDamage(damage);
	}

	
}
