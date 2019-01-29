package com.sb9.foloke.sectorb9.game.Entities;
import android.graphics.*;

import com.sb9.foloke.sectorb9.game.Managers.GameManager;
import com.sb9.foloke.sectorb9.game.DataSheets.BuildingsDataSheet;

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

		canvas.drawBitmap(image,x,y,null);
		if(gameManager.drawDebugInfo)
			drawDebugCollision(canvas);
	}

	@Override
	public void tick()
	{
		if(getHp()<=0)
		{
			active=false;
			return;
		}


	}
}
