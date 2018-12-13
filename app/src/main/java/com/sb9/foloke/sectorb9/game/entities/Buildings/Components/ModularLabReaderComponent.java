package com.sb9.foloke.sectorb9.game.entities.Buildings.Components;
import com.sb9.foloke.sectorb9.game.entities.Buildings.*;
import android.graphics.*;
import com.sb9.foloke.sectorb9.game.ParticleSystem.*;
import com.sb9.foloke.sectorb9.game.display.*;

public class ModularLabReaderComponent extends ComponentEntity
{
	boolean clockward=true;
	LabStream_ParticleSystem labStream;
	
	public ModularLabReaderComponent(float x,float y,float rotation,String name,Game game)
	{

		super(x,y,rotation,game.buildingsData.asset.labreader_mk1,name,game);
		labStream=new LabStream_ParticleSystem(game);
	}

	@Override
	public void render(Canvas canvas)
	{
		// TODO: Implement this method
	}


	
	@Override
	public void render(Canvas canvas,float x,float y,float rotation)
	{
		float mathRotation=(float)(Math.PI/180*this.rotation);
		//collisionPath.reset();
		float x1=this.x-x;
		float y1=this.y-y;


		float tempX = (float)(x1 * Math.cos(mathRotation) - y1 * Math.sin(mathRotation));
		float tempY = (float)(x1 * Math.sin(mathRotation) + y1 * Math.cos(mathRotation));
		Paint p=new Paint();
		p.setColor(Color.GREEN);
		
		canvas.save();
		//canvas.drawCircle(x,y,2,p);
		//canvas.drawCircle(x+tempX,y+tempY,2,p);
		canvas.translate(x+tempX,y+tempY);
		//canvas.drawCircle(0,0,2,p);
		canvas.rotate(this.rotation-45,0,0);
		labStream.render(canvas,0,0);
		canvas.drawBitmap(image,-16,-16,null);
		canvas.restore();

		//canvas.drawCircle(x+tempX,y+tempY,30,new Paint());
			
	}
	@Override
	public void tick()
	{
		labStream.tick();
		// TODO: Implement this method
		if(clockward)
			rotation++;
		else
			rotation--;
		

	}


	
}
