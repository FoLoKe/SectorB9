package com.sb9.foloke.sectorb9.game.Funtions;
import android.graphics.*;
import android.icu.text.*;

public class Line2D
{
	private float x1,y1,x2,y2;
	
	PointF pointOfIntersection=new PointF(0,0);
	
	boolean collided;
	Paint paint;
	public Line2D(float x1,float y1,float x2,float y2)
	{
		this.x1=x1;
		this.x2=x2;
		this.y2=y2;
		this.y1=y1;
		paint =new Paint();
		paint.setColor(Color.GREEN);
	}
	
	
	public boolean intersect(CustomCollisionObject obj)
	{
 		float cx=obj.getCenterX();
		float cy=obj.getCenterY();
		float r=obj.getRadius();
			
		boolean inside1 = pointCircle(x1,y1, cx,cy,r);
		boolean inside2 = pointCircle(x2,y2, cx,cy,r);
		if (inside1) 
		{
			pointOfIntersection.set(x1,y1);
		
			return true;
		}
		if (inside2) 
		{
			pointOfIntersection.set(x2,y2);

			return true;
		}
		float distX = x1 - x2;
		float distY = y1 - y2;
		float len = (float)Math.sqrt( (distX*distX) + (distY*distY) );

		float dot = ( ((cx-x1)*(x2-x1)) + ((cy-y1)*(y2-y1)) ) / (float)Math.pow(len,2);

		float closestX = x1 + (dot * (x2-x1));
		float closestY = y1 + (dot * (y2-y1));

		boolean onSegment = linePoint(x1,y1,x2,y2, closestX,closestY);
		if (!onSegment) 
			return false;

		distX = closestX - cx;
		distY = closestY - cy;
		float distance = (float)Math.sqrt( (distX*distX) + (distY*distY) );

		if (distance <= r) {
			pointOfIntersection.set(closestX,closestY);
			return true;
		}
		return false;
	}

	private float dist (float x1,float y1,float x2,float y2) 
	{
		float distX=x2-x1;
		float distY=y2-y1;
		return (float)Math.sqrt( (distX*distX) + (distY*distY) );
	}
	
	boolean pointCircle(float px, float py, float cx, float cy, float r) 
	{
		float distX = px - cx;
		float distY = py - cy;
		float distance =(float) Math.sqrt( (distX*distX) + (distY*distY) );

	
		if (distance <= r) {
			return true;
		}
		return false;
	}

	boolean linePoint(float x1, float y1, float x2, float y2, float px, float py) 
	{	
		float d1 = dist(px,py, x1,y1);
		float d2 = dist(px,py, x2,y2);
		float lineLen = dist(x1,y1, x2,y2);

		float buffer = 0.1f; 
		
		if (d1+d2 >= lineLen-buffer && d1+d2 <= lineLen+buffer) {
			return true;
		}
		return false;
	}
	
	public void render(Canvas canvas )
	{

		canvas.drawLine(x1,y1,x2,y2,paint);
        if(collided)
		canvas.drawCircle(pointOfIntersection.x, pointOfIntersection.y, 5,paint);
		collided=false;
	}
	public void set(float x1,float y1,float x2,float y2)
	{
		this.x1=x1;
		this.x2=x2;
		this.y2=y2;
		this.y1=y1;
	}
	public PointF getPoint()
	{
		return new PointF(pointOfIntersection.x,pointOfIntersection.y);
	}

	public PointF getFirstPoint()
	{
		return( new PointF(x1,y1));
	}
	public PointF getSecondPoint()
	{
		return( new PointF(x2,y2));
	}
	public void setThickness(int thikness)
	{
		paint.setStrokeWidth(thikness);
	}
	
}
