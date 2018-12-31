package com.sb9.foloke.sectorb9.game.entities.Buildings;
import com.sb9.foloke.sectorb9.game.Assets.*;
import com.sb9.foloke.sectorb9.game.display.*;
import com.sb9.foloke.sectorb9.game.funtions.*;
import java.util.Map;
import com.sb9.foloke.sectorb9.*;
import android.graphics.*;
import com.sb9.foloke.sectorb9.game.UI.*;
//import java.util;

public class FuelGenerator extends Generator
{
	private final static int ID=4;
	private UIProgressBar prgBar;
	private Timer prodTimer;
	private Animation fuelGeneratorAnim;
	
	
	public FuelGenerator(float x,float y,float rotation,Game game)
	{
		super(x,y,rotation,game.buildingsData.findById(ID).image,game.buildingsData.findById(ID).name,game,ID);
		
		
		
		///critical enabled=true
		enabled					=game.buildingsData.findById(ID).enabledByDefault;
		inventoryMaxCapacity	=game.buildingsData.findById(ID).inventoryCapacity;
		isInteractable			=game.buildingsData.findById(ID).interactableByDefault;
		opened					=game.buildingsData.findById(ID).openByDefault;
		
		prodTimer=new Timer(0);
		prgBar=new UIProgressBar(this,50,8,-25,-20,UIAsset.stunBackground,UIAsset.stunLine,UIAsset.progressBarBorder,prodTimer.getTick());
		fuelGeneratorAnim=new Animation(game.buildingsData.findById(ID).animation,15);
		
		calculateConsumers();
	}

	@Override
	public void tick()
	{
		// TODO: Implement this method
		if(enabled)
		{
		super.tick();
			prodTimer.tick();
		}
		
		if(prodTimer.getTick()<=0)
			if(true)
			{
			
					if(inventory.takeOneItemFromAllInventory(3,1))
					{
						prodTimer.setTimer(50);
						game.updateInventory(this);
						if(!enabled)
						{
							onAndOff();
						}
					}
			else
			{
				if(enabled)
					onAndOff();
			}
		}
			else
			{
				if(enabled)
					onAndOff();
			}
		if(prodTimer.getTick()>0)
			prgBar.tick(prodTimer.getTick()/(30f));
		
		if(enabled)
		{
			fuelGeneratorAnim.tick();
		image=fuelGeneratorAnim.getImage();
		}
		calculateCollisionObject();
	}

	@Override
	public void render(Canvas canvas)
	{
		// TODO: Implement this method
		super.render(canvas);
		prgBar.render(canvas);
	}
	
	
}
