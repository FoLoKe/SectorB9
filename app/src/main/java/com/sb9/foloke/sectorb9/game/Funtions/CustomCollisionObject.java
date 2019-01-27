package com.sb9.foloke.sectorb9.game.Funtions;
import android.graphics.*;
import com.sb9.foloke.sectorb9.game.entities.*;

public class CustomCollisionObject
{
	
	private PointF collisionPoints[];
	private PointF collisionInitPoints[];
	private Line2D collisionlines[];
	private Entity parent;
	public CustomCollisionObject(PointF collisionInitPoints[],Entity parent)
	{
		this.parent=parent;
		this.collisionInitPoints=collisionInitPoints;
		collisionlines=new Line2D[collisionInitPoints.length];
		for(int i=0;i< collisionInitPoints.length;i++)
		{
			if(i<collisionInitPoints.length-1)//is not final point
		collisionlines[i]=new Line2D(collisionInitPoints[i].x,collisionInitPoints[i].y
		,collisionInitPoints[i+1].x,collisionInitPoints[i+1].y);
		else
				collisionlines[i]=new Line2D(collisionInitPoints[i].x,collisionInitPoints[i].y
											 ,collisionInitPoints[0].x,collisionInitPoints[0].y);
		}
		collisionPoints=new PointF[collisionInitPoints.length];
	}
	
	public void calculateCollisionObject()
	{
		float mathRotation=(float)(Math.PI/180*parent.getWorldRotation());
		//collisionPath.reset();
		for (int i=0;i<collisionPoints.length;i++)
		{
			float x1 = parent.getCenterX()-collisionInitPoints[i].x - parent.getCenterX();
			float y1 = parent.getCenterY()+collisionInitPoints[i].y - parent.getCenterY();

			float x2 = (float)(x1 * Math.cos(mathRotation) - y1 * Math.sin(mathRotation));
			float y2 = (float)(x1 * Math.sin(mathRotation) + y1 * Math.cos(mathRotation));

			if(collisionPoints[i]==null)
				collisionPoints[i]=new PointF(x2 + parent.getCenterX(),y2+parent.getCenterY());
			else
				collisionPoints[i].set(x2 + parent.getCenterX(),y2+parent.getCenterY());
		}
		//collisionPath.moveTo(collisionPoints[0].x,collisionPoints[0].y);
		//collisionPath.lineTo(collisionPoints[1].x,collisionPoints[1].y);
		//collisionPath.lineTo(collisionPoints[2].x,collisionPoints[2].y);
		//collisionPath.close();
		for(int i=0;i< collisionInitPoints.length;i++)
		{
			if(i<collisionInitPoints.length-1)//is not final point
				collisionlines[i].set(collisionPoints[i].x,collisionPoints[i].y
											 ,collisionPoints[i+1].x,collisionPoints[i+1].y);
			else
				collisionlines[i].set(collisionPoints[i].x,collisionPoints[i].y
											 ,collisionPoints[0].x,collisionPoints[0].y);
		}
		//collisionlines[0].set(collisionPoints[0].x,collisionPoints[0].y,collisionPoints[1].x,collisionPoints[1].y);
		
	}
	
	public boolean intersect(RectF rect)
	{
		for(Line2D l: collisionlines)
		{
			if(l.intersect(rect))
				return true;
				
		}
		return false;
		
	}
	public boolean intersect(Line2D line)
	{
		for(Line2D l: collisionlines)
		{
			if(l.lineLine(line))
				return true;

		}
		return false;

	}
	public void render(Canvas canvas)
	{
		for(Line2D l: collisionlines)
		{
			l.render(canvas);
		}
	}
}
