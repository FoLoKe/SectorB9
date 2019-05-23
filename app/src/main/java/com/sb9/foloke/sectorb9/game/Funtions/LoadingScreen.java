package com.sb9.foloke.sectorb9.game.Funtions;
import android.graphics.*;

public class LoadingScreen
{
	private static float rotation=0;
	private static int alpha=255;
	private static boolean desc=true;
	public static void render(Canvas canvas)
	{
		Path path=new Path();
		
		float signSize=canvas.getWidth()/50;
		path.lineTo(0,0);
		path.lineTo(1*signSize,2*signSize);
		path.lineTo(2*signSize,0*signSize);
		path.lineTo(1*signSize,0.5f*signSize);
		path.close();
		
		path.offset(canvas.getWidth()-1*signSize,canvas.getHeight()-1*signSize);
		Paint p=new Paint();
		p.setColor(Color.CYAN);
		p.setStyle(Paint.Style.STROKE);
		p.setStrokeWidth(signSize/2);
		p.setStrokeMiter(1);
		p.setTextSize(100);
		//p.setPathEffect(new DashPathEffect(new float[]{signSize*2*3.14f-signSize,signSize},0));
		
		
		canvas.save();
		canvas.translate(-1.5f*signSize,-1.5f*signSize);
		canvas.rotate(rotation,canvas.getWidth()-0.1f*signSize,canvas.getHeight()-0.1f*signSize);
		
		//canvas.rotate(rotation,canvas.getWidth()-signSize-signSize/5,canvas.getHeight()-signSize-signSize/5);
		//canvas.drawCircle(canvas.getWidth()-signSize-signSize/5,canvas.getHeight()-signSize-signSize/5,signSize,p);
		canvas.drawPath(path,p);
		canvas.restore();
		
		p.setStyle(Paint.Style.FILL);
		p.setAlpha(alpha);
		canvas.drawText("LOADING PLEASE WAIT...",canvas.getWidth()/3,canvas.getHeight()/2-100,p);
	}
	
	public static void tick()
	{
		rotation+=1f;
		
		if(desc)
			alpha-=5;
		else
			alpha+=5;
		
		if(alpha<=0)
		{
			alpha=0;
			desc=false;
		}
		if(alpha>=255)
		{
			alpha=255;
			desc=true;
		}
		
	}
}
