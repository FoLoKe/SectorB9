package com.sb9.foloke.sectorb9.game.UI;
import android.graphics.*;
import com.sb9.foloke.sectorb9.game.display.Camera;
import com.sb9.foloke.sectorb9.game.Assets.*;

public class UIcustomImage
{
	private Bitmap image;
	public UIcustomImage(UIAsset sheet,int index)
	{
		this.image=sheet.getByIndex(index);
		index=0;
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
