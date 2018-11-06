package com.sb9.foloke.sectorb9.game.ParticleSystem;
import android.graphics.*;
import com.sb9.foloke.sectorb9.game.funtions.*;
import java.util.Random;
import java.util.ArrayList;
import com.sb9.foloke.sectorb9.game.display.*;
import java.util.List;
import com.sb9.foloke.sectorb9.game.entities.*;

public class ParticleSystem
{
	//private Particle particles[];
	private static final int maxCount=120;
	private float x,y;
	private float lenght=lenght;
	private Bitmap image;
	private Point accuracy;
	private boolean createNew;
	private boolean useTimer;
	private Timer timer;
	private Timer delay;
	private Random rand=new Random();
	ArrayList<Particle> particles;
	public ParticleSystem(Bitmap image,float x,float y,float lenght,Game game,Entity holder)
	{
		this.accuracy=new Point(1,1);
		this.image=image;
		this.x=x;
		this.y=y;
		this.lenght=lenght;
		this.particles=new ArrayList<Particle>();
		delay=new Timer(0);
		
		for(int i=0;i<maxCount;i++)
		{
			particles.add(new Particle(image,x,y,lenght,holder));
		}
		
	}
	//static Bitmap image;
	

	/*public ProjectilesPool(Bitmap image,Entity target,float speed, int maxSize,Game game) {
		projectiles=new ArrayList<Projectile>();
		for(int i=0;i<maxSize;i++)
		{
			projectiles.add(new Projectile(0,0,image,"p"+i,(int)1,speed,0,target,game));
		}
	}*/
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
	public void draw()
	{
		if(delay.tick())
		for(Particle p:particles)
		{
			if(!p.getActive())
			{
				p.draw();
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
}
