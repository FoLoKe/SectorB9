package com.sb9.foloke.sectorb9.game.funtions;
import android.graphics.*;
import android.animation.*;

public class Line2D
{
	private float x1,y1,x2,y2;
	float intersectionX=0,intersectionY=0;
	float tintersectionX=-1,tintersectionY=-1;
	Paint paint;
	public Line2D(float x1,float y1,float x2,float y2)
	{
		this.x1=x1;
		this.x2=x2;
		this.y2=y2;
		this.y1=y1;
		paint =new Paint();
		paint.setColor(Color.GREEN);
	}
	public boolean intersect(RectF rect) {

		// check if the line has hit any of the rectangle's sides
		// uses the Line/Line function below
		tintersectionX=tintersectionY=-1;
		boolean left =   lineLine(x1,y1,x2,y2, rect.left				,rect.top				,rect.left				,rect.top+rect.height());
		boolean right =  lineLine(x1,y1,x2,y2, rect.left+rect.width()	,rect.top				,rect.left+rect.width()	,rect.top+rect.height());
		boolean top =    lineLine(x1,y1,x2,y2, rect.left				,rect.top				,rect.left+rect.width()	,rect.top);
		boolean bottom = lineLine(x1,y1,x2,y2, rect.left				,rect.top+rect.height()	,rect.left+rect.width()	,rect.top+rect.height());

		// if ANY of the above are true, the line
		// has hit the rectangle
		if (left || right || top || bottom) {
			resetLineMaxLenght();
			return true;
		}
		resetLineMaxLenght();
		return false;
		
	}


// LINE/LINE
	public boolean lineLine(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4) {

		// calculate the direction of the lines
		float uA = ((x4-x3)*(y1-y3) - (y4-y3)*(x1-x3)) / ((y4-y3)*(x2-x1) - (x4-x3)*(y2-y1));
		float uB = ((x2-x1)*(y1-y3) - (y2-y1)*(x1-x3)) / ((y4-y3)*(x2-x1) - (x4-x3)*(y2-y1));

		// if uA and uB are between 0-1, lines are colliding
		if (uA >= 0 && uA <= 1 && uB >= 0 && uB <= 1) {

		
			// optionally, draw a circle where the lines meet
			float tfintersectionX = x1 + (uA * (x2-x1));
			float tfintersectionY = y1 + (uA * (y2-y1));
			double a =Math.sqrt((tfintersectionX-x1)*(tfintersectionX-x1)+(tfintersectionY-y1)*(tfintersectionY-y1));
			double b=Math.sqrt((tintersectionX-x1)*(tintersectionX-x1)+(tintersectionY-y1)*(tintersectionY-y1));
			if(tintersectionX!=-1)
			if(b>a)
			{
			intersectionX=tintersectionX=tfintersectionX;
			intersectionY=tintersectionY=tfintersectionY;
			}
			else 
				return false;
				else
				{
					intersectionX=tintersectionX=tfintersectionX;
					intersectionY=tintersectionY=tfintersectionY;
					return true;
				}
			//ellipse(intersectionX, intersectionY, 20, 20);

			return true;
		}
		return false;
	}
	public boolean lineLine(Line2D line) {
		//calculate the direction of the lines
		float uA = ((line.getSecondPoint().x-line.getFirstPoint().x)*(y1-line.getFirstPoint().y) - (line.getSecondPoint().y-line.getFirstPoint().y)*(x1-line.getFirstPoint().x)) / ((line.getSecondPoint().y-line.getFirstPoint().y)*(x2-x1) - (line.getSecondPoint().x-line.getFirstPoint().x)*(y2-y1));
		float uB = ((x2-x1)*(y1-line.getFirstPoint().y) - (y2-y1)*(x1-line.getFirstPoint().x)) / ((line.getSecondPoint().y-line.getFirstPoint().y)*(x2-x1) - (line.getSecondPoint().x-line.getFirstPoint().x)*(y2-y1));

		// if uA and uB are between 0-1, lines are colliding
		if (uA >= 0 && uA <= 1 && uB >= 0 && uB <= 1) {

			// optionally, draw a circle where the lines meet
			/*intersectionX = x1 + (uA * (x2-x1));
			intersectionY = y1 + (uA * (y2-y1));*/

			tintersectionX = x1 + (uA * (x2-x1));
			tintersectionY = y1 + (uA * (y2-y1));
			if(Math.sqrt(tintersectionX*tintersectionX+tintersectionY+tintersectionY)>Math.sqrt(tintersectionX*intersectionX+intersectionY*intersectionY))
			{
				intersectionX=tintersectionX;
				intersectionY=tintersectionY;
			}
			//ellipse(intersectionX, intersectionY, 20, 20);

			return true;
		}
		return false;
	}
	
	
	public void render(Canvas canvas )
	{
		
		canvas.drawLine(x1,y1,x2,y2,paint);
		
		canvas.drawCircle(intersectionX, intersectionY, 2,paint);
	}
	public void set(float x1,float y1,float x2,float y2)
	{
		this.x1=x1;
		this.x2=x2;
		this.y2=y2;
		this.y1=y1;
	}
	public PointF getPoint()
	{
		return new PointF(intersectionX,intersectionY);
	}
	public PointF getFirstPoint()
	{
		return( new PointF(x1,y1));
	}
	public PointF getSecondPoint()
	{
		return( new PointF(x2,y2));
	}
	public void setThickness(int thikness)
	{
		paint.setStrokeWidth(thikness);
	}
	public void resetLineMaxLenght()
	{
		tintersectionX=tintersectionY=-1;
	}
}
