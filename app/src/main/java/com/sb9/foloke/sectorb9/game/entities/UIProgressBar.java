package com.sb9.foloke.sectorb9.game.entities;
import android.graphics.*;

public class UIProgressBar
{
	private Bitmap background,fill;
	Entity target;
	float sizeX,sizeY,percent;
	Camera camera;
	public UIProgressBar(Entity target,float sizeX,float sizeY,Bitmap background,Bitmap fill,float percent)
	{
		this.background=background;
		this.fill=fill;
		
		this.sizeX=sizeX;
		this.sizeY=sizeY;
		this.target=target;
		this.percent=percent;
	}
	public void render(Canvas canvas)
	{
		Rect src = new Rect((int)target.getCenterX(),(int)target.getCenterY(),(int)target.getCenterX()+fill.getWidth()-1,(int)target.getCenterY()+fill.getHeight()-1);
		Rect dest = new Rect((int)target.getCenterX(),(int)target.getCenterY(),(int)target.getCenterX()+(int)sizeX-1,(int)target.getCenterY()+(int)sizeY-1);
		canvas.drawBitmap(fill,src,dest,null);
	}
	public void tick()
	{
		
	}
}
