package com.sb9.foloke.sectorb9.game.entities;

import android.graphics.Bitmap;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;

public abstract class Entity {
    protected float x,y;
    protected RectF collisionBox;
    protected int Height,Width;
    protected Bitmap image;
	protected boolean renderable;
	private int frameTimer;


    public Entity(float x,float y,Bitmap image)
    {
        this.x=x;
        this.y=y;
        this.collisionBox=new RectF(x,y,x+image.getWidth(),y+image.getHeight());
        this.image=image;
		this.renderable=false;
    }
    abstract public void render(Canvas canvas);
    abstract public void tick();

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void drawDebugBox(Canvas canvas)
    {
		if(!renderable)
			return;
        Paint temppaint=new Paint();
        temppaint.setColor(Color.rgb(0,255,0));
        temppaint.setStyle(Paint.Style.STROKE);
        canvas.drawRect(collisionBox,temppaint);
    }

    public PointF getWorldLocation()
    {
        return new PointF(x,y);
    }
    public void setWorldLocation(PointF location)
    {
        x=location.x;
        y=location.y;
    }
	public float getCenterX()
    {
        return x+image.getWidth()/2;
    }
    public float getCenterY()
    {
        return y+image.getHeight()/2;
    }
	public RectF getCollsionBox()
	{
		return collisionBox;
	}
	public void setRenderable(boolean renderable)
	{
		this.renderable=renderable;
	}
    public void setTimer(int delay)
	{
		frameTimer=delay*60;
	}
	public void timerTick()
	{
		if((frameTimer-=1)<0)
			frameTimer=0;
			
	}
	public int getTimer()
	{
		return frameTimer;
	}
}
