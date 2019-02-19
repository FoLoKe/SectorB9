package com.sb9.foloke.sectorb9.game.Entities.Buildings;
import com.sb9.foloke.sectorb9.game.Managers.GameManager;
import com.sb9.foloke.sectorb9.game.DataSheets.BuildingsDataSheet;
import com.sb9.foloke.sectorb9.game.Entities.*;
import android.graphics.*;

import com.sb9.foloke.sectorb9.game.Funtions.*;
import com.sb9.foloke.sectorb9.game.Entities.Buildings.Components.*;

public class ModularLab extends StaticEntity
{
	final static int ID=7;
	EntitySocket floatingReader;
	public ModularLab(float x, float y, float rotation, GameManager gameManager)
	{
		super(x,y,rotation,  gameManager,ID);
		floatingReader=new EntitySocket(this,new ModularLabReaderComponent(x+8,y+8,0,"", gameManager),90,new PointF(50,0));
		
	}

	@Override
	public void render(Canvas canvas)
	{
		if(!renderable)
			return;
		super.render(canvas);
		canvas.drawBitmap(image,x,y,null);
		//floatingReader.render(canvas,getCenterX(),getCenterY(),rotation);

        if(gameManager.drawDebugInfo)
            drawDebugCollision(canvas);
	}

	@Override
	public void tick()
	{

		super.tick();
		floatingReader.tick();

	}

	
}
