package com.sb9.foloke.sectorb9.game.Entities.Ships;
import android.graphics.*;

import com.sb9.foloke.sectorb9.game.Assets.EffectsAsset;
import com.sb9.foloke.sectorb9.game.Entities.DynamicEntity;
import com.sb9.foloke.sectorb9.game.Entities.Entity;
import com.sb9.foloke.sectorb9.game.Funtions.*;
import com.sb9.foloke.sectorb9.game.ParticleSystem.*;

public class Ship
{
	protected Bitmap shipImage,engineImage,engineShieldImage=null;
	protected PointF pointOfengine;
	protected PointF pointOfShooting;
	protected PointF pointOfEngineSmoke;
	protected DynamicEntity holder;

	private CustomCollisionObject collisonObject;
	protected PointF collisionInitPoints[];

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
	}
	public void setPoints(PointF points[])
	{
		this.collisionInitPoints=points;
		collisonObject=new CustomCollisionObject(points,holder);
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

			engineSmoke.draw(holder.getCenterX()+tpointOfEngineSmoke.x,holder.getCenterY()+tpointOfEngineSmoke.y,holder.getWorldRotation(), (pointOfEngineSmoke));
			}
		engineSmoke.render(canvas);
		
		if(holder.getGameManager().drawDebugInfo)
			collisonObject.render(canvas);
		
	}
	
	public void tick()
	{
		for(TurretSystem t:turrets)
			t.tick();
		engineSmoke.tick();
	}


	public CustomCollisionObject getCollisonObject() {
		return collisonObject;
	}

	public void setPointOfEngine(PointF point)
	{
		pointOfengine=point;
	}
	public void setPointOfShooting(PointF point)
	{
		pointOfShooting=point;
	}
	public void calculateCollisionObject(Matrix matrix)
	{
		collisonObject.calculateCollisionObject(matrix);
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
