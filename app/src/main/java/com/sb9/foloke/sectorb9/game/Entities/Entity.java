package com.sb9.foloke.sectorb9.game.Entities;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;

import com.sb9.foloke.sectorb9.game.Assets.ShipAsset;
import com.sb9.foloke.sectorb9.game.Assets.UIAsset;
import com.sb9.foloke.sectorb9.game.Managers.GameManager;
import com.sb9.foloke.sectorb9.game.UI.*;
import com.sb9.foloke.sectorb9.game.DataSheets.ObjectsDataSheet;
import com.sb9.foloke.sectorb9.game.Funtions.*;
import java.io.*;
import com.sb9.foloke.sectorb9.game.UI.Inventory.*;
import com.sb9.foloke.sectorb9.game.UI.CustomViews.*;

public abstract class Entity {
	///TODO:
	///LOADING SCREEN                                       '''''
    ///FIX SHIELDS                                          '''''
	protected float width=2,height=2;
	protected float relativeCentreX,relativeCenterY;
    protected float x,y;
	protected float maxHP=100;
	protected float SP;
	protected float maxSP=0;
	protected float HP;
	protected float rotation;
	protected int TEAM=0;
	private int ID=0;
	private int frameTimer;
	protected int inventoryMaxCapacity=0;
	
	protected boolean renderable=false;
	protected boolean active=true;
	protected boolean collidable=true;
	protected boolean opened=false;
	protected boolean energy=false;
	protected boolean enabled=true;
	protected boolean isInteractable=true;
	protected boolean drawHp=true;
	public boolean toRemove=false;
	protected Paint debugPaint=new Paint();
	protected CustomCollisionObject collisionObject;
	protected ProgressBarUI uIhp;
    protected ProgressBarUI uIsh;
	protected String name;
	protected GameManager gameManager;
    protected Matrix transformMatrix=new Matrix();
	protected Inventory inventory;
	protected RectF renderBox=new RectF();
	protected Bitmap image,shieldImg;
    protected Timer shieldShow=new Timer(0);
    protected Paint shieldPaint=new Paint();
    private float shieldShowTime=2;
    protected float regainSH=0.2f;

    public Entity(float x, float y, float rotation, GameManager gameManager, int ID)
    {
        this.debugPaint.setColor(Color.RED);
        this.debugPaint.setStyle(Paint.Style.STROKE);
        this.debugPaint.setStrokeWidth(2);
		this.ID=ID;

		applyStandardOptions();

		this.HP=maxHP;
		this.SP=maxSP;
        this.x=x;
        this.y=y;
		this.gameManager = gameManager;
        
		this.relativeCenterY=(float)image.getHeight()/2;
		this.relativeCentreX=(float)image.getWidth()/2;

		this.inventory=new Inventory(this, inventoryMaxCapacity,1);
		this.rotation=rotation;
        shieldImg= ShipAsset.shield;

		this.uIhp=new ProgressBarUI(this,50,8,-25,-64,UIAsset.hpBackground,UIAsset.hpLine,UIAsset.progressBarBorder,getHp());
        this.uIsh=new ProgressBarUI(this,50,8,-25,-56,UIAsset.stunBackground,UIAsset.stunLine,UIAsset.progressBarBorder,getSH());

		createCollision();
    }
	
	private void applyStandardOptions()
	{
		enabled					= ObjectsDataSheet.findById(ID).enabledByDefault;
		inventoryMaxCapacity	= ObjectsDataSheet.findById(ID).inventoryCapacity;
		isInteractable			= ObjectsDataSheet.findById(ID).interactableByDefault;
		opened					= ObjectsDataSheet.findById(ID).openByDefault;
		image					= ObjectsDataSheet.findById(ID).image;
		name					= ObjectsDataSheet.findById(ID).name;
		collidable				= ObjectsDataSheet.findById(ID).collidable;
	}

	public void setShieldSize(int i)
    {
        switch (i) {
            case 1:
                shieldImg=ShipAsset.shield;
                break;
            case 2:
                shieldImg=ShipAsset.largeShield;
                break;
            default:
                shieldImg=ShipAsset.shield;
                break;
        }
    }

    protected void createCollision()
    {
        if(width<image.getWidth())
            width=image.getWidth();
        if(height<image.getHeight())
            height=image.getHeight();
        
        
        renderBox.set(0,0,width,height);
        setCollisionObject();
    }

	public String getSaveString()
	{
			String s=getInvSaveString();
		    return "["+ID+" "+name+" "+x+" "+y+" "+rotation+" "+HP+" "+SP+" "+TEAM+" "+s+"] ";
	}

	//**********SYNTAX***********
	//WORDS: ID NAME X Y ROTATION 
	//       HP SH TEAM INVENTORY
	//***************************
	public void loadFromStrings(String[] words)
	{
		try
		{		
			name=words[1];
			x=Float.parseFloat(words[2]);
			y=Float.parseFloat(words[3]);
			rotation= Float.parseFloat(words[4]);
			HP=Float.parseFloat(words[5]);
			SP=Float.parseFloat(words[6]);
			TEAM=Integer.parseInt(words[7]);
			LoadInvFromString(words[8]);
			

			setCollisionObject();
		}
		catch(Throwable t)
		{
            GameLog.update("Entity: "+t.toString(),1);
		}
	}

	public void LoadInvFromString(String saveString)
	{
		inventory.clear();
		String[] invWords;
		if((invWords = saveString.split(":")).length>0)
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
	
	//*************SYNTAX***************
	// :(FIRST)ITEM ID=ITEM COUNT:(NEXT)
	//**********************************
	public String getInvSaveString()
	{
		String s=":";
		for (Inventory.InventoryItem i: inventory.getArray())
		{
			s+=i.getID()+"="+i.getCount()+":";
		}
		return s;
	}

    abstract public void render(Canvas canvas);

	public void setImage(Bitmap i)
	{
		image=i;
	}

    public void tick()
    {
        if(shieldShow.tick())
            SP+=regainSH;
        if(SP>maxSP)
            SP=maxSP;
        shieldPaint.setAlpha((int)(255*shieldShow.getSecond()/shieldShowTime));
        renderBox.set(getCenterX()-32,getCenterY()-32,getCenterX()+32,getCenterY()+32);


        uIsh.tick(SP/maxSP*100);
        uIhp.tick(HP/maxHP*100);
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

	protected void timerTick()
	{
		if((frameTimer-=1)<0)
			frameTimer=0;	
	}

	public int getTimer()
	{
		return frameTimer;
	}

	public Inventory getInventory(){return inventory;}

	public String getName(){return name;}

	public float getWorldRotation(){return rotation;}

	public void applyDamage(float damage)
	{
        if(SP>maxSP)
            SP=maxSP;
        if(HP>maxHP)
            HP=maxHP;
	    float sum=HP+SP-damage;

        shieldShow.setTimer(shieldShowTime);
		
	    if(sum-maxHP>0)
	    {
            SP = sum - maxHP;
        }
        else
        {
            SP = 0;
            HP = sum;
        }
		

		if(HP<=0)
			onDestroy();			
	}
	
	protected void onDestroy()
	{
		active=false;
        toRemove=true;
		gameManager.spawnDestroyed(this);
	}
	
	public void setCenterX(float x){this.x=x-relativeCentreX;}

	public void setCenterY(float y){this.y=y-relativeCenterY;}

	public float getHp(){return HP;}

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

	protected void setCollisionObject()
	{
		collisionObject=new CustomCollisionObject(width,height,gameManager);
        calculateCollisionObject();
	}

	public void drawDebugCollision(Canvas canvas)
	{
		collisionObject.render(canvas);
		canvas.drawRect(renderBox,debugPaint);
	}

	public void calculateCollisionObject()
	{
		collisionObject.calculateCollisionObject(getCenterX(),getCenterY());
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
	
	public int getTeam()
	{
		return TEAM;
	}
	
	public boolean getCollidable()
	{
		return collidable;
	}
	
	public void setTeam(int team)
	{
		TEAM=team;
	}

	public void setMaxHP(float mhp)
    {
        maxHP=mhp;
        HP=mhp;
    }

    public void setMaxSH(float msh)
    {
        maxSP=msh;
        SP=msh;
    }

    public float getMaxHP()
    {
        return  maxHP;
    }

    public float getMaxSH()
    {
        return maxSP;
    }

    public float getSH()
    {
        return SP;
    }

	public void setHP(float HP)
	{
		this.HP=HP;
	}

	public void setSH(float SH)
	{
		this.SP=SH;
	}
	
	public boolean getInteractable()
    {
        return isInteractable;
    }
}
