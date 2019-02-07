package com.sb9.foloke.sectorb9.game.ParticleSystem;
import android.graphics.*;
import com.sb9.foloke.sectorb9.game.Funtions.*;

import java.util.Random;

public class Particle
{
	Bitmap image;
	float x,y;
	Timer lifetimer;
	boolean state;
	float lifetime;
	float rotation;
	boolean active=false;
	boolean renderable=false;
	float dx,dy;
	Random rnd=new Random();
	PointF dXY;
	Paint optionsPaint=new Paint();
	boolean randomDirection=true;
	//Entity holder;
	
	
	public Particle(Bitmap image,float x,float y,float lenght,PointF dXY,int rotation,boolean randomDirection)
	{
		this.randomDirection=randomDirection;
		this.image=image;
		this.x=x;
		this.lifetime=lenght;
		this.y=y;
		this.rotation=rotation;
		this.dXY=dXY;
		//this.dx=dXY.x;
		//this.dy=dXY.y;
		lifetimer=new Timer(0);
		//this.holder=holder;
	}
	public void render(Canvas canvas)
	{
		if(active)
		{
			//canvas.save();
			//canvas.rotate(rotation,getCenterX(),getCenterY());
			
			//100% = lifetime
			//%= lifetimer.gettick()
			
			optionsPaint.setAlpha((int)(255*lifetimer.getTick()/(60*(lifetime))));
			canvas.drawBitmap(image,x-image.getWidth()/2,y-image.getHeight()/2,optionsPaint);
			//canvas.restore();
			//drawDebugBox(canvas);
		}
	}
	public void tick()
	{
		if(active)
		{
			y+=dy;
			x+=dx;
			//y-=1;
			if(lifetimer.tick())
			{
				//g.debugText.setString(name+" ended");
				active=false;	
				return;
			}
		}
	}
	public float getState()
	{
		return lifetimer.getTick();
		}
	public void setWorldLocation(float x,float y)
	{
		this.x=x;
		this.y=y;
	}
	public void setActive(boolean condition)
	{
		active=condition;
	}
	public boolean getActive()
	{
		return active;
	}
	public void draw()
	{
		active=false;

		setCenterX(x);
		setCenterY(y);
		float mathRotation=(float)(Math.PI/180*rotation);
		PointF tPoint =new PointF((float)(dXY.x * Math.cos(mathRotation) - (dXY.y) * Math.sin(mathRotation))
											 ,(float)(dXY.x * Math.sin(mathRotation) + (dXY.y) * Math.cos(mathRotation)));
		//tfpointOfShooting.set(turret.getParent().getHolder().getCenterX()+tfpointOfShooting.x,turret.getParent().getHolder().getCenterY()+tfpointOfShooting.y);
		
		if(randomDirection)
		{
			if(rnd.nextBoolean())
				dx=tPoint.x;
			else
				dx=-tPoint.x;
			if(rnd.nextBoolean())
				dy=tPoint.y;
			else
				dy=-tPoint.y;
		}
		else
		{
			dx=tPoint.x;
			dy=tPoint.y;
		}
		
		this.lifetimer.setTimer(lifetime);

		//calculateCollisionObject();
		renderable=true;
		active=true;
		//renderable=true;
	}
	public void setCenterX(float x)
	{
		this.x=x-image.getWidth()/2;
	}
	public void setCenterY(float y)
	{
		this.y=y-image.getHeight()/2;
	}
	public float getCenterX()
    {
        return x+image.getWidth()/2;
    }
    public float getCenterY()
    {
        return y+image.getHeight()/2;
    }
	public void setLifetime(float lenght)
	{
		lifetime=lenght;
	}
	public void setWorldRotation(float rotation)
	{
		this.rotation=rotation;
	}
}
