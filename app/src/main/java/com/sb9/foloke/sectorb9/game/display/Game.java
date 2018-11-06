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
import com.sb9.foloke.sectorb9.game.Assets.ImageAssets;
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


public class Game extends SurfaceView implements SurfaceHolder.Callback
{
    //thread
    private MainThread mainThread;

    //background
    private Bitmap background;

    //camera
    private Camera camera;

    //assets
    private BitmapFactory.Options options;
    private ImageAssets shipAsset;
    public UIAsset uiAsset;
	private InventoryAsset invAsset;
	public ObjectsAsset objAsset;
	
	private UIcustomImage destroyedImage;
	
    //objects
    private Player player;
    private Cursor cursor;
	
	//objects arrays
	
	//private Asteroid asteroids[];
	//private StaticEntity staticEntities[];
	protected EntityManager entityManager;
	
    //UI

    private Text textPointOfTouch;
	private Text textScreenWH;
    private float scale=5;

    private float canvasH,canvasW;
    private PointF pointOfTouch;
    private PointF screenPointOfTouch;

    public boolean drawDebugInf=true;
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
	
	
	//debug 
	String exchangeFrom;
	String exchangeTo;
	Text debugExchange;
	//private
	public Text debugText;
	public Text errorText;
	public Context mAcontext;
	
    public Game(Context context, AttributeSet attributeSet)
    {
		
        super(context, attributeSet);
		this.mAcontext=context;
		//View rootView = ((Activity)_context).Window.DecorView.FindViewById(Android.Resource.Id.Content);
		//View rootView = ((Activity)context).getWindow().getDecorView().findViewById(android.R.id.content);
		//TableLayout playerTable=rootView.findViewById(R.id.PlayerTableLayout);
		//TableLayout objectTable=((Activity)context).getWindow().getDecorView().findViewById(R.id.ObjectTableLayout);
	
		entityManager=new EntityManager(this);
        options=new BitmapFactory.Options();
        options.inScaled=false;
		Random rand=new Random();
		
        background=Bitmap.createBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.galactic_outflow,options));
        shipAsset.init(Bitmap.createBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.ships_sheet,options)));
		invAsset.init(Bitmap.createBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.ui_inventory_sheet,options)));
        uiAsset.init(Bitmap.createBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.ui_asset_sheet,options)));
		objAsset.init(Bitmap.createBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.objects_sheet,options)));
		exchangeInteface.init(this);
        screenPointOfTouch=new PointF(0,0);
        pointOfTouch=new PointF(0,0);
		//TableLayout playerTable=findViewById(R.id.PlayerTableLayout);
		//TableLayout objectTable=findViewById(R.id.ObjectTableLayout);
        player=new Player(900,900,0,shipAsset,uiAsset,this,"player");
		
		entityManager.addObject( new SmallCargoContainer(850,900,rand.nextInt(180),objAsset,"debug cargo",this));
		entityManager.addObject(new SmallCargoContainer(1000,900,10,objAsset,"debug cargo1",this));
		//entityManager.addObject(new SmallCargoContainer());
		entityManager.addObject(new Crusher(900,1000,rand.nextInt(180),objAsset,"debug crusher",this));
        cursor=new Cursor(900,900,shipAsset,"cursor",this);
		
		//asteroids=new Asteroid[50];
		//staticEntities=new StaticEntity[3];
		//staticEntities[0]=container;
		//staticEntities[1]=container2;
		//staticEntities[2]=container3;
		
		for (int i=0;i<50;i++)
		entityManager.addObject(new Asteroid(50*rand.nextInt(10)+25*rand.nextInt(20),100*rand.nextInt(10)+20*rand.nextInt(50),rand.nextInt(180),shipAsset,"asteroid_"+i,this));
		canvasH=canvasW=100;
        textPointOfTouch=new Text(""+0+" "+0,500,400);
		textScreenWH=new Text("",500,250);
		debugText=new Text("",500,350);
		debugExchange=new Text("",500,300);
		errorText=new Text("",500,450);
        camera=new Camera(0,0,scale,player);
		
		
		//playerInventory=new UIinventory(invAsset,player.getInventory());
		//playerInventory.setVisability(true);
		destroyedImage=new UIcustomImage(Bitmap.createBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.ui_asset_sheet,options)),0,24,64,24);
		uIhp=new UIProgressBar(this,500,100,50,50,uiAsset.hpBackground,uiAsset.hpLine,uiAsset.progressBarBorder,(int)100);
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
				player.Shoot();
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
			textScreenWH.setString(canvasW+"x"+canvasH);
			//objectInv.setTarget(player);
			//objectInv.init();
			
    }
    public void render(Canvas canvas)
    {
		if(true)
		{
       		super.draw(canvas);
       		canvasW=canvas.getWidth();
        	canvasH=canvas.getHeight();
			
			camera.setScreenRect(canvasW,canvasH);
        	canvas.save();
		
        	//canvas.drawColor(Color.rgb(50,50,50));
		
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
			for(Entity e: entityManager.getArray())
			{
			e.render(canvas);
			e.drawDebugBox(canvas);
			}
        	canvas.restore();
			//UI
			if(drawDebugInf)
			textScreenWH.render(canvas);	
			textPointOfTouch.render(canvas);
			debugExchange.render(canvas);
			debugText.render(canvas);
			errorText.render(canvas);
			uIhp.render(canvas);
			
		}
		//TODO: make PLAYER DESTROYED CLOSE INVENTORY
		if(playerDestroyed)
			destroyedImage.render(canvas);
		
		
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
		if(!gamePause)
		{
        float x=event.getX(),y=event.getY();
        screenPointOfTouch.set(x,y);
        pointOfTouch.set((x-canvasW/2)/camera.getScale()+player.getCenterX(),(y-canvasH/2)/camera.getScale()+player.getCenterY());
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
					
                    pointOfTouch.set((x-canvasW/2)/camera.getScale()+player.getCenterX(),(y-canvasH/2)/camera.getScale()+player.getCenterY());
					
                 //   textPointOfTouch.setString(pointOfTouch.x+" "+pointOfTouch.y);
					cursor.setDrawable(true);
					
					switch (command)
					{
					case commandMoving:
                    player.setMovable(true);
					break;
					case commandInteraction: 
						break;
					}
					//openNewInventory(playerInventory,player.getInventory());
                    break;
                case MotionEvent.ACTION_MOVE:
                    pointOfTouch.set((x-canvasW/2)/camera.getScale()+player.getCenterX(),(y-canvasH/2)/camera.getScale()+player.getCenterY());
                    
                    break;
                    case MotionEvent.ACTION_UP:
						
						cursor.setDrawable(false);
					switch (command)
					{
						case commandMoving:
                        player.setMovable(false);
						break;
						case commandInteraction:
							for(Entity e: entityManager.getArray())
							{
								if(e.getCollisionBox().contains(pointOfTouch.x,pointOfTouch.y)&&e.getOpened())
								{
									if(Math.sqrt((e.getCenterX()-player.getCenterX())*(e.getCenterX()-player.getCenterX())
												 +(e.getCenterY()-player.getCenterY())*(e.getCenterY()-player.getCenterY()))-32<50)
												 
									{
										
									objectInv.setTarget(e);
									

									((MainActivity)mAcontext).initInvenories();
									((MainActivity)mAcontext).openObjectInventory();
									}
									
									textPointOfTouch.setString(Math.sqrt((e.getCenterX()-player.getCenterX())*(e.getCenterX()-player.getCenterX())
																		 +(e.getCenterY()-player.getCenterY())*(e.getCenterY()-player.getCenterY()))+"");
								}
							}
							break;
						}
						//closeInventory(playerInventory);
                    break;

                default:
                    break;
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
	/*public void openNewInventory(UIinventory inventory,int items[][])
	{
		gamePause=true;
		OpenInventory=true;
		//inventory=null;
		//inventory=new UIinventory(invAsset,items);
		inventory.setVisability(true);
		//return inventory;
	}
	public void closeInventory(UIinventory inventory)
	{
		inventory.setVisability(false);
		gamePause=false;
		OpenInventory=false;
		//inventory=null;
	}*/
	public Player getPlayer()
	{
		return player;
	}
	public UIinventory getPlayerUIInventory()
	{
		return playerInv;
	}
	public void initInventoryUI(TableLayout playerTable,TableLayout objectTable,Context context)
	{
		//exchangeInteface=exchangeInteface;
		objectInv=new UIinventory(invAsset,objectTable,context,null,exchangeInteface);
		playerInv=new UIinventory(invAsset,playerTable,context,player,exchangeInteface);
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
	
	
	
}
