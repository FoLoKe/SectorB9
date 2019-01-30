package com.sb9.foloke.sectorb9.game.Entities.Buildings.Components;
import android.graphics.*;

import com.sb9.foloke.sectorb9.game.Assets.ObjectsAsset;
import com.sb9.foloke.sectorb9.game.Managers.GameManager;
import com.sb9.foloke.sectorb9.game.ParticleSystem.*;

public class AssemblerArm extends ComponentEntity
{
	boolean clockward=true;
	Sparks_ParticleSystem sparks;
	public AssemblerArm(float x, float y, float rotation, String name, GameManager gameManager)
	{
		
		super(x,y,rotation, ObjectsAsset.assembler_arm1,name, gameManager);
		sparks=new Sparks_ParticleSystem(gameManager);
	}

	@Override
	public void render(Canvas canvas)
	{

	}
	@Override
public void render(Canvas canvas,float x,float y,float rotation)
{
	canvas.drawCircle(x,y,2,new Paint());
	canvas.save();
	canvas.rotate(rotation+this.rotation,x,y);
	
	canvas.drawBitmap(image,x-3,y-image.getHeight()/2-1,null);
	canvas.restore();
	
	if(this.rotation>85)
		sparks.render(canvas,x,y);
}
	@Override
	public void tick()
	{
		super.tick();
		super.timerTick();
		sparks.tick();

		if(clockward)
		rotation++;
		else
			rotation--;
		if(rotation>90)
			clockward=false;
		if(rotation<0)
			clockward=true;
			
	}

	
	
}
