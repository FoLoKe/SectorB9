package com.sb9.foloke.sectorb9.game.Entities.Buildings;
import com.sb9.foloke.sectorb9.game.Managers.GameManager;
import com.sb9.foloke.sectorb9.game.UI.*;
import com.sb9.foloke.sectorb9.game.Assets.*;
import com.sb9.foloke.sectorb9.game.DataSheets.BuildingsDataSheet;
import com.sb9.foloke.sectorb9.game.DataSheets.ItemsDataSheet;
import com.sb9.foloke.sectorb9.game.Funtions.*;
import android.graphics.*;
import com.sb9.foloke.sectorb9.game.Entities.*;
import com.sb9.foloke.sectorb9.game.UI.Inventory.*;

public class BigSmelter extends StaticEntity
{
	private final static int ID=5;
	private ProgressBarUI prgBar;
	private CustomImageUI statusImageNoEnergy;
	private CustomImageUI statusImageEnebled;
	private int prodTimeLength=10;
	private Inventory.InventoryItem inProduction;
	private Timer prodTimer;


	private Bitmap smelterInWorkBitmap;

	

	public BigSmelter(float x, float y, float rotation, GameManager gameManager)
	{
		super(x,y,rotation, gameManager,ID);
		smelterInWorkBitmap= BuildingsDataSheet.findById(ID).animation[0];
		this.inventoryMaxCapacity=3;
		this.opened=true;
		inProduction=new Inventory.InventoryItem(0,0,0,0);
		prodTimer=new Timer(0);
		prgBar=new ProgressBarUI(this,50,8,-25,-20,UIAsset.stunBackground,UIAsset.stunLine,UIAsset.progressBarBorder,prodTimer.getTick());





		statusImageNoEnergy=new CustomImageUI(UIAsset.noEnergySign);
		statusImageEnebled=new CustomImageUI(UIAsset.invFullSign);

	}

	@Override
	public void render(Canvas canvas)
	{
		
		if(!renderable)
			return;
		super.render(canvas);
		canvas.save();
		canvas.rotate(rotation,getCenterX(),getCenterY());
		if(inProduction.getID()==0)
		canvas.drawBitmap(image,x,y,null);
		else
		canvas.drawBitmap(smelterInWorkBitmap,x,y,null);
		prgBar.render(canvas);

		canvas.restore();

		if(!energy)
			statusImageNoEnergy.render(canvas,new PointF(x,y));
        if(!enabled)
        	statusImageEnebled.render(canvas,new PointF(x+16,y));
		
	}

	@Override
	public void tick()
	{
	    super.tick();
		if(energy)
		{
			///make flag on inventory empty
			if(inProduction.getID()==0)
			{
				for(Inventory.InventoryItem e: inventory.getArray())
				{
					if(ItemsDataSheet.findById(e.getID()).smeltToID!=0)
					{
						int toProdID=e.getID();
						int toProdCount=2;
						if(inventory.takeOneItemFromAllInventory(toProdID,toProdCount))
						{
							inProduction.set(toProdID,toProdCount);
							prodTimer.setTimer(prodTimeLength);
							gameManager.updateInventory(this);
							break;
						}
					}
				}

			}
			//flag on inventory full
			if(inProduction.getID()!=0)
			{
				if(prodTimer.tick())
				{
					if(inventory.addToExistingOrNull(ItemsDataSheet.findById(inProduction.getID()).smeltToID,1))
					{
						gameManager.updateInventory(this);
					inProduction.set(0,0);}
				}
			}
			if(prodTimer.getTick()>0)
				prgBar.set(prodTimer.getTick()/(prodTimeLength*0.6f));
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
