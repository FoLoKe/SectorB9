package com.sb9.foloke.sectorb9.game.Entities.Buildings.Components;
import android.graphics.*;

import com.sb9.foloke.sectorb9.game.Assets.ObjectsAsset;
import com.sb9.foloke.sectorb9.game.Managers.GameManager;
import com.sb9.foloke.sectorb9.game.ParticleSystem.*;

public class ModularLabReaderComponent extends ComponentEntity
{
	boolean clockward=true;
	LabStream_ParticleSystem labStream;
	
	public ModularLabReaderComponent(float x, float y, float rotation, String name, GameManager gameManager)
	{

		super(x,y,rotation, ObjectsAsset.labreader_mk1,name, gameManager);
		labStream=new LabStream_ParticleSystem(gameManager);
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
