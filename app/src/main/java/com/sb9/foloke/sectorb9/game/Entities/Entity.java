package com.sb9.foloke.sectorb9.game.Entities;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;

import com.sb9.foloke.sectorb9.game.Assets.UIAsset;
import com.sb9.foloke.sectorb9.game.Managers.GameManager;
import com.sb9.foloke.sectorb9.game.UI.*;
import com.sb9.foloke.sectorb9.game.DataSheets.BuildingsDataSheet;
import com.sb9.foloke.sectorb9.game.Funtions.*;
import java.io.*;
import com.sb9.foloke.sectorb9.game.UI.Inventory.*;

public abstract class Entity {
	
	private float width=2,height=2;
	private float relativeCentreX,relativeCenterY;
    protected float x,y;
	protected float maxHp=100;
	protected float HP;
	protected float rotation;
	
	private int ID=0;
	private int frameTimer;
	protected int inventoryMaxCapacity=0;
	
	protected boolean renderable=false;
	protected boolean active=true;
	protected boolean collidable=true;
	protected boolean opened=false;
	
	private PointF collisionInitPoints[];
	private Paint debugPaint=new Paint();
	private CustomCollisionObject collisionObject;
	protected ProgressBarUI uIhp;
	protected String name;
	protected GameManager gameManager;
    protected Matrix transformMatrix=new Matrix();
	protected Inventory inventory;
	protected RectF renderBox=new RectF();
	protected Bitmap image;
	
    public Entity(float x, float y, float rotation, Bitmap image, String name, GameManager gameManager, int ID)
    {
        this.debugPaint.setColor(Color.RED);
        this.debugPaint.setStyle(Paint.Style.STROKE);
        this.debugPaint.setStrokeWidth(2);
		this.ID=ID;
		this.HP=maxHp;
        this.x=x;
        this.y=y;
		this.gameManager = gameManager;
        this.image=image;
		this.relativeCenterY=image.getHeight()/2;
		this.relativeCentreX=image.getWidth()/2;
		this.name=name;
		this.inventory=new Inventory(this, BuildingsDataSheet.findById(ID).inventoryCapacity,4);
		this.rotation=rotation;
		this.uIhp=new ProgressBarUI(this,50,8,-25,-20,UIAsset.hpBackground,UIAsset.hpLine,UIAsset.progressBarBorder,getHp());
		this.ID=ID;
		createCollision();
    }

    protected void createCollision()
    {
        if(width<image.getWidth())
            width=image.getWidth();
        if(height<image.getHeight())
            height=image.getHeight();
        collisionInitPoints=new PointF[4];
        collisionInitPoints[0]=new PointF(-width/2,-height/2);
        collisionInitPoints[1]=new PointF(width/2,-height/2);
        collisionInitPoints[2]=new PointF(width/2,height/2);
        collisionInitPoints[3]=new PointF(-width/2,height/2);
        renderBox.set(0,0,width,height);
        setCollisionObject(collisionInitPoints);
    }

	public void save(BufferedWriter writer)
	{
		try
        {
			String s=invSave();
		    writer.write(ID+" "+name+" "+x+" "+y+" "+rotation+" "+HP+" "+s);
		    writer.newLine();
		}
		catch(Throwable t)
		{
			System.out.println(t);
		}
	}

	public void load(String[] words)
	{
		try
		{		
			name=words[1];
			x=Float.parseFloat(words[2]);
			y=Float.parseFloat(words[3]);
			rotation= Float.parseFloat(words[4]);
			HP=Float.parseFloat(words[5]);
			String[] invWords;
			if((invWords = words[6].split(":")).length>0)
			{
				for(String s: invWords)
				{
					if(s.length()>0)
					{
						String elemWords[]=s.split("=");
						inventory.addNewItem(Integer.parseInt(elemWords[0]),Integer.parseInt(elemWords[1]));
					}
				}
			}
			setCollisionObject(collisionInitPoints);
		}
		catch(Throwable t)
		{
			System.out.println(t);
		}
	}

	private String invSave()
	{
		String s=":";
		for (Inventory.InventoryItem i: inventory.getArray())
		{
			s+=i.getID()+"="+i.getCount()+":";
		}
		return s;
	}

    abstract public void render(Canvas canvas);

    public void tick()
    {
        renderBox.set(getCenterX()-32,getCenterY()-32,getCenterX()+32,getCenterY()+32);
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public PointF getWorldLocation()
	{
		return new PointF(x,y);
	}

	public PointF getCenterWorldLocation()
	{
		return new PointF(getCenterX(),getCenterY());
	}

    public void setWorldLocation(PointF location)
    {
        x=location.x;
        y=location.y;
    }

	public Matrix getTransformMatrix() {
		return transformMatrix;
	}

	public float getCenterX()
    {
        return x+relativeCentreX;
    }

    public float getCenterY()
    {
        return y+relativeCenterY;
    }

	public void setRenderable(boolean renderable)
	{
		this.renderable=renderable;
	}

    public void setTimer(int delay)
	{
		frameTimer=delay*60;
	}

	public void timerTick()
	{
		if((frameTimer-=1)<0)
			frameTimer=0;	
	}

	public int getTimer()
	{
		return frameTimer;
	}

	public Inventory getInventory()
	{
		return inventory;
	}

	public String getName()
	{
		return name;
	}

	public float getWorldRotation()
	{
		return rotation;
	}

	public void applyDamage(int damage)
	{
		HP-=damage;
		uIhp.set(HP);
		if(HP<=0)
			onDestroy();			
	}
	
	private void onDestroy()
	{
		active=false;
	}
	
	public void setCenterX(float x)
	{
		this.x=x-relativeCentreX;
	}

	public void setCenterY(float y)
	{
		this.y=y-relativeCenterY;
	}

	public float getHp()
	{
		return HP;
	}

	public GameManager getGameManager()
	{
		return gameManager;
	}

	public boolean getActive()
	{
		return active;
	}

	public boolean getOpened()
	{
		return opened;
	}

	public CustomCollisionObject getCollisionObject()
	{
		return collisionObject;
	}

	private void setCollisionObject(PointF points[])
	{
        transformMatrix.reset();
        transformMatrix.postRotate(rotation);
		collisionObject=new CustomCollisionObject(points,this);
        calculateCollisionObject();
	}

	public void drawDebugCollision(Canvas canvas)
	{
		collisionObject.render(canvas);
		canvas.drawRect(renderBox,debugPaint);
	}

	public void calculateCollisionObject()
	{
		collisionObject.calculateCollisionObject(transformMatrix);
	}

	public void setWorldRotation(float rotation)
	{
		this.rotation=rotation;
	}

	public Bitmap getSprite()
	{
		return image;
	}

	public RectF getRenderBox()
    {
        return renderBox;
    }
}
