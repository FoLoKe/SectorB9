package com.sb9.foloke.sectorb9.game.entities;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;

import static java.lang.Math.PI;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import android.graphics.*;

import com.sb9.foloke.sectorb9.game.Assets.ImageAssets;
import com.sb9.foloke.sectorb9.game.Assets.UIAsset;
import com.sb9.foloke.sectorb9.game.UI.Text;
import com.sb9.foloke.sectorb9.game.UI.UIProgressBar;
import com.sb9.foloke.sectorb9.game.display.*;
import com.sb9.foloke.sectorb9.game.ParticleSystem.*;
import java.util.HashMap;
import java.sql.*;
import com.sb9.foloke.sectorb9.game.funtions.*;
import org.apache.http.impl.client.*;
import com.sb9.foloke.sectorb9.game.entities.Ships.*;

public class Player extends DynamicEntity {
	//TODO: ENGINE PARTICLESPOOL SMOKE OR MAKE ANIMATION
	
    //private Bitmap engine;
   // private Bitmap projectileImage;
	
    //private Text textdXdY;
	
	private UIProgressBar stun;
	
	
	
	public boolean shootFlag;
	
	private boolean drawInteractionCitcle=false;
	Ship ship;
	
    public Player(float x, float y,float rotation, ImageAssets asset, UIAsset uiasset, Game game,String name)
    {
        super(x,y,rotation,asset.player_mk1,name,game);		
		
        //this.engine=asset.engine_mk1;
		//projectileImage=asset.cursor;
		
		
		
		ship=new ShipMk1(asset,this);
        this.dx=this.dy=0;
        this.movable=false;
		this.renderable=true;
        //this.textdXdY=new Text("",x-100,y-50);
		//this.uIhp=new UIProgressBar(this,50,8,-25,-20,uiasset.hpBackground,uiasset.hpLine,getHp());
		this.stun=new UIProgressBar(this,50,8,-25,+image.getHeight(),uiasset.stunBackground,uiasset.stunLine,uiasset.progressBarBorder,getTimer());
		inventoryMaxCapacity=10;
		for (int i=1;i<4;i++)
			inventory.put(i,10);
		
		
		//Point particleAccuracy=new Point(40,40);
		//this.engineSmoke=new ParticleSystem(asset.asteroid_1,x,y,1,particleAccuracy);
		
		
		
		
		calculateCollisionObject();
		
    }

    @Override
    public void tick() {
		if(getHp()<=0)
		{
			getHolder().setPlayerDestroyed(true);
			return;
		}
        //no inertia damping
		timerTick();
		//engineSmoke.tick();
		//engineSmoke.setWorldLocation(x,y);
		
		
		for (Entity e : getHolder().getEntities())
		{
			if(e.active)
			{
				if(e.collidable)
				{
					for (Line2D l: ship.collisionlines)
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
		//uIhp.tick(getHp());
		stun.tick(getTimer());
		
			Shoot();
		ship.tick();
		
		//acceleration=0;
			
    }
	
	public void impulse(Entity e)
	{
		float trotation=360-(float)Math.toDegrees(Math.PI+Math.atan2(-e.getCenterX()+x,-e.getCenterY()+y)); 
		
		textSpeed.setString(""+trotation);
		float mathRotation=(float)(PI/180*trotation);
		this.dy = -(float) (1*speed * cos(mathRotation));
		this.dx = (float) (1*speed * sin(mathRotation));
		 //1 of speed% 3/1300
		
	}

    @Override
    public void render(Canvas canvas) {
		if(!renderable)
			return;
        canvas.save();
        
		ship.render(canvas);
        /*if(movable&&(getTimer()==0))
        	canvas.drawBitmap(engine,x,y-5+(acceleration)*5,null);*/
       // canvas.drawBitmap(image,x,y,null);
		//engineSmoke.render(canvas);
       // canvas.restore();
		//uIhp.render(canvas);
		stun.render(canvas);
		
		for(Line2D l: ship.collisionlines)
		{
			l.render(canvas);
		}
		if(drawInteractionCitcle)
		drawInteractionCircle(canvas);
		
		acceleration=0;
    }

    @Override
    public void RotationToPoint(PointF targetPoint) {
		// rotation=(float)-Math.toDegrees(Math.PI+Math.atan2(targetPoint.x-x,targetPoint.y-y));   /coord rotation
	}

    


    public void addMovement(PointF screenPoint,float screenW, float screenH) {
        if (movable&&(getTimer()==0)) {
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

    public void setMovable(boolean movable) {
        this.movable = movable;
    }
	@Override
	public void calculateCollisionObject()
	{
		ship.calculateCollisionObject();
		/*float mathRotation=(float)(PI/180*rotation);
		//collisionPath.reset();
		for (int i=0;i<collisionPoints.length;i++)
		{
			float x1 = getCenterX()-ship.collisionInitPoints[i].x - getCenterX();
			float y1 = getCenterY()+ship.collisionInitPoints[i].y - getCenterY();

			float x2 = (float)(x1 * Math.cos(mathRotation) - y1 * Math.sin(mathRotation));
			float y2 = (float)(x1 * Math.sin(mathRotation) + y1 * Math.cos(mathRotation));

		if(collisionPoints[i]==null)
			collisionPoints[i]=new PointF(x2 + getCenterX(),y2+getCenterY());
		else
			collisionPoints[i].set(x2 + getCenterX(),y2+getCenterY());
		}
		collisionlines[0].set(collisionPoints[0].x,collisionPoints[0].y,collisionPoints[1].x,collisionPoints[1].y);
		collisionlines[1].set(collisionPoints[1].x,collisionPoints[1].y,collisionPoints[2].x,collisionPoints[2].y);
		collisionlines[2].set(collisionPoints[2].x,collisionPoints[2].y,collisionPoints[0].x,collisionPoints[0].y);*/
	}
	
	public void Shoot()
	{
		
		if(shootFlag)
		{
			ship.shoot();
		
		}
		//projectiles.shoot();
		//projectiles.newObject(new Projectile(x,y,projectileImage,"p"+projectiles.size(),(int)speed,2,rotation,this));
	}
	public void drawInteractionCircle(Canvas canvas)
	{
		Paint tPaint = new Paint();
		tPaint.setColor(Color.YELLOW);
		tPaint.setStyle(Paint.Style.STROKE);
		tPaint.setStrokeWidth(2);
		tPaint.setPathEffect(new DashPathEffect(new float[] { 15, 16}, 0));
		canvas.drawCircle(getCenterX(),getCenterY(),50,tPaint);
	}
	public void setDrawInteractionCicle(boolean state)
	{
		drawInteractionCitcle=state;
	}
	public void setShip()
	{
		if (ship.getClass()==ShipMk1.class)
			ship=new ShipMk2(game.shipAsset,this);
			else
			ship=new ShipMk1(game.shipAsset,this);
	}
}
