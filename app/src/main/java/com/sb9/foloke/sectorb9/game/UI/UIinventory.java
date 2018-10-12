package com.sb9.foloke.sectorb9.game.UI;
import com.sb9.foloke.sectorb9.game.Assets.*;
import android.graphics.*;
//import java.util.*;
public class UIinventory
{
	private InventoryAsset asset;
	//private HashMap<int,int> inv_indexes; //item_index //count
	private int inv_items[][]; //index,count
	private boolean visible;
	
	public UIinventory(InventoryAsset inventoryAsset,int inv_items[][])
	{
		//this.asset=inventoryAsset;
		//this.inv_items=inv_items;
		inv_items=new int [2][2];
	}
	public void render(Canvas canvas)
	{
		if(visible)
		{
			canvas.save();
			float scale=15;
			canvas.scale(scale,scale);
			float H=canvas.getHeight();
			float W=canvas.getWidth();
			float imagesize=64;
			float leftOffset=0;
			float topOffset=0;
			if(W/2>imagesize*scale)
				leftOffset=(W/2-imagesize*scale)/(2*scale);
			if(H>imagesize*scale)
				topOffset=(H-imagesize*scale)/(2*scale);
		canvas.drawBitmap(asset.inv_background,leftOffset,topOffset,null);
		canvas.restore();
		canvas.scale(scale-5,scale-5);
		canvas.drawBitmap(asset.inv_steel_ingot,leftOffset+64/(5),topOffset+64,null);
		canvas.restore();
			}
	}
	public void setVisability(boolean visability)
	{
		this.visible=visability;
	}
	
}
