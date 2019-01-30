package com.sb9.foloke.sectorb9.game.Entities;

import android.graphics.Bitmap;
import android.graphics.PointF;
import android.graphics.Canvas;
import android.graphics.*;

import com.sb9.foloke.sectorb9.MainActivity;
import com.sb9.foloke.sectorb9.game.Funtions.Line2D;
import com.sb9.foloke.sectorb9.game.Managers.GameManager;

import java.util.Vector;

import static java.lang.Math.PI;
import static java.lang.Math.abs;
import static java.lang.Math.cos;
import static java.lang.Math.sin;


public abstract class DynamicEntity extends Entity {

   	float dx,dy;
	static float speed=3;
	float acceleration;
	boolean movable;
	private PointF frontPoint=new PointF(1,0);

    DynamicEntity(float x, float y, float rotation, Bitmap image, String name, GameManager gameManager, int ID)
    {
        super(x,y,rotation,image,name, gameManager,ID);
        this.rotation=rotation;
		dx=dy=0;
    }

	
	public void drawVelocity(Canvas canvas)
	{
		if(!renderable)
			return;
		Paint tPaint=new Paint();
		tPaint.setColor(Color.rgb(255,255,0));
		tPaint.setStrokeWidth(5);
		canvas.drawLine(getCenterX(),getCenterY(),getCenterX()+dx*20,getCenterY()+dy*20,tPaint);
	}

	public void impulse(PointF pointOfimpulse,float dx,float dy)
	{
		
		float tdx=this.dx+dx;  //1000-100
		float tdy=this.dy+dy;  //500-100
        float accel=0;
        if(tdx!=0&&tdy!=0)
		accel=speed/(float)Math.sqrt(tdx*tdx+tdy*tdy); //1 of speed% 3/1300
	
		
		this.dx=tdx*accel;
		if(this.dx>speed)
			this.dx=speed;
		this.dy=tdy*accel;
		if(this.dy>speed)
			this.dy=speed;
	}

    @Override
    public void tick() {
       /////////
        super.tick();
        calculateCollisionObject();
        if(rotation>360)
            rotation=0;
        if(rotation<0)
            rotation=360;

        float mathRotation=(float)(Math.PI/180*getWorldRotation());
        float x1 =0 ;
        float y1 =-10 ;

        float x2 = (float)(x1 * Math.cos(mathRotation) - y1 * Math.sin(mathRotation));
        float y2 = (float)(x1 * Math.sin(mathRotation) + y1 * Math.cos(mathRotation));
        frontPoint.set(x2,y2);

        for (Entity e : getGameManager().getEntities())
        {
            if(e!=this)
            if(e.active)
            {
                if(e.collidable)
                {
                    if ((e.getCollisionObject().intersects(getCollisionObject())))
                    {
                        if(!(this instanceof Projectile)) {
                            onCollide(e);
                            break;
                        }
                        else
                        {
                            if(((Projectile) this).getParent()!=e) {
                                onCollide(e);
                                break;
                            }
                        }
                    }
                }
            }
        }

        if(getHp()<=0)
        {
            ////make on destroy image
            return;
        }
        //no inertia damping
        timerTick();

        x += dx;
        y += dy;
        dx = dy = 0;
    }

    public abstract void onCollide(Entity e);

    public boolean rotationToPoint(PointF point,float p)
	{
        PointF B=new PointF(getCenterX()-point.x,getCenterY()-point.y);
        float sinPhi = (frontPoint.x*B.y - frontPoint.y*B.x);

        if (sinPhi < -5)
            rotation++;
        if (sinPhi >=5)
            rotation--;

        if(sinPhi>-10-10*p&&sinPhi<10+10*p)
            return true;
        return false;
	}

	public void addMovement()
	{
	    acceleration=0.5f;
			float mathRotation=(float)(PI/180*rotation);
			 //screen relative rotation
			this.dy = -(float) (acceleration*speed * cos(mathRotation));
			this.dx = (float) (acceleration*speed * sin(mathRotation));

	}
	public float getAcceleration()
	{
		return acceleration;
	}

	public boolean getMoveable()
	{
		return movable;
	}

    @Override
    public void calculateCollisionObject() {
        transformMatrix.reset();
        transformMatrix.postRotate(rotation);
        super.calculateCollisionObject();
    }
}
