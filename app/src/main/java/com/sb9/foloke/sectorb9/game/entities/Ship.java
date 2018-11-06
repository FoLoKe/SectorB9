package com.sb9.foloke.sectorb9.game.entities;
import android.graphics.*;
import com.sb9.foloke.sectorb9.game.funtions.*;

public class Ship
{
	protected Bitmap shipImage,engineImage,engineShieldImage=null;
	protected PointF pointOfengine;
	protected PointF pointOfShooting;
	protected Entity holder;
	protected PointF collisionInitPoints[];
	protected PointF collisionPoints[];
	//private PointF collisionInitPoints[];

	protected Line2D collisionlines[];
	public Ship(Bitmap shipImage,Bitmap engineImage,Bitmap engineShieldImage,Entity holder)
	{
		this.shipImage=shipImage;
		this.engineImage=engineImage;
		this.engineShieldImage=engineShieldImage;
		this.holder=holder;
		pointOfengine=new PointF(0,0);
		pointOfShooting=new PointF(0,0);
		calculateCollisionObject();
		
	}
	public void setPoints(PointF points[])
	{
		collisionInitPoints=points;
		collisionlines=new Line2D[3];
		for(int i=0;i<collisionInitPoints.length;i++)
			collisionlines[i]=new Line2D(0,0,1,1);

		collisionPoints=new PointF[collisionInitPoints.length];
	}
	public void render(Canvas canvas)
	{
		canvas.drawBitmap(shipImage,holder.x,holder.y,null);
		canvas.drawBitmap(engineImage,holder.x+pointOfengine.x,holder.y+pointOfengine.y,null);
	}
	public void setPointOfEngine(PointF point)
	{
		pointOfengine=point;
	}
	public void calculateCollisionObject()
	{
		float mathRotation=(float)(Math.PI/180*holder.rotation);
		//collisionPath.reset();
		for (int i=0;i<collisionPoints.length;i++)
		{
			float x1 = holder.getCenterX()-collisionInitPoints[i].x - holder.getCenterX();
			float y1 = holder.getCenterY()+collisionInitPoints[i].y - holder.getCenterY();

			float x2 = (float)(x1 * Math.cos(mathRotation) - y1 * Math.sin(mathRotation));
			float y2 = (float)(x1 * Math.sin(mathRotation) + y1 * Math.cos(mathRotation));

			if(collisionPoints[i]==null)
				collisionPoints[i]=new PointF(x2 + holder.getCenterX(),y2+holder.getCenterY());
			else
				collisionPoints[i].set(x2 + holder.getCenterX(),y2+holder.getCenterY());
		}
		int i=0;
		for(;i<collisionPoints.length-1;i++)
		collisionlines[i].set(collisionPoints[i].x,collisionPoints[i].y,collisionPoints[i+1].x,collisionPoints[i+1].y);
		//collisionlines[1].set(collisionPoints[1].x,collisionPoints[1].y,collisionPoints[2].x,collisionPoints[2].y);
		collisionlines[i+1].set(collisionPoints[i+1].x,collisionPoints[+1].y,collisionPoints[0].x,collisionPoints[0].y);
	}
}
