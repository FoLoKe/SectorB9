package com.sb9.foloke.sectorb9.game.UI.Inventory;
import android.graphics.*;
import java.util.*;
import com.sb9.foloke.sectorb9.game.Entities.*;

public class Inventory
{
	int height=0;
	Entity parent;
	int width=4;
	ArrayList<InventoryItem> items=new ArrayList<InventoryItem>();
	public static class InventoryItem
	{
		int pos,row; ///from 0,0 left up
		
		int ID=0,count=0;
		Color backgroundColor;
		
		public InventoryItem(int x,int y,int ID,int count)
		{
			pos=x;
			row=y;
			this.ID=ID;
			this.count=count;
		}
		public void set(int ID,int count)
		{
			this.ID=ID;
			this.count=count;
		}
		public void add(int ID,int count)
		{
			this.ID=ID;
			this.count+=count;
		}
		public void clear()
		{
			ID=0;
			count=0;
		}
		public int getID(){return ID;}
		public int getCount(){return count;}
		public void setID(int ID){this.ID=ID;}
	}

	public Inventory copy(Entity caller)
    {
        Inventory inv=new Inventory(caller,this.height,this.width);
        for(InventoryItem invI:inv.items)
                invI.set(this.getItemIdOnPos(invI.pos,invI.row),this.getItemCountOnPos(invI.pos,invI.row));
        return inv;
    }

	public Inventory(Entity parent,int height,int width)
	{
		this.parent=parent;
		this.height=height;
		this.width=width;
		for(int i=0;i<height;i++)
		for(int j=0;j<width;j++)
		{
			items.add(new InventoryItem(j,i,0,0));
		}
	}
	public int getWidth(){return width;}
	public int getHeight(){return height;}
	public boolean addNewItem(int ID,int count)
	{
		if(ID!=0)
		for(InventoryItem i: items)
		{
			if(i.ID==0)
			{
				i.ID=ID;
				i.count=count;
				return true;
			}
		}
		return false;
	}
	
	public boolean addItemToPos(int x,int y,int itemID,int itemCount)
	{
		if(itemID!=0&&itemCount!=0)
		for(InventoryItem i: items)
		{
			if((i.pos==x)&&(i.row==y))
			{
				if((i.ID==itemID)||(i.ID==0))
				{
					i.add(itemID,itemCount);
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean contains(int ID)
	{
		if(ID!=0)
		for(InventoryItem i: items)
		{
			if((i.ID==ID))
			{
				return true;
			}
		}
		return false;
	}
	public boolean contains(int ID,int count)
	{
		if (ID!=0&&count!=0)
		for(InventoryItem i: items)
		{
			if((i.ID==ID)&&(i.count>=count))
			{
				return true;
			}
		}
		return false;
	}
	public Point getPosByID(int ID)
	{
		if(ID!=0)
		for(InventoryItem i: items)
		{
			if((i.ID==ID))
			{
				return new Point(i.pos,i.row);
			}
		}
		return new Point(256,256);
	}
	public boolean takeItemFromPos(int x,int y,int count)
	{
		if(count!=0)
		for(InventoryItem i: items)
		{
			if((i.pos==x)&&(i.row==y))
			{
				if(i.count>=count)
				{
					i.count-=count;
					if(i.count==0)
						i.ID=0;
					return true;
				}
			}
		}
		return false;
	}
	public boolean takeItemByID(int ID,int count)
	{
		if(ID!=0&&count!=0)
		for(InventoryItem i: items)
		{
			if((i.ID)==ID)
			{
				if(i.count>=count)
				{
					i.count-=count;
					if(i.count==0)
						i.ID=0;
					return true;
				}
			}
		}
		return false;
	}
	public ArrayList<InventoryItem> getArray()
	{
		return items;
	}
	public Point getItemOnPos(int x,int y)
	{
		for(InventoryItem i: items)
		{
			if((i.pos==x)&&(i.row==y))
			{return new Point(i.ID,i.count);}
		}
		return new Point(0,0);
	}
	public boolean equalOnPosByID(int x,int y,int ID)
	{
		if(ID!=0)
		for(InventoryItem i: items)
		{
			if((i.pos==x)&&(i.row==y)&&(i.ID==ID))
			{return true;}
		}
		return false;
	}
	public boolean equaOrNullOnPosByID(int x,int y,int ID)
	{
		if(ID!=0)
		for(InventoryItem i: items)
		{
			if((i.pos==x)&&(i.row==y)&&((i.ID==ID)||(i.ID==0)))
			{return true;}
		}
		return false;
	}
	
	public int getItemIdOnPos(int x,int y)
	{
		for(InventoryItem i: items)
		{
			if((i.pos==x)&&(i.row==y))
			{return i.ID;}
		}
		return 0;
	
	}
	public int getItemCountOnPos(int x,int y)
	{
		for(InventoryItem i: items)
		{
			if((i.pos==x)&&(i.row==y))
			{return i.count;}
		}
		return 0;

	}
	public boolean containsOneItemInAllInventory(int ID,int count)
	{
		if (ID==0||count==0)
		return false;
		
		int inventoryCount=0;
		for(Inventory.InventoryItem e: items)
		{
			if(e.getID()==ID)
			inventoryCount+=e.getCount();
		}
		if(inventoryCount>=count)
			return true;
		return false;
	}
	public boolean takeOneItemFromAllInventory(int ID,int count)
	{
		if(ID!=0&&count!=0)
		if(containsOneItemInAllInventory(ID,count))
		{
			for(Inventory.InventoryItem e: items)
			{
				if(e.ID==ID)
				{
					if(count-e.count>=0)
					{
						count-=e.count;
						e.set(0,0);
					}
					else
					{				
						e.count-=count;
						count=0;
					}
				}
			}
			if(count==0)
				return true;
		}
		return false;
		
	}
	
	public boolean takeArrayItemFromAllInventory(ArrayList<Inventory.InventoryItem> array)
	{
		for(InventoryItem i:array)
		{
			if(!containsOneItemInAllInventory(i.ID,i.count))
			{
				return false;
			}
		}
		for(InventoryItem i:array)
		{
			if(!takeOneItemFromAllInventory(i.ID,i.count))
			{
				return false;
			}
		}
		return true;
	}
	public boolean addToExistingOrNull(int ID,int count)
	{
		if(ID!=0&&count!=0)
		for(InventoryItem i: items)
		{
			if(i.ID==ID)
			{
				i.ID=ID;
				i.count+=count;
				return true;
			}
			
		}
		return addNewItem(ID,count);
	}
	public boolean addToExistingOrNull(InventoryItem item)
	{
		if(item.ID!=0&&item.count!=0)
		for(InventoryItem i: items)
		{
			if(i.ID==item.ID)
			{
				i.ID=item.ID;
				i.count+=item.count;
				return true;
			}

		}
		return addNewItem(item.ID,item.count);
	}
	
	public float count()
	{
		float c=0;
		for(InventoryItem i: items)
		{
			if(i.ID!=0&&i.count!=0)
			{
				c++;
			}

		}
		return c;
	}
	
}
