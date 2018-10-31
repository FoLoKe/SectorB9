package com.sb9.foloke.sectorb9.game.ParticleSystem;
import android.graphics.*;
import com.sb9.foloke.sectorb9.game.funtions.*;

public class Particle
{
	Bitmap image;
	float x,y;
	Timer timer;
	boolean state;
	int lenght;
	
	
	public Particle(Bitmap image,float x,float y,int lenght)
	{
		this.image=image;
		this.x=x;
		this.lenght=lenght;
		this.y=y;
		timer=new Timer(lenght);
	}
	public void render(Canvas canvas)
	{
		Paint tPaint=new Paint();
		//tPaint.setAlpha(1/(lenght-timer.getTick()));
		canvas.drawBitmap(image,x,y,null);
	}
	public void tick()
	{
		state=timer.tick();
	}
	public boolean getState()
	{
		return state;
		}
	public void setWorldLocation(float x,float y)
	{
		this.x=x;
		this.y=y;
	}
}
