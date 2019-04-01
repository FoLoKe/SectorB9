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

import com.sb9.foloke.sectorb9.*;
import android.view.ScaleGestureDetector.*;
import android.view.*;
import java.io.*;
import static com.sb9.foloke.sectorb9.game.Managers.GameManager.commandInteraction;
import static com.sb9.foloke.sectorb9.game.Managers.GameManager.commandMoving;
import android.graphics.*;

import com.sb9.foloke.sectorb9.game.Funtions.*;
import com.sb9.foloke.sectorb9.game.UI.CustomViews.*;




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
    private float scale=4;
    public float canvasH,canvasW;
    public PointF pointOfTouch;
    public PointF screenPointOfTouch;
	private float worldSize=3000;

	public StaticEntity pressedObject;
	private GameManager gameManager;

	private boolean gestureInProgress=false;
	private Paint borderPaint = new Paint();
    private boolean touched=false;
	private RectF joystickBox=new RectF();
	private PointF joystickInitPoint=new PointF();
	private PointF joystickCurrentPoint=new PointF();
	private float joystickSafeZone=100;
	private float joysticSize=200;
	private PointF relatedPoint=new PointF();
	private float joysticAcceleration=0;
	private Paint debugPaint=new Paint();
	private Paint joystickPaint=new Paint();
	private Paint joystickBorderPaint=new Paint();
	private Paint joystickBgPaint=new Paint();
	private PointF stickPoint=new PointF();
	private Paint speedPaint=new Paint();
	//public GameLog LOG;
    public GamePanel(Context context, AttributeSet attributeSet)
    {
        super(context, attributeSet);
		
       MainActivity MA=(MainActivity)context;
		
		WindowManager wm = ((WindowManager) 
			context.getSystemService(Context.WINDOW_SERVICE));

		Display display = wm.getDefaultDisplay();

                if(display!=null) {

                    Point screenSize = new Point();
                    display.getRealSize(screenSize);
                    canvasW = screenSize.x;
                    canvasH = screenSize.y;
                }

		
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
		
		joystickBox.set(canvasW/1.5f,canvasH/2,canvasW,canvasH);
        gameManager= new GameManager(this, MA);

        camera=new Camera(0,0,scale,gameManager.getPlayer());

        //for building and ship leading
        cursor=new Cursor(900,900,"cursor",gameManager);

		getHolder().addCallback(this);

        mainThread= new MainThread(getHolder(),this);
        setFocusable(false);
		
		cursor.setDrawable(true);
		//buildDrawingCache();
		
		
		joystickBgPaint.setColor(Color.GRAY);
		joystickBgPaint.setAlpha(200);
		joystickPaint.setColor(Color.DKGRAY);
		joystickPaint.setAlpha(200);
		joystickBorderPaint.setColor(Color.DKGRAY);
		joystickBorderPaint.setAlpha(200);
		joystickBorderPaint.setStyle(Paint.Style.STROKE);
		joystickBorderPaint.setStrokeWidth(5);
		
		debugPaint.setColor(Color.RED);
		debugPaint.setStyle(Paint.Style.STROKE);
		borderPaint.setColor(Color.RED);
		borderPaint.setStyle(Paint.Style.STROKE);
		
		speedPaint.setColor(Color.GREEN);
		speedPaint.setTextSize((50));
		
		borderPaint.setStrokeWidth(20/camera.getScale());
		borderPaint.setPathEffect(new DashPathEffect(new float[] { 200/camera.getScale(), 200/camera.getScale()}, 0));
		

		
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
            {GameLog.update(""+e,1);}
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
		joystickCurrentPoint.set(screenPointOfTouch.x,screenPointOfTouch.y);
		relatedPoint.set(-joystickInitPoint.x+joystickCurrentPoint.x+camera.getWorldLocation().x,-joystickInitPoint.y+joystickCurrentPoint.y+camera.getWorldLocation().y);
		
		if(touched)
		{
			relatedPoint.set(-joystickInitPoint.x+joystickCurrentPoint.x+camera.getWorldLocation().x,-joystickInitPoint.y+joystickCurrentPoint.y+camera.getWorldLocation().y);
			
			PointF tpoint=new PointF(-joystickInitPoint.x+joystickCurrentPoint.x,-joystickInitPoint.y+joystickCurrentPoint.y);
			float size=(float)Math.sqrt(tpoint.x*tpoint.x+tpoint.y*tpoint.y);
			float rl=1;
		
			if (size!=0&&size>200)
			rl=200/size;
		
			if(size>200)
			{
				size=200;
			}
		
			float minAcceleration=joystickSafeZone; //0%
			float maxAcceleration=joysticSize; //100%
		
			size=(size-minAcceleration)/(maxAcceleration-minAcceleration);
			joysticAcceleration=size;//200;
			
			stickPoint.set(joystickInitPoint.x+tpoint.x*rl,joystickInitPoint.y+tpoint.y*rl);
		}
    }

    public void render(Canvas canvas)
    {
		if(canvas==null)
			return;
			
        camera.setScreenRect(canvasW,canvasH);
		
        canvas.save();
        canvas.translate(-camera.getWorldLocation().x+canvasW/2,-camera.getWorldLocation().y+canvasH/2);
        canvas.scale(camera.getScale(),camera.getScale(),camera.getWorldLocation().x,camera.getWorldLocation().y);
			
        //objects
        gameManager.render(canvas);
        cursor.render(canvas);

        if (Options.drawDebugInfo.getBoolean())
        {
            camera.render(canvas);
			canvas.drawCircle(relatedPoint.x,relatedPoint.y,5,debugPaint);
		}
			
        if(pressedObject!=null)
        {
            pressedObject.drawDebugCollision(canvas);
        }

		canvas.drawRect(0,0,worldSize,worldSize,borderPaint);
		
        canvas.restore();

        //UI
        if(gameManager.command==commandMoving) 
		{
            gameManager.uIhp.render(canvas);
            gameManager.uIsh.render(canvas);
			if(touched)
			{
				canvas.drawCircle(joystickInitPoint.x,joystickInitPoint.y,joysticSize,joystickBgPaint);
				canvas.drawCircle(joystickInitPoint.x,joystickInitPoint.y,joystickSafeZone,joystickBorderPaint);
				canvas.drawCircle(stickPoint.x,stickPoint.y,75,joystickPaint);
			}
			
			String s=(""+(gameManager.getPlayer().getSpeed()));
			if(s.length()>4)
				s=s.substring(0,4);
			s+=(" m/s");
				
			canvas.drawText(""+s,canvasW/2+128,canvasH/2,speedPaint);
        }

		Player player=gameManager.getPlayer();
		
        if(player.getSpeed()>0) 
		{
            Path p = new Path();
			float size=player.getSpeed()/100+0.5f;
            p.moveTo(32*size, -160*size - player.getSpeed() * 2);
            p.lineTo(0, -192*size - player.getSpeed() * 2);
            p.lineTo(-32*size, -160*size - player.getSpeed() * 2);

            Paint debugPaint2=new Paint();
            debugPaint2.setColor(Color.CYAN);
            Matrix m = new Matrix();
            m.postRotate((float)Math.toDegrees(Math.atan2(player.getDy(),player.getDx()))+90);
            m.postTranslate(canvasW / 2, canvasH / 2);
            p.transform(m);
            canvas.drawPath(p, debugPaint2);
        }
		
		for(Entity e:gameManager.getEntities())
		{
			if(e.getTeam()!=0)
				if(e.getTeam()!=player.getTeam())
				{
					Path p = new Path();
					float dist=distanceTo(player.getWorldLocation(),e.getWorldLocation());
					float size=Math.max(1,dist/1000);
					size=Math.min(4,size);
			
					size=1/size;
           		 	p.moveTo(32*size, -64);
            		p.lineTo(0, -64-32*size);
            		p.lineTo(-32*size, -64);

            		Paint debugPaint2=new Paint();
            		debugPaint2.setColor(Color.RED);
					debugPaint2.setStyle(Paint.Style.STROKE);
					debugPaint2.setStrokeWidth(5);
            		Matrix m = new Matrix();
			
            		m.postRotate((float)Math.toDegrees(-Math.atan2(-player.getWorldLocation().x+e.getWorldLocation().x,-player.getWorldLocation().y+e.getWorldLocation().y))+180);
					m.postTranslate(canvasW / 2, canvasH / 2);
            		p.transform(m);
					
					canvas.drawPath(p, debugPaint2);
					
					float[] f=new float[]{0,-192*size};
					m.mapPoints(f);
					debugPaint2.setStrokeWidth(2);
					debugPaint2.setTextSize(40);
					debugPaint2.setStyle(Paint.Style.FILL);
					//debugPaint2.set
					
					String s;
					if(dist>1000)
					{
						s=(""+(dist/1000));
						if(s.length()>3)
							s=s.substring(0,3);
						s+=(" km");
					}
					else
					{
						s=(""+(dist));
						if(s.length()>3)
							s=s.substring(0,3);
						s+=(" m");
					}
					//canvas.drawText(""+s,canvasW/2+128,canvasH/2,speedPaint);
					
					canvas.drawText(""+s,f[0],f[1],debugPaint2);
            		
				}
			}
			
        textFPS.render(canvas);

		if(Options.drawDebugInfo.getBoolean())
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
			
			canvas.drawRect(joystickBox,debugPaint);
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
			scale = Math.max(0.5f, Math.min(scale, 4.0f));
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
                	    
						switch (gameManager.command)
						{
							case commandMoving:
							if(joystickBox.contains(x,y))
							{
								touched=true;
								joystickInitPoint.set(x,y);
								
         		           		player.setMovable(true);
							}
								break;
							case commandInteraction:
								break;
						}
                    break;
					
                	case MotionEvent.ACTION_MOVE:
                    	break;

                	case MotionEvent.ACTION_UP:
                	    
						switch (gameManager.command)
						{
							case commandMoving:
								touched=false;
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
	
	public PointF getMovementPoint()
	{
		return relatedPoint;
	}
	
	public float getMovementSpeed()
	{
		return joysticAcceleration;
	}
	
	public boolean getTouched()
	{
		return touched;
	}

	public float distanceTo(PointF a,PointF b)
	{
		return (float)Math.sqrt((a.x-b.x)*(a.x-b.x)+(a.y-b.y)*(a.y-b.y));
	}
	
	public void setWorldSize(float ws){
		worldSize=ws;
	}
}
