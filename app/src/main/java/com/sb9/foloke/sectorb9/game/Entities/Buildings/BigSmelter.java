package com.sb9.foloke.sectorb9.game.Entities.Buildings;
import com.sb9.foloke.sectorb9.game.Managers.GameManager;
import com.sb9.foloke.sectorb9.game.UI.*;
import com.sb9.foloke.sectorb9.game.Assets.*;
import com.sb9.foloke.sectorb9.game.DataSheets.ObjectsDataSheet;
import com.sb9.foloke.sectorb9.game.DataSheets.ItemsDataSheet;
import com.sb9.foloke.sectorb9.game.Funtions.*;
import android.graphics.*;
import com.sb9.foloke.sectorb9.game.Entities.*;
import com.sb9.foloke.sectorb9.game.UI.Inventory.*;
import java.util.*;
import com.sb9.foloke.sectorb9.game.UI.Inventory.Inventory.*;

public class BigSmelter extends ProductionBuilding
{
	private final static int ID=5;
	private Bitmap smelterInWorkBitmap;

	public BigSmelter(float x, float y, float rotation, GameManager gameManager)
	{
		super(x,y,rotation, gameManager,ID);
		smelterInWorkBitmap= ObjectsDataSheet.findById(ID).animation[0];
		this.inventoryMaxCapacity=3;
		this.opened=true;
	}

	@Override
	protected int chooseItemToProduce(ArrayList<Inventory.InventoryItem> items)
	{
		int tItemToProduce=-1;
		for(Inventory.InventoryItem e: inventory.getArray())
		{
			int smeltTo=ItemsDataSheet.findById(e.getID()).smeltToID;
			if(smeltTo!=0)
			{
				int toProdID=e.getID();
				int toProdCount=2;
				tItemToProduce=smeltTo;
				items.add(new Inventory.InventoryItem(0,0,toProdID,toProdCount));	
				break;	
			}
		}
		return tItemToProduce;
	}

	@Override
	protected void onProductionEnded()
	{
		// TODO: Implement this method	
	}

	@Override
	public void render(Canvas canvas)
	{
		if(!renderable)
			return;
			
		super.render(canvas);
		canvas.save();
		canvas.rotate(rotation,getCenterX(),getCenterY());
		
		if(inProgress)
			canvas.drawBitmap(smelterInWorkBitmap,x,y,null);
		else
			canvas.drawBitmap(image,x,y,null);
		
		canvas.restore();	
	}

	@Override
	public void tick()
	{
		if(!active)
			return;
	    super.tick();
		production();
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
