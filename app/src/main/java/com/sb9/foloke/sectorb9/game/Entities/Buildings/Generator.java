package com.sb9.foloke.sectorb9.game.Entities.Buildings;
import com.sb9.foloke.sectorb9.game.Managers.GameManager;
import com.sb9.foloke.sectorb9.game.Entities.*;
import android.graphics.*;

import java.util.*;

	public class Generator extends StaticEntity
	{

		private int radius=200;
		private int energyCount=3;
		Paint circlePaint = new Paint();
		Paint consumersPaintOn=new Paint();
	Paint consumersPaintOff=new Paint();
	
		private ArrayList<StaticEntity> consumers;
		public Generator(float x, float y, float rotation, Bitmap image, String name, GameManager gameManager, int ID)
		{
			super(x,y,rotation,image,name, gameManager,ID);

			consumers=new ArrayList<StaticEntity>();
			
			
			
			consumersPaintOn.setColor(Color.GREEN);
			consumersPaintOn.setAlpha(100);
			consumersPaintOn.setStrokeWidth(2);

			consumersPaintOff.setColor(Color.RED);
			consumersPaintOff.setAlpha(100);
			consumersPaintOff.setStrokeWidth(2);
			
			circlePaint.setColor(Color.YELLOW);
			circlePaint.setStyle(Paint.Style.STROKE);
			circlePaint.setStrokeWidth(2);
			circlePaint.setPathEffect(new DashPathEffect(new float[] { 15, 16}, 0));

			if(enabled)
				calculateConsumers();
		}

		@Override
		public void render(Canvas canvas)
		{
			if(!renderable)
				return;
			canvas.save();
			canvas.rotate(rotation,getCenterX(),getCenterY());
			canvas.drawBitmap(image,x,y,null);
			canvas.restore();
			if(gameManager.drawDebugInfo)
				drawDebugCollision(canvas);
			
			if(gameManager.command== GameManager.commandInteraction)
			{
				canvas.drawCircle(getCenterX(),getCenterY(),radius,circlePaint);
				for(Entity e:consumers)
				{
					if(enabled)
					canvas.drawLine(e.getCenterX(),e.getCenterY(),getCenterX(),getCenterY(),consumersPaintOn);
					else
						canvas.drawLine(e.getCenterX(),e.getCenterY(),getCenterX(),getCenterY(),consumersPaintOff);
				}
			}
		}

		@Override
		public void tick()
		{super.tick();}


		public void giveEnergy(StaticEntity target)
		{
			if(target.getEnergy())
				return;
			if(energyCount>0)
			{
				target.setPowerSupplier(this);
				target.setEnergy(true);
				}
				
			energyCount--;
		}
		public void onAndOff()
		{
			super.onAndOff();
			if(enabled)
			{
				calculateConsumers();
				for(StaticEntity e:consumers)
				{
					e.setEnergy(true);
					
				}
			}
			else
			{
				for(StaticEntity e:consumers)
				{
					e.setEnergy(false);
				}
			}
		}
		public void takeEnergy(StaticEntity entity)
		{
			consumers.remove(entity);
			entity.setEnergy(false);
		}
		public void calculateConsumers()
		{
			consumers.clear();
			if(enabled)
			for(Entity e: gameManager.getEntities())
			{
				if(e instanceof StaticEntity)
					if(!(e instanceof Generator))
						if(((StaticEntity)e).getEnabled()==true)
							if(((StaticEntity)e).getEnergy()==false)
							{
								PointF point=new PointF(e.getCenterX()-getCenterX(),e.getCenterY()-getCenterY());
								if(Math.sqrt(point.x*point.x+point.y*point.y)<radius)
								{
									giveEnergy((StaticEntity)e);
									consumers.add((StaticEntity)e);
								}
							}
			}
		}
		
}
