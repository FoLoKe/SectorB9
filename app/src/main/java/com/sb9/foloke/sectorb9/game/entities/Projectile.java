package com.sb9.foloke.sectorb9.game.entities;
import android.graphics.*;
import com.sb9.foloke.sectorb9.game.funtions.*;
import com.sb9.foloke.sectorb9.game.display.*;

public class Projectile extends DynamicEntity
{
	private int lifetime;
	private Timer lifetimer;
	private Entity holder;
	protected boolean active=false;
	public Projectile(float x,float y,Bitmap image,String name,int lifetime,float speed, float rotation,Entity holder,Game game)
	{
		super(x,y,0,image,name,game);
		this.rotation=rotation;
		this.speed=speed;
		this.lifetime=3;
		this.collisionBox=new RectF(x+image.getWidth()/2-3,y+image.getHeight()-3,x+image.getWidth()/2+3,y+image.getHeight()/2+3);
		this.holder=holder;
		lifetimer=new Timer(0);
	}
	@Override
	public void render(Canvas canvas)
	{
		if(active)
		{
		canvas.save();
		canvas.rotate(rotation,getCenterX(),getCenterY());
			
		canvas.drawBitmap(image,x,y,null);
		canvas.restore();
		drawDebugBox(canvas);
		}
		// TODO: Implement this method
	}

	@Override
	public void tick()
	{
		if(active)
		{
			if(lifetimer.tick())
			{
				getGame().debugText.setString(name+" ended");
			active=false;	
			return;
			}
		else
			{
					boolean collisionFlag=false;
					Entity collidedObject=null;
						for (Entity e : getHolder().getEntities())
						{
							if (e.getCollsionBox().intersect(collisionBox))
							{
								
								collidedObject=e;
								collisionFlag=true;
								collidedObject.applyDamage((10));
								this.lifetimer.setTimer(0);
								active=false;
								calculateCollisionObject();
								
								return;
							}
						}

					if (!collisionFlag)
					{
						float mathRotation=(float)(3.14/180*rotation);
						//shooter relative rotation
						this.dy = -(float) (speed * Math.cos(mathRotation));
						this.dx = (float) (speed * Math.sin(mathRotation));
						x += dx;
						y += dy;
						//dx = dy = 0;
					}
					calculateCollisionObject();
					
					
				}
			}
			
		// TODO: Implement this method
	}

	@Override
	public void RotationToPoint(PointF targetPoint)
	{
		// TODO: Implement this method
	}
	public void setActive(boolean condition)
	{
		active=condition;
	}
	public boolean getActive()
	{
		return active;
	}
	public void shoot(PointF point)
	{
		active=false;
		
		setCenterX(point.x);
		setCenterY(point.y);
		rotation=holder.getWorldRotation();
		this.lifetimer.setTimer(lifetime);
		
		calculateCollisionObject();
		renderable=true;
		active=true;
		//renderable=true;
	}

	@Override
	public void calculateCollisionObject()
	{
		// TODO: Implement this method
		//super.calculateCollisionObject();
		collisionBox.set(x+image.getWidth()/2-3,y+image.getHeight()/2-3,x+image.getWidth()/2+3,y+image.getHeight()/2+3);
	}
	
	
	
}
