package com.sb9.foloke.sectorb9.game.Entities.Buildings;
import com.sb9.foloke.sectorb9.game.Managers.GameManager;
import com.sb9.foloke.sectorb9.game.DataSheets.BuildingsDataSheet;
import com.sb9.foloke.sectorb9.game.DataSheets.ItemsDataSheet;
import com.sb9.foloke.sectorb9.game.Entities.*;
import android.graphics.*;

import com.sb9.foloke.sectorb9.game.Funtions.*;
import com.sb9.foloke.sectorb9.game.UI.*;
import com.sb9.foloke.sectorb9.game.Assets.*;
import com.sb9.foloke.sectorb9.game.UI.Inventory.*;

public class Crusher extends StaticEntity
{
	private final static int ID=2;
	private ProgressBarUI prgBar;
	private CustomImageUI statusImage;
	
	private Inventory.InventoryItem inProduction;
	private int count;
	private Timer prodTimer;
	private PointF collisionInitPoints[];
	private Animation crusherAnim;
	
	private boolean noInventorySpaceBlock=false;
	private boolean noItemsBlock=false;
	
	
	public Crusher(float x, float y, float rotation, GameManager gameManager)
	{
		super(x,y,rotation, BuildingsDataSheet.findById(ID).image, BuildingsDataSheet.findById(ID).name,gameManager,ID);
		crusherAnim=new Animation(BuildingsDataSheet.findById(ID).animation,15);
		this.inventoryMaxCapacity=3;
		this.opened=true;
		inProduction=new Inventory.InventoryItem(0,0,0,0);
		count=0;prodTimer=new Timer(0);
		prgBar=new ProgressBarUI(this,50,8,-25,-20,UIAsset.stunBackground,UIAsset.stunLine,UIAsset.progressBarBorder,prodTimer.getTick());



		statusImage=new CustomImageUI(UIAsset.invFullSign);

	}

	@Override
	public void render(Canvas canvas)
	{
		
		if(!renderable)
			return;
		super.render(canvas);
		canvas.save();
		canvas.rotate(rotation,getCenterX(),getCenterY());
		canvas.drawBitmap(crusherAnim.getImage(),x,y,null);
		//if(prodTimer.getTick()>0)
		prgBar.render(canvas);

		canvas.restore();
		
		if(energy==false)
		statusImage.render(canvas,new PointF(x,y));
		if(gameManager.drawDebugInfo)
		drawDebugCollision(canvas);
		// TODO: Implement this method
	}

	@Override
	public void tick()
	{
	   	super.tick();
		if(energy)
		{
			if(!noItemsBlock)
				if(inProduction.getID()==0)
				{
					for(Inventory.InventoryItem e :inventory.getArray())
					{
						int crushableID=e.getID();
						int crushedInto= ItemsDataSheet.findById(crushableID).crushToID;
						int crushedFromCount=2;
						if(crushedInto!=0)
						{				
							if(inventory.takeOneItemFromAllInventory(crushableID,crushedFromCount))
							{
								prodTimer.setTimer(2);
								gameManager.updateInventory(this);
								inProduction.set(crushableID,2);
								break;
							}
						}
					}
				}

			if(!noInventorySpaceBlock)
				if(inProduction.getID()!=0)
				{	
					crusherAnim.tick();
					if(prodTimer.tick())
					{
						int crushableID=inProduction.getID();
						int crushedInto= ItemsDataSheet.findById(crushableID).crushToID;
						int crushedIntoCount=1;
						
						inventory.addToExistingOrNull(crushedInto,crushedIntoCount);
						inProduction.set(0,0);
						gameManager.updateInventory(this);
					}
				{	
						
				
				}
			
		}
		if(prodTimer.getTick()>0)
			prgBar.set(prodTimer.getTick()/(1.2f));
		}
		
	}




	@Override
	public void onAndOff()
	{
		super.onAndOff();
		if(!enabled&&this.powerSupplier!=null)
		{
		((Generator)this.powerSupplier).takeEnergy(this);
		this.powerSupplier=null;
		}
	}
	
	
	


	
}
