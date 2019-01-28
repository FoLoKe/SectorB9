package com.sb9.foloke.sectorb9.game.Entities.Ships;
import android.graphics.*;

import com.sb9.foloke.sectorb9.game.Assets.EffectsAsset;
import com.sb9.foloke.sectorb9.game.Entities.DynamicEntity;
import com.sb9.foloke.sectorb9.game.Funtions.*;
import com.sb9.foloke.sectorb9.game.ParticleSystem.*;

public class Ship
{
	protected Bitmap shipImage,engineImage,engineShieldImage=null;
	protected PointF pointOfengine;
	protected PointF pointOfShooting;
	protected PointF pointOfEngineSmoke;
	protected DynamicEntity holder;
	protected PointF collisionInitPoints[];
	protected PointF collisionPoints[];

	protected Line2D collisionLines[];
	protected TurretSystem turrets[];
	public ParticleSystem engineSmoke;
	
	public Ship(Bitmap shipImage,Bitmap engineImage,Bitmap engineShieldImage,DynamicEntity holder)
	{
		this.shipImage=shipImage;
		this.engineImage=engineImage;
		this.engineShieldImage=engineShieldImage;
		this.holder=holder;
		this.pointOfengine=new PointF(0,0);
		this.pointOfShooting=new PointF(0,shipImage.getHeight()/2);
		this.engineSmoke=new ParticleSystem(EffectsAsset.yellow_pixel,holder.getWorldLocation().x,holder.getWorldLocation().y,1f,new PointF(0.2f,0),true,120,holder.getGameManager());
		engineSmoke.setAccuracy(new Point(16,1));
		pointOfEngineSmoke=new PointF(0,shipImage.getHeight()/2);
		//calculateCollisionObject();
		
	}
	public void setPoints(PointF points[])
	{
		this.collisionInitPoints=points;
		this.collisionLines=new Line2D[points.length];
		for(int i=0;i<this.collisionInitPoints.length;i++)
			this.collisionLines[i]=new Line2D(0,0,1,1);

		this.collisionPoints=new PointF[collisionInitPoints.length];
	}
	public void render(Canvas canvas)
	{
        canvas.save();
		canvas.rotate(holder.getWorldRotation(),holder.getCenterX(),holder.getCenterY());
		if(holder.getMoveable())
		canvas.drawBitmap(engineImage,holder.getCenterX()-engineImage.getWidth()/2+pointOfengine.x,holder.getCenterY()-engineImage.getHeight()/2+pointOfengine.y-5+(holder.getAcceleration())*5,null);
		canvas.drawBitmap(shipImage,holder.getCenterX()-shipImage.getWidth()/2,holder.getCenterY()-shipImage.getHeight()/2,null);
		canvas.restore();
		Paint tpaint=new Paint();
		tpaint.setColor(Color.RED);
		//canvas.drawCircle(holder.getWorldLocation().x+pointOfEngineSmoke.x,holder.getWorldLocation().y+pointOfEngineSmoke.y,1,tpaint);
		for(TurretSystem t:turrets)
		t.render(canvas);
		
		if(holder.getAcceleration()>0.4)
		{
			float mathRotation=(float)(Math.PI/180*(holder.getWorldRotation()));
			PointF tpointOfEngineSmoke =new PointF((float)((pointOfEngineSmoke.x )* Math.cos(mathRotation) - pointOfEngineSmoke.y * Math.sin(mathRotation))
												,(float)(pointOfEngineSmoke.x * Math.sin(mathRotation) + pointOfEngineSmoke.y * Math.cos(mathRotation)));
			//tpointOfShooting.set(pointOfEngineSmoke.x+tpointOfShooting.x,pointOfEngineSmoke.x+tpointOfShooting.y);
			engineSmoke.draw(holder.getCenterX()+tpointOfEngineSmoke.x,holder.getCenterY()+tpointOfEngineSmoke.y,holder.getWorldRotation(), (pointOfEngineSmoke));
			//canvas.drawCircle(holder.getCenterX()+tpointOfEngineSmoke.x,holder.getCenterY()+tpointOfEngineSmoke.y,1,tpaint);
			}
		engineSmoke.render(canvas);
		
		if(holder.getGameManager().drawDebugInfo)
		for(Line2D l: collisionLines)
		{
			l.render(canvas);
		}
		
	}
	
	public void tick()
	{
		for(TurretSystem t:turrets)
			t.tick();
		engineSmoke.tick();
	}
	public Line2D[] getCollisionLines()
	{
			return collisionLines;
	}
	public void setPointOfEngine(PointF point)
	{
		pointOfengine=point;
	}
	public void setPointOfShooting(PointF point)
	{
		pointOfShooting=point;
	}
	public void calculateCollisionObject()
	{
		float mathRotation=(float)(Math.PI/180*holder.getWorldRotation());
		//collisionPath.reset();
		for (int i=0;i<collisionPoints.length;i++)
		{
			float x1 = -collisionInitPoints[i].x ;
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
		collisionLines[i].set(collisionPoints[i].x,collisionPoints[i].y,collisionPoints[i+1].x,collisionPoints[i+1].y);
		//collisionlines[1].set(collisionPoints[1].x,collisionPoints[1].y,collisionPoints[2].x,collisionPoints[2].y);
		collisionLines[i].set(collisionPoints[i].x,collisionPoints[i].y,collisionPoints[0].x,collisionPoints[0].y);
		i=0;
		
	}
	
	public void shoot()
	{
		for(TurretSystem t: turrets)
		{
			t.shoot();
		}
	}
	public DynamicEntity getHolder()
	{
		return holder;
	}
}
