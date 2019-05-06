package com.sb9.foloke.sectorb9.game.DataSheets;

import java.util.*;
import android.graphics.*;
import com.sb9.foloke.sectorb9.game.Assets.*;
import com.sb9.foloke.sectorb9.*;

public class ObjectsDataSheet
{
    private static ArrayList<BuildingObjectInfo> objects = new ArrayList<>();

	public static class BuildingObjectInfo
	{
		public String name;
		public int ID;
		public Bitmap image;
		public Bitmap[] animation;
		public int[] resToBuild;
		public int[] countToBuild;
		
		public boolean enabledByDefault;
		public int inventoryCapacity;
		public boolean openByDefault;
		public boolean interactableByDefault;
		public boolean collidable;
		public boolean buildable;
		
		BuildingObjectInfo(String name,int ID,Bitmap image,Bitmap[] animation,boolean opened,boolean enabled,int capacity,boolean interacable,boolean collidable,boolean buildable,int[] res,int[] count)
		{
			this.name=name;
			this.ID=ID;
			this.image=image;
			this.animation=animation;
			this.enabledByDefault=enabled;
			this.openByDefault=opened;
			this.inventoryCapacity=capacity;
			this.interactableByDefault=interacable;
			this.collidable=collidable;
			this.resToBuild=res;
			this.countToBuild=count;
			this.buildable=buildable;
		}
		
	}

	public static void init(MainActivity MA)
	{
		objects.clear();
		BitmapFactory.Options options=new BitmapFactory.Options();
        options.inScaled=false;
		ObjectsAsset.init(Bitmap.createBitmap(BitmapFactory.decodeResource(MA.getResources(),R.drawable.objects_sheet,options)));
		///									NAME					ID	IMAGE								            ANIMATION				OPENED	ENABLED	INVSIZE	INTERACTABLE	COLLIDABLE	BUILDABLE	RES TO BUILD	COUNT OF RES
		objects.add(new BuildingObjectInfo("nullItem"				,0	,ObjectsAsset.nullItem					,null							,false	,false	,0		,false			,true		,false		,new int[]{}	,new int[]{}));
		objects.add(new BuildingObjectInfo("small_cargo_container"	,1	,ObjectsAsset.smallCargoContainer		,null							,true	,true	,5		,false			,true		,true		,new int[]{12}	,new int[]{1}));
		objects.add(new BuildingObjectInfo("rocks_crusher"			,2	,ObjectsAsset.crusher					,ObjectsAsset.crusherAnim      	,true	,true	,3		,true			,true		,true		,new int[]{12}	,new int[]{1}));
		objects.add(new BuildingObjectInfo("solar_panel"			,3	,ObjectsAsset.solarPanel				,null							,false	,true	,0		,true			,true		,true		,new int[]{12}	,new int[]{1}));
		objects.add(new BuildingObjectInfo("fuel_generator"			,4	,ObjectsAsset.fuelGenerator				,ObjectsAsset.fuelGeneratorAnim	,true	,false	,1		,true			,true		,true		,new int[]{12}	,new int[]{1}));
		objects.add(new BuildingObjectInfo("big_smelter"			,5	,ObjectsAsset.smelterCold				,ObjectsAsset.smelterHot		,true	,true	,2		,true			,true		,true		,new int[]{12}	,new int[]{1}));
		objects.add(new BuildingObjectInfo("assembler"				,6	,ObjectsAsset.assembler					,ObjectsAsset.assemblerAnim		,true	,true	,8		,true			,true		,true		,new int[]{12}	,new int[]{1}));
		objects.add(new BuildingObjectInfo("lab_mk1"				,7	,ObjectsAsset.lab_mk1					,null							,true	,true	,10		,true			,true		,true		,new int[]{12}	,new int[]{1}));
		objects.add(new BuildingObjectInfo("smallAsteroid"			,8	,ObjectsAsset.smallAsteroid				,null							,false	,false 	,1		,false			,true		,false		,new int[]{12}	,new int[]{1}));
		objects.add(new BuildingObjectInfo("droppedItem"			,9	,ObjectsAsset.asteroidDust_1			,null							,true	,false	,1		,true			,false		,false		,new int[]{}	,new int[]{}));
		objects.add(new BuildingObjectInfo("ship"					,10	,ShipAsset.player_mk1					,null							,true	,true	,1		,true			,true		,true		,new int[]{12}	,new int[]{1}));
		objects.add(new BuildingObjectInfo("buildable"				,11	,ObjectsAsset.asteroidDust_2			,null							,false	,false	,0		,false			,true		,false		,new int[]{}	,new int[]{}));
		objects.add(new BuildingObjectInfo("basicSpaceDock"			,12	,ObjectsAsset.smallShipDock				,null							,true	,true	,8		,true			,true		,true		,new int[]{11}	,new int[]{10}));
		
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

	public static int getLength()
	{
		return objects.size();
	}
}
