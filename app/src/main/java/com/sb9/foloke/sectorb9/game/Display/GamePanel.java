package com.sb9.foloke.sectorb9.game.Display;

import android.graphics.PointF;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.SurfaceHolder;
import android.content.Context;
import android.graphics.Canvas;
import android.util.*;
import com.sb9.foloke.sectorb9.MainThread;
import com.sb9.foloke.sectorb9.game.Managers.GameManager;
import com.sb9.foloke.sectorb9.game.UI.Text;
import com.sb9.foloke.sectorb9.game.Entities.*;
import android.app.*;
import com.sb9.foloke.sectorb9.*;
import android.view.ScaleGestureDetector.*;
import android.view.*;
import java.io.*;
import static com.sb9.foloke.sectorb9.game.Managers.GameManager.commandInteraction;
import static com.sb9.foloke.sectorb9.game.Managers.GameManager.commandMoving;
import android.graphics.*;

public class GamePanel extends SurfaceView implements SurfaceHolder.Callback
{
    //thread
    private MainThread mainThread;

    //camera
    private Camera camera;

    //objects
    private Cursor cursor;

    //UI texts
    private Text textPointOfTouch;
	private Text textScreenWH;
	public Text textFPS;
	public Text textDebug2;
	public Text textDebug3;
	public Text textDebug4;
	public Text textDebug5;
	public Text textInQueue;
	public Text debugText;
	public Text errorText;
	private Text debugExchange;
	
	///camera positioning
    private float scale=5;
    public float canvasH,canvasW;
    public PointF pointOfTouch;
    public PointF screenPointOfTouch;
	private float worldSize=3000;
	private MainActivity MA;
	public StaticEntity pressedObject;
	private GameManager gameManager;
	private MainThread MT;
	private boolean gestureInProgress=false;
	private Paint borderPaint = new Paint();
    private boolean touched=false;
    public GamePanel(Context context, AttributeSet attributeSet)
    {
        super(context, attributeSet);
        this.MA=(MainActivity)context;
		DisplayMetrics displayMetrics = new DisplayMetrics();
		((Activity) getContext()).getWindowManager()
			.getDefaultDisplay()
			.getMetrics(displayMetrics);
		canvasH = displayMetrics.heightPixels;
		canvasW = displayMetrics.widthPixels;


        screenPointOfTouch=new PointF(0,0);
        pointOfTouch=new PointF(0,0);

        textPointOfTouch=new Text(""+0+" "+0,500,400);
		textScreenWH=new Text("",500,250);
		debugText=new Text("",500,350);
		debugExchange=new Text("exchange",500,300);
		errorText=new Text("",500,450);
		textFPS=new Text("",0,30);
		textDebug2=new Text("",500,550);
		textDebug3=new Text("",500,600);
		textDebug4=new Text("",500,650);
		textDebug5=new Text("",500,700);
		textInQueue=new Text("",500,750);
		textScreenWH.setString(canvasW+"x"+canvasH);

        gameManager= new GameManager(this, MA);

        camera=new Camera(0,0,scale,gameManager.getPlayer());

        //for building and ship leading
        cursor=new Cursor(900,900,"cursor",gameManager);

		getHolder().addCallback(this);

        mainThread= new MainThread(getHolder(),this);
        setFocusable(false);
		
		cursor.setDrawable(true);
		//buildDrawingCache();
		
		
		borderPaint.setColor(Color.RED);
		borderPaint.setStyle(Paint.Style.STROKE);
		borderPaint.setStrokeWidth(20/camera.getScale());
		borderPaint.setPathEffect(new DashPathEffect(new float[] { 200/camera.getScale(), 200/camera.getScale()}, 0));
		
    }

	public void linkThread(MainThread MT)
	{
		this.MT=MT;
	}
	
	public void setFrameLimiter(boolean state)
	{
		MT.switchFrameLimit(state);
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
            pointOfTouch.set((screenPointOfTouch.x-canvasW/2)/camera.getScale()+getPalyer().getCenterX(),(screenPointOfTouch.y-canvasH/2)/camera.getScale()+getPalyer().getCenterY());
            cursor.setWorldLocation(pointOfTouch);
            cursor.tick();
        	camera.tick(scale,canvasW,canvasH);
        	gameManager.tick();
			
    }

    public void render(Canvas canvas)
    {

        super.draw(canvas);
        camera.setScreenRect(canvasW,canvasH);
        canvas.save();
        canvas.translate(-camera.getWorldLocation().x+canvas.getWidth()/2,-camera.getWorldLocation().y+canvas.getHeight()/2);
        canvas.scale(camera.getScale(),camera.getScale(),camera.getWorldLocation().x,camera.getWorldLocation().y);
			
        //objects
        gameManager.render(canvas);
        cursor.render(canvas);

			
        if (gameManager.drawDebugInfo)
        {
            camera.render(canvas);
        }

        if(pressedObject!=null)
        {
            pressedObject.drawDebugCollision(canvas);
        }

		canvas.drawRect(0,0,worldSize,worldSize,borderPaint);
        canvas.restore();

        //UI
        if(gameManager.command==commandMoving) {
            gameManager.uIhp.render(canvas);
            gameManager.uIsh.render(canvas);
        }
        textFPS.render(canvas);

		if(gameManager.drawDebugInfo)
		{
			textScreenWH.render(canvas);	
			textPointOfTouch.render(canvas);
			debugExchange.render(canvas);
			debugText.render(canvas);
			errorText.render(canvas);

			textDebug2.render(canvas);
			textDebug3.render(canvas);
			textDebug4.render(canvas);
			textDebug5.render(canvas);
			textInQueue.render(canvas);
		}
		
		
    }

    private SimpleOnScaleGestureListener gestureListener = new SimpleOnScaleGestureListener()
	{
    	@Override
		public boolean onScaleBegin(ScaleGestureDetector detector)
		{
			return true;
		}

		@Override
		public boolean onScale(ScaleGestureDetector detector)
		{
			scale *= detector.getScaleFactor();
			// Don't let the object get too small or too large.
			scale = Math.max(1f, Math.min(scale, 5.0f));
			gestureInProgress=true;
			return true;
		}

		@Override
		public void onScaleEnd(ScaleGestureDetector detector)
		{

			super.onScaleEnd(detector);
			gestureInProgress=false;
		}
		
			
			
	};

    private ScaleGestureDetector gestureDetector = new ScaleGestureDetector(getContext(), gestureListener);

	

	
	
	
    @Override
	public boolean onTouchEvent(MotionEvent event)
	{
		if(!gameManager.gamePause)
		{
			gestureDetector.onTouchEvent(event);
			if(!gestureInProgress)
			{
      		  	float x=event.getX(),y=event.getY();
      		  	screenPointOfTouch.set(x,y);
      		  	Player player=getPalyer();

            	switch (event.getAction())
                {
                	case MotionEvent.ACTION_DOWN:
                	    touched=true;
						switch (gameManager.command)
						{
							case commandMoving:
         		           		player.setMovable(true);
								break;
							case commandInteraction:
								break;
						}
                    break;
					
                	case MotionEvent.ACTION_MOVE:
                    	break;

                	case MotionEvent.ACTION_UP:
                	    touched=false;
						switch (gameManager.command)
						{
							case commandMoving:
                       			player.setMovable(false);
								break;
							case commandInteraction:
								gameManager.interactionCheck(pointOfTouch.x,pointOfTouch.y);
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

	public void save(BufferedWriter w)
	{
		gameManager.save(w);
	}

	public void load(BufferedReader r)
	{
		gameManager.load(r);
	}

	private Player getPalyer()
    {
        return gameManager.getPlayer();
    }

    public Camera getCamera()
    {
        return camera;
    }

    public GameManager getGameManager() {
        return gameManager;
    }

	public Cursor getCursor()
	{
		return cursor;
	}

	public float getWorldSize()
	{
		return worldSize;
	}
}