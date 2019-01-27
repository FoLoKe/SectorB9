package com.sb9.foloke.sectorb9.game.entities;
import android.graphics.*;

import com.sb9.foloke.sectorb9.game.Managers.GameManager;
import com.sb9.foloke.sectorb9.game.dataSheets.BuildingsDataSheet;

public class SmallCargoContainer extends CargoContainer
{
	final static int ID=1;
	
	public SmallCargoContainer(float x, float y, float rotation, GameManager gameManager)
	{
		
		super(x,y,rotation, BuildingsDataSheet.findById(ID).image, BuildingsDataSheet.findById(ID).name, BuildingsDataSheet.findById(ID).inventoryCapacity, gameManager,ID);
	
		
		enabled					= BuildingsDataSheet.findById(ID).enabledByDefault;
		inventoryMaxCapacity	= BuildingsDataSheet.findById(ID).inventoryCapacity;
		isInteractable			= BuildingsDataSheet.findById(ID).interactableByDefault;
		opened					= BuildingsDataSheet.findById(ID).openByDefault;
		energy=true;
	}
	
	@Override
	public void render(Canvas canvas)
	{
		if(!renderable)
			return;
		// TODO: Implement this method
		canvas.drawBitmap(image,x,y,null);
	}

	@Override
	public void tick()
	{
		if(getHp()<=0)
		{
			active=false;
			return;
		}
		this.collisionBox.set(x,y,x+image.getWidth(),y+image.getHeight());
		// TODO: Implement this method
	}
}
