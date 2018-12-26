package com.sb9.foloke.sectorb9.game.entities;
import android.graphics.*;
import java.util.*;
import com.sb9.foloke.sectorb9.game.display.*;

public abstract class CargoContainer extends StaticEntity
{
	public CargoContainer(float x, float y,float rotation, Bitmap image,String name,int capacity,Game game,int ID)
	{
		super(x,y,rotation,image,name,game,ID);
		inventoryMaxCapacity=capacity;
	}


	
}
