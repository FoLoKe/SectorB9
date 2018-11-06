package com.sb9.foloke.sectorb9.game.ParticleSystem;
import android.graphics.*;
import com.sb9.foloke.sectorb9.game.funtions.*;
import com.sb9.foloke.sectorb9.game.entities.*;
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
	Entity holder;
	public Particle(Bitmap image,float x,float y,float lenght,Entity holder)
	{
		this.image=image;
		this.x=x;
		this.lifetime=lenght;
		this.y=y;
		lifetimer=new Timer(0);
		this.holder=holder;
	}
	public void render(Canvas canvas)
	{
		if(active)
		{
			canvas.save();
			canvas.rotate(rotation,getCenterX(),getCenterY());
			Paint tPaint=new Paint();
			//100% = lifetime
			//%= lifetimer.gettick()
			tPaint.setAlpha((int)(255*lifetimer.getTick()/(60*(lifetime))));
			canvas.drawBitmap(image,x,y+16,tPaint);
			canvas.restore();
			//drawDebugBox(canvas);
		}
	}
	public void tick()
	{
		if(active)
		{
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

		setCenterX(holder.getCenterX()-16+new Random().nextInt(32));
		setCenterY(holder.getCenterY());
		rotation=holder.getWorldRotation();
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
}
