package com.sb9.foloke.sectorb9.game.Entities;


import android.graphics.PointF;
import android.graphics.Canvas;
import android.graphics.*;

import com.sb9.foloke.sectorb9.game.Assets.ShipAsset;
import com.sb9.foloke.sectorb9.game.Managers.GameManager;


public abstract class DynamicEntity extends Entity {

   	float dx,dy;
	float speed=1;
	float acceleration=0;
	float targetAcceleration=0;
	float rotationSpeed=0.01f;
	boolean movable;
	private PointF frontPoint=new PointF(1,0);
	private float frontAcceleration=0.01f;
	private float frontDeceleration=0.01f;
	protected boolean inBounds=true;

    DynamicEntity(float x, float y, float rotation,  GameManager gameManager, int ID)
    {
        super(x,y,rotation,gameManager,ID);
		dx=dy=0;
    }
	
	public void drawVelocity(Canvas canvas)
	{
		if(!renderable)
			return;
		Paint tPaint=new Paint();
		tPaint.setColor(Color.rgb(255,255,0));
		tPaint.setStrokeWidth(5);
		canvas.drawLine(getCenterX(),getCenterY(),getCenterX()+frontPoint.x*acceleration*20,getCenterY()+frontPoint.y*acceleration*20,tPaint);
	}

	private void calculateMovement()
	{
		if(acceleration<targetAcceleration)
			acceleration+=frontAcceleration;
		else
			acceleration-=frontDeceleration;
			
		if(acceleration<0)
			acceleration=0;
		//if(acceleration>1)
		//	acceleration=1;

		
		this.dy =  (acceleration*speed * this.frontPoint.y);
		this.dx = (acceleration*speed *this.frontPoint.x);
		if(targetAcceleration>0)
		targetAcceleration-=0.1;
		
		
	}
	
    @Override
    public void tick() {
        super.tick();

        transformMatrix.reset();
        transformMatrix.postRotate(rotation);
		
		calculateMovement();
		
        if(rotation>360)
            rotation=0;
        if(rotation<0)
            rotation=360;

        float[] vector =new float[]{0,-1};
        transformMatrix.mapVectors(vector);

        x += dx;
        y += dy;
		dx = dy = 0;
		calculateCollisionObject();

		if(inBounds) {
            if (x < 0)
                x = 0;
            if (y < 0)
                y = 0;
            if (x > gameManager.getGamePanel().getWorldSize())
                x = gameManager.getGamePanel().getWorldSize();
            if (y > gameManager.getGamePanel().getWorldSize())
                y = gameManager.getGamePanel().getWorldSize();
        }
        frontPoint.set(vector[0],vector[1]);
		
        checkCollision();
		
		x += dx;
        y += dy;
        dx = dy = 0;
        timerTick();

    }

    protected void checkCollision()
    {

        for (Entity e : getGameManager().getEntities())
        {
            if(e!=this)
            {
                if(e.active)
                {
                    if(e.collidable)
                    {
                        if ((e.getCollisionObject().intersects(getCollisionObject())))
                        {
                            if(!(this instanceof Projectile))
                            {
                                onCollide(e);

                            }
                            else
                            {
                                if(((Projectile) this).getParent()!=e)
                                {
                                    onCollide(e);

                                }
                            }
                        }
                    }
                }
            }

        }
    }

    public void onCollide(Entity e)
	{
		impulse(e);
	}
	
	private void impulse(Entity e)
	{

		float midpointx = (e.getCollisionObject().getCenterX() +getCollisionObject().getCenterX()) / 2; 
		float midpointy = (e.getCollisionObject().getCenterY() +getCollisionObject().getCenterY()) / 2;

		gameManager.getGamePanel().textDebug2.setString(""+midpointx);
		gameManager.getGamePanel().textDebug3.setString(""+midpointy);
		float dist=(float)Math.sqrt((e.getCenterX()-getCenterX())*(e.getCenterX()-getCenterX())+(e.getCenterY()-getCenterY())*(e.getCenterY()-getCenterY()));

		dist=(float)Math.max(0.1,dist);

		float rad=(getCollisionObject().getRadius()+ e.getCollisionObject().getRadius())/2;
		float newcenterX=(midpointx +rad * (getCollisionObject().getCenterX() - e.getCollisionObject().getCenterX()) / dist);
		float newcenterY=(midpointy + rad * (getCollisionObject().getCenterY() - e.getCollisionObject().getCenterY())/ dist);

		dx+=-getCenterX()+newcenterX;
		dy+=-getCenterY()+newcenterY;


	}
	
    public boolean rotationToPoint(PointF point)
	{
        PointF B=new PointF(getCenterX()-point.x,getCenterY()-point.y);
		float BLength=(float)Math.sqrt(B.x*B.x+B.y*B.y);
		B.set(B.x/BLength,B.y/BLength);
        float sinPhi = (frontPoint.x*B.y - frontPoint.y*B.x);
		
		if(sinPhi>-0.02&&sinPhi<0.02)
            return true;
		sinPhi*=100;
		if(Math.abs(sinPhi)<rotationSpeed)
		{
			rotation-=sinPhi/2;
		}
		else
		{
    	    if (sinPhi < 0)
          		rotation+=rotationSpeed;
        	if (sinPhi >=0)
				rotation-=rotationSpeed;
		}
       
        return false;
	}

	public void addMovement(float accel)
	{
		if(movable)
		targetAcceleration=accel;
	}
	
	public float getAcceleration()
	{
		return targetAcceleration;
	}

	public boolean getMoveable()
	{
		return movable;
	}

    @Override
    public void calculateCollisionObject() {
        
        super.calculateCollisionObject();
    }

	@Override
	public void render(Canvas canvas)
	{
		if(!renderable)
			return;
        if((SH<maxSH||HP<maxHP)&&drawHp)
        {
            uIsh.render(canvas);
            uIhp.render(canvas);
        }
        if(SH>0&&shieldShow.getTick()>0)
        canvas.drawBitmap(shieldImg,getCenterX()-shieldImg.getWidth()/2,getCenterY()-shieldImg.getHeight()/2,shieldpaint);
		if(gameManager.drawDebugInfo)
			drawVelocity(canvas);
	}
	
	public void setSpeed(float speed)
	{
		this.speed=speed;
	}

	public void setRotationSpeed(float rotationSpeed)
	{
		this.rotationSpeed=rotationSpeed;
	}
	
	public void setFrontAcceleration(float frontAcceleration)
	{
		this.frontAcceleration=frontAcceleration;
	}
	
	public void setFrontDeceleration(float frontDeceleration)
	{
		this.frontDeceleration=frontDeceleration;
	}
}