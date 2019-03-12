package com.sb9.foloke.sectorb9.game.Entities;
import android.graphics.*;

import com.sb9.foloke.sectorb9.game.Managers.GameManager;
import com.sb9.foloke.sectorb9.game.Funtions.*;
import com.sb9.foloke.sectorb9.game.ParticleSystem.*;

public class Projectile extends DynamicEntity
{
	private int lifetime;
	private Timer lifeTimer;
	private float damage;
	//private float effectDelay;
	
	private boolean active=false;
	//private SmallDustPuff dustPuff;
	private boolean collided=false;
	//private Timer dustDelay;
	private Entity parent;
	private float addedAcceleration;
	
	public Projectile(float x, float y, int lifetime, float speed, float rotation, float damage,Entity parent, GameManager gameManager)
	{
		super(x,y,0, gameManager,0);
		this.inBounds=false;
		this.parent=parent;
		this.rotation=rotation;
		this.speed=speed;
		this.lifetime=lifetime;
		this.damage=damage;
		this.lifeTimer=new Timer(0);
		//this.dustDelay=new Timer(0);
		//this.dustPuff=new SmallDustPuff(gameManager);
		//this.effectDelay=1f;
		
	}
	
	public void recreateCollision()
	{
		this.relativeCenterY=image.getHeight()/2;
		this.relativeCentreX=image.getWidth()/2;
		this.width=image.getWidth();
		this.height=image.getHeight();
		createCollision();
	}
	@Override
	public void render(Canvas canvas)
	{
		if(collided)
		{

			//dustPuff.render(canvas,x,y);
		}
		else
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
			targetAcceleration=1f+addedAcceleration;
			acceleration=targetAcceleration;
            super.tick();
			
            if (lifeTimer.tick())
			{
				//if(!collided)
               	active = false;
				return;
			}
			
			gameManager.getGamePanel().textDebug2.setString(targetAcceleration+"");
			//dustPuff.tick();
			if(collided)
			{
		    //if (dustDelay.tick())
		    	{
			    	collided=false;
			    	active=false;
		    	}
		    return;
			}	
        }
			
		
	}

    @Override
    public void onCollide(Entity e){
		if(e.getTeam()!=parent.getTeam())
		{
       		e.applyDamage((damage));
        //dustDelay.setTimer(effectDelay);
        	collided=true;
       	 	this.lifeTimer.setTimer(0);
		}
    }

    public void setActive(boolean condition)
	{
		active=condition;
	}
	public boolean getActive()
	{
		return active;
	}
	public void shoot(PointF point,float rotation,float acceleration)
	{
		active=false;
		collided=false;
		//dustPuff.reset();
		setCenterX(point.x);
		setCenterY(point.y);
		this.rotation=rotation;
		this.lifeTimer.setTimer(lifetime);
		addedAcceleration=acceleration;

		renderable=true;
		active=true;
	}

    public Entity getParent() {
        return parent;
    }
}
