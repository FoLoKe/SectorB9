package com.sb9.foloke.sectorb9.game.Funtions;
import android.graphics.*;
import com.sb9.foloke.sectorb9.game.UI.CustomViews.*;

public class Joystick
{
	private RectF joystickBox=new RectF();
	private PointF joystickInitPoint=new PointF();
	private PointF joystickCurrentPoint=new PointF();
	private float joystickSafeZone=100;
	private float joystickSize=200;
	private PointF touchPoint=new PointF();
	//private PointF screenPointOfTouch=new PointF();
	private float joystickAcceleration=0;
	
	private Paint joystickPaint=new Paint();
	private Paint joystickBorderPaint=new Paint();
	private Paint joystickBgPaint=new Paint();
	private PointF stickPoint=new PointF();
	private boolean touched=false;
	
	public Joystick(RectF joystickZone)
	{
		joystickBgPaint.setColor(Color.GRAY);
		joystickBgPaint.setAlpha(200);
		joystickPaint.setColor(Color.DKGRAY);
		joystickPaint.setAlpha(200);
		joystickBorderPaint.setColor(Color.DKGRAY);
		joystickBorderPaint.setAlpha(200);
		joystickBorderPaint.setStyle(Paint.Style.STROKE);
		joystickBorderPaint.setStrokeWidth(5);

		
		joystickBox.set(joystickZone);
	}
	
	public void render(Canvas canvas)
	{
		if(touched)
		{
			canvas.drawCircle(joystickInitPoint.x,joystickInitPoint.y,joystickSize,joystickBgPaint);
			canvas.drawCircle(joystickInitPoint.x,joystickInitPoint.y,joystickSafeZone,joystickBorderPaint);
			canvas.drawCircle(stickPoint.x,stickPoint.y,75,joystickPaint);
		}
	}
	public void setTouched(boolean touched,PointF screenPoint)
	{
		this.touched=touched;
		joystickInitPoint.set(screenPoint.x,screenPoint.y);

		if(!joystickBox.contains(screenPoint.x,screenPoint.y))
			this.touched=false;
	}
	
	public void tick(PointF screenPointOfTouch)
	{
		joystickCurrentPoint.set(screenPointOfTouch.x,screenPointOfTouch.y);
		
		if(touched)
		{
			touchPoint.set(-joystickInitPoint.x+joystickCurrentPoint.x,-joystickInitPoint.y+joystickCurrentPoint.y);
			
			PointF tpoint=new PointF(-joystickInitPoint.x+joystickCurrentPoint.x,-joystickInitPoint.y+joystickCurrentPoint.y);
			float size=(float)Math.sqrt(tpoint.x*tpoint.x+tpoint.y*tpoint.y);
			float rl=1;

			if (size!=0&&size>200)
				rl=200/size;

			if(size>200)
			{
				size=200;
			}

			float minAcceleration=joystickSafeZone; //0%
			float maxAcceleration=joystickSize; //100%

			size=(size-minAcceleration)/(maxAcceleration-minAcceleration);
			joystickAcceleration=size;//200;

			stickPoint.set(joystickInitPoint.x+tpoint.x*rl,joystickInitPoint.y+tpoint.y*rl);
		}
	}
	
	public boolean getTouched()
	{
		return this.touched;
	}
	
	public RectF getActionZone()
	{
		return joystickBox;
	}
	
	public PointF getPoint()
	{
		
		return touchPoint;
	}
	
	public float getAcceleration()
	{
		return joystickAcceleration;
	}
	
}
