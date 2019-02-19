package com.sb9.foloke.sectorb9.game.Entities.Buildings.Components;
import com.sb9.foloke.sectorb9.game.Managers.GameManager;
import com.sb9.foloke.sectorb9.game.Entities.*;

import android.graphics.*;

public abstract class ComponentEntity extends Entity
{
	public ComponentEntity(float x, float y, float rotation, Bitmap image, String name, GameManager gameManager)
	{
		super(x,y,rotation, gameManager,0);
		this.image=image;
		this.name=name;
	}
	public abstract void render(Canvas canvas,float x,float y,float rotation);
	
}
