package com.sb9.foloke.sectorb9.game.Entities.Buildings;
import com.sb9.foloke.sectorb9.game.Managers.GameManager;
import com.sb9.foloke.sectorb9.game.DataSheets.BuildingsDataSheet;

public class SolarPanel extends Generator
{
	final static private int ID=3;

	public SolarPanel(float x, float y, float rotation, GameManager gameManager)
	{
		super(x,y,rotation, BuildingsDataSheet.findById(ID).image, BuildingsDataSheet.findById(ID).name, gameManager,ID);
	enabled					= BuildingsDataSheet.findById(ID).enabledByDefault;
	inventoryMaxCapacity	= BuildingsDataSheet.findById(ID).inventoryCapacity;
	isInteractable			= BuildingsDataSheet.findById(ID).interactableByDefault;
	opened					= BuildingsDataSheet.findById(ID).openByDefault;
	
	
	}
}
