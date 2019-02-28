package com.sb9.foloke.sectorb9.game.UI;
import android.graphics.*;

import com.sb9.foloke.sectorb9.game.Managers.GameManager;
import com.sb9.foloke.sectorb9.game.Entities.Entity;

public class ProgressBarUI
{
	private Bitmap background,fill,border;
	private Object target;
	private float sizeX,sizeY,percent,offsetX,offsetY,x,y;
	private boolean active=true;

	public ProgressBarUI(Object target, float sizeX, float sizeY, float offsetX , float offsetY, Bitmap background, Bitmap fill, Bitmap border, float percent)
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
		///int tY=(int)(sizeY*(2/12));
		this.border=Bitmap.createScaledBitmap(border, 5, (int)(sizeY), false);
	}
	public void render(Canvas canvas)
	{
		if(active)
		{
		if(target.getClass() == GameManager.class)
		{
			//((Game)target).
			canvas.save();
			canvas.scale(1,1);
			canvas.drawBitmap(background,0+offsetX,0+offsetY,null);
			canvas.drawBitmap(fill,0+offsetX,0+offsetY,null);
			canvas.drawBitmap(border,10+offsetX,offsetY,null);
			canvas.restore();
			return;
		}
		else
		{
			if(target instanceof Entity)
			{
				x=((Entity)target).getCenterX();
				y=((Entity)target).getCenterY();
			}
		int tSizeX=(int)(sizeX*percent/100);
		if (tSizeX<=0)
			return;
		canvas.drawBitmap(background,x+offsetX,y+offsetY,null);
        canvas.drawBitmap(fill,x+offsetX,y+offsetY,null);
		}
		}
	}
	public void setWorldLocation(float x,float y)
	{
		this.x=x;
		this.y=y;
	}
	public void set(float percent)
	{
		this.percent=percent;
		int tSizeX=(int)(sizeX*percent/100);
		if (tSizeX<=0)
			return;
		this.fill = Bitmap.createScaledBitmap(fill, tSizeX, (int)(sizeY), false);
	}
	public void setActive(boolean condition)
	{
		active=condition;
	}
}
