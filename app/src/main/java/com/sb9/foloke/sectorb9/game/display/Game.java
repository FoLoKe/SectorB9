package com.sb9.foloke.sectorb9.game.display;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.SurfaceHolder;
import android.content.Context;
import android.graphics.Canvas;

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

import android.view.ScaleGestureDetector.*;
import android.view.*;
import com.sb9.foloke.sectorb9.game.dataSheets.*;
import com.sb9.foloke.sectorb9.game.UI.Inventory.*;
import android.graphics.*;
import com.sb9.foloke.sectorb9.game.Managers.*;
import java.io.*;


public class Game extends SurfaceView implements SurfaceHolder.Callback
{
    //thread
    private MainThread mainThread;

    //background
    private Bitmap background;

    //camera
    private Camera camera;

	private UIcustomImage destroyedImage;
	
    //objects
    private Player player;
    private Cursor cursor;
	
	//world manager
	private WorldManager worldManager;
	
	
	
    //UI texts

    private Text textPointOfTouch;
	private Text textScreenWH;
	public Text textFPS;
	public Text textInProduction;
	public Text textInQueue;
	public Text debugText;
	public Text errorText;
	private Text debugExchange;
	
	///camera positioning
    private float scale=5;
    private float canvasH,canvasW;
    private PointF pointOfTouch;
    private PointF screenPointOfTouch;

	///booleans
    public boolean drawDebugInf=false;
	private boolean playerDestroyed=false;
	private boolean gamePause=false;
	private Timer destroyedTimer;
	public int command=0;
	public static final int commandInteraction=1,commandMoving=0;

	//UIs NEEDS REWORK
	private UIinventory inventoryUi;
	private InventoryExchangeInterface excInterface;
	private UIProgressBar uIhp;
	private objectOptionsUI objOptionsUI=new objectOptionsUI();
	public BuildingsDataSheet buildingsData;
	public ItemsDataSheet itemsData;
	
	
	private MainActivity mContext;
	private StaticEntity pressedObject;
	
    public Game(Context context, AttributeSet attributeSet)
    {
		
        super(context, attributeSet);
		worldManager=new WorldManager((MainActivity)context,this);
		DisplayMetrics displayMetrics = new DisplayMetrics();
		((Activity) getContext()).getWindowManager()
			.getDefaultDisplay()
			.getMetrics(displayMetrics);
		canvasH = displayMetrics.heightPixels;
		canvasW = displayMetrics.widthPixels;
		this.mContext=(MainActivity)context;
		
        BitmapFactory.Options bitmapOptions=new BitmapFactory.Options();
        bitmapOptions.inScaled=false;
		
		
		
        background=Bitmap.createBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.galactic_outflow,bitmapOptions));

		InventoryAsset.init(Bitmap.createBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.ui_inventory_sheet,bitmapOptions)));
       	ShipAsset.init(Bitmap.createBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.ships_sheet,bitmapOptions)));
        UIAsset.init(Bitmap.createBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.ui_asset_sheet,bitmapOptions)));
		WeaponsAsset.init(Bitmap.createBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.ships_sheet,bitmapOptions)));
		EffectsAsset.init(Bitmap.createBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.ships_sheet,bitmapOptions)));

		buildingsData=new BuildingsDataSheet(this);
		itemsData=new ItemsDataSheet(this);
		
		
        screenPointOfTouch=new PointF(0,0);
        pointOfTouch=new PointF(0,0);
		
        player=new Player(900,900,0,this,"player");
		excInterface=new InventoryExchangeInterface(this);
		
		
		//for building and ship leading
        cursor=new Cursor(900,900,"cursor",this);
		
		
        textPointOfTouch=new Text(""+0+" "+0,500,400);
		textScreenWH=new Text("",500,250);
		debugText=new Text("",500,350);
		debugExchange=new Text("exchange",500,300);
		errorText=new Text("",500,450);
		textFPS=new Text("",500,500);
		textInProduction=new Text("",500,550);
		textInQueue=new Text("",500,600);
		textScreenWH.setString(canvasW+"x"+canvasH);
        camera=new Camera(0,0,scale,player);
		
		
		uIhp=new UIProgressBar(this,canvasW/3,canvasH/20,50,50,UIAsset.hpBackground,UIAsset.hpLine,UIAsset.progressBarBorder,100);
        destroyedImage=new UIcustomImage(UIAsset.destroyedText);
		getHolder().addCallback(this);
		worldManager.loadDebugWorld();
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
		debugExchange.setString(excInterface.getItemHolder()+"->"+excInterface.getItemID()+":"+excInterface.getItemCount());
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
				worldManager.updateWorld();
				//entityManager.tick();
				uIhp.tick(player.getHp());
				
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
        	}
			worldManager.renderWorld(canvas);
			
			if(pressedObject!=null)
			{pressedObject.calculateCollisionObject();
				pressedObject.drawDebugBox(canvas);
			}
        	canvas.restore();
			//UI
			
			if(command==commandMoving)
			uIhp.render(canvas);
			
		
		//TODO: make PLAYER DESTROYED CLOSE INVENTORY
		if(playerDestroyed)
			destroyedImage.render(canvas);
		}
		if(drawDebugInf)
		{
			canvas.drawColor(Color.BLACK);
			textScreenWH.render(canvas);	
			textPointOfTouch.render(canvas);
			debugExchange.render(canvas);
			debugText.render(canvas);
			errorText.render(canvas);
			textFPS.render(canvas);
			textInProduction.render(canvas);
			textInQueue.render(canvas);
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
							worldManager.interactionCheck(pointOfTouch.x,pointOfTouch.y);
							
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
		return worldManager.getEntityManager().getArray();
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
		player=new Player(900,900,0,this,"player");
		playerDestroyed=false;
		camera.setPointOfLook(player);
		gamePause=false;
	}
	
	public Player getPlayer()
	{
		return player;
	}
	
	public UIinventory getInventoryUi()
	{
		return inventoryUi;
	}
	
	public void makeInventoryUI(TableLayout playerTable,TableLayout objectTable,MainActivity context)
	{
		
		inventoryUi=new UIinventory(playerTable,player,objectTable,null,excInterface,context);
	}
	
	
	
	
	public void updateInventory(final Entity caller)
	{
		mContext.runOnUiThread(new Runnable(){
		public void run()
		{
			inventoryUi.update(caller);
		}
		});
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
	public void setPressedObject(StaticEntity pressedObject)
	{
		this.pressedObject=pressedObject;
	}
	
	public PointF getTouchPoint()
	{
		return pointOfTouch;
	}
	
	public void initAssemblerUI(final Assembler assembler)
	{
		mContext.runOnUiThread(new Runnable() {
				@Override
				public void run() {

					mContext.assemblerUIi.init(mContext,assembler);
				}});
	}
	public MainActivity getMainActivity()
	{
		return mContext;
	}
	public Camera getCamera()
	{
		return camera;
	}
	public void addObject(Entity e)
	{
		worldManager.addObject(e);
	}
	public void save(BufferedWriter w)
	{
		worldManager.save(w);
	}
	public void load(BufferedReader r)
	{
		worldManager.load(r);
	}
}
