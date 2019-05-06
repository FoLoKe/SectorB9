package com.sb9.foloke.sectorb9.game.UI.TechUIs;
import android.view.*;
import android.graphics.*;
import com.sb9.foloke.sectorb9.game.Funtions.*;
import android.content.*;
import android.widget.*;

public class TechUILink extends View
{
	Paint paint;
	PointF start=new PointF();
	PointF end=new PointF();
	PointF firstCorner=new PointF();
	PointF secondCorner=new PointF();
	View target,scource;
	public TechUILink(Context context,View target,View scource)
	{
		super(context);
		paint=new Paint();
		this.target=target;
		this.scource=scource;
		paint.setColor(Color.parseColor("#55ffffff"));
		paint.setStrokeWidth(10);
		this.setWillNotDraw(false);
	}
	
	public void updatePos()
	{
		if(target==null)
		return;
		AbsoluteLayout.LayoutParams tparams=(AbsoluteLayout.LayoutParams)target.getLayoutParams();
		AbsoluteLayout.LayoutParams sparams=(AbsoluteLayout.LayoutParams)scource.getLayoutParams();
		start.set(sparams.x+sparams.width,sparams.y+sparams.height/2);
		end.set(tparams.x,tparams.y+tparams.height/2);
		firstCorner.set(start.x+(end.x-start.x)/2,start.y);
		secondCorner.set(start.x+(end.x-start.x)/2,end.y);
	}
	
	public void updateState()
	{
		if(TechTree.getTech(((TechUIObject)target).techID).isAvailable())
		{
			if(TechTree.getTech(((TechUIObject)target).techID).isOpened())
				paint.setColor(Color.parseColor("#5500ff00"));
			else
				paint.setColor(Color.parseColor("#55ffff00"));
		}
		else		
			paint.setColor(Color.parseColor("#55ffffff"));
		invalidate();
	}
	
	@Override
	protected void onDraw(Canvas canvas)
	{
		// TODO: Implement this method
		super.onDraw(canvas);
		canvas.drawLine(start.x,start.y,firstCorner.x,firstCorner.y,paint);
		canvas.drawLine(firstCorner.x,firstCorner.y,secondCorner.x,secondCorner.y,paint);
		canvas.drawLine(secondCorner.x,secondCorner.y,end.x,end.y,paint);
	}
}
