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
import com.sb9.foloke.sectorb9.game.Entities.Buildings.*;
import com.sb9.foloke.sectorb9.game.UI.Inventory.*;
import com.sb9.foloke.sectorb9.game.UI.CustomViews.*;
import com.sb9.foloke.sectorb9.game.DataSheets.*;

public class Player extends DynamicEntity
{
	public boolean shootFlag;
	private Ship ship;
	private int interactionRadius=150;
	
    public Player(float x, float y, float rotation, GameManager gameManager)
    {
        super(x,y,rotation,gameManager,1);
        drawHp=true;

		TEAM=1;
		this.ship=Ship.createSimple();
        this.dx=this.dy=0;
        this.movable=false;
		this.renderable=true;
		
		this.inventoryMaxCapacity=18;
		this.inventory=new Inventory(this,inventoryMaxCapacity,1);
		for(int i=0;i<inventoryMaxCapacity;i++)
		{
			
				inventory.addNewItem(i,20);
		}
		inventory.addToExistingOrNull(18,800);
		ShipUI.setUI(this, gameManager.getMainActivity());
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

		///DEBUG
        this.applyDamage(0.1f);
    }

    @Override
    public void render(Canvas canvas) 
	{
		if(!active)
			return;

		if(!renderable)
			return;
			
		ship.render(canvas);
        super.render(canvas);
		if(getGameManager().command== GameManager.commandInteraction)
			drawInteractionCircle(canvas);
	}

    @Override
    public void calculateCollisionObject()
    {
        super.calculateCollisionObject();

        if(ship!=null)
       	ship.calculateCollisionObject();
    }

    @Override
    public CustomCollisionObject getCollisionObject()
    {
		return ship.getCollisionObject();
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
	
	public void setShip(Ship in_ship)
	{
                ship = in_ship;
	}

	protected void onDestroy()
	{
		super.onDestroy();
		gameManager.onPlayerDestroyed();
	}

	public int getRadius(){return interactionRadius;}
	
	public void initShip()
	{
		ship.init(this);
	}

    public void respawn(SpaceDock sd)
	{
		x=sd.getX();
		y=sd.getY();
		//x=90;y=90;
		gameManager.getGamePanel().pointOfTouch=getWorldLocation();
		TEAM=1;
		this.ship=Ship.createSimple();
		
        this.dx=this.dy=0;
        this.movable=false;
		this.renderable=true;
		active=true;
		
		

		ShipUI.setUI(this, gameManager.getMainActivity());
		initShip();
		HP=100;
	}
	
	public void forceRespawn()
	{
		x=90;
		y=90;

		gameManager.getGamePanel().pointOfTouch=getWorldLocation();
		TEAM=1;
		this.ship=Ship.createSimple();

        this.dx=this.dy=0;
        this.movable=false;
		this.renderable=true;
		active=true;



		ShipUI.setUI(this, gameManager.getMainActivity());
		initShip();
		HP=100;
	}
	
	public Ship getShip()
	{
		return ship;
	}
}
