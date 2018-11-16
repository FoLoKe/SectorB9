package com.sb9.foloke.sectorb9.game.entities;
import android.graphics.*;
import com.sb9.foloke.sectorb9.game.Assets.*;
import com.sb9.foloke.sectorb9.game.display.*;

public class SmallCargoContainer extends CargoContainer
{
	
	public SmallCargoContainer(int x,int y,float rotation,ObjectsAsset asset,String name,Game game)
	{
		super(x,y,rotation,asset.smallCargoContainer,name,4,game);
		this.renderable=true;
		this.opened=true;
		energy=true;
	}
	
	@Override
	public void render(Canvas canvas)
	{
		if(!renderable)
			return;
		// TODO: Implement this method
		canvas.drawBitmap(image,x,y,null);
	}

	@Override
	public void tick()
	{
		if(getHp()<=0)
		{
			active=false;
			return;
		}
		this.collisionBox.set(x,y,x+image.getWidth(),y+image.getHeight());
		// TODO: Implement this method
	}
}
