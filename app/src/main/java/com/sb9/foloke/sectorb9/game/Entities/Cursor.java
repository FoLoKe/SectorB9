package com.sb9.foloke.sectorb9.game.Entities;

	import android.graphics.Canvas;
	import android.graphics.Paint;
	import android.graphics.PointF;

	import com.sb9.foloke.sectorb9.game.Assets.ShipAsset;

    import com.sb9.foloke.sectorb9.game.Funtions.CustomCollisionObject;
    import com.sb9.foloke.sectorb9.game.Managers.GameManager;
import android.graphics.*;

public class Cursor extends DynamicEntity {
		

		private boolean drawable;
        private boolean collided=false;
        private Paint buildPaint=new Paint();
		public Cursor(float x, float y, String name, GameManager gameManager)
		{
			super(x,y,0, gameManager,0);
			this.image=ShipAsset.cursor;
			this.dx=this.dy=0;
			this.drawable=false;
            buildPaint.setColor(Color.RED);
            buildPaint.setStrokeWidth(5);
            buildPaint.setStyle(Paint.Style.STROKE);
		}
    public void calculateCollisionObject()
    {
        collisionObject.calculateCollisionObject(x,y);
    }
		@Override
		public void tick() {
            collided=false;
            calculateCollisionObject();
            super.tick();


		}

		@Override
		public void render(Canvas canvas) {
			if(drawable)
			canvas.drawBitmap(image,x-image.getWidth()/2,y-image.getHeight()/2,new Paint());


			if(collided)
			    canvas.drawCircle(x,y,collisionObject.getRadius(),buildPaint);
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
        collided=true;
	}

	public void setDrawable(boolean drawable) {
			this.drawable = drawable;
		}
	public void setImage(Bitmap img)
	{
	    this.image=img;
        collisionObject=new CustomCollisionObject(img.getWidth(),img.getHeight(),gameManager);
        calculateCollisionObject();
	}

	public boolean onBuild()
    {
        for (Entity e : getGameManager().getEntities())
        {
            if(e!=this)
            {
                if(e.active)
                {
                    if(e.collidable)
                    {
                        if ((e.getCollisionObject().intersects(getCollisionObject())))
                        {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }
}

