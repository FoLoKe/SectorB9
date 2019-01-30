package com.sb9.foloke.sectorb9.game.Entities;

	import android.graphics.Canvas;
    import android.graphics.PointF;

	import static java.lang.Math.PI;
	import static java.lang.Math.cos;
	import static java.lang.Math.sin;

	import com.sb9.foloke.sectorb9.game.Managers.GameManager;
	import com.sb9.foloke.sectorb9.game.DataSheets.BuildingsDataSheet;

public class Asteroid extends StaticEntity {

    final static int ID=8;
		
	public Asteroid(float x, float y, float rotation, GameManager gameManager)
	{
		super(x,y,rotation, BuildingsDataSheet.findById(ID).image, BuildingsDataSheet.findById(ID).name, gameManager,ID);

	}
	
		@Override
		public void tick() {
	    super.tick();

			if(active)
			{
			if(!renderable)
				return;
			if(getHp()<=0)
			{
					active=false;
					//onDestroy();
					return;
					}
			uIhp.tick(getHp());
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
				if(gameManager.drawDebugInfo)
					drawDebugCollision(canvas);
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


	
}
