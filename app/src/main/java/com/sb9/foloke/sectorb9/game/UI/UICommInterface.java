package com.sb9.foloke.sectorb9.game.UI;
import com.sb9.foloke.sectorb9.game.entities.Entity;
import android.view.ViewDebug.*;
import com.sb9.foloke.sectorb9.game.display.*;
public class UICommInterface
{
	public static boolean flagStartedExchange;
	public static Entity ExchangeFrom;
	public static Entity ExchangeTo;
	public static int itemID,itemCount,targetItemId;
	public static Game game;
	public static void init(Game gameScr)
	{
		flagStartedExchange=false;
		game=gameScr;
	}
	public static boolean exchange()
	{
		if((ExchangeFrom==null)||(ExchangeTo==null))
			return false;
			if(ExchangeTo.getInventory().containsKey(itemID))
			{
				ExchangeTo.getInventory().put(itemID,ExchangeTo.getInventory().get(itemID)+itemCount);
				//ExchangeTo.getInventory().replace(itemID,ExchangeTo.getInventory().get(itemID)+itemCount);
				ExchangeFrom.getInventory().remove(itemID);
				flagStartedExchange=false;
				ExchangeFrom=null;
				ExchangeTo=null;
				return true;
			}
			if(ExchangeTo.getInventory().size()<ExchangeTo.getInventoryMaxCapacity())
			{
				ExchangeTo.getInventory().put(itemID,itemCount);
				ExchangeFrom.getInventory().remove(itemID);
				flagStartedExchange=false;
				ExchangeFrom=null;
				ExchangeTo=null;
				return true;
			}
		flagStartedExchange=false;
		ExchangeFrom=null;
		ExchangeTo=null;
		return false;
	}
	public static boolean setSide(Entity target,int initemID,int initemCount)
	{
		if(target==ExchangeFrom)
		{
			ExchangeFrom=null;
			ExchangeTo=null;
			flagStartedExchange=false;
			return false;
		}
		if(flagStartedExchange)
		{
			ExchangeTo=target;
			targetItemId=initemID;
			return exchange();
		}
		else
		{
			ExchangeFrom=target;
			itemID=initemID;
			itemCount=initemCount;
			flagStartedExchange=true;
			return true;
		}
		//return false;
	}
	public static Entity getExchangeFrom()
	{
		return ExchangeFrom;
	}
	public static Entity getExchangeTo()
	{
		return ExchangeTo;
	}
}
