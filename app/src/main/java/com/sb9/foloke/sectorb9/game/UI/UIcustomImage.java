package com.sb9.foloke.sectorb9.game.UI;
import android.graphics.*;
import com.sb9.foloke.sectorb9.game.display.Camera;

public class UIcustomImage
{
	Bitmap image;
	public UIcustomImage(Bitmap sheet,int imageX,int imageY,int sizeX,int sizeY)
	{
		image=Bitmap.createBitmap(sheet,imageX,imageY,sizeX,sizeY,null,false);
	}
	public void render(Canvas canvas,Camera camera)
	{
		canvas.save();
		canvas.scale(10,10);
		canvas.drawBitmap(image,camera.getScreenXcenter(),camera.getScreenYcenter(),null);
		canvas.restore();
	}
	public void render(Canvas canvas)
	{
		canvas.save();
		canvas.scale(20,20);
		canvas.drawBitmap(image,canvas.getWidth()/(2*20)-image.getWidth()/2,canvas.getHeight()/(2*20)-image.getHeight()/2,null);
		canvas.restore();
	}
	public void tick()
	{
		
	}
}
