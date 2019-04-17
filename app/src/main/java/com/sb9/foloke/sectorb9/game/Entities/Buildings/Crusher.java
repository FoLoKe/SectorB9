package com.sb9.foloke.sectorb9.game.Entities.Buildings;
import com.sb9.foloke.sectorb9.game.Managers.GameManager;
import com.sb9.foloke.sectorb9.game.DataSheets.ObjectsDataSheet;
import com.sb9.foloke.sectorb9.game.DataSheets.ItemsDataSheet;
import com.sb9.foloke.sectorb9.game.Entities.*;
import android.graphics.*;

import com.sb9.foloke.sectorb9.game.Funtions.*;
import com.sb9.foloke.sectorb9.game.UI.*;
import com.sb9.foloke.sectorb9.game.Assets.*;
import com.sb9.foloke.sectorb9.game.UI.Inventory.*;
import java.util.ArrayList;
import com.sb9.foloke.sectorb9.game.UI.Inventory.Inventory.*;

public class Crusher extends ProductionBuilding
{
	private final static int ID=2;
	private Animation crusherAnim;
	
	public Crusher(float x, float y, float rotation, GameManager gameManager)
	{
		super(x,y,rotation, gameManager,ID);
		crusherAnim=new Animation(ObjectsDataSheet.findById(ID).animation,15);
		this.inventoryMaxCapacity=3;
		this.opened=true;
	}

	@Override
	public void render(Canvas canvas)
	{
		if(!renderable||!active)
			return;
			
		super.render(canvas);
		canvas.save();
		canvas.rotate(rotation,getCenterX(),getCenterY());
		canvas.drawBitmap(crusherAnim.getImage(),x,y,null);
		canvas.restore();
	}

	@Override
	public void tick()
	{
		if(!active)
			return;
			
	   	super.tick();
		production();	
		
		if(inProgress)
			crusherAnim.tick();
	}

	@Override
	protected int chooseItemToProduce(ArrayList<Inventory.InventoryItem> items)
	{
		// TODO: Implement this method
		int tItemToProduce=-1;
		for(Inventory.InventoryItem e :inventory.getArray())
		{
			int crushableID=e.getID();
			int crushedInto= ItemsDataSheet.findById(crushableID).crushToID;
			int crushedFromCount=2;
			if(crushedInto!=0)
			{			
				items.add(new Inventory.InventoryItem(0,0,crushableID,crushedFromCount));
				tItemToProduce=crushedInto;
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
