package com.sb9.foloke.sectorb9.game.Entities;
import com.sb9.foloke.sectorb9.game.Managers.GameManager;
import com.sb9.foloke.sectorb9.game.DataSheets.BuildingsDataSheet;

import java.util.*;
import android.graphics.*;

public class DroppedItems extends Entity
{
	private final static int ID=9;
	float x,y;
	//Game game;
	//protected HashMap<Integer,Integer> inventory;
	public DroppedItems(Entity e)
	{
		
		super(e.getX(),e.getY(),e.getWorldRotation(), e.getGameManager(),ID);
		inventory=e.getInventory();
		
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
		/*if(!renderable||!active)
			return;
		this.calculateCollisionObject();
		
		if(inventory.size()==0)
		{
			active=false;
			renderable=false;
			opened=false;
		inventoryMaxCapacity=0;
		getGame().getObjectUIInventory().setTarget(null);
		//Context tcontext=getGame().mAcontext;
		//((MainActivity)tcontext).closeObjectInventory();
		active=false;
		renderable=false;
		opened=false;
		}*/
		// TODO: Implement this method
	}

	
}
