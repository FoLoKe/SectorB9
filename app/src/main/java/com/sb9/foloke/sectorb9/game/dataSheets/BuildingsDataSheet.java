package com.sb9.foloke.sectorb9.game.dataSheets;
import java.util.*;
import android.graphics.*;
import com.sb9.foloke.sectorb9.game.funtions.*;
import com.sb9.foloke.sectorb9.game.Assets.*;
import com.sb9.foloke.sectorb9.game.display.*;
import com.sb9.foloke.sectorb9.*;

public class BuildingsDataSheet
{
	//
	public static ObjectsAsset asset;
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
	public BuildingsDataSheet(Game game)
	{
		BitmapFactory.Options options=new BitmapFactory.Options();
        options.inScaled=false;
		asset.init(Bitmap.createBitmap(BitmapFactory.decodeResource(game.getResources(),R.drawable.objects_sheet,options)));
		///									NAME					ID	IMAGE								ANIMATION				OPENED	ENABLED	INVSIZE	INTERACTABLE
		objects.add(new BuildingObjectInfo("small cargo container"	,1	,asset.smallCargoContainer			,null					,true	,true	,5		,false			));
		objects.add(new BuildingObjectInfo("rocks crusher"			,2	,asset.crusher						,asset.crusherAnim		,true	,true	,3		,true			));
		objects.add(new BuildingObjectInfo("solar panel"			,3	,asset.solarPanel					,null					,false	,true	,0		,true			));
		objects.add(new BuildingObjectInfo("fuel generator"			,4	,asset.fuelGenerator				,asset.fuelGeneratorAnim,true	,false	,1		,true			));
		objects.add(new BuildingObjectInfo("big smelter"			,5	,asset.smelterCold					,asset.smelterHot		,true	,true	,1		,true			));
		objects.add(new BuildingObjectInfo("assembler"				,6	,asset.assembler					,asset.assemblerAnim	,true	,true	,8		,true			));
		objects.add(new BuildingObjectInfo("lab_mk1"				,7	,asset.lab_mk1						,null					,true	,true	,10		,true			));
		
		
		}
	public BuildingObjectInfo findById(int id)
	{
		for(BuildingObjectInfo e:objects)
		{
			if(e.ID==id)
			{return e;}
		}
		return objects.get(0);
	}
	public int getLenght()
	{
		return objects.size();
	}
}
