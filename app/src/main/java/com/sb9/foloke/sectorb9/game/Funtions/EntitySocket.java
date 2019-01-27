package com.sb9.foloke.sectorb9.game.Funtions;
//import android.content.*;
import android.graphics.*;
import com.sb9.foloke.sectorb9.game.Entities.*;
import com.sb9.foloke.sectorb9.game.Entities.Buildings.Components.*;


public class EntitySocket
{
	Entity parent;
	ComponentEntity child;
	float rotation,x,y;
	float tempX,tempY;
	public EntitySocket(Entity parent,ComponentEntity child,float relativeRotation,PointF relativeLocation)
	{
		this.parent=parent;
		this.child=child;
		rotation=relativeRotation;
		tempX=x=relativeLocation.x;
		tempY=y=relativeLocation.y;
		
	}
	public void render(Canvas canvas)
	{
			child.render(canvas,tempX+parent.getCenterX(),tempY+parent.getCenterY(),rotation+parent.getWorldRotation());
		
	}
	public void render(Canvas canvas,float x,float y,float rotation)
	{
		child.render(canvas,x,y,rotation);

	}
	public void tick()
	{
		float mathRotation=(float)(Math.PI/180*parent.getWorldRotation());
		//collisionPath.reset();
		
			

			tempX = (float)(x * Math.cos(mathRotation) - y * Math.sin(mathRotation));
			tempY = (float)(x * Math.sin(mathRotation) + y * Math.cos(mathRotation));

			
		
		child.tick();
	}
	public void setRotation(int r)
	{
		child.setWorldRotation(r);
	}
}
