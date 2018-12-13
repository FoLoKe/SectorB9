package com.sb9.foloke.sectorb9.game.ParticleSystem;
import android.graphics.*;
import java.util.*;
import com.sb9.foloke.sectorb9.game.display.*;

public class SmallDustPuff
{
	ParticleSystem sparks;
	private Random rnd=new Random();
	private boolean isActive=true;
	public SmallDustPuff(Game game)
	{
		sparks=new ParticleSystem(game.effAsset.dust_2,0,0,1f,new PointF(0.4f,0.4f),true,5,game);
		sparks.setAccuracy(new Point(1,1));
	}
	public void tick()
	{
		sparks.tick();
	}
	public void render(Canvas canvas,float x,float y)
	{
		if(isActive)
		{
		for(Particle p :sparks.getArray())
		sparks.draw(x,y,rnd.nextInt(360),new PointF(0,0));
			isActive=false;
		}
		sparks.render(canvas);
	}
	public void reset(){
		isActive=true;
	}
}
