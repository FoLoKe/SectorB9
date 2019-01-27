package com.sb9.foloke.sectorb9.game.Funtions;
import android.graphics.*;

public class Animation
{
	Bitmap images[];
	int speed;
	int frame=60;
	int currentImage=0;
	public Animation(Bitmap images[],int speed)
	{
		this.images=images;
		this.speed=speed;
	}
	public void tick()
	{
		frame=frame-speed;
		if(frame<=0)
		{
			frame=60;
			currentImage++;
			if(currentImage>=images.length)
				currentImage=0;
		}
	}
	public Bitmap getImage()
	{
		return images[currentImage];
	}
}
