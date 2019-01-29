package com.sb9.foloke.sectorb9.game.Entities;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
//import android.graphics.Matrix;
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
import java.lang.reflect.Array;
import java.util.Vector;

import com.sb9.foloke.sectorb9.game.UI.Inventory.*;

public abstract class Entity {
    protected float x,y;
    //protected RectF collisionBox;
	private PointF collisionInitPoints[];


    protected Bitmap image;
	protected boolean renderable;
	private int frameTimer;
	protected int inventoryMaxCapacity=0;
	protected String name;
	protected float rotation;
	protected GameManager gameManager;
	private float maxHp=100;
	private float HP;
	protected boolean active=true;
	protected boolean collidable=true;
	protected boolean opened=false;

	private CustomCollisionObject collisionObject;
	protected ProgressBarUI uIhp;
	private int ID=0;

    Matrix transformMatrix=new Matrix();

	protected Inventory inventory;

    public Entity(float x, float y, float rotation, Bitmap image, String name, GameManager gameManager, int ID)
    {
		this.ID=ID;
		this.HP=maxHp;
        this.x=x;
        this.y=y;
		this.gameManager = gameManager;

        this.image=image;
		this.renderable=false;
		this.name=name;
		
		//if(BuildingsDataSheet.findById(ID).inventoryCapacity!=0)
		this.inventory=new Inventory(this, BuildingsDataSheet.findById(ID).inventoryCapacity,4);
		this.rotation=rotation;
		this.uIhp=new ProgressBarUI(this,50,8,-25,-20,UIAsset.hpBackground,UIAsset.hpLine,UIAsset.progressBarBorder,getHp());
		this.ID=ID;
		createCollision();

    }

    protected void createCollision()
    {
        collisionInitPoints=new PointF[4];
        collisionInitPoints[0]=new PointF(-image.getWidth()/2,-image.getHeight()/2);
        collisionInitPoints[1]=new PointF(image.getWidth()/2,-image.getHeight()/2);
        collisionInitPoints[2]=new PointF(image.getWidth()/2,image.getHeight()/2);
        collisionInitPoints[3]=new PointF(-image.getWidth()/2,image.getHeight()/2);
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

	public float getCenterX()
    {
        return x+image.getWidth()/2;
    }

    public float getCenterY()
    {
        return y+image.getHeight()/2;
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

	public GameManager getGameManager()
	{
		return gameManager;
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

	public CustomCollisionObject getCollisionObject()
	{
		return collisionObject;
	}

	public void setCollisionObject(PointF points[])
	{
        transformMatrix.reset();
        transformMatrix.postRotate(rotation);
		collisionObject=new CustomCollisionObject(points,this);

        calculateCollisionObject();
	}

	public void drawDebugCollision(Canvas canvas)
	{
		collisionObject.render(canvas);
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
}
