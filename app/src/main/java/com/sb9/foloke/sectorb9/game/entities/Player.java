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
import java.util.*;
public class Player extends DynamicEntity {
    Bitmap engine;
    //float speed=3;
    boolean movable;
	private float maxHp=100;
	private int HP;
    private Text textdXdY;
	private float acceleration;
	private UIProgressBar uIhp;
	private UIProgressBar stun;
	private Path collisionPath;
	private PointF collisionPoints[];
	private PointF collisionInitPoints[];
	//private int inventory[][];

	//private ParticleSystem engineSmoke;
	private Game game;
	
    public Player(float x, float y, ImageAssets asset, UIAsset uiasset, Game game)
    {
        super(x,y,asset.player_mk1);
		this.game=game;
        this.engine=asset.engine_mk1;
		this.HP=100;
        this.dx=this.dy=0;
        this.movable=false;
		this.renderable=true;
        textdXdY=new Text("",x-100,y-50);
		this.uIhp=new UIProgressBar(this,50,8,-25,-20,uiasset.hpBackground,uiasset.hpLine,HP);
		this.stun=new UIProgressBar(this,50,8,-25,+image.getHeight(),uiasset.stunBackground,uiasset.stunLine,getTimer());
		inventoryMaxCapacity=10;
		for (int i=0;i<inventoryMaxCapacity;i++)
			inventory.put(i,i+i);
		
		
		//Point particleAccuracy=new Point(40,40);
		//this.engineSmoke=new ParticleSystem(asset.asteroid_1,x,y,1,particleAccuracy);
		
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
		if(HP<=0)
		{
			game.setPlayerDestroyed(true);
			return;
		}

        //no inertia damping
		timerTick();
		//engineSmoke.tick();
		//engineSmoke.setWorldLocation(x,y);
		
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
			applyDamage((int)Math.sqrt((dx*dx+dy*dy)*200));
			x-=dx+1;
			y-=dy+1;
			dx=dx/2;
			dy=dy/2;
			//make block input for 1sec (60 frames);
			setTimer(1);
		}
		calculateCollisionObject();
		uIhp.tick(HP);
		stun.tick(getTimer());
    }

    @Override
    public void render(Canvas canvas) {
		if(!renderable)
			return;
        canvas.save();
        canvas.rotate(rotation,getCenterX(),getCenterY());
        if(movable&&(getTimer()==0))
        	canvas.drawBitmap(engine,x,y-5+(acceleration)*5,null);
        canvas.drawBitmap(image,x,y,null);
		//engineSmoke.render(canvas);
        canvas.restore();
		uIhp.render(canvas);
		stun.render(canvas);
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
		collisionPath.moveTo(collisionPoints[0].x,collisionPoints[0].y);
		collisionPath.lineTo(collisionPoints[1].x,collisionPoints[1].y);
		collisionPath.lineTo(collisionPoints[2].x,collisionPoints[2].y);
		collisionPath.close();
	}
	public void applyDamage(int damage)
	{
		HP-=damage;
	}
	
}
