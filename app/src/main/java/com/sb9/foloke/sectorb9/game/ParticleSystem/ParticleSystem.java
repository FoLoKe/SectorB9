package com.sb9.foloke.sectorb9.game.ParticleSystem;
import android.graphics.*;

import com.sb9.foloke.sectorb9.game.Managers.GameManager;
import com.sb9.foloke.sectorb9.game.Funtions.*;
import java.util.Random;
import java.util.ArrayList;
import java.util.List;

public class ParticleSystem
{
	//private Particle particles[];
	private static int maxCount=120;
	private float x,y;
	private float dx,dy;
	private float lenght;
	private Bitmap image;
	private Point accuracy;
	private boolean createNew;
	private boolean useTimer;
	private Timer timer;
	private Timer delay;
	private Random rand=new Random();
	ArrayList<Particle> particles;
	public ParticleSystem(Bitmap image, float x, float y, float lenght, PointF dXY, boolean randomDirection, int count, GameManager gameManager)
	{
		this.maxCount=count;
		this.accuracy=new Point(1,1);
		this.image=image;
		this.x=x;
		this.y=y;
		this.lenght=lenght;
		this.particles=new ArrayList<Particle>();
		delay=new Timer(0);
		dx=dXY.x;
		dy=dXY.y;
		for(int i=0;i<maxCount;i++)
		{
			particles.add(new Particle(image,x,y,lenght,dXY,0,randomDirection));
		}
		
	}

	public List<Particle> getArray()
	{
		return particles;
	}
	public void tick()
	{
		for(Particle p:particles)
		{
			p.tick();
		}
	}
	public void render(Canvas canvas)
	{
		for(Particle p:particles)
		{
			p.render(canvas);
		}
	}
	public void draw(float x,float y,float rotation,PointF rotationPrivot)
	{
		if(Options.drawParticles.getBoolean())
		if(delay.tick())
		for(Particle p:particles)
		{
			if(!p.getActive())
			{
				p.setWorldRotation(rotation);
				p.draw();
				float mathRotation=(float)(Math.PI/180*(rotation));
				PointF tXY =new PointF((float)((-accuracy.x/2+rand.nextInt(accuracy.x))* Math.cos(mathRotation) - (-accuracy.y/2+rand.nextInt(accuracy.y)) * Math.sin(mathRotation))
									   ,(float)((-accuracy.x/2+rand.nextInt(accuracy.x)) * Math.sin(mathRotation) + (-accuracy.y/2+rand.nextInt(accuracy.y)) * Math.cos(mathRotation)));
				p.setWorldLocation(x+tXY.x,y+tXY.y);
				//p.setWorldRotation(rotation);
				//p.getGame().debugText.setString(p.getName());
				delay.setTimer(0.01f);
				return;
			}
		}
	}
	public void setWorldLocation(float x,float y)
	{
		this.x=x;
		this.y=y;
		for(int i=0;i<maxCount;i++)
		{
			//particles[i].setWorldLocation(x,y);
		}
	}
	/*private void addNewParticle(int index)
	{
		particles[index]=null;
		particles[index]=new Particle(image,x+rand.nextInt(accuracy.x)-rand.nextInt(accuracy.x),y,lenght+rand.nextInt(accuracy.x));
	}*/
	public void setLifetime(float lenght)
	{
		for(Particle p:particles)
		{
			p.setLifetime(lenght);
		}
	}
	public void setAccuracy(Point accuracy)
	{
		this.accuracy=accuracy;
	}
}
