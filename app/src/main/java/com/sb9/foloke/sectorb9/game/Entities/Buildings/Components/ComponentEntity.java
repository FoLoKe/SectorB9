package com.sb9.foloke.sectorb9.game.entities.Buildings.Components;
import com.sb9.foloke.sectorb9.game.Managers.GameManager;
import com.sb9.foloke.sectorb9.game.entities.*;

import android.graphics.*;

public abstract class ComponentEntity extends Entity
{
	public ComponentEntity(float x, float y, float rotation, Bitmap image, String name, GameManager gameManager)
	{
		super(x,y,rotation,image,name, gameManager,0);
	}
	public abstract void render(Canvas canvas,float x,float y,float rotation);
	
}
