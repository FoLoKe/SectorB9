package com.sb9.foloke.sectorb9.game.Entities.Buildings;
import com.sb9.foloke.sectorb9.game.Assets.*;
import com.sb9.foloke.sectorb9.game.Managers.GameManager;
import com.sb9.foloke.sectorb9.game.DataSheets.BuildingsDataSheet;
import com.sb9.foloke.sectorb9.game.Funtions.*;

import android.graphics.*;
import com.sb9.foloke.sectorb9.game.UI.*;
//import java.util;

public class FuelGenerator extends Generator
{
	private final static int ID=4;
	private ProgressBarUI prgBar;
	private Timer prodTimer;
	private Animation fuelGeneratorAnim;
	
	
	public FuelGenerator(float x, float y, float rotation, GameManager gameManager)
	{
		super(x,y,rotation, gameManager,ID);
		
		
		
		///critical enabled=true
		
		prodTimer=new Timer(0);
		prgBar=new ProgressBarUI(this,50,8,-25,-20,UIAsset.stunBackground,UIAsset.stunLine,UIAsset.progressBarBorder,prodTimer.getTick());
		fuelGeneratorAnim=new Animation(BuildingsDataSheet.findById(ID).animation,15);
		
		calculateConsumers();
	}

	@Override
	public void tick()
	{
		super.tick();
		if(enabled)
		{
			prodTimer.tick();
		}
		
		if(prodTimer.getTick()<=0)
			if(true)
			{
			
					if(inventory.takeOneItemFromAllInventory(3,1))
					{
						prodTimer.setTimer(50);
						gameManager.updateInventory(this);
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
			prgBar.set(prodTimer.getTick()/(30f));
		
		if(enabled)
		{
			fuelGeneratorAnim.tick();
		image=fuelGeneratorAnim.getImage();
		}

	}

	@Override
	public void render(Canvas canvas)
	{
		
		if(!renderable)
			return;
		super.render(canvas);
		prgBar.render(canvas);
		
	}
	
	
}
