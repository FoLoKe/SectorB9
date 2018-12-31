package com.sb9.foloke.sectorb9.game.ParticleSystem;
import java.util.*;

import com.sb9.foloke.sectorb9.game.Assets.EffectsAsset;
import com.sb9.foloke.sectorb9.game.display.*;
import android.graphics.*;

public class LabStream_ParticleSystem
{
	ParticleSystem sparks;
	private Random rnd=new Random();

	public LabStream_ParticleSystem(Game game)
	{
		sparks=new ParticleSystem(EffectsAsset.blue_pixel,0,0,1f,new PointF(0,1),false,120,game);
		sparks.setAccuracy(new Point(16,1));
	}
	public void tick()
	{
		sparks.tick();
	}
	public void render(Canvas canvas,float x,float y)
	{
		sparks.draw(x,y,0,new PointF(0,0));
		sparks.render(canvas);
	}
}
