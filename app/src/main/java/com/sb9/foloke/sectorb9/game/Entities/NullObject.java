package com.sb9.foloke.sectorb9.game.Entities;
import com.sb9.foloke.sectorb9.game.Managers.*;
import android.graphics.*;

public class NullObject extends Entity
{
	public NullObject(GameManager gameManager)
	{
		super(0f,0f,0f,gameManager,0);
	}

	@Override
	public void render(Canvas canvas)
	{
		
		canvas.drawBitmap(image,x,y,null);
	}

	@Override
	public void tick()
	{
		// TODO: Implement this method
		super.tick();
	}
	
	
}
