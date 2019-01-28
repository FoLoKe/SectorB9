package com.sb9.foloke.sectorb9.game.Entities;

	import android.graphics.Canvas;
    import android.graphics.PointF;

	import static java.lang.Math.PI;
	import static java.lang.Math.cos;
	import static java.lang.Math.sin;

	import com.sb9.foloke.sectorb9.game.Managers.GameManager;
	import com.sb9.foloke.sectorb9.game.DataSheets.BuildingsDataSheet;

public class Asteroid extends DynamicEntity {

		private float speed=3;
		private boolean movable;
		
		
		private float acceleration;
		private PointF[] collisionInitPoints;
		final static int ID=8;
		
	public Asteroid(float x, float y, float rotation, GameManager gameManager)
	{
		super(x,y,rotation, BuildingsDataSheet.findById(ID).image, BuildingsDataSheet.findById(ID).name, gameManager,ID);
		
		
		
		isUsingCustomCollision=false;
		
		
		calculateCollisionObject();
		

	}
	
		@Override
		public void tick() {
			//inertia damping
			if(active)
			{
				calculateCollisionObject();
			if(!renderable)
				return;
			if(getHp()<=0)
			{
					active=false;
					//onDestroy();
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
				
			}
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
