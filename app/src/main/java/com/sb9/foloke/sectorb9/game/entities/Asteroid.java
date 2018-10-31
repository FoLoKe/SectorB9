package com.sb9.foloke.sectorb9.game.entities;

	import android.graphics.Canvas;
	import android.graphics.Paint;
	import android.graphics.PointF;

	import static java.lang.Math.PI;
	import static java.lang.Math.cos;
	import static java.lang.Math.sin;
	import android.graphics.*;

	import com.sb9.foloke.sectorb9.game.Assets.ImageAssets;
	import com.sb9.foloke.sectorb9.game.UI.Text;
import com.sb9.foloke.sectorb9.game.display.*;
import com.sb9.foloke.sectorb9.game.UI.*;
import java.util.*;

public class Asteroid extends DynamicEntity {

		private float speed=3;
		private boolean movable;
		
		private Text textdXdY;
		private float acceleration;
		
		public Asteroid(float x, float y,float rotation, ImageAssets asset,String name,Game game)
		{
			super(x,y,rotation,asset.asteroid_1,name,game);

			this.dx=this.dy=0;
			this.movable=false;
			this.textdXdY=new Text("",x-100,y-50);
			this.inventoryMaxCapacity=1;
			Random itemR=new Random();
			
			for (int i=1;i<inventoryMaxCapacity+1;i++)
			{
				int k=0;
				int v=0;
				while(k==0)
				{
					k=itemR.nextInt(3);
				}
				while(v==0)
				{
					v=itemR.nextInt(25);
				}
				this.inventory.put(k,v);
			}
		}

		@Override
		public void tick() {
			//inertia damping
			if(active)
			{
			if(!renderable)
				return;
			if(getHp()<=0)
			{
					active=false;
					onDestroy();
					return;
					}
			uIhp.tick(getHp());
				x += dx;
				y += dy;
					if(dx>0.01)
				dx-=0.021;
				if(dx<-0.01)
					dx+=0.02;
					if(dx>-0.01&&dx<0.01)
					if(dy>0.01)
				dy -= 0.02;
				if(dy<-0.01)
					dy+=0.021;
					if((dy>-0.01)&&(dy<0.01))
						dy=0;
			textdXdY.setWorldLocation(new PointF(x,y));
			this.collisionBox.set(x,y,x+image.getWidth(),y+image.getHeight());
			}
		}

		@Override
		public void render(Canvas canvas) {
		if(active)
			{
				if(!renderable)
					return;
				if(getHp()<100)
					uIhp.render(canvas);
				canvas.save();
				canvas.rotate(rotation,getCenterX(),getCenterY());
				canvas.drawBitmap(image,x,y,null);
				canvas.restore();
				textdXdY.render(canvas);
			}
		}
		@Override
		public void RotationToPoint(PointF targetPoint) {
			// rotation=(float)-Math.toDegrees(Math.PI+Math.atan2(targetPoint.x-x,targetPoint.y-y));   /coord rotation
		}

		public float getCenterX()
		{
			return x+image.getWidth()/2;
		}
		public float getCenterY()
		{
			return y+image.getHeight()/2;
		}

		public PointF getWorldLocation()
		{
			return new PointF(x,y);
		}


		public void addMovement(PointF screenPoint,float screenW, float screenH) {
			//float hundredPercent=dx+dy;
			if (movable) {
				//dx = dx / hundredPercent * speed;
				//dy = dy / hundredPercent * speed;

				float minAcceleration=screenH/8; //0%
				float maxAcceleration=screenH/2; //100%

				PointF relativePoint=new PointF(screenPoint.x-screenW/2,screenPoint.y-screenH/2);
				acceleration=(float)Math.sqrt(relativePoint.x*relativePoint.x+relativePoint.y*relativePoint.y);

				acceleration=(acceleration-minAcceleration)/(maxAcceleration-minAcceleration);
				if(acceleration<0)
					acceleration=0;
				if(acceleration>1)
					acceleration=1;

				float mathRotation=(float)(PI/180*rotation);
				rotation=360-(float)Math.toDegrees(Math.PI+Math.atan2(-screenW/2+screenPoint.x,-screenH/2+screenPoint.y)); //screen relative rotation
				//this.dx=(float)(screenH/2*cos(-rotation)-(-screenW/2+screenPoint.x*sin(-rotation)));
				this.dy = -(float) (acceleration*speed * cos(mathRotation));
				this.dx = (float) (acceleration*speed * sin(mathRotation));



			}
		}

		public void setMovable(boolean movable) {
			this.movable = movable;
		}

	
}
