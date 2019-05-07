package com.sb9.foloke.sectorb9.game.Display;

import android.graphics.PointF;

import com.sb9.foloke.sectorb9.game.Entities.Entity;


import android.graphics.*;
import com.sb9.foloke.sectorb9.game.Funtions.*;

public class Camera {
    private PointF location;
    
    private float screenXcenter,screenYcenter;
    private float scale;
	private RectF screenRect;
	private float canvasW,canvasH;
    public Camera(float x,float y,float scale)
    {
        
        this.location=new PointF(x,y);
        this.scale=scale;
		this.screenRect=new RectF(0,0,1,1);
    }

    public void tick(float scale,PointF point)
    {
        this.scale=scale;
        this.location.x= point.x;
		this.location.y= point.y;	
    }
	
	public void render(Canvas canvas)
	{
		if(Options.drawDebugInfo.getBoolean())
		{
			
			screenRect.set(location.x-canvasW/(2*scale)+10,location.y-canvasH/(2*scale)+10,location.x+canvasW/(2*scale)-10,location.y+canvasH/(2*scale)-10);
			Paint tPaint=new Paint();
			tPaint.setStyle(Paint.Style.STROKE);
			tPaint.setColor(Color.rgb(0,255,0));
		
			canvas.drawRect(screenRect,tPaint);
			canvas.drawCircle(location.x,location.y,canvas.getHeight()/(8*scale),tPaint);
			}
	}
    public float getxOffset()
    {
        return location.x;
    }

    public float getyOffset()
    {
        return location.y;
    }

    

    public void setScreenXcenter(float screenXcenter) {
        this.screenXcenter = screenXcenter;
    }

    public void setScreenYcenter(float screenYcenter)
    {
        this.screenYcenter=screenYcenter;
    }

    public float getScale()
    {
        return scale;
    }

    public float getScreenXcenter() {
        return screenXcenter;
    }

    public float getScreenYcenter() {
        return screenYcenter;
    }

    public PointF getWorldLocation()
    {
        return location;
    }
	public void setScreenRect(float screenW,float screenH)
	{
		canvasH=screenH;
		canvasW=screenW;
		
		screenRect.set(location.x-screenW/(2*scale),location.y-screenH/(2*scale),location.x+screenW/(2*scale),location.y+screenH/(2*scale));
	}
	public RectF getScreenRect()
	{
		return screenRect;
	}
}
