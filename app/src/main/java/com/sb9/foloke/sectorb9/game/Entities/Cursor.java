package com.sb9.foloke.sectorb9.game.Entities;

	import android.graphics.Canvas;
	import android.graphics.Paint;
	import android.graphics.PointF;

	import com.sb9.foloke.sectorb9.game.Assets.ShipAsset;

	import com.sb9.foloke.sectorb9.game.Managers.GameManager;
import android.graphics.*;

public class Cursor extends DynamicEntity {
		

		private boolean drawable;

		public Cursor(float x, float y, String name, GameManager gameManager)
		{
			super(x,y,0, gameManager,0);
			this.image=ShipAsset.cursor;
			this.dx=this.dy=0;
			this.drawable=false;

		}

		@Override
		public void tick() {
			super.tick();
		}

		@Override
		public void render(Canvas canvas) {
			if(drawable)
			canvas.drawBitmap(image,x-image.getWidth()/2,y-image.getHeight()/2,new Paint());
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


	@Override
	public void onCollide(Entity e) {

	}

	public void setDrawable(boolean drawable) {
			this.drawable = drawable;
		}
	public void setImage(Bitmap img)
	{
		this.image=img;
	}
}

