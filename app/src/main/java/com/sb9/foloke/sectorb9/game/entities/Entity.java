package com.sb9.foloke.sectorb9.game.entities;

import android.graphics.Bitmap;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import java.util.*;
import com.sb9.foloke.sectorb9.game.UI.*;
import com.sb9.foloke.sectorb9.game.display.*;
import com.sb9.foloke.sectorb9.game.funtions.*;
import java.io.*;
import com.sb9.foloke.sectorb9.game.UI.Inventory.*;

public abstract class Entity {
    protected float x,y;
    protected RectF collisionBox;
    protected int Height,Width;
    protected Bitmap image;
	protected boolean renderable;
	private int frameTimer;
	//protected Map<Integer,Integer> inventory;
	protected int inventoryMaxCapacity=0;
	protected String name;
	protected float rotation;
	protected Game game;
	private float maxHp=100;
	private float HP;
	protected boolean active=true;
	protected boolean collidable=true;
	protected boolean opened=false;
	protected boolean isUsingCustomCollision=false;
	private CustomCollisionObject collisionObject;
	protected UIProgressBar uIhp;
	private int ID=0;
	boolean hasInventory=true;
	
	protected Inventory inventory;
    public Entity(float x,float y,float rotation,Bitmap image,String name,Game game,int ID)
    {
		this.ID=ID;
		this.HP=maxHp;
        this.x=x;
        this.y=y;
		this.game=game;
        this.collisionBox=new RectF(x,y,x+image.getWidth(),y+image.getHeight());
        this.image=image;
		this.renderable=false;
		this.name=name;
		
		if(game.buildingsData.findById(ID).inventoryCapacity!=0)
		this.inventory=new Inventory(this,game.buildingsData.findById(ID).inventoryCapacity,4);
		this.rotation=rotation;
		this.uIhp=new UIProgressBar(this,50,8,-25,-20,game.uiAsset.hpBackground,game.uiAsset.hpLine,game.uiAsset.progressBarBorder,getHp());
		this.ID=ID;
    }
	public void save(BufferedWriter writer)
	{
		try{
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
						//inventory.put(Integer.parseInt(elemWords[0]),Integer.parseInt(elemWords[1]));
					}
				}
			}
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
    abstract public void tick();
	///on destroy
	///on damage
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

    public void drawDebugBox(Canvas canvas)
    {
		if(!renderable)
			return;
        Paint temppaint=new Paint();
        temppaint.setColor(Color.rgb(0,255,0));
        temppaint.setStyle(Paint.Style.STROKE);
        canvas.drawRect(collisionBox,temppaint);
    }

    public PointF getWorldLocation()
    {
        return new PointF(x,y);
    }
    public void setWorldLocation(PointF location)
    {
        x=location.x;
        y=location.y;
    }
	public float getCenterX()
    {
        return x+image.getWidth()/2;
    }
    public float getCenterY()
    {
        return y+image.getHeight()/2;
    }
	public RectF getCollsionBox()
	{
		return collisionBox;
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
	public int getInventoryMaxCapacity()
	{
		return inventoryMaxCapacity;
	}
	public RectF getCollisionBox()
	{
		return collisionBox;
	}
	public String getName()
	{
		return name;
	}
	public float getWorldRotation()
	{
		return rotation;
	}
	public ArrayList<Entity>getObjects()
	{
		return game.getEntities();
	}
	public Game getHolder()
	{
		return game;
	}
	public void applyDamage(int damage)
	{
		HP-=damage;
	}
	public void calculateCollisionObject()
	{
		this.collisionBox.set(x,y,x+image.getWidth(),y+image.getHeight());
	}
	public void setCenterX(float x)
	{
		this.x=x-image.getWidth()/2;
	}
	public void setCenterY(float y)
	{
		this.y=y-image.getHeight()/2;
	}
	public float getHp()
	{
		return HP;
	}
	public Game getGame()
	{
		return game;
	}
	/*public void onDestroy()
	{
		game.getEntityManager().addObject(new DroppedItems(x,y,inventory,game));
	}*/
	/*public void setInventory(Inventory inv)
	{
		inventory.clear();
		for (Map.Entry<Integer,Integer> i: inv.entrySet())
			{
				this.inventory.put(i.getKey(),i.getValue());
			}
	}*/
	public boolean getActive()
	{
		return active;
	}
	public boolean getOpened()
	{
		return opened;
	}
	public CustomCollisionObject getCustomCollisionObject()
	{
		return collisionObject;
	}
	public void setCustomCollisionObject(PointF points[])
	{
		collisionObject=new CustomCollisionObject(points,this);
		}
	public void drawDebugCollision(Canvas canvas)
	{
		collisionObject.render(canvas);
	}
	public void calculateCustomCollisionObject()
	{
		collisionObject.calculateCollisionObject();
	}
	public void setWorldRotation(float rotation)
	{
		this.rotation=rotation;
	}
}
