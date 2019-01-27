package com.sb9.foloke.sectorb9.game.UI.CustomViews;
import android.view.*;
import android.content.*;
import android.graphics.*;
import com.sb9.foloke.sectorb9.game.Funtions.*;

public class LineView extends View
{
	Paint paint;
	Line2D line;
	public LineView(Context context)
	{
		super(context);
		paint=new Paint();
		line=new Line2D(0,0,1,1);
		paint.setColor(Color.YELLOW);
		paint.setStrokeWidth(10);
		this.setWillNotDraw(false);
	}
	public void setLine(Line2D newLine)
	{
		line=newLine;
	}
	@Override
	protected void onDraw(Canvas canvas)
	{
		// TODO: Implement this method
		super.onDraw(canvas);
		canvas.drawLine(line.getFirstPoint().x,line.getFirstPoint().y
						,line.getSecondPoint().x,line.getSecondPoint().y,paint);
						int a=0;
	}
	
}
