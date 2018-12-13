package com.sb9.foloke.sectorb9.game.UI.Inventory;
import com.sb9.foloke.sectorb9.game.display.*;
import com.sb9.foloke.sectorb9.game.entities.*;

public class InventoryExchangeInterface
{
	Game game;
	int itemCount,itemID;
	Entity itemHolder,itemCatcher;
	public InventoryExchangeInterface(Game game)
	{
		this.game=game;
	}
	public void started(Entity itemHolder,int itemCount,int itemID)
	{
		this.itemHolder=itemHolder;
		this.itemCount=itemCount;
		this.itemID=itemID;
	}
	
	public void ended(Entity itemCatcher)
	{
		if(itemCatcher!=null&&itemCatcher!=itemHolder)
		{
			itemHolder.getInventory().remove(itemID);
			if(itemCatcher.getInventory().containsKey(itemID))
			{
				itemCatcher.getInventory().put(itemID,itemCatcher.getInventory().get(itemID)+itemCount);
			}
			else
			{
				itemCatcher.getInventory().put(itemID,itemCount);
			}
		}
		itemHolder=null;
		itemID=0;
		itemCount=0;
		game.initObjInventory();
		game.initPlayerInventory();
		
	}
	
	public Entity getItemHolder()
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
