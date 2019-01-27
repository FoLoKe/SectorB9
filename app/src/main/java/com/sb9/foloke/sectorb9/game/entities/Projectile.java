package com.sb9.foloke.sectorb9.game.Entities;
import android.graphics.*;

import com.sb9.foloke.sectorb9.game.Managers.GameManager;
import com.sb9.foloke.sectorb9.game.Funtions.*;
import com.sb9.foloke.sectorb9.game.ParticleSystem.*;

public class Projectile extends DynamicEntity
{
	private int lifetime;
	private Timer lifeTimer;
	private int damage;
	private float effectDelay;
	private boolean active=false;
	private SmallDustPuff dustPuff;
	private boolean collided=false;
	private Timer dustDelay;
	public Projectile(float x, float y, Bitmap image, String name, int lifetime, float speed, float rotation, int damage, GameManager gameManager)
	{
		super(x,y,0,image,name, gameManager,0);
		this.rotation=rotation;
		this.speed=speed;
		this.lifetime=lifetime;
		this.collisionBox=new RectF(x+image.getWidth()/2-3,y+image.getHeight()-3,x+image.getWidth()/2+3,y+image.getHeight()/2+3);
		this.damage=damage;
		this.lifeTimer=new Timer(0);
		this.dustDelay=new Timer(0);
		this.dustPuff=new SmallDustPuff(gameManager);
		this.effectDelay=1f;
	}
	@Override
	public void render(Canvas canvas)
	{
		if(collided)
		{

			dustPuff.render(canvas,x,y);
		}
		else
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
		dustPuff.tick();
		if(collided)
		{
		if (dustDelay.tick())
		{
			collided=false;
			active=false;
		}
		return;
		}
		if(active)
		{
			
			if(lifeTimer.tick())
			{
				active=false;
				return;
			}
			else
			{
				
					boolean collisionFlag=false;
					Entity collidedObject=null;
						for (Entity e : gameManager.getEntities())
						{
							if (e.getCollsionBox().intersect(collisionBox))
							{
								
								collidedObject=e;
								collisionFlag=true;
								collidedObject.applyDamage((damage));
								this.lifeTimer.setTimer(0);
								dustDelay.setTimer(effectDelay);
								calculateCollisionObject();
								
								collided=true;
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
	public void shoot(PointF point,float rotation)
	{
		active=false;
		collided=false;
		dustPuff.reset();
		setCenterX(point.x);
		setCenterY(point.y);
		this.rotation=rotation;
		this.lifeTimer.setTimer(lifetime);
		
		calculateCollisionObject();
		renderable=true;
		active=true;
	}

	@Override
	public void calculateCollisionObject()
	{
		// TODO: Implement this method
		//super.calculateCollisionObject();
		collisionBox.set(x+image.getWidth()/2-3,y+image.getHeight()/2-3,x+image.getWidth()/2+3,y+image.getHeight()/2+3);
	}
	
	
	
}
