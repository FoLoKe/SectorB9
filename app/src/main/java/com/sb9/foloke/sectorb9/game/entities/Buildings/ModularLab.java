package com.sb9.foloke.sectorb9.game.entities.Buildings;
import com.sb9.foloke.sectorb9.game.entities.*;
import android.graphics.*;
import com.sb9.foloke.sectorb9.game.display.*;
import com.sb9.foloke.sectorb9.game.funtions.*;
import com.sb9.foloke.sectorb9.game.entities.Buildings.Components.*;

public class ModularLab extends StaticEntity
{
	final static int ID=7;
	EntitySocket floatingReader;
	public ModularLab(float x,float y,float rotation,Game game)
	{
		super(x,y,rotation,game.buildingsData.findById(ID).image,game.buildingsData.findById(ID).name,game,ID);
		floatingReader=new EntitySocket(this,new ModularLabReaderComponent(x+8,y+8,0,"",game),90,new PointF(50,0));
		
	}

	@Override
	public void render(Canvas canvas)
	{
		canvas.drawBitmap(image,x,y,null);
		floatingReader.render(canvas,getCenterX(),getCenterY(),rotation);
		
		
		// TODO: Implement this method
	}

	@Override
	public void tick()
	{
		// TODO: Implement this method
		super.tick();
		floatingReader.tick();
	}

	
}
