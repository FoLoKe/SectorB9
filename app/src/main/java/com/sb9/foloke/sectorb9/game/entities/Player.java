package com.sb9.foloke.sectorb9.game.entities;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;

import static java.lang.Math.PI;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import android.graphics.*;
import com.sb9.foloke.sectorb9.game.display.*;

public class Player extends DynamicEntity {
    Bitmap engine;
    //float speed=3;
    boolean movable;
    private Text textdXdY;
	private float acceleration;
	private UIProgressBar uIhp;
	
	private Path collisionPath;
	private PointF collisionPoints[];
	private PointF collisionInitPoints[];
	
	private Game game;
    public Player(float x, float y, ImageAssets asset,Game game)
    {
        super(x,y,asset.player_mk1);
		this.game=game;
        this.engine=asset.engine_mk1;
		
        this.dx=this.dy=0;
        this.movable=false;
		this.renderable=true;
        textdXdY=new Text("",x-100,y-50);
		this.uIhp=new UIProgressBar(this,100,20,asset.asteroid_1,asset.asteroid_1,5);
		collisionInitPoints=new PointF[3];
		collisionInitPoints[0]=new PointF(0,-image.getHeight()/2);
		collisionInitPoints[1]=new PointF(-image.getWidth()/2,image.getHeight()/2);
		collisionInitPoints[2]=new PointF(image.getWidth()/2,image.getHeight()/2);
		collisionPoints=new PointF[collisionInitPoints.length];
		
		collisionPath=new Path();
		
		calculateCollisionObject();
		
		
		
    }

    @Override
    public void tick() {
        if(movable||true) {//no inertia damping
		UIProgressBar uIhp;
		boolean collisionFlag=false;
		DynamicEntity asteroids[]=game.getAsteroids();
		for (int i=0;i<collisionPoints.length;i++)
		for (int j=0;j<asteroids.length;j++)
		{
		if (asteroids[j].getCollsionBox().contains(collisionPoints[i].x,collisionPoints[i].y))
		{
			collisionFlag=true;
			asteroids[j].impulse(collisionPoints[i],dx,dy);
			
			break;
			}
			
		}
		
		
		if (!collisionFlag)
		{
           x += dx;
           y += dy;
           //dx = dy = 0;
			
		}
		else
		{
			x-=dx+1;
			y-=dy+1;
			dx=dx/2;
			dy=dy/2;
			
		}
			calculateCollisionObject();
           //this.collisionBox.set(x,y,x+image.getWidth(),y+image.getHeight());
        }
    }

    @Override
    public void render(Canvas canvas) {
		if(!renderable)
			return;
        canvas.save();
		
        canvas.rotate(rotation,getCenterX(),getCenterY());
        if(movable)
        canvas.drawBitmap(engine,x,y-5+(acceleration)*5,new Paint());
		
        canvas.drawBitmap(image,x,y,new Paint());
        canvas.restore();

        textdXdY.setString(acceleration+" ");
        textdXdY.setWorldLocation(new PointF(x,y));
        textdXdY.render(canvas);
		
		Paint tPaint=new Paint();
		tPaint.setColor(Color.rgb(0,255,0));
		tPaint.setStyle(Paint.Style.STROKE);
		canvas.drawPath(collisionPath,tPaint);
		
		//canvas.drawLine(getCenterX(),getCenterY(),collisionPoints[1].x,collisionPoints[1].y,tPaint);

    }

    @Override
    public void RotationToPoint(PointF targetPoint) {
      // rotation=(float)-Math.toDegrees(Math.PI+Math.atan2(targetPoint.x-x,targetPoint.y-y));   /coord rotation
    }

    


    public void addMovement(PointF screenPoint,float screenW, float screenH) {
        //float hundredPercent=dx+dy;
        if (movable) {
            //dx = dx / hundredPercent * speed;
            //dy = dy / hundredPercent * speed;
			
			float minAcceleration=screenH/8; //0%
			float maxAcceleration=screenH/2; //100%
			
			PointF relativePoint=new PointF(screenPoint.x-screenW/2,screenPoint.y-screenH/2);
			acceleration=(float)Math.sqrt(relativePoint.x*relativePoint.x+relativePoint.y*relativePoint.y);
			
			acceleration=(acceleration-minAcceleration)/(maxAcceleration-minAcceleration);
			if(acceleration<0)
				acceleration=0;
			if(acceleration>1)
				acceleration=1;
				
            float mathRotation=(float)(PI/180*rotation);
            rotation=360-(float)Math.toDegrees(Math.PI+Math.atan2(-screenW/2+screenPoint.x,-screenH/2+screenPoint.y)); //screen relative rotation
            //this.dx=(float)(screenH/2*cos(-rotation)-(-screenW/2+screenPoint.x*sin(-rotation)));
            this.dy = -(float) (acceleration*speed * cos(mathRotation));
            this.dx = (float) (acceleration*speed * sin(mathRotation));
			


        }
    }

    public void setMovable(boolean movable) {
        this.movable = movable;
    }
	
	private void calculateCollisionObject()
	{
		float mathRotation=(float)(PI/180*rotation);
		
		collisionPath.reset();
		for (int i=0;i<collisionPoints.length;i++)
		{
		float x1 = getCenterX()-collisionInitPoints[i].x - getCenterX();
		float y1 = getCenterY()+collisionInitPoints[i].y - getCenterY();

		float x2 = (float)(x1 * Math.cos(mathRotation) - y1 * Math.sin(mathRotation));
		float y2 = (float)(x1 * Math.sin(mathRotation) + y1 * Math.cos(mathRotation));

		if(collisionPoints[i]==null)
		collisionPoints[i]=new PointF(x2 + getCenterX(),y2+getCenterY());
		else
		collisionPoints[i].set(x2 + getCenterX(),y2+getCenterY());
		}
		
		//collisionPoints[1]=new PointF(-image.getWidth()/2*(float)sin(mathRotation),image.getHeight()/2*(float)cos(mathRotation));
		//collisionPoints[2]=new PointF(getCenterX()+image.getWidth()/2*(float)sin(mathRotation),getCenterY()+image.getHeight()/2*-(float)cos(mathRotation));
		collisionPath.moveTo(collisionPoints[0].x,collisionPoints[0].y);
		collisionPath.lineTo(collisionPoints[1].x,collisionPoints[1].y);
		collisionPath.lineTo(collisionPoints[2].x,collisionPoints[2].y);
		collisionPath.close();
	}
}
