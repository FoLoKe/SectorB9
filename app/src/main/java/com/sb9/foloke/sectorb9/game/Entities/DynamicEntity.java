package com.sb9.foloke.sectorb9.game.Entities;

import android.graphics.Bitmap;
import android.graphics.PointF;
import android.graphics.Canvas;
import android.graphics.*;

import com.sb9.foloke.sectorb9.game.Managers.GameManager;


public abstract class DynamicEntity extends Entity {

   	float dx,dy;
	static float speed=3;
	float acceleration;
	boolean movable;
    DynamicEntity(float x, float y, float rotation, Bitmap image, String name, GameManager gameManager, int ID)
    {
        super(x,y,rotation,image,name, gameManager,ID);
        this.rotation=rotation;
		dx=dy=0;
		//textSpeed=new Text("0",x,y-32);
    }
    abstract public void RotationToPoint(PointF targetPoint);
	
	public void drawVelocity(Canvas canvas)
	{
		if(!renderable)
			return;
		Paint tPaint=new Paint();
		tPaint.setColor(Color.rgb(255,255,0));
		tPaint.setStrokeWidth(5);
		canvas.drawLine(getCenterX(),getCenterY(),getCenterX()+dx*20,getCenterY()+dy*20,tPaint);
		//textSpeed.render(canvas);
	}
	void impulse(PointF pointOfimpulse,float dx,float dy)
	{
		
		float tdx=this.dx+dx;  //1000-100
		float tdy=this.dy+dy;  //500-100
		
		float accel=speed/(float)Math.sqrt(tdx*tdx+tdy*tdy); //1 of speed% 3/1300
	
		
		this.dx=tdx*accel;
		if(this.dx>speed)
			this.dx=speed;
		this.dy=tdy*accel;
		if(this.dy>speed)
			this.dy=speed;
	}
	public float getAcceleration()
	{
		return acceleration;
	}
	public boolean getMoveable()
	{
		return movable;
	}
}
