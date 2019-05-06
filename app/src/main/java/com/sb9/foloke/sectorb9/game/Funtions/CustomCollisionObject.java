package com.sb9.foloke.sectorb9.game.Funtions;
import android.graphics.*;
import com.sb9.foloke.sectorb9.game.Entities.*;
import com.sb9.foloke.sectorb9.game.Managers.*;

public class CustomCollisionObject
{
	private float width,height;
	private RectF collisionBox;
	private Paint paint;
	private float radius=1;
	private float centerX,centerY;
	private GameManager GM;
	
	
	public CustomCollisionObject(float width,float height,GameManager GM)
	{
		radius=width>height?width/2:height/2;
		this.width=width;
		this.height=height;
		
		this.GM=GM;
		collisionBox=new RectF();
		paint=new Paint();
		paint.setColor(Color.GREEN);
		paint.setStyle(Paint.Style.STROKE);
	}
	
	public void calculateCollisionObject(float x,float y)
	{
		centerX=x;
		centerY=y;
	}
	
	
	
	public boolean intersects(CustomCollisionObject obj)
	{
		return Math.abs((centerX - obj.getCenterX()) * (centerX - obj.getCenterX()) + (centerY - obj.getCenterY()) * (centerY - obj.getCenterY())) <= (radius + obj.getRadius()) * (radius + obj.getRadius());
	
		
	}
	
	public RectF getCollisionBox()
	{
		return collisionBox;
	}
	public void render(Canvas canvas)
	{
		canvas.drawRect(collisionBox,paint);
		canvas.drawCircle(centerX,centerY,radius,paint);
	}
	
	public float getCenterX()
	{
		return centerX;
	}
	public float getCenterY(){return centerY;}
	public float getRadius(){return radius;}
    public void setRadius(float width,float height){radius=width>height?width/2:height/2;}
	
}
