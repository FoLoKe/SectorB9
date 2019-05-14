package com.sb9.foloke.sectorb9.game.Entities;

import android.graphics.PointF;
import android.graphics.Canvas;
import android.graphics.*;
import com.sb9.foloke.sectorb9.game.Managers.GameManager;
import com.sb9.foloke.sectorb9.game.Funtions.*;
import java.util.*;
import com.sb9.foloke.sectorb9.game.UI.CustomViews.*;

public abstract class DynamicEntity extends Entity {

   	protected float dx,dy;
	protected float maxSpeed=200;
	private float mass=1;
	protected float acceleration=0;
	private float targetAcceleration=0;
	private float sidewaysImpulse=1f;
	protected boolean movable;
	protected float  frontImpulse;
	private float backwardImpulse;
	protected PointF frontPoint=new PointF(1,0);
	private PointF debugPoint=new PointF();
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
		float speed=getSpeed();
			if(movable)
			{
				if(speed>=maxSpeed)
				{
					float deltaSpeed=maxSpeed/getSpeed();
					dx*=deltaSpeed;
					dy*=deltaSpeed;
					
				}
				//if(this instanceof Player)
				
                acceleration=frontImpulse/mass*targetAcceleration;
                this.dy += (acceleration * this.frontPoint.y);
                this.dx += (acceleration * this.frontPoint.x);
				
			}
			else
			{
				
					acceleration=backwardImpulse/mass*60;
					if(speed>acceleration)
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
		//GameLog.update(this+"s",3);
        transformMatrix.reset();
        transformMatrix.postRotate(rotation);
		x += dx;
        y += dy;
		if(inBounds) {
            if (x < 0)
			{
                x = 0;
				dx=0;
			}
            if (y < 0)
			{
				dy=0;
                y = 0;
			}
            if (x > gameManager.getGamePanel().getWorldSize())
			{
				dx=0;
                x = gameManager.getGamePanel().getWorldSize();
			}
            if (y > gameManager.getGamePanel().getWorldSize())
			{
				dy=0;
                y = gameManager.getGamePanel().getWorldSize();
			}
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
		

		float midpointx = (e.getCollisionObject().getCenterX() +getCollisionObject().getCenterX()) / 2; 
		float midpointy = (e.getCollisionObject().getCenterY() +getCollisionObject().getCenterY()) / 2;

		float dist=(float)Math.sqrt((e.getCenterX()-getCenterX())*(e.getCenterX()-getCenterX())+(e.getCenterY()-getCenterY())*(e.getCenterY()-getCenterY()));

		//dist=(float)Math.max(0.1,dist);

		float rad=(getCollisionObject().getRadius()+ e.getCollisionObject().getRadius())/2;
		float newcenterX,newcenterY;
		
		
		if(e instanceof DynamicEntity)
		{
			DynamicEntity dE=(DynamicEntity)e;
			
			collidedWith.add(dE);
			if(dE.collidedWith.contains(this))
				return;
			dE.collidedWith.add(this);
			
			float sumMass=mass+dE.mass;
			
			newcenterX=(midpointx + rad * (getCollisionObject().getCenterX() - e.getCollisionObject().getCenterX())/ dist);//*mass/sumMass;
			newcenterY=(midpointy + rad * (getCollisionObject().getCenterY() - e.getCollisionObject().getCenterY())/ dist);//*mass/sumMass;
			
			float newcenterXE=(midpointx + rad * (getCollisionObject().getCenterX() - e.getCollisionObject().getCenterX())/ dist);
			float newcenterYE=(midpointy + rad * (getCollisionObject().getCenterY() - e.getCollisionObject().getCenterY())/ dist);
			
			debugPoint.set(newcenterXE,newcenterYE);
			//dE.dx += (-dE.getCenterX() + newcenterXE)*dE.mass/sumMass;
           //	dE.dy += (-dE.getCenterY() + newcenterYE)*dE.mass/sumMass;
			//GameLog.update("dE: "+ (-dE.getCenterX() + newcenterXE)+"",2);
			//GameLog.update((-getCenterX() + newcenterX)+"",2);
			
            float dEspeed=(float)Math.sqrt(dE.dx*dE.dx+dE.dy*dE.dy)*60;
            if(dEspeed>dE.maxSpeed)
            {
                dE.dx *= dE.maxSpeed / dEspeed;
                dE.dy *= dE.maxSpeed / dEspeed;
            }
			
			dx += (-getCenterX() + newcenterX)*mass/sumMass;
            dy += (-getCenterY() + newcenterY)*mass/sumMass;
			
            float speed=(float)Math.sqrt(dx*dx+dy*dy)*60;
            if(speed>maxSpeed)
            {
                dx *= maxSpeed / speed;
                dy *= maxSpeed / speed;
            }
			
		}
		else
		{
			newcenterX=(midpointx + rad * (getCollisionObject().getCenterX() - e.getCollisionObject().getCenterX())/ dist);
			newcenterY=(midpointy + rad * (getCollisionObject().getCenterY() - e.getCollisionObject().getCenterY())/ dist);
			dx += -getCenterX() + newcenterX;
            dy += -getCenterY() + newcenterY;
            float speed=(float)Math.sqrt(dx*dx+dy*dy)*60;
            if(speed>maxSpeed)
            {
                dx *= maxSpeed / speed;
                dy *= maxSpeed / speed;
            }
			
		}
		

			/*float cx=getCenterX();
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
			dy = (f-f2)/mass * (float)Math.sin(d1+d2 - ang);*/

         
	}
	
    public boolean rotationToPoint(PointF point)
	{
        PointF B=new PointF(getCenterX()-point.x,getCenterY()-point.y);
		float BLength=(float)Math.sqrt(B.x*B.x+B.y*B.y);
		B.set(B.x/BLength,B.y/BLength);
        float sinPhi = (frontPoint.x*B.y - frontPoint.y*B.x);
		
		
		
		
		if(sinPhi>-0.02&&sinPhi<0.02)
            return true;
			
		sinPhi*=100;
		
		if(Math.abs(sinPhi)<sidewaysImpulse/mass)
		{
			rotation-=sinPhi/2;
		}
		else
		{
    	    if (sinPhi < 0)
          		rotation+=sidewaysImpulse/mass;
        	if (sinPhi >=0)
				rotation-=sidewaysImpulse/mass;
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

	public boolean getMovable()
	{
		return movable;
	}

	@Override
	public void render(Canvas canvas)
	{
		if(!renderable)
			return;
        if((SP<maxSP||HP<maxHP)&&drawHp)
        {
            uIsh.render(canvas);
            uIhp.render(canvas);
        }
        if(SP>0&&shieldShow.getTick()>0)
        	canvas.drawBitmap(shieldImg,getCenterX()-(float)shieldImg.getWidth()/2,getCenterY()-(float)shieldImg.getHeight()/2,shieldPaint);
		if(Options.drawDebugInfo.getBoolean())
		{
			drawVelocity(canvas);
			drawDebugCollision(canvas);
			canvas.drawText("M:  "+mass,x-100,y,debugPaint);
            canvas.drawText("FI: "+frontImpulse,x-100,y+32,debugPaint);
            canvas.drawText("BI: "+backwardImpulse,x-100,y+64,debugPaint);
            canvas.drawText("SI: "+sidewaysImpulse,x-100,y+96,debugPaint);
            canvas.drawText("HP: "+HP+"/"+maxHP,x-100,y+128,debugPaint);
            canvas.drawText("HP: "+SP+"/"+maxSP,x-100,y+160,debugPaint);
			canvas.drawText("TEAM: "+TEAM,x-100,y+192,debugPaint);
            //canvas.drawBitmap(image,new Rect(0,0,32,20),new RectF(x,y,x+32,y+32),null);
			//canvas.drawCircle(debugPoint.x,debugPoint.y,4,debugPaint);

		}
	}

	public void setSidewaysImpulse(float sidewayImpulse)
	{
		this.sidewaysImpulse=sidewayImpulse;
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

    public float getSpeed()
    {
        return (float)Math.sqrt(dx*dx+dy*dy)*60;
    }
	
	public void setMovable(boolean movable)
	{
		this.movable=movable;
	}

}
