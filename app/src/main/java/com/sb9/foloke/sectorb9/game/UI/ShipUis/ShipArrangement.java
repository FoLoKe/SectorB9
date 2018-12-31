package com.sb9.foloke.sectorb9.game.UI.ShipUis;
import com.sb9.foloke.sectorb9.*;
import android.widget.*;
import android.graphics.drawable.*;
import android.view.View.*;
import android.view.*;

import com.sb9.foloke.sectorb9.game.Assets.UIAsset;
import com.sb9.foloke.sectorb9.game.UI.*;

import com.sb9.foloke.sectorb9.game.display.*;
import com.sb9.foloke.sectorb9.game.funtions.*;
import com.sb9.foloke.sectorb9.game.entities.*;

public class ShipArrangement
{
	Game game;
	int itemCount,itemID;
	Entity itemHolder;
	private class ShipModule
	{
		int ID=0;
		int type=0;
		float bonus=1;
		int pos;
		public ShipModule(int type,int pos)
		{
			this.type=type;
			this.pos=pos;
		}
	}
	int height=10;
	int width=10;
	ShipModule elements[][]=new ShipModule[width][height];
	MainActivity MA;
	ShipUI parent;
	public ShipArrangement(MainActivity MA,ShipUI parent)
	{
		this.game=MA.getGame();
		this.MA=MA;
		this.parent=parent;
		for(int i=0;i<height;i++)
			for(int j=0;j<width;j++)
				elements[i][j]=new ShipModule(0,i*width+j);
	}
	public void init()
	{
		TableLayout TL=MA.findViewById(R.id.shipUI_ArrangementTable);
		TL.removeAllViews();
		for(int i=0;i<height;i++)
		{
			TableRow TR=new TableRow(MA);
			
			for(int j=0;j<width;j++)
			{
				ImageView sprite=new ImageView(MA);
				TableRow.LayoutParams trp=new TableRow.LayoutParams();
				trp.setMargins(10,10,10,10);
				
				BitmapDrawable bdrawable;

				bdrawable = new BitmapDrawable(MA.getResources(),UIAsset.uiBgBlur);
				sprite.setImageDrawable(bdrawable);
				sprite.setLayoutParams(trp);

				sprite.setOnTouchListener(
					new OnTouchListener() 
					{
						@Override
						public boolean onTouch(View v,MotionEvent e) 
						{

							return true;
						}
					});
				parent.setDragAndDrop(sprite,elements[i][j].ID,false);
				TR.addView(sprite);
				sprite.setId(i*width+j);
				
			}
			TL.addView(TR);
		}
	}
	
	
	public void started(Entity itemHolder,int itemCount,int itemID)
	{
		this.itemHolder=itemHolder;
		this.itemCount=itemCount;
		this.itemID=itemID;
	}

	public void ended(boolean itemCatcher,int pos)
	{
		if(itemCatcher)
		{
			
			if(getElement(pos).ID==0)
			{
				getElement(pos).ID=itemID;
				//itemHolder.getInventory().remove(itemID);
			}
		}
		itemHolder=null;
		itemID=0;
		itemCount=0;
		MA.shipUI.init();

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
	public ShipModule getElement(int pos)
	{
		return elements[(int)(pos/height)][(int)(pos%height)];
	}
}
