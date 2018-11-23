package com.sb9.foloke.sectorb9.game.display;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.SurfaceHolder;
import android.content.Context;
import android.graphics.Canvas;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import android.util.*;

import com.sb9.foloke.sectorb9.MainThread;
import com.sb9.foloke.sectorb9.R;
import com.sb9.foloke.sectorb9.game.Assets.UIAsset;
import com.sb9.foloke.sectorb9.game.entities.Player;
import com.sb9.foloke.sectorb9.game.Assets.ShipAsset;
import com.sb9.foloke.sectorb9.game.UI.Text;
import com.sb9.foloke.sectorb9.game.entities.Cursor;
import com.sb9.foloke.sectorb9.game.entities.*;
import com.sb9.foloke.sectorb9.game.UI.*;
import com.sb9.foloke.sectorb9.game.funtions.*;
import java.util.Random;
import com.sb9.foloke.sectorb9.game.Assets.*;
import android.widget.*;
import android.app.*;
import java.util.ArrayList;
import com.sb9.foloke.sectorb9.*;
import com.sb9.foloke.sectorb9.game.entities.Buildings.*;
import android.os.*;
import android.view.ScaleGestureDetector.*;
import android.view.GestureDetector.*;
import android.view.*;
import com.sb9.foloke.sectorb9.game.dataSheets.*;
//TODO: MAP BORDER AND PAUSE FIX ON MOVEMENT

public class Game extends SurfaceView implements SurfaceHolder.Callback
{
    //thread
    private MainThread mainThread;

    //background
    private Bitmap background;

    //camera
    private Camera camera;
	float lastFocusX,lastFocusY;
    //assets
    private BitmapFactory.Options options;
    public ShipAsset shipAsset;
	public WeaponsAsset weaponAsset;
    public UIAsset uiAsset;
	//public InventoryAsset invAsset;
	
	public EffectsAsset effAsset;
	
	private UIcustomImage destroyedImage;
	
    //objects
    private Player player;
    private Cursor cursor;
	
	//objects manager
	protected EntityManager entityManager;
	
    //UI

    private Text textPointOfTouch;
	private Text textScreenWH;
	public Text textFPS;
    private float scale=5;

    private float canvasH,canvasW;
    private PointF pointOfTouch;
    private PointF screenPointOfTouch;

    public boolean drawDebugInf=false;
	boolean playerDestroyed=false;
	boolean OpenInventory=false;
	boolean gamePause=false;
	private Timer destroyedTimer;
	public int command=0;
	public static final int commandInteraction=1,commandMoving=0;
	
	//private UIinventory playerInventory;
	UIinventory playerInv;
	UIinventory objectInv;
	public UICommInterface exchangeInteface;
	UIProgressBar uIhp;
	objectOptionsUI objOptionsUI=new objectOptionsUI();
	public BuildingsDataSheet buildingsData;
	public ItemsDataSheet itemsData;
	
	//debug 
	String exchangeFrom;
	String exchangeTo;
	Text debugExchange;
	//private
	public Text debugText;
	public Text errorText;
	public MainActivity mAcontext;
	private StaticEntity pressedObject;
	
    public Game(Context context, AttributeSet attributeSet)
    {
		
        super(context, attributeSet);
		DisplayMetrics displayMetrics = new DisplayMetrics();
		((Activity) getContext()).getWindowManager()
			.getDefaultDisplay()
			.getMetrics(displayMetrics);
		canvasH = displayMetrics.heightPixels;
		canvasW = displayMetrics.widthPixels;
		this.mAcontext=(MainActivity)context;
		this.entityManager=new EntityManager(this);
        options=new BitmapFactory.Options();
        options.inScaled=false;
		Random rand=new Random();
		
		
        background=Bitmap.createBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.galactic_outflow,options));
        shipAsset.init(Bitmap.createBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.ships_sheet,options)));
		//invAsset.init(Bitmap.createBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.ui_inventory_sheet,options)));
        uiAsset.init(Bitmap.createBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.ui_asset_sheet,options)));
		
		weaponAsset.init(Bitmap.createBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.ships_sheet,options)));
		effAsset.init(Bitmap.createBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.ships_sheet,options)));
		buildingsData=new BuildingsDataSheet(this);
		itemsData=new ItemsDataSheet(this);
		
		exchangeInteface.init(this);
        screenPointOfTouch=new PointF(0,0);
        pointOfTouch=new PointF(0,0);
		
        player=new Player(900,900,0,shipAsset,uiAsset,this,"player");
		
		
		entityManager.addObject(new SmallCargoContainer(1000,900,10,this));
		entityManager.addObject(new Crusher(900,1000,rand.nextInt(180),this));
		entityManager.addObject( new FuelGenerator(850,900,rand.nextInt(180),this));
		
		//for building and ship leading
        cursor=new Cursor(900,900,shipAsset,"cursor",this);
		
		for (int i=0;i<50;i++)
		entityManager.addObject(new Asteroid(50*rand.nextInt(50)+25*rand.nextInt(20),100*rand.nextInt(20)+20*rand.nextInt(50),rand.nextInt(180),shipAsset,"asteroid_"+i,this));
		
        textPointOfTouch=new Text(""+0+" "+0,500,400);
		textScreenWH=new Text("",500,250);
		debugText=new Text("",500,350);
		debugExchange=new Text("",500,300);
		errorText=new Text("",500,450);
		textFPS=new Text("",500,500);
		textScreenWH.setString(canvasW+"x"+canvasH);
        camera=new Camera(0,0,scale,player);
		
		uIhp=new UIProgressBar(this,canvasW/3,canvasH/20,50,50,uiAsset.hpBackground,uiAsset.hpLine,uiAsset.progressBarBorder,100);
        destroyedImage=new UIcustomImage(uiAsset,3);
		getHolder().addCallback(this);
        mainThread= new MainThread(getHolder(),this);
        setFocusable(true);
		
    }

    @Override
    public void surfaceCreated(SurfaceHolder p1)
    {
        mainThread.setRunning(true);
        mainThread.start();

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder p1)
    {
        boolean retry = true;
        while(retry)
        {
            try{mainThread.setRunning(false);
                mainThread.join();
                retry=false;
            }catch(InterruptedException e)
            {e.printStackTrace();}
        }
    }
    @Override
    public void surfaceChanged(SurfaceHolder p1, int p2, int p3, int p4) { }
    public void tick()
    {
		if(gamePause)
			return;
		debugExchange.setString(exchangeInteface.getExchangeFrom()+"->"+exchangeInteface.ExchangeTo);
		if(playerDestroyed)
		{
			if(destroyedTimer.tick())
			{
				destroyedTimer=null;
				createNewPlayer();
			}
			return;
		}
			
			if(!gamePause)
			{
       		 	cursor.setWorldLocation(pointOfTouch);
        		player.addMovement(screenPointOfTouch,canvasW,canvasH);
        		player.RotationToPoint(pointOfTouch);
        		player.tick();
				//player.Shoot();
				entityManager.tick();
				uIhp.tick(player.getHp());
				for(Entity e : entityManager.getArray())
				{
					if(e.getActive())
					{
						if(e.getCollsionBox().intersect(camera.getScreenRect()))
							e.setRenderable(true);
						else
							e.setRenderable(false);
					}
					else
						e.setRenderable(false);
				}
			}
        	camera.tick(scale,canvasW,canvasH);
			
    }
    public void render(Canvas canvas)
    {
		if(!gamePause)
		{
       		super.draw(canvas);
			
			camera.setScreenRect(canvasW,canvasH);
        	canvas.save();
		
        	canvas.translate(-camera.getWorldLocation().x+canvas.getWidth()/2,-camera.getWorldLocation().y+canvas.getHeight()/2);
        	canvas.scale(camera.getScale(),camera.getScale(),camera.getWorldLocation().x,camera.getWorldLocation().y);
			canvas.drawBitmap(background,0,0,null);
			
        	//objects
        		player.render(canvas);
				cursor.render(canvas);

			//render
			
			
        	if (drawDebugInf) 
			{
            	camera.render(canvas);
            	player.drawVelocity(canvas);
				for(Entity e: entityManager.getArray())
				{
					e.drawDebugBox(canvas);
				}
        	}
			for(Entity e: entityManager.getArray())
			{
				e.render(canvas);
			}
			
			if(pressedObject!=null)
			{pressedObject.calculateCollisionObject();
				pressedObject.drawDebugBox(canvas);
			}
        	canvas.restore();
			//UI
			if(drawDebugInf)
			{
				textScreenWH.render(canvas);	
				textPointOfTouch.render(canvas);
				debugExchange.render(canvas);
				debugText.render(canvas);
				errorText.render(canvas);
				textFPS.render(canvas);
			}
			uIhp.render(canvas);
			
		
		//TODO: make PLAYER DESTROYED CLOSE INVENTORY
		if(playerDestroyed)
			destroyedImage.render(canvas);
		}
		
    }
	
	

		private SimpleOnScaleGestureListener gestureListener = new SimpleOnScaleGestureListener() {
			@Override
			public boolean onScaleBegin(ScaleGestureDetector detector) {
				return true;
			}

			@Override
			public boolean onScale(ScaleGestureDetector detector) {
				scale *= detector.getScaleFactor();
				// Don't let the object get too small or too large.
				scale = Math.max(1f, Math.min(scale, 5.0f));
				return true;
			}
			
			
		};

		private ScaleGestureDetector gestureDetector = new ScaleGestureDetector(getContext(), gestureListener);
  		@Override
   		public boolean onTouchEvent(MotionEvent event) {
		if(!gamePause)
		{
			gestureDetector.onTouchEvent(event);
			//TODO: if gesture in progress ignore input
			if(true)
			{
      		  float x=event.getX(),y=event.getY();
      		  screenPointOfTouch.set(x,y);
   		      pointOfTouch.set((x-canvasW/2)/camera.getScale()+player.getCenterX(),(y-canvasH/2)/camera.getScale()+player.getCenterY());
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
					
                    pointOfTouch.set((x-canvasW/2)/camera.getScale()+player.getCenterX(),(y-canvasH/2)/camera.getScale()+player.getCenterY());
					cursor.setDrawable(true);
					
					switch (command)
					{
					case commandMoving:
                    player.setMovable(true);
					break;
					case commandInteraction: 
						break;
					}
			
                    break;
                case MotionEvent.ACTION_MOVE:
                    pointOfTouch.set((x-canvasW/2)/camera.getScale()+player.getCenterX(),(y-canvasH/2)/camera.getScale()+player.getCenterY());
                    
                    break;
                    case MotionEvent.ACTION_UP:
						
						
					switch (command)
					{
						case commandMoving:
							cursor.setDrawable(false);
                        player.setMovable(false);
						break;
						case commandInteraction:
							for(Entity e: entityManager.getArray())
							{
								if(e.getCollisionBox().contains(pointOfTouch.x,pointOfTouch.y))
								{
									if(Math.sqrt(
									(e.getCenterX()-player.getCenterX())*(e.getCenterX()-player.getCenterX())
												 +(e.getCenterY()-player.getCenterY())*(e.getCenterY()-player.getCenterY()))-32<player.getRadius())											 
									{								
										if(e instanceof StaticEntity)
										{
											pressedObject=(StaticEntity)e;
											mAcontext.uiInteraction.init(mAcontext,mAcontext.getViewFlipper(),pressedObject);
										}
									
									}							
									textPointOfTouch.setString(Math.sqrt((e.getCenterX()-player.getCenterX())*(e.getCenterX()-player.getCenterX())
																		 +(e.getCenterY()-player.getCenterY())*(e.getCenterY()-player.getCenterY()))+"");
								}
							}
							break;
						}
                    break;
                default:
                    break;
       			 }
			}
		}
                return true;
    }
	public ArrayList<Entity> getEntities()
	{
		return entityManager.getArray();
	}
	public void setPlayerDestroyed(boolean condition)
	{
		playerDestroyed=condition;
		destroyedTimer=new Timer(2);
		gamePause=condition;
	}
	public void createNewPlayer()
	{
		player=null;
		camera.setPointOfLook(null);
		player=new Player(900,900,0,shipAsset,uiAsset,this,"player");
		playerDestroyed=false;
		camera.setPointOfLook(player);
		gamePause=false;
	}
	
	public Player getPlayer()
	{
		return player;
	}
	
	public UIinventory getPlayerUIInventory()
	{
		return playerInv;
	}
	
	public void initInventoryUI(TableLayout playerTable,TableLayout objectTable,MainActivity context)
	{
		objectInv=new UIinventory(objectTable,context,null,exchangeInteface);
		playerInv=new UIinventory(playerTable,context,player,exchangeInteface);
	}
	
	public UIinventory getObjectUIInventory()
	{
		return objectInv;
	}
	
	public EntityManager getEntityManager()
	{
		return entityManager;
	}
	
	public void initObjInventory()
	{
		objectInv.init();
	}
	
	public void setPause(boolean state)
	{
		gamePause=state;
	}
	
	public objectOptionsUI getObjOptions()
	{
		return objOptionsUI;
	}
	
	public void nullPressedObject()
	{
		pressedObject=null;
	}
	
	public PointF getTouchPoint()
	{
		return pointOfTouch;
	}
}
