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
public class Player extends DynamicEntity {
    Bitmap engine;
    //float speed=3;

    boolean movable;
	private float maxHp=100;

    private Text textdXdY;
	private float acceleration;
	private UIProgressBar uIhp;
	private UIProgressBar stun;
	//private Path collisionPath;
	private PointF collisionPoints[];
	private PointF collisionInitPoints[];
	private Line2D collisionlines[];
	public boolean shootFlag;
	
	//private int inventory[][];
   
	//private ParticleSystem engineSmoke;
	
	
	public  ProjectilesPool projectiles;
	private Bitmap projectileImage;
	private Timer fireDelay=new Timer(0);
	private int fireRate= 600;
	
    public Player(float x, float y,float rotation, ImageAssets asset, UIAsset uiasset, Game game,String name)
    {
        super(x,y,rotation,asset.player_mk1,name,game);		
		this.projectiles=new ProjectilesPool(asset.shell,this,5,500,game);
		
        this.engine=asset.engine_mk1;
		projectileImage=asset.cursor;
		
        this.dx=this.dy=0;
        this.movable=false;
		this.renderable=true;
        textdXdY=new Text("",x-100,y-50);
		//this.uIhp=new UIProgressBar(this,50,8,-25,-20,uiasset.hpBackground,uiasset.hpLine,getHp());
		this.stun=new UIProgressBar(this,50,8,-25,+image.getHeight(),uiasset.stunBackground,uiasset.stunLine,uiasset.progressBarBorder,getTimer());
		inventoryMaxCapacity=10;
		for (int i=1;i<inventoryMaxCapacity;i++)
			inventory.put(i,i+i);
		
		
		//Point particleAccuracy=new Point(40,40);
		//this.engineSmoke=new ParticleSystem(asset.asteroid_1,x,y,1,particleAccuracy);
		
		collisionInitPoints=new PointF[3];
		collisionInitPoints[0]=new PointF(0,-image.getHeight()/2);
		collisionInitPoints[1]=new PointF(-image.getWidth()/2,image.getHeight()/2);
		collisionInitPoints[2]=new PointF(image.getWidth()/2,image.getHeight()/2);
		collisionlines=new Line2D[3];
		collisionlines[0]=new Line2D(0,0,1,1);
		collisionlines[1]=new Line2D(0,0,1,1);
		collisionlines[2]=new Line2D(0,0,1,1);
		collisionPoints=new PointF[collisionInitPoints.length];
		//collisionPath=new Path();
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
					for (Line2D l: collisionlines)
						if ((l.intersect(e.getCollisionBox()))&&e.collidable)
							{
								if (e.getClass().equals(DynamicEntity.class))
									((DynamicEntity)e).impulse(new PointF(0,0),dx,dy);
								impulse(e);
								break;
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
		fireDelay.tick();
			Shoot();
		for(Projectile p: projectiles.getArray())
			p.tick();
			
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
        canvas.rotate(rotation,getCenterX(),getCenterY());
        if(movable&&(getTimer()==0))
        	canvas.drawBitmap(engine,x,y-5+(acceleration)*5,null);
        canvas.drawBitmap(image,x,y,null);
		//engineSmoke.render(canvas);
        canvas.restore();
		//uIhp.render(canvas);
		stun.render(canvas);
		for(Projectile p: projectiles.getArray())
		p.render(canvas);
		for(Line2D l: collisionlines)
		{
			l.render(canvas);
		}
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
		float mathRotation=(float)(PI/180*rotation);
		//collisionPath.reset();
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
		//collisionPath.moveTo(collisionPoints[0].x,collisionPoints[0].y);
		//collisionPath.lineTo(collisionPoints[1].x,collisionPoints[1].y);
		//collisionPath.lineTo(collisionPoints[2].x,collisionPoints[2].y);
		//collisionPath.close();
		collisionlines[0].set(collisionPoints[0].x,collisionPoints[0].y,collisionPoints[1].x,collisionPoints[1].y);
		collisionlines[1].set(collisionPoints[1].x,collisionPoints[1].y,collisionPoints[2].x,collisionPoints[2].y);
		collisionlines[2].set(collisionPoints[2].x,collisionPoints[2].y,collisionPoints[0].x,collisionPoints[0].y);
	}
	
	public void Shoot()
	{
		
		if(shootFlag&&fireDelay.getTick()<=1)
		{
		projectiles.shoot();
		fireDelay.setTimer(60f/fireRate);
			//shootFlag=false;
		}
		//projectiles.shoot();
		//projectiles.newObject(new Projectile(x,y,projectileImage,"p"+projectiles.size(),(int)speed,2,rotation,this));
	}
}
