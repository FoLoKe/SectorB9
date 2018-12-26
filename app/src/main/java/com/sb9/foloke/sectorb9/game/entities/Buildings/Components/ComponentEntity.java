package com.sb9.foloke.sectorb9.game.entities.Buildings.Components;
import com.sb9.foloke.sectorb9.game.entities.*;
import com.sb9.foloke.sectorb9.game.display.*;
import android.graphics.*;

public abstract class ComponentEntity extends Entity
{
	public ComponentEntity(float x,float y,float rotation,Bitmap image,String name,Game game)
	{
		super(x,y,rotation,image,name,game,0);
	}
	public abstract void render(Canvas canvas,float x,float y,float rotation);
	
}
