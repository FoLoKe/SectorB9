package com.sb9.foloke.sectorb9.game.entities;

import android.graphics.Bitmap;
import android.graphics.PointF;
import android.graphics.Canvas;
import android.graphics.*;

import com.sb9.foloke.sectorb9.game.UI.Text;
import com.sb9.foloke.sectorb9.game.display.*;

public abstract class DynamicEntity extends Entity {

    float dx,dy;
	float speed=3;
	Text textSpeed;
	private int debugCounter=0;
    public DynamicEntity(float x, float y,float rotation, Bitmap image,String name,Game game)
    {
        super(x,y,rotation,image,name,game);
        this.rotation=rotation;
		dx=dy=0;
		textSpeed=new Text("0",x,y-32);
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
		textSpeed.render(canvas);
	}
	public void impulse(PointF pointOfimpulse,float dx,float dy)
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
			debugCounter++;
		textSpeed.setString(accel+"");
	}

}
