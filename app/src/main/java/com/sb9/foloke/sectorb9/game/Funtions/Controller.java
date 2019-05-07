package com.sb9.foloke.sectorb9.game.Funtions;
import android.graphics.*;

public abstract class Controller
{
	public boolean shootFlag=false;
	
	public abstract void tick();
	public abstract void render(Canvas canvas);
}
