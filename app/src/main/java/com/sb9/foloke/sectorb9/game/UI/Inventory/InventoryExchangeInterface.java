package com.sb9.foloke.sectorb9.game.UI.Inventory;
import com.sb9.foloke.sectorb9.game.Managers.GameManager;

public class InventoryExchangeInterface
{
	GameManager gameManager;
	int itemCount,itemID,x,y;
	
	Inventory itemHolder;
	public InventoryExchangeInterface(GameManager gameManager)
	{
		this.gameManager = gameManager;
	}
	public void started(Inventory inventory,int x,int y,int count)
	{
		this.itemHolder=inventory;
		this.x=x;
		this.y=y;
		this.itemCount=count;
		this.itemID=inventory.getItemIdOnPos(x,y);
	}
	
	public void ended(Inventory itemCatcher,int cx,int cy)
	{
		if(itemCatcher!=null)
		{
			if(itemCatcher.equaOrNullOnPosByID(cx,cy,itemHolder.getItemIdOnPos(x,y)))
				if(itemHolder.takeItemFromPos(x,y,itemCount))
				{
					itemCatcher.addItemToPos(cx,cy,itemID,itemCount);
				}
		}
		itemHolder=null;
		itemID=0;
		itemCount=0;
		gameManager.updateInventory(itemCatcher.parent);
		
	}
	
	public Inventory getItemHolder()
	{
		return itemHolder;
	}
	
	public int getItemID()
	{
		return itemID;
	}
	
	public int getItemCount()
	{
		return itemCount;
	}
	
}
