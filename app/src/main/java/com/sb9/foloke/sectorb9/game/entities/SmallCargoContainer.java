package com.sb9.foloke.sectorb9.game.entities;
import android.graphics.*;
import com.sb9.foloke.sectorb9.game.Assets.*;
import com.sb9.foloke.sectorb9.game.display.*;
import com.sb9.foloke.sectorb9.game.dataSheets.*;

public class SmallCargoContainer extends CargoContainer
{
	final static int ID=1;
	public SmallCargoContainer(float x,float y,float rotation,ObjectsAsset asset,String name,Game game)
	{
		super(x,y,rotation,asset.smallCargoContainer,name,4,game);
		
		this.opened=true;
		energy=true;
	}
	public SmallCargoContainer(float x,float y,float rotation,Game game)
	{
		
		super(x,y,rotation,game.buildingsData.findById(ID).image,game.buildingsData.findById(ID).name,game.buildingsData.findById(ID).inventoryCapacity,game);
	
		
		enabled					=game.buildingsData.findById(ID).enabledByDefault;
		inventoryMaxCapacity	=game.buildingsData.findById(ID).inventoryCapacity;
		isInteractable			=game.buildingsData.findById(ID).interactableByDefault;
		opened					=game.buildingsData.findById(ID).openByDefault;
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
