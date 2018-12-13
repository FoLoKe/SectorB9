package com.sb9.foloke.sectorb9.game.entities.Buildings.Components;
import com.sb9.foloke.sectorb9.game.entities.*;
import android.graphics.*;
import com.sb9.foloke.sectorb9.game.display.*;
import com.sb9.foloke.sectorb9.game.ParticleSystem.*;

public class AssemblerArm extends ComponentEntity
{
	boolean clockward=true;
	Sparks_ParticleSystem sparks;
	public AssemblerArm(float x,float y,float rotation,String name,Game game)
	{
		
		super(x,y,rotation,game.buildingsData.asset.assembler_arm1,name,game);
		sparks=new Sparks_ParticleSystem(game);
	}

	@Override
	public void render(Canvas canvas)
	{//canvas.save();
		
		// TODO: Implement this method
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
		sparks.tick();
		// TODO: Implement this method
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