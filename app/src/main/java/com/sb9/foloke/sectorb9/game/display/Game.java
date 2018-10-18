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
    private UIAsset uiAsset;
	private InventoryAsset invAsset;
	
	private UIcustomImage destroyedImage;
	
    //objects
    private Player player;
    private Cursor cursor;
	
	//objects arrays
	private Asteroid asteroids[];
	
    //UI

    private Text textPointOfTouch;
	private Text textScreenWH;
    private float scale=4;

    private float canvasH,canvasW;
    private PointF pointOfTouch;
    private PointF screenPointOfTouch;

    boolean drawDebugInf=false;
	boolean playerDestroyed=false;
	boolean OpenInventory=false;
	boolean gamePause=false;
	private Timer destroyedTimer;
	
	//private UIinventory playerInventory;
	UIinventory playerInv;
	UIinventory objectInv;
	UICommInterface exchangeInteface;
	
	
	//debug 
	String exchangeFrom;
	String exchangeTo;
	Text debugExchange;
	
    public Game(Context context, AttributeSet attributeSet)
    {
		
        super(context, attributeSet);
		
		//View rootView = ((Activity)_context).Window.DecorView.FindViewById(Android.Resource.Id.Content);
		//View rootView = ((Activity)context).getWindow().getDecorView().findViewById(android.R.id.content);
		//TableLayout playerTable=rootView.findViewById(R.id.PlayerTableLayout);
		//TableLayout objectTable=((Activity)context).getWindow().getDecorView().findViewById(R.id.ObjectTableLayout);
		

        options=new BitmapFactory.Options();
        options.inScaled=false;
		
        background=Bitmap.createBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.galactic_outflow,options));
        shipAsset.init(Bitmap.createBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.ships_sheet,options)));
		invAsset.init(Bitmap.createBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.ui_inventory_sheet,options)));
        uiAsset.init(Bitmap.createBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.ui_asset_sheet,options)));
		exchangeInteface.init(this);
        screenPointOfTouch=new PointF(0,0);
        pointOfTouch=new PointF(0,0);
		//TableLayout playerTable=findViewById(R.id.PlayerTableLayout);
		//TableLayout objectTable=findViewById(R.id.ObjectTableLayout);
        player=new Player(900,900,shipAsset,uiAsset,this);
		
		
        cursor=new Cursor(900,900,shipAsset);
		asteroids=new Asteroid[50];
		Random rand=new Random();
		for (int i=0;i<asteroids.length;i++)
		asteroids[i]=new Asteroid(50*rand.nextInt(10)+25*rand.nextInt(20),100*rand.nextInt(10)+20*rand.nextInt(50),shipAsset);
		canvasH=canvasW=100;
        textPointOfTouch=new Text(""+0+" "+0,200,250);
		textScreenWH=new Text("",200,300);
		debugExchange=new Text("",500,300);
        camera=new Camera(0,0,scale,player);
		
		
		//playerInventory=new UIinventory(invAsset,player.getInventory());
		//playerInventory.setVisability(true);
		destroyedImage=new UIcustomImage(Bitmap.createBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.ui_asset_sheet,options)),0,24,64,24);
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
				for(int i=0;i<asteroids.length;i++)
				{
					asteroids[i].tick();
				}
			}
        	camera.tick(scale,canvasW,canvasH);
			textScreenWH.setString(canvasW+"x"+canvasH);
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
			for(int i=0;i<asteroids.length;i++)
			{
				if(asteroids[i].getCollsionBox().intersect(camera.getScreenRect()))
					asteroids[i].setRenderable(true);
				else
					asteroids[i].setRenderable(false);
			}
			for(int i=0;i<asteroids.length;i++)
			{
				asteroids[i].render(canvas);
				if (drawDebugInf) 
				{
            		asteroids[i].drawVelocity(canvas);
            		asteroids[i].drawDebugBox(canvas);
        		}
			}
        	if (drawDebugInf) 
			{
            	camera.render(canvas);
            	player.drawVelocity(canvas);
        	}
        	canvas.restore();
			//UI
			if(drawDebugInf)
			textScreenWH.render(canvas);	
			
			debugExchange.render(canvas);
			
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
        pointOfTouch.set((x-canvasW/2)/camera.getScale()+player.getWorldLocation().x,(y-canvasH/2)/camera.getScale()+player.getWorldLocation().y);
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:

                    pointOfTouch.set((x-canvasW/2)/camera.getScale()+player.getWorldLocation().x,(y-canvasH/2)/camera.getScale()+player.getWorldLocation().y);
					
                    textPointOfTouch.setString(pointOfTouch.x+" "+pointOfTouch.y);
					cursor.setDrawable(true);
                    player.setMovable(true);
					//openNewInventory(playerInventory,player.getInventory());
                    break;
                case MotionEvent.ACTION_MOVE:
                    pointOfTouch.set((x-canvasW/2)/camera.getScale()+player.getWorldLocation().x,(y-canvasH/2)/camera.getScale()+player.getWorldLocation().y);
                    textPointOfTouch.setString(pointOfTouch.x+" "+pointOfTouch.y);
                    break;
                    case MotionEvent.ACTION_UP:
						cursor.setDrawable(false);
                        player.setMovable(false);
						
						//closeInventory(playerInventory);
                    break;

                default:
                    break;
        }
		}
                return true;
    }
	public Asteroid[] getAsteroids()
	{
		return asteroids;
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
		player=new Player(900,900,shipAsset,uiAsset,this);
		playerDestroyed=false;
		camera.setPointOfLook(player);
		gamePause=false;
	}
	public void openNewInventory(UIinventory inventory,int items[][])
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
	}
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
		objectInv=new UIinventory(invAsset,objectTable,context,asteroids[0],exchangeInteface);
		playerInv=new UIinventory(invAsset,playerTable,context,player,exchangeInteface);
	}
	public UIinventory getObjectUIInventory()
	{
		return objectInv;
	}
}
