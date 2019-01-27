package com.sb9.foloke.sectorb9.game.entities;
import com.sb9.foloke.sectorb9.game.Managers.GameManager;
import com.sb9.foloke.sectorb9.game.dataSheets.BuildingsDataSheet;

import java.util.*;
import android.graphics.*;

public class DroppedItems extends Entity
{
	private final static int ID=0;
	float x,y;
	//Game game;
	//protected HashMap<Integer,Integer> inventory;
	public DroppedItems(float x, float y, Map<Integer,Integer> inventory, GameManager gameManager)
	{
		
		super(x,y,((new Random()).nextInt(180)), BuildingsDataSheet.findById(ID).image,"g", gameManager,0);
		this.x=x;
		this.y=y;
		this.inventoryMaxCapacity=inventory.size();
		//this.inventory=new HashMap<Integer,Integer>();
		//this.setInventory(inventory);
		this.collidable=false;
		this.opened=true;
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
