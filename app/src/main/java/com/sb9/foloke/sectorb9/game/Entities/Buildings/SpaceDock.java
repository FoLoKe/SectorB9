package com.sb9.foloke.sectorb9.game.Entities.Buildings;
import com.sb9.foloke.sectorb9.game.Entities.*;
import com.sb9.foloke.sectorb9.game.Managers.*;
import android.graphics.*;

public class SpaceDock extends StaticEntity
{
	final static int ID=12;
	public SpaceDock(float x,float y,float rotation,GameManager gm)
	{
		super(x,y,rotation,gm,ID);
	}

	@Override
	public void render(Canvas canvas)
	{
		// TODO: Implement this method
		//super.render(canvas);
		if(!renderable)
			return;
		super.render(canvas);
		canvas.save();
		canvas.rotate(rotation,getCenterX(),getCenterY());
		canvas.drawBitmap(image,x,y,null);
		canvas.restore();
	}

	@Override
	public void tick()
	{
		// TODO: Implement this method
		super.tick();
	}
	
	
}
