package com.sb9.foloke.sectorb9.game.Funtions;
import android.graphics.*;
import com.sb9.foloke.sectorb9.game.Entities.*;

public class CustomCollisionObject
{
	///calculations is too heavy make rectangles
	private float width,height,offsetX,offsetY;
	private RectF collisionBox;
	//private PointF collisionPoints[];
	//private PointF collisionInitPoints[];
	//private Line2D collisionlines[];
	//private Entity parent;
	private Paint paint;

	/*public CustomCollisionObject(PointF collisionInitPoints[],Entity parent)
	{
		this.parent=parent;
		this.collisionInitPoints=collisionInitPoints;

		collisionlines=new Line2D[collisionInitPoints.length];
        for(int i=0;i< collisionInitPoints.length;i++)
            collisionlines[i]=new Line2D(0,0,0,0);

		collisionPoints=new PointF[collisionInitPoints.length];

        for (int i=0;i<collisionPoints.length;i++)
        {
            float x = collisionInitPoints[i].x;
            float y = collisionInitPoints[i].y;


            if(collisionPoints[i]==null)
                collisionPoints[i]=new PointF(x + parent.getCenterX(),y+parent.getCenterY());
            else
                collisionPoints[i].set(x + parent.getCenterX(),y+parent.getCenterY());
        }
	}*/
	
	public CustomCollisionObject(float widht,float heigth,float offsetX,float offsetY)
	{
		this.width=widht;
		this.height=heigth;
		this.offsetX=offsetX;
		this.offsetY=offsetY;
		collisionBox=new RectF();
		paint=new Paint();
		paint.setColor(Color.GREEN);
		paint.setStyle(Paint.Style.STROKE);
	}
	
	public void calculateCollisionObject(float x,float y)
	{
		
		collisionBox.set(x+offsetX,y+offsetY,x+width+offsetX,y+height+offsetY);
	}
	
	public boolean intersect(RectF obj)
	{
		
		return collisionBox.intersects(obj,collisionBox);
		
	}
	
	public boolean intersects(CustomCollisionObject obj)
	{
		return RectF.intersects(obj.getCollisionBox(),collisionBox);
	}
	
	public RectF getCollisionBox()
	{
		return collisionBox;
	}
	public void render(Canvas canvas)
	{
		canvas.drawRect(collisionBox,paint);
	}
	/*public void calculateCollisionObject(Matrix m)
	{

		
		for (int i=0;i<collisionInitPoints.length;i++)
		{
		    float[] temp={0,0};
		    float[] init={collisionInitPoints[i].x,collisionInitPoints[i].y};
            m.mapVectors(temp,init);
				collisionPoints[i].set(temp[0]+ parent.getCenterX(),temp[1]+parent.getCenterY());

		}
        for(int i=0;i< collisionPoints.length;i++)
        {
            if(i<collisionInitPoints.length-1)//is not final point
                collisionlines[i].set(collisionPoints[i].x,collisionPoints[i].y
                        ,collisionPoints[i+1].x,collisionPoints[i+1].y);
            else
                collisionlines[i].set(collisionPoints[i].x,collisionPoints[i].y
                        ,collisionPoints[0].x,collisionPoints[0].y);
        }
	

	}
	
	public boolean intersect(RectF rect)
	{
		
		for(Line2D l: collisionlines)
		{
			if(l.intersect(rect))
				return true;
				
		}
		return false;
		
	}
	public boolean intersect(Line2D line)
	{
		double timecheck=System.nanoTime();
		boolean collision=false;
		for(Line2D l: collisionlines)
		{

			if(l.lineLine(line))
				collision=true;

		}
		parent.getGameManager().getGamePanel().textDebug2.setString(System.nanoTime()-timecheck+"ms");
		return  collision;
		

	}
	public void render(Canvas canvas)
	{
		for(Line2D l: collisionlines)
		{
			l.render(canvas);
		}
	}

	public boolean intersects(CustomCollisionObject o)
    {
        for (Line2D l: collisionlines) {
            if ((o.intersect(l))) {
                return true;
            }
        }
        return false;
    }

	public Line2D[] getCollisionlines() {
		return collisionlines;
	}*/
}
