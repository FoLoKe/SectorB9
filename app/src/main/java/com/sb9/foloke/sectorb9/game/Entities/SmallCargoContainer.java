package com.sb9.foloke.sectorb9.game.Entities;
import android.graphics.*;

import com.sb9.foloke.sectorb9.game.Managers.GameManager;
import com.sb9.foloke.sectorb9.game.DataSheets.ObjectsDataSheet;

public class SmallCargoContainer extends CargoContainer
{
	final static int ID=1;
	
	public SmallCargoContainer(float x, float y, float rotation, GameManager gameManager)
	{
		
		super(x,y,rotation, gameManager,ID);

	}
	
	@Override
	public void render(Canvas canvas)
	{
		
		if(!renderable)
			return;
		super.render(canvas);
		canvas.drawBitmap(image,x,y,null);
		
	}

	@Override
	public void tick()
	{
		super.tick();
		


	}
}
