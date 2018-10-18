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

public class Asteroid extends DynamicEntity {

		private float speed=3;
		private boolean movable;
		private Text textdXdY;
		private float acceleration;
		;
		public Asteroid(float x, float y, ImageAssets asset)
		{
			super(x,y,asset.asteroid_1);

			this.dx=this.dy=0;
			this.movable=false;
			textdXdY=new Text("",x-100,y-50);
			for (int i=1;i<inventoryMaxCapacity-2;i++)
				inventory.put(i,i+i);
		}

		@Override
		public void tick() {
			//no inertia damping
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
						
				this.collisionBox.set(x,y,x+image.getWidth(),y+image.getHeight());
		}

		@Override
		public void render(Canvas canvas) {

			if(!renderable)
				return;
			canvas.save();
			canvas.rotate(rotation,getCenterX(),getCenterY());
			canvas.drawBitmap(image,x,y,new Paint());
			canvas.restore();

			textdXdY.setWorldLocation(new PointF(x,y));
			textdXdY.render(canvas);

			Paint tPaint=new Paint();
			tPaint.setColor(Color.rgb(0,255,0));
			tPaint.setStyle(Paint.Style.STROKE);
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
