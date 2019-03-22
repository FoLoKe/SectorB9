package com.sb9.foloke.sectorb9.game.Entities;


import android.graphics.PointF;
import android.graphics.Canvas;
import android.graphics.*;


import com.sb9.foloke.sectorb9.game.Managers.GameManager;
import com.sb9.foloke.sectorb9.game.Funtions.*;
import com.sb9.foloke.sectorb9.game.UI.CustomViews.*;
import java.util.*;
import android.text.style.*;



public abstract class DynamicEntity extends Entity {

   	protected float dx,dy;
	
	private float mass=1;
	protected float acceleration=0;
	private float targetAcceleration=0;
	private float sidewayImpulse=1f;
	protected boolean movable;
	protected float  frontImpulse;
	private float backwardImpulse;
	protected PointF frontPoint=new PointF(1,0);

	protected boolean inBounds=true;
	public ArrayList<DynamicEntity> collidedWith=new ArrayList<DynamicEntity>();
	
    DynamicEntity(float x, float y, float rotation,  GameManager gameManager, int ID)
    {
        super(x,y,rotation,gameManager,ID);
		dx=dy=0;
    }
	
	private void drawVelocity(Canvas canvas)
	{
		if(!renderable)
			return;
		Paint tPaint=new Paint();
		tPaint.setColor(Color.rgb(255,255,0));
		tPaint.setStrokeWidth(5);
		canvas.drawLine(getCenterX(),getCenterY(),getCenterX()+dx*20,getCenterY()+dy*20,tPaint);
	}

	protected void calculateMovement()
	{
		int maxSpeed=100;
		float speed=getSpeed();
			if(movable&&(speed<maxSpeed))
			{
                acceleration=frontImpulse/mass*targetAcceleration;
                this.dy += (acceleration * this.frontPoint.y);
                this.dx += (acceleration * this.frontPoint.x);
			}
			else
			{
				acceleration=backwardImpulse/mass*60;
				if(speed>frontImpulse*60)
				{
                    dy -= dy / speed * acceleration;// + (acceleration * this.frontPoint.y);
                    dx -= dx / speed * acceleration;// + (acceleration * this.frontPoint.x);
				}
                else
				{
					dx=dy=0;
				}
				//GameLog.update(dx+" "+dy,1);
			}
		if(speed>maxSpeed)
		{
			acceleration=backwardImpulse/mass*60;
			if(speed>frontImpulse*60)
			{
				dy -= dy / speed * acceleration;// + (acceleration * this.frontPoint.y);
				dx -= dx / speed * acceleration;// + (acceleration * this.frontPoint.x);
			}
			else
			{
				dx=dy=0;
			}
		}
	}
	
    @Override
    public void tick() 
	{
        super.tick();

        transformMatrix.reset();
        transformMatrix.postRotate(rotation);
		x += dx;
        y += dy;
		if(inBounds) {
            if (x < 0)
                x = 0;
            if (y < 0)
                y = 0;
            if (x > gameManager.getGamePanel().getWorldSize())
                x = gameManager.getGamePanel().getWorldSize();
            if (y > gameManager.getGamePanel().getWorldSize())
                y = gameManager.getGamePanel().getWorldSize();
        }
		calculateMovement();
		
        if(rotation>360)
            rotation=0;
        if(rotation<0)
            rotation=360;

        float[] vector =new float[]{0,-1};
        transformMatrix.mapVectors(vector);
		calculateCollisionObject();

        frontPoint.set(vector[0],vector[1]);
		
        checkCollision();
	
        timerTick();
    }

    private void checkCollision()
    {
		collidedWith.clear();
        for (Entity e : getGameManager().getEntities())
        {
            if(e!=this)
            {
                if(e.active)
                {
                    if(e.collidable)
                    {
                        if ((e.getCollisionObject().intersects(getCollisionObject())))
                        {
                            if(!(this instanceof Projectile))
                            {
                                onCollide(e);
                            }
                            else
                            {
                                if(((Projectile) this).getParent()!=e)
                                {
                                    onCollide(e);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public void onCollide(Entity e)
	{
		impulse(e);
	}
	
	private void impulse(Entity e)
	{
		if(e instanceof DynamicEntity)
		{
			collidedWith.add((DynamicEntity)e);
			if(((DynamicEntity)e).collidedWith.contains(this))
				return;
		}
		
		float midpointx = (e.getCollisionObject().getCenterX() +getCollisionObject().getCenterX()) / 2; 
		float midpointy = (e.getCollisionObject().getCenterY() +getCollisionObject().getCenterY()) / 2;

		float dist=(float)Math.sqrt((e.getCenterX()-getCenterX())*(e.getCenterX()-getCenterX())+(e.getCenterY()-getCenterY())*(e.getCenterY()-getCenterY()));

		dist=(float)Math.max(0.1,dist);

		float rad=(getCollisionObject().getRadius()+ e.getCollisionObject().getRadius())/2;
		float newcenterX=(midpointx + rad * (getCollisionObject().getCenterX() - e.getCollisionObject().getCenterX())/ dist);
		float newcenterY=(midpointy + rad * (getCollisionObject().getCenterY() - e.getCollisionObject().getCenterY())/ dist);

		x+=-getCenterX()+newcenterX;
		y+=-getCenterY()+newcenterY;

		if(e instanceof DynamicEntity) 
		{
			float cx=getCenterX();
			float cy=getCenterY();
			float cx2=e.getCenterY();
			float cy2=e.getCenterX();
			
			float ang = (float)Math.atan2(cy - cy2, cx - cx2);
			float d1 = (float)Math.atan2(dy, dx);
			float d2 = (float)Math.atan2(((DynamicEntity)e).getDy(), ((DynamicEntity)e).getDx());

			float v1 = getSpeed()/60;
			float v2 =((DynamicEntity) e).getSpeed()/60;
			
			//F=mv+mv
			float f=mass*v1;
			float f2=((DynamicEntity)e).getMass()*v2;
			
			dx = (f-f2)/mass* (float)Math.cos(d1+d2 - ang);
			dy = (f-f2)/mass * (float)Math.sin(d1+d2 - ang);
        }
	}
	
    public boolean rotationToPoint(PointF point)
	{
        PointF B=new PointF(getCenterX()-point.x,getCenterY()-point.y);
		float BLength=(float)Math.sqrt(B.x*B.x+B.y*B.y);
		B.set(B.x/BLength,B.y/BLength);
        float sinPhi = (frontPoint.x*B.y - frontPoint.y*B.x);
		
		float deg=(float)Math.toDegrees(sinPhi);
		gameManager.getGamePanel().textDebug5.setString(""+deg);//(
		
		if(sinPhi>-0.02&&sinPhi<0.02)
            return true;
			
		sinPhi*=100;
		
		if(Math.abs(sinPhi)<sidewayImpulse/mass)
		{
			rotation-=sinPhi/2;
		}
		else
		{
    	    if (sinPhi < 0)
          		rotation+=sidewayImpulse/mass;
        	if (sinPhi >=0)
				rotation-=sidewayImpulse/mass;
		}
       
        return false;
	}

	public void addMovement(float accel)
	{
		if(movable)
			targetAcceleration=accel;
	}
	
	public float getAcceleration()
	{
		return (float)Math.sqrt(dx*dx+dy*dy);
	}

	public boolean getMoveable()
	{
		return movable;
	}

	@Override
	public void render(Canvas canvas)
	{
		if(!renderable)
			return;
        if((SH<maxSH||HP<maxHP)&&drawHp)
        {
            uIsh.render(canvas);
            uIhp.render(canvas);
        }
        if(SH>0&&shieldShow.getTick()>0)
        	canvas.drawBitmap(shieldImg,getCenterX()-(float)shieldImg.getWidth()/2,getCenterY()-(float)shieldImg.getHeight()/2,shieldpaint);
		if(Options.drawDebugInfo.getBoolean())
		{
			drawVelocity(canvas);
			drawDebugCollision(canvas);
		}
	}

	public void setSidewayImpulse(float sidewayImpulse)
	{
		this.sidewayImpulse=sidewayImpulse;
	}
	
	public void setFrontImpulse(float frontImpulse)
	{
		this.frontImpulse=frontImpulse;
	}
	
	public void setBackwardImpulse(float backwardImpulse)
	{
		this.backwardImpulse=backwardImpulse;
	}
	
	public void setMass(float mass)
	{
		this.mass=mass;
	}
	
	public float getMass()
	{
		return mass;
	}
	
	public float getDx()
	{
		return dx;
	}
	
	public float getDy()
	{
		return dy;
	}
	
    public void setDx(float dx)
    {
        this.dx=dx;
    }

    public void setDy(float dy)
    {
        this.dy=dy;
    }

    public float getSpeed()
    {
        return (float)Math.sqrt(dx*dx+dy*dy)*60;
    }

}
