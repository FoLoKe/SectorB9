package com.sb9.foloke.sectorb9.game.entities;

	import android.graphics.Canvas;
	import android.graphics.Paint;
	import android.graphics.PointF;

	import com.sb9.foloke.sectorb9.game.Assets.ImageAssets;
	import com.sb9.foloke.sectorb9.game.UI.Text;

public class Cursor extends DynamicEntity {
		
		float speed=3;
		boolean drawable;
		private Text textdXdY;

		public Cursor(float x, float y, ImageAssets asset)
		{
			super(x,y,asset.cursor);
			
			this.dx=this.dy=0;
			this.drawable=false;
			textdXdY=new Text("",x-100,y-50);
		}

		@Override
		public void tick() {
			
		}

		@Override
		public void render(Canvas canvas) {
			if(drawable)
			canvas.drawBitmap(image,x,y,new Paint());
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


		

		public void setDrawable(boolean drawable) {
			this.drawable = drawable;
		}

}

