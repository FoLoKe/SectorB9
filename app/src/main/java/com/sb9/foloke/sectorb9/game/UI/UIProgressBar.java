package com.sb9.foloke.sectorb9.game.UI;
import android.graphics.*;
import com.sb9.foloke.sectorb9.game.entities.Entity;

public class UIProgressBar
{
	private Bitmap background,fill;
	private Entity target;
	private float sizeX,sizeY,percent,offsetX,offsetY;

	public UIProgressBar(Entity target, float sizeX, float sizeY,float offsetX ,float offsetY,Bitmap background,Bitmap fill, float percent)
	{
		this.background=background;
		this.fill=fill;
		this.sizeX=sizeX;
		this.sizeY=sizeY;
		this.target=target;
		this.percent=percent;
		this.offsetX=offsetX;
		this.offsetY=offsetY;
		int tSizeX=(int)(sizeX*percent/100);
		if (tSizeX==0)
			tSizeX=1;
        this.background = Bitmap.createScaledBitmap(background, (int)sizeX, (int)sizeY, false);
		this.fill = Bitmap.createScaledBitmap(fill, tSizeX, (int)(sizeY), false);
	}
	public void render(Canvas canvas)
	{
		int tSizeX=(int)(sizeX*percent/100);
		if (tSizeX<=0)
			return;
		canvas.drawBitmap(background,target.getCenterX()+offsetX,target.getY()+offsetY,null);
        canvas.drawBitmap(fill,target.getCenterX()+offsetX,target.getY()+offsetY,null);
	}
	public void tick(float percent)
	{
		this.percent=percent;
		int tSizeX=(int)(sizeX*percent/100);
		if (tSizeX<=0)
			return;
		this.fill = Bitmap.createScaledBitmap(fill, tSizeX, (int)(sizeY), false);
	}
}
