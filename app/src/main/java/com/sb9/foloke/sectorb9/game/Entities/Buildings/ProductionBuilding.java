package com.sb9.foloke.sectorb9.game.Entities.Buildings;
import com.sb9.foloke.sectorb9.game.Entities.*;
import com.sb9.foloke.sectorb9.game.Managers.*;
import com.sb9.foloke.sectorb9.game.Funtions.*;
import java.util.ArrayList;
import com.sb9.foloke.sectorb9.game.UI.Inventory.*;
import com.sb9.foloke.sectorb9.game.DataSheets.*;
import java.util.Map;
import com.sb9.foloke.sectorb9.game.UI.*;
import com.sb9.foloke.sectorb9.game.Assets.*;
import android.graphics.*;

public abstract class ProductionBuilding extends StaticEntity
{
	protected boolean inProgress=false;
	private Timer progression=new Timer(0);
	private int itemToProduce=0;
	protected ArrayList<Integer> productionQueue=new ArrayList<Integer>();
	private int progressionTime=2;
	private ProgressBarUI prgBar;
	private boolean inventoryfullFlag=false;
	
	private CustomImageUI statusImageNoEnergy;
	private CustomImageUI statusImageEnebled;
	private CustomImageUI statusImageInventory;
	
	public ProductionBuilding(float x, float y, float rotation, GameManager gameManager, int ID)
	{
		super(x,y,rotation, gameManager,ID);
		prgBar=new ProgressBarUI(this,50,8,-25,-20,UIAsset.stunBackground,UIAsset.stunLine,UIAsset.progressBarBorder,progression.getTick());
		statusImageNoEnergy=new CustomImageUI(UIAsset.noEnergySign);
		statusImageEnebled=new CustomImageUI(UIAsset.invFullSign);
		statusImageInventory=new CustomImageUI(UIAsset.invFullSign);
	}
	
	protected void production()
	{
			if(energy)
			{
				//if not working
				if(!inProgress)
				{
					///abstarct item choosing
					ArrayList<Inventory.InventoryItem> neededItems=new ArrayList<>();
					int tItemToProduce =chooseItemToProduce(neededItems);
					
					if(tItemToProduce!=-1)
					{						
							if(inventory.takeArrayItemFromAllInventory(neededItems))
							{
								gameManager.updateInventory(this);
								inProgress=true;
								progression.setTimer(progressionTime);
								itemToProduce=tItemToProduce;
								if(this instanceof Assembler)
								AssemblerUI.updateCurrentProduction((Assembler)this);
							}
						}
					}
				}

				//if in work
				if(inProgress)
				{
					if(progression.tick())
					{
						if(inventory.addToExistingOrNull(itemToProduce,1))
						{
							gameManager.updateInventory(this);
							inProgress=false;
							itemToProduce=0;
							onProductionEnded();
							inventoryfullFlag=false;
						}
						else
						{
							inventoryfullFlag=true;
						}
					}
					
				}
				if(progression.getTick()>0)
					prgBar.tick(progression.getTick()/(progressionTime*0.6f));
			}

	protected abstract int chooseItemToProduce(ArrayList<Inventory.InventoryItem> items);
	protected abstract void onProductionEnded();

	@Override
	public void render(Canvas canvas)
	{
		// TODO: Implement this method
		if(!renderable||!active)
			return;
		super.render(canvas);
		if(inProgress)
			prgBar.render(canvas);
		if(inventoryfullFlag)
			statusImageInventory.render(canvas,new PointF(x-16,y));
        if(!enabled)
        	statusImageEnebled.render(canvas,new PointF(x+16,y));
		if(!energy)
			statusImageNoEnergy.render(canvas,new PointF(x,y));
	}

	@Override
	public void tick()
	{
		// TODO: Implement this method
		if(!active)
			return;
		super.tick();
		prgBar.tick(progression.getTick()/(progressionTime*0.6f));
	}
	
	
		
	public ArrayList<Integer> getQueue()
	{
		return productionQueue;
	}
	public int getInProduction()
	{
		return itemToProduce;
	}
		
}
