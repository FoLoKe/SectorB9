package com.sb9.foloke.sectorb9.game.entities;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import static java.lang.Math.PI;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import android.graphics.*;
import com.sb9.foloke.sectorb9.game.Assets.ShipAsset;
import com.sb9.foloke.sectorb9.game.Assets.UIAsset;
import com.sb9.foloke.sectorb9.game.UI.UIProgressBar;
import com.sb9.foloke.sectorb9.game.display.*;

import com.sb9.foloke.sectorb9.game.funtions.*;
import com.sb9.foloke.sectorb9.game.entities.Ships.*;
import com.sb9.foloke.sectorb9.game.UI.*;

public class Player extends DynamicEntity
{
	private UIProgressBar stun;
	public boolean shootFlag;

	private Ship ship;
	private int interactionRadius=150;
    public Player(float x, float y,float rotation, Game game,String name)
    {
        super(x,y,rotation,ShipAsset.player_mk1,name,game,1);
		this.ship=new ShipMk1(this);
        this.dx=this.dy=0;
        this.movable=false;
		this.renderable=true;
		this.stun=new UIProgressBar(this,50,8,-25,+image.getHeight(),UIAsset.stunBackground,UIAsset.stunLine,UIAsset.progressBarBorder,getTimer());
		inventoryMaxCapacity=10;
		for (int i=0;i<inventory.getHeight();i++)
		for(int j=0;j<inventory.getWidth();j++)
				inventory.addNewItem(i*inventory.getWidth()+j,i*inventory.getWidth()+j);
		calculateCollisionObject();
		game.mContext.shipUI=new ShipUI(this,game.mContext);
    }

    @Override
    public void tick() 
	{
		if(getHp()<=0)
		{
			getHolder().setPlayerDestroyed(true);
			return;
		}
        //no inertia damping
		timerTick();
		for (Entity e : getHolder().getEntities())
		{
			if(e.active)
			{
				if(e.collidable)
				{
					for (Line2D l: ship.getCollisionLines())
					{
						if(!e.isUsingCustomCollision)
						{
						if ((l.intersect(e.getCollisionBox())))
							{
								if (e.getClass().equals(DynamicEntity.class))
									((DynamicEntity)e).impulse(new PointF(0,0),dx,dy);
								impulse(e);
								break;
							}
						}
						else
						{
							if ((e.getCustomCollisionObject().intersect(l)))
							{
								if (e.getClass().equals(DynamicEntity.class))
									((DynamicEntity)e).impulse(new PointF(0,0),dx,dy);
								impulse(e);
								break;
							}
						}
					}
				}
			}
		}
        x += dx;
        y += dy;
        dx = dy = 0;
		
		calculateCollisionObject();
		stun.tick(getTimer());
		Shoot();
		ship.tick();
    }
	
	private void impulse(Entity e)
	{
		float trotation=360-(float)Math.toDegrees(Math.PI+Math.atan2(-e.getCenterX()+x,-e.getCenterY()+y)); 
		float mathRotation=(float)(PI/180*trotation);
		this.dy = -(float) (0.02*speed * cos(mathRotation));
		this.dx = (float) (0.02*speed * sin(mathRotation));
	}

    @Override
    public void render(Canvas canvas) 
	{
		if(!renderable)
			return;
			
        canvas.save();
		ship.render(canvas);
		stun.render(canvas);
		
		if(getGame().command==Game.commandInteraction)
			drawInteractionCircle(canvas);
		
		//renew acceleration after drawing in debug
		acceleration=0;
    }

    @Override
    public void RotationToPoint(PointF targetPoint) {
		// rotation=(float)-Math.toDegrees(Math.PI+Math.atan2(targetPoint.x-x,targetPoint.y-y));   /coord rotation
	}

    


    public void addMovement(PointF screenPoint,float screenW, float screenH) 
	{
        if (movable&&(getTimer()==0)) 
		{
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
            this.dy = -(float) (acceleration*speed * cos(mathRotation));
            this.dx = (float) (acceleration*speed * sin(mathRotation));
        }
    }

    public void setMovable(boolean movable) 
	{
        this.movable = movable;
    }
	
	//custom collision
	@Override
	public void calculateCollisionObject()
	{
		ship.calculateCollisionObject();
	}
	
	private void Shoot()
	{
		if(shootFlag)
		{
			ship.shoot();
		}
	}
	
	private void drawInteractionCircle(Canvas canvas)
	{
		Paint tPaint = new Paint();
		tPaint.setColor(Color.YELLOW);
		tPaint.setStyle(Paint.Style.STROKE);
		tPaint.setStrokeWidth(2);
		tPaint.setPathEffect(new DashPathEffect(new float[] { 15, 16}, 0));
		canvas.drawCircle(getCenterX(),getCenterY(),interactionRadius,tPaint);
	}
	public void setShip()
	{
		if (ship.getClass()==ShipMk1.class)
			ship=new ShipMk2(this);
		else
			ship=new ShipMk1(this);
	}
	public int getRadius()
	{
		return interactionRadius;
	}
}
