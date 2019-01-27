package com.sb9.foloke.sectorb9.game.ParticleSystem;
import com.sb9.foloke.sectorb9.game.Assets.EffectsAsset;
import com.sb9.foloke.sectorb9.game.Managers.GameManager;
import com.sb9.foloke.sectorb9.game.display.*;
import android.graphics.*;
import java.util.*;

public class Sparks_ParticleSystem
{
	ParticleSystem sparks;
	private Random rnd=new Random();
	
	public Sparks_ParticleSystem(GameManager gameManager)
	{
	sparks=new ParticleSystem(EffectsAsset.yellow_pixel,0,0,1f,new PointF(1,1),true,120, gameManager);
	sparks.setAccuracy(new Point(10,10));
	}
	public void tick()
	{
		sparks.tick();
	}
	public void render(Canvas canvas,float x,float y)
	{
		sparks.draw(x,y,rnd.nextInt(360),new PointF(0,0));
		sparks.render(canvas);
	}
}
