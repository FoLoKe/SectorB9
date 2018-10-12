package com.sb9.foloke.sectorb9.game.ParticleSystem;
import android.graphics.*;
import com.sb9.foloke.sectorb9.game.funtions.*;
import java.util.Random;

public class ParticleSystem
{
	private Particle particles[];
	private static final int maxCount=5;
	private float x,y;
	private int lenght=lenght;
	private Bitmap image;
	private Point accuracy;
	private boolean createNew;
	private boolean useTimer;
	private Timer timer;
	private Random rand=new Random();
	public ParticleSystem(Bitmap image,float x,float y,int lenght,Point accuracy)
	{
		this.accuracy=accuracy;
		this.image=image;
		this.x=x;
		this.y=y;
		this.lenght=lenght;
		this.particles=new Particle[maxCount];
		
		for(int i=0;i<maxCount;i++)
		{
			addNewParticle(i);
		}
		
	}
	public void render(Canvas canvas)
	{
		for(int i=0;i<maxCount;i++)
		{
			particles[i].render(canvas);
		}
	}
	public void tick()
	{
		for(int i=0;i<maxCount;i++)
		{
			if(particles[i].getState())
			{
				addNewParticle(i);
			}
			particles[i].tick();
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
	private void addNewParticle(int index)
	{
		particles[index]=null;
		particles[index]=new Particle(image,x+rand.nextInt(accuracy.x)-rand.nextInt(accuracy.x),y,lenght+rand.nextInt(accuracy.x));
	}
}
