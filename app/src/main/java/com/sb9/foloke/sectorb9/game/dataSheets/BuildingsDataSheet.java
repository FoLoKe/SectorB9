package com.sb9.foloke.sectorb9.game.dataSheets;
import java.util.*;
import android.graphics.*;

import com.sb9.foloke.sectorb9.game.Assets.*;
import com.sb9.foloke.sectorb9.*;

public class BuildingsDataSheet
{

	private static ArrayList<BuildingObjectInfo> objects = new ArrayList<BuildingObjectInfo>();
	public class BuildingObjectInfo
	{
		public String name;
		public int ID;
		public Bitmap image;
		public Bitmap[] animation;
		
		public boolean enabledByDefault;
		public int inventoryCapacity;
		public boolean openByDefault;
		public boolean interactableByDefault;
		
		public BuildingObjectInfo(String name,int ID,Bitmap image,Bitmap[] animation,boolean opened,boolean enabled,int capacity,boolean interacable)
		{
			this.name=name;
			this.ID=ID;
			this.image=image;
			this.animation=animation;
			this.enabledByDefault=enabled;
			this.openByDefault=opened;
			this.inventoryCapacity=capacity;
			this.interactableByDefault=interacable;
		}
		
	}
	public BuildingsDataSheet(MainActivity MA)
	{
		BitmapFactory.Options options=new BitmapFactory.Options();
        options.inScaled=false;
		ObjectsAsset.init(Bitmap.createBitmap(BitmapFactory.decodeResource(MA.getResources(),R.drawable.objects_sheet,options)));
		///									NAME					ID	IMAGE								ANIMATION				OPENED	ENABLED	INVSIZE	INTERACTABLE
		objects.add(new BuildingObjectInfo("nullItem"				,0	,ObjectsAsset.smallCargoContainer			,null					,false	,false	,0		,false			));
		objects.add(new BuildingObjectInfo("small_cargo_container"	,1	,ObjectsAsset.smallCargoContainer			,null					,true	,true	,5		,false			));
		objects.add(new BuildingObjectInfo("rocks_crusher"			,2	,ObjectsAsset.crusher						,ObjectsAsset.crusherAnim		,true	,true	,3		,true			));
		objects.add(new BuildingObjectInfo("solar_panel"			,3	,ObjectsAsset.solarPanel					,null					,false	,true	,0		,true			));
		objects.add(new BuildingObjectInfo("fuel_generator"		,4	,ObjectsAsset.fuelGenerator				,ObjectsAsset.fuelGeneratorAnim,true	,false	,1		,true			));
		objects.add(new BuildingObjectInfo("big_smelter"			,5	,ObjectsAsset.smelterCold					,ObjectsAsset.smelterHot		,true	,true	,1		,true			));
		objects.add(new BuildingObjectInfo("assembler"				,6	,ObjectsAsset.assembler					,ObjectsAsset.assemblerAnim	,true	,true	,8		,true			));
		objects.add(new BuildingObjectInfo("lab_mk1"				,7	,ObjectsAsset.lab_mk1						,null					,true	,true	,10		,true			));
		objects.add(new BuildingObjectInfo("smallAsteroid"			,8	,ObjectsAsset.smallAsteroid				,null					,false	,false 	,1		,false			));
		
		}
	public static BuildingObjectInfo findById(int id)
	{
		for(BuildingObjectInfo e:objects)
		{
			if(e.ID==id)
			{return e;}
		}
		return objects.get(0);
	}
	public static int getLenght()
	{
		return objects.size();
	}
}
