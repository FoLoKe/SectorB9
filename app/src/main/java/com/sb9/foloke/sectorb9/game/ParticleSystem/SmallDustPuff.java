package com.sb9.foloke.sectorb9.game.ParticleSystem;
import android.graphics.*;
import java.util.*;

import com.sb9.foloke.sectorb9.game.Assets.EffectsAsset;
import com.sb9.foloke.sectorb9.game.Managers.GameManager;
import com.sb9.foloke.sectorb9.game.display.*;

public class SmallDustPuff
{
	ParticleSystem sparks;
	private Random rnd=new Random();
	private boolean isActive=true;
	public SmallDustPuff(GameManager gameManager)
	{
		sparks=new ParticleSystem(EffectsAsset.dust_2,0,0,1f,new PointF(0.4f,0.4f),true,5, gameManager);
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
