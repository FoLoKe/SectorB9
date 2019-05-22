package com.sb9.foloke.sectorb9.game.Funtions;
import android.graphics.*;

public class LoadingScreen
{
	private static float rotation=0;
	public static void render(Canvas canvas)
	{
		
		float signSize=canvas.getWidth()/20;
		
		Paint p=new Paint();
		p.setColor(Color.CYAN);
		p.setStyle(Paint.Style.STROKE);
		p.setStrokeWidth(signSize/5);
		p.setStrokeMiter(1);
		p.setPathEffect(new DashPathEffect(new float[]{signSize*2*3.14f-signSize,signSize},0));
		
		
		canvas.save();
		canvas.rotate(rotation,canvas.getWidth()-signSize-signSize/5,canvas.getHeight()-signSize-signSize/5);
		canvas.drawCircle(canvas.getWidth()-signSize-signSize/5,canvas.getHeight()-signSize-signSize/5,signSize,p);
		canvas.restore();
	}
	
	public static void tick()
	{
		rotation+=10f;
	}
}
