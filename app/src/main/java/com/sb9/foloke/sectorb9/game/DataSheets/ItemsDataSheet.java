package com.sb9.foloke.sectorb9.game.DataSheets;
import com.sb9.foloke.sectorb9.game.Assets.*;
import java.util.*;
import android.graphics.*;

import com.sb9.foloke.sectorb9.*;

public class ItemsDataSheet
{
	private static ArrayList<ItemInfo> objects = new ArrayList<>();
	
	/*public static class MadeFrom
	{
		public int item;
		public int count;
		public MadeFrom(int items,int count)
		{
			this.item=items;
			this.count=count;
		}
	}*/
	public static class ItemInfo
	{
		public String name;
		public int ID;
		public Bitmap image;
		public int crushToID;
		public int smeltToID;
		public int crushFromID;
		public int smeltFromID;
		public Map<Integer,Integer> madeFrom;
		public String info;

		ItemInfo(String name,int ID,Bitmap image,int[] items,int[] counts,int crushToID,int SmeltToID,int crushFromID,int smeltFromID,String info)
		{
			this.name=name;
			this.ID=ID;
			this.image=image;
			this.crushToID=crushToID;
			this.smeltToID=SmeltToID;
			this.smeltFromID=smeltFromID;
			this.crushFromID=crushFromID;
			this.info=info;
			madeFrom=new HashMap<Integer,Integer>();
			for(int i=0;i<items.length;i++)
			{
				madeFrom.put(items[i],counts[i]);
			}
		}

	}
	public static void init(MainActivity MA)
	{
		BitmapFactory.Options options=new BitmapFactory.Options();
        options.inScaled=false;
		InventoryAsset.init(Bitmap.createBitmap(BitmapFactory.decodeResource(MA.getResources(),R.drawable.ui_inv_sheet_v2,options)));
		///						 NAME				 	ID	IMAGE						            made from items				items count					crushed into		smeltedinto	crushedFrom smeltedFrom info
		objects.add(new ItemInfo("null item"			,0	,InventoryAsset.inv_empty			    ,new int[]{0}				,new int[]{0}				,0					,0			,0			,0			,"error please contact developer"));
		objects.add(new ItemInfo("iron ore"				,1	,InventoryAsset.inv_item_fe			    ,new int[]{0}				,new int[]{0}				,4					,0			,0			,0			,"can be mined on asteroids by drilling them or destroying"));
		objects.add(new ItemInfo("gold ore"				,2	,InventoryAsset.inv_item_au			    ,new int[]{0}				,new int[]{0}				,5					,0			,0			,0			,"can be mined on asteroids by drilling them or destroying"));
		objects.add(new ItemInfo("silicon ore"			,3	,InventoryAsset.inv_item_si			    ,new int[]{0}				,new int[]{0}				,6					,0			,0			,0			,"can be mined on asteroids by drilling them or destroying"));
		objects.add(new ItemInfo("iron dust"			,4	,InventoryAsset.inv_item_fe_dust		,new int[]{0}				,new int[]{0}				,0					,7			,1			,0			,""));
		objects.add(new ItemInfo("gold dust"			,5	,InventoryAsset.inv_item_au_dust		,new int[]{0}				,new int[]{0}				,0					,8			,2			,0			,""));
		objects.add(new ItemInfo("monosilan dust"		,6	,InventoryAsset.inv_item_si_dust		,new int[]{0}				,new int[]{0}				,0  				,9			,3			,0			,""));
		objects.add(new ItemInfo("steel Ingot"			,7	,InventoryAsset.inv_item_steel_ingot	,new int[]{0}				,new int[]{0}				,4  				,0			,0			,4			,""));
		objects.add(new ItemInfo("gold Ingot"			,8	,InventoryAsset.inv_item_au_ingot	    ,new int[]{0}				,new int[]{0}				,5  				,0			,0			,5			,""));
		objects.add(new ItemInfo("polycrystal silicon"	,9	,InventoryAsset.inv_item_si_ingot	    ,new int[]{0}				,new int[]{0}				,6  				,0			,0			,6			,""));
		objects.add(new ItemInfo("steel wire"			,10	,InventoryAsset.inv_item_steel_wire	    ,new int[]{7}				,new int[]{1}				,4  				,0			,0			,0			,""));
		objects.add(new ItemInfo("gold wire"			,11	,InventoryAsset.inv_item_gold_wire	    ,new int[]{8}				,new int[]{1}				,5  				,0			,0			,0			,""));
		objects.add(new ItemInfo("silicon plate"		,12	,InventoryAsset.inv_item_si_plate	    ,new int[]{9}				,new int[]{2}				,6  				,0			,0			,0			,""));
		objects.add(new ItemInfo("processor"			,13	,InventoryAsset.inv_item_proc		    ,new int[]{10,11,12}		,new int[]{1,2,1}			,5  				,0			,0			,0			,""));
		objects.add(new ItemInfo("steel plate"			,14	,InventoryAsset.inv_item_steel_plate	,new int[]{7}				,new int[]{1}				,4  				,7			,0			,0			,""));
		objects.add(new ItemInfo("steel girder"			,15	,InventoryAsset.inv_item_girder		    ,new int[]{14}				,new int[]{2}				,4  				,7			,0			,0			,""));
		objects.add(new ItemInfo("steel bar"			,16	,InventoryAsset.inv_item_steel_ingot	,new int[]{14}				,new int[]{1}				,4  				,7			,0			,0			,""));
		objects.add(new ItemInfo("integrated circuit"	,17	,InventoryAsset.inv_item_circuit		,new int[]{11,12}			,new int[]{3,1}				,5  				,0			,0			,0			,""));
		objects.add(new ItemInfo("8.3mm ammo"			,18	,InventoryAsset.inv_empty				,new int[]{14}				,new int[]{1}				,5  				,0			,0			,0			,""));
	}

	public static ItemInfo findById(int id)
	{
		for(ItemInfo e:objects)
		{
			if(e.ID==id)
			{return e;}
		}
		return objects.get(0);
	}
	public static int getLength()
	{
		return objects.size();
	}
}
