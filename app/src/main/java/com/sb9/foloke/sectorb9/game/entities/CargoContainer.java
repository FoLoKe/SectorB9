package com.sb9.foloke.sectorb9.game.Entities;
import android.graphics.*;

import com.sb9.foloke.sectorb9.game.Managers.GameManager;

public abstract class CargoContainer extends StaticEntity
{
	public CargoContainer(float x, float y, float rotation, Bitmap image, String name, int capacity, GameManager gameManager, int ID)
	{
		super(x,y,rotation,image,name, gameManager,ID);
		inventoryMaxCapacity=capacity;
	}


	
}
