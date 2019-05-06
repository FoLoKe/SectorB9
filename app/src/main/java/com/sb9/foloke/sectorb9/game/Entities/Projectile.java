package com.sb9.foloke.sectorb9.game.Entities;
import android.graphics.*;

import com.sb9.foloke.sectorb9.game.Managers.GameManager;
import com.sb9.foloke.sectorb9.game.Funtions.*;


public class Projectile extends DynamicEntity
{
	private int lifetime;
	private Timer lifeTimer;
	private float damage;
	
	private boolean active=false;

	private boolean collided=false;

	private Entity parent;
	private float adx=1,ady=1;

	
	public Projectile(float x, float y, int lifetime, float speed, float rotation, float damage,Entity parent, GameManager gameManager)
	{
		super(x,y,0, gameManager,0);
		this.inBounds=false;
		this.movable=true;
		this.parent=parent;
		this.rotation=rotation;
		this.frontImpulse=0.1f;
		this.lifetime=lifetime;
		this.damage=damage;
		this.lifeTimer=new Timer(0);
		this.maxSpeed=speed;
	}
	
	public void recreateCollision()
	{
		this.relativeCenterY=(float)image.getHeight()/2;
		this.relativeCentreX=(float)image.getWidth()/2;
		this.width=image.getWidth();
		this.height=image.getHeight();
		createCollision();
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
			}

	}


	@Override
	public void tick()
	{
		if(active) 
		{
			
            super.tick();
			
            if (lifeTimer.tick())
			{
               	active = false;
				return;
			}
			
			
			if(collided)
			{
			    collided=false;
			    active=false;

			}	
        }
	}

    @Override
    public void onCollide(Entity e){
		if(e.getTeam()!=parent.getTeam())
		{
       		e.applyDamage((damage));
        	collided=true;
       	 	this.lifeTimer.setTimer(0);
		}
    }
	
	@Override
	protected void calculateMovement()
	{
			acceleration=2;
			this.dy = (acceleration* this.frontPoint.y)+ady;
			this.dx = (acceleration*this.frontPoint.x)+adx;
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
		//dustPuff.reset();
		setCenterX(point.x);
		setCenterY(point.y);
		this.rotation=rotation;
		this.lifeTimer.setTimer(lifetime);

		if(parent instanceof DynamicEntity)
		{
			adx=((DynamicEntity)parent).getDx();
			ady=((DynamicEntity)parent).getDy();
		}
		renderable=true;
		active=true;
	}

    public Entity getParent() {
        return parent;
    }
}
