package com.sb9.foloke.sectorb9.game.entities;

	import android.graphics.Canvas;
	import android.graphics.Paint;
	import android.graphics.PointF;

	import com.sb9.foloke.sectorb9.game.Assets.ShipAsset;

	import com.sb9.foloke.sectorb9.game.Managers.GameManager;

public class Cursor extends DynamicEntity {
		

		private boolean drawable;

		public Cursor(float x, float y, String name, GameManager gameManager)
		{
			super(x,y,0,ShipAsset.cursor,name, gameManager,0);
			
			this.dx=this.dy=0;
			this.drawable=false;

		}

		@Override
		public void tick() {
			
		}

		@Override
		public void render(Canvas canvas) {
			if(drawable)
			canvas.drawBitmap(image,x-image.getWidth()/2,y-image.getHeight()/2,new Paint());
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

