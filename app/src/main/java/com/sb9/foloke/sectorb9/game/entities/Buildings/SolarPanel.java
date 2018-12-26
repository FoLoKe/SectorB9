package com.sb9.foloke.sectorb9.game.entities.Buildings;
import com.sb9.foloke.sectorb9.game.Assets.*;
import com.sb9.foloke.sectorb9.game.display.*;

public class SolarPanel extends Generator
{
	final static int ID=3;
	public SolarPanel(float x,float y,float rotation,ObjectsAsset asset,String name,Game game)
	{
		super(x,y,rotation,asset.solarPanel,name,game,ID);
	}
	
	public SolarPanel(float x,float y,float rotation,Game game)
	{
		super(x,y,rotation,game.buildingsData.findById(ID).image,game.buildingsData.findById(ID).name,game,ID);
	enabled					=game.buildingsData.findById(ID).enabledByDefault;
	inventoryMaxCapacity	=game.buildingsData.findById(ID).inventoryCapacity;
	isInteractable			=game.buildingsData.findById(ID).interactableByDefault;
	opened					=game.buildingsData.findById(ID).openByDefault;
	
	
	}
}
