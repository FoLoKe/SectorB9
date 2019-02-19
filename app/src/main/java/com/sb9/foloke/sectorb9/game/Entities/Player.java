package com.sb9.foloke.sectorb9.game.Entities;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import static java.lang.Math.PI;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import android.graphics.*;
import com.sb9.foloke.sectorb9.game.Assets.ShipAsset;
import com.sb9.foloke.sectorb9.game.Assets.UIAsset;
import com.sb9.foloke.sectorb9.game.Managers.GameManager;
import com.sb9.foloke.sectorb9.game.UI.ProgressBarUI;

import com.sb9.foloke.sectorb9.game.Funtions.*;
import com.sb9.foloke.sectorb9.game.Entities.Ships.*;
import com.sb9.foloke.sectorb9.game.UI.*;

public class Player extends DynamicEntity
{
	public boolean shootFlag;

	private Ship ship;
	private int interactionRadius=150;
	
    public Player(float x, float y, float rotation, GameManager gameManager, String name)
    {
		
        super(x,y,rotation,gameManager,1);
		TEAM=1;
		this.ship=new ShipMk1(this);
        this.dx=this.dy=0;
        this.movable=false;
		this.renderable=true;
		speed=1;
		this.inventoryMaxCapacity=10;
		for(int i=0;i<inventory.getHeight();i++)
			for(int j=0;j<inventory.getWidth();j++)
				inventory.addNewItem(i*inventory.getWidth()+j,i*inventory.getWidth()+j);

		gameManager.getMainActivity().shipUI=new ShipUI(this, gameManager.getMainActivity());
		initShip();
    }

    @Override
    public void tick() 
	{
		if(!active)
			return;
	    super.tick();
		Shoot();
		ship.tick();	
    }
	
	

    @Override
    public void render(Canvas canvas) 
	{
		if(!active)
			return;
		super.render(canvas);
		if(!renderable)
			return;
			
		ship.render(canvas);
		
		if(getGameManager().command== GameManager.commandInteraction)
			drawInteractionCircle(canvas);
	}

    


    @Override
    public void calculateCollisionObject()
    {
        super.calculateCollisionObject();

        if(ship!=null)
       	ship.calculateCollisionObject(transformMatrix);
    }

    @Override
    public CustomCollisionObject getCollisionObject()
    {return ship.getCollisonObject();}

    public void addMovement(PointF screenPoint,float screenW, float screenH) 
	{
        if (movable&&(getTimer()==0)) 
		{
			float minAcceleration=screenH/8; //0%
			float maxAcceleration=screenH/2; //100%
			PointF relativePoint=new PointF(screenPoint.x-screenW/2,screenPoint.y-screenH/2);
			targetAcceleration=(float)Math.sqrt(relativePoint.x*relativePoint.x+relativePoint.y*relativePoint.y);
			targetAcceleration=(targetAcceleration-minAcceleration)/(maxAcceleration-minAcceleration);
        }
    }
	
    public void setMovable(boolean movable) 
	{
        this.movable = movable;
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
	
	public int getRadius(){return interactionRadius;}
	
	public void initShip()
	{
		ship.init(this);
	}
}
