package com.sb9.foloke.sectorb9.game.entities.Buildings;
import com.sb9.foloke.sectorb9.game.entities.*;
import android.graphics.*;
import com.sb9.foloke.sectorb9.game.display.*;
import com.sb9.foloke.sectorb9.game.funtions.*;
import java.util.Map;
import com.sb9.foloke.sectorb9.*;
import com.sb9.foloke.sectorb9.game.UI.*;
import com.sb9.foloke.sectorb9.game.Assets.*;
import java.lang.reflect.*;
import java.util.*;

	public class SolarPanel extends StaticEntity
	{
		private PointF collisionInitPoints[];
		private int radius=200;
		private int energyCount=3;
		Paint circlePaint = new Paint();
		Paint consumersPaint2=new Paint();
		
		//private Line2D collisionlines[];
		//private Animation crusherAnim;

		private ArrayList<StaticEntity> consumers;
		public SolarPanel(float x, float y,float rotation,ObjectsAsset  objAsset,String name,Game game)
		{
			
			super(x,y,rotation,objAsset.solarPanel,name,game);
			//crusherAnim=new Animation(objAsset.crusherAnim,15);
			this.inventoryMaxCapacity=0;
			this.opened=false;
			//inProduction=0;
			//count=0;prodTimer=new Timer(0);
			//prgBar=new UIProgressBar(this,50,8,-25,-20,game.uiAsset.stunBackground,game.uiAsset.stunLine,game.uiAsset.progressBarBorder,prodTimer.getTick());

			collisionInitPoints=new PointF[4];
			collisionInitPoints[0]=new PointF(-image.getWidth()/2,-image.getHeight()/2);
			collisionInitPoints[1]=new PointF(image.getWidth()/2,-image.getHeight()/2);
			collisionInitPoints[2]=new PointF(image.getWidth()/2,image.getHeight()/2);
			collisionInitPoints[3]=new PointF(-image.getWidth()/2,image.getHeight()/2);
			isUsingCustomCollision=true;
			setCustomCollisionObject(collisionInitPoints);
			consumers=new ArrayList<StaticEntity>();
			for(Entity e:game.getEntities())
			{
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
			
			
			consumersPaint2.setColor(Color.GREEN);
			consumersPaint2.setAlpha(100);
			consumersPaint2.setStrokeWidth(2);

			circlePaint.setColor(Color.YELLOW);
			circlePaint.setStyle(Paint.Style.STROKE);
			circlePaint.setStrokeWidth(2);
			circlePaint.setPathEffect(new DashPathEffect(new float[] { 15, 16}, 0));
			calculateCustomCollisionObject();
			calculateCollisionObject();
		}

		@Override
		public void render(Canvas canvas)
		{
			if(!renderable)
				return;
			canvas.save();
			canvas.rotate(rotation,getCenterX(),getCenterY());
			canvas.drawBitmap(getGame().objAsset.solarPanel,x,y,null);
			canvas.restore();
			if(game.drawDebugInf)
			drawDebugCollision(canvas);
			
			if(game.command==game.commandInteraction)
			{
			canvas.drawCircle(getCenterX(),getCenterY(),radius,circlePaint);
			
			for(Entity e:consumers)
			{
				if(enabled)
				canvas.drawLine(e.getCenterX(),e.getCenterY(),getCenterX(),getCenterY(),consumersPaint2);
			}
			}
			// TODO: Implement this method
		}

		@Override
		public void tick()
		{
			// TODO: Implement this method
		}

		@Override
		public void calculateCollisionObject()
		{
			// TODO: Implement this method
			super.calculateCollisionObject();
			calculateCustomCollisionObject();
		}
		public void giveEnergy(StaticEntity target)
		{
			if(target.getEnergy())
				return;
			if(energyCount>0)
				target.setEnergy(true);
				
			energyCount--;
		}
		public void onAndOff()
		{
			super.onAndOff();
			if(enabled)
			{
				for(StaticEntity e:consumers)
				{
					e.setEnergy(false);
					
				}
				
			}
			else
			{
				for(StaticEntity e:consumers)
				{
					e.setEnergy(true);
				}
				
			}
		}
		//@Override




	
}
