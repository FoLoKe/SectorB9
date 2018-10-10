package com.sb9.foloke.sectorb9.game.UI;
import android.graphics.*;

import com.sb9.foloke.sectorb9.game.Assets.UIAsset;
import com.sb9.foloke.sectorb9.game.entities.Entity;

public class UIProgressBar
{
	private Bitmap background,fill;
	Entity target;
	float sizeX,sizeY,percent;

	public UIProgressBar(Entity target, float sizeX, float sizeY, UIAsset uiAsset, float percent)
	{
		this.background=uiAsset.hpBackground;
		this.fill=uiAsset.hpLine;
		
		this.sizeX=sizeX;
		this.sizeY=sizeY;
		this.target=target;
		this.percent=percent;
		int tSizeX=(int)(sizeX*percent/100);
		if (tSizeX==0)
			tSizeX=100;
        this.background = Bitmap.createScaledBitmap(background, (int)sizeX, (int)sizeY, false);
		this.fill = Bitmap.createScaledBitmap(fill, tSizeX, (int)(sizeY), false);
	}
	public void render(Canvas canvas)
	{
		canvas.drawBitmap(background,target.getCenterX()-sizeX/2,target.getY()-20,null);
        canvas.drawBitmap(fill,target.getCenterX()-sizeX/2,target.getY()-20,null);
	}
	public void tick()
	{
		
	}
}
