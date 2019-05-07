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

        this.background = background;
		this.fill = fill;
		///int tY=(int)(sizeY*(2/12));
		this.border=border;
	}

	public void render(Canvas canvas)
	{
		if(active)
		{
		    if(target.getClass() == GameManager.class)
		    {
			    canvas.save();
			    canvas.scale(1,1);
			    canvas.drawBitmap(background,0+offsetX,0+offsetY,null);
			    canvas.drawBitmap(fill,new Rect(0,0,(int)sizeX,(int)sizeY),new RectF(0+offsetX,0+offsetY,0+offsetX+(sizeX)*percent/100,0+offsetY+(int)sizeY),null);
			    //canvas.drawBitmap(border,10+offsetX,offsetY,null);
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
                canvas.drawBitmap(fill,new Rect(0,0,(int)sizeX,(int)sizeY),new RectF(x+offsetX,y+offsetY,x+offsetX+(sizeX)*percent/100,y+offsetY+sizeY),null);
		    }
		}
	}
	public void setWorldLocation(float x,float y)
	{
		this.x=x;
		this.y=y;
	}
	public void tick(float percent)
    {
        this.percent=percent;

        if (this.percent<=0)
            this.percent=0;
    }

	public void setActive(boolean condition)
	{
		active=condition;
	}
}
