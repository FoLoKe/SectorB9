package com.sb9.foloke.sectorb9.game.display;

import android.graphics.PointF;

import com.sb9.foloke.sectorb9.game.entities.DynamicEntity;
import com.sb9.foloke.sectorb9.game.entities.Entity;
import com.sb9.foloke.sectorb9.game.entities.Player;

import java.lang.annotation.Target;
import android.graphics.*;

public class Camera {
    private PointF location;
    private Entity pointOfLook;
    private float screenXcenter,screenYcenter;
    private float scale;
	private RectF screenRect;
	private float canvasW,canvasH;
    public Camera(float x,float y,float scale,Entity target)
    {
        this.pointOfLook=target;
        this.location=new PointF(x,y);
        this.scale=scale;
		this.screenRect=new RectF(0,0,1,1);
    }

    public void tick(float scale,float screenW,float screenH)
    {
        this.scale=scale;
        this.location.x= ((Player) pointOfLook).getCenterX();
		if((this.location.x-screenW/(2*scale))<0)
			this.location.x=screenW/(2*scale);
			
        this.location.y= ((Player) pointOfLook).getCenterY();
		if((this.location.y-screenH/(2*scale))<0)
			this.location.y=screenH/(2*scale);
			
		
    }
	
	public void render(Canvas canvas)
	{
		if(pointOfLook.getGame().drawDebugInf)
		{
			if(pointOfLook.getGame().drawDebugInf)
				screenRect.set(pointOfLook.getCenterX()-canvasW/(2*scale)+10,pointOfLook.getCenterY()-canvasH/(2*scale)+10,pointOfLook.getCenterX()+canvasW/(2*scale)-10,pointOfLook.getCenterY()+canvasH/(2*scale)-10);
			Paint tPaint=new Paint();
			tPaint.setStyle(Paint.Style.STROKE);
			tPaint.setColor(Color.rgb(0,255,0));
		
			canvas.drawRect(screenRect,tPaint);
			canvas.drawCircle(((Player)pointOfLook).getCenterX(),((Player)pointOfLook).getCenterY(),canvas.getHeight()/(8*scale),tPaint);
			canvas.drawCircle(((Player)pointOfLook).getCenterX(),((Player)pointOfLook).getCenterY(),canvas.getHeight()/(2*scale),tPaint);
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

    public void setPointOfLook(Entity pointOfLook) {
        this.pointOfLook = pointOfLook;
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
			screenRect.set(pointOfLook.getCenterX()-screenW/(2*scale),pointOfLook.getCenterY()-screenH/(2*scale),pointOfLook.getCenterX()+screenW/(2*scale),pointOfLook.getCenterY()+screenH/(2*scale));
	}
	public RectF getScreenRect()
	{
		return screenRect;
	}
}
