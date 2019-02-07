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
	static float speed=1;
	float acceleration=0;
	float targetAcceleration=0;
	float deceleraton=0.05f;
	boolean movable;
	PointF frontPoint=new PointF(1,0);

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
        if(tdx!=0||tdy!=0)
		accel=speed/(float)Math.sqrt(tdx*tdx+tdy*tdy); //1 of speed% 3/1300
	
		
		this.dx=tdx*accel;
		if(this.dx>speed)
			this.dx=speed;
		this.dy=tdy*accel;
		if(this.dy>speed)
			this.dy=speed;
	}

	private void calculateMovement()
	{
		if(acceleration<targetAcceleration)
			acceleration+=0.01;
		else
			acceleration-=0.01;
			
		if(acceleration<0)
			acceleration=0;
		if(acceleration>1)
			acceleration=1;

		
		this.dy =  (acceleration*speed * this.frontPoint.y);
		this.dx = (acceleration*speed *this.frontPoint.x);
		targetAcceleration-=0.05;
		
		
	}
	
    @Override
    public void tick() {
       /////////
		
        super.tick();
		
		
        calculateCollisionObject();
		
		calculateMovement();
		
        if(rotation>360)
            rotation=0;
        if(rotation<0)
            rotation=360;

   
        float[] vector =new float[]{0,-1};
        transformMatrix.mapVectors(vector);

        
        frontPoint.set(vector[0],vector[1]);
		
        for (Entity e : getGameManager().getEntities())
        {
			double timecheck=System.nanoTime();
			
            if(e!=this)
			{
				
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
						gameManager.getGamePanel().textDebug2.setString(System.nanoTime()-timecheck+"ms");
                	}
            	}
			}
        }

  
       
        timerTick();

        x += dx;
        y += dy;
        dx = dy = 0;
		if(x<0)
			x=0;
		if(y<0)
			y=0;
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
		
		targetAcceleration=1;
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

	@Override
	public void render(Canvas canvas)
	{
		if(!renderable)
			return;
		if(this.HP<this.maxHp)
			uIhp.render(canvas);
		if(gameManager.drawDebugInfo)
			drawVelocity(canvas);
	}

	
}
