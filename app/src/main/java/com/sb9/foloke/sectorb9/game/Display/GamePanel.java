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
    
    private GameManager gameManager;
    //camera
    private Camera camera;

    //objects
    private Cursor cursor;

	///camera positioning
    private float scale=4;
    private float canvasH,canvasW;
    public PointF pointOfTouch;
    public PointF screenPointOfTouch;
	private float worldSize=3000;
	public Text textFPS;
	public StaticEntity pressedObject;
	private Paint debugPaint=new Paint();

	private boolean gestureInProgress=false;
	private Paint borderPaint = new Paint();
    private boolean touched=false;
	
	private Paint speedPaint=new Paint();

    public GamePanel(Context context, AttributeSet attributeSet)
    {
        super(context, attributeSet);
		
        MainActivity MA=(MainActivity)context;
		gameManager=MA.getGameManager();
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

        
		textFPS=new Text("",0,30);
		
		
		debugPaint.setColor(Color.RED);
		debugPaint.setStyle(Paint.Style.STROKE);


        camera=new Camera(0,0,scale,gameManager.getPlayer());

        cursor=new Cursor(900,900,"cursor",gameManager);

		getHolder().addCallback(this);

        
        setFocusable(false);
		
		cursor.setDrawable(true);

		
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
        
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder p1)
    {
        gameManager.shutdown();
    }

    @Override
    public void surfaceChanged(SurfaceHolder p1, int p2, int p3, int p4) { }

    public void tick()
    {
        pointOfTouch.set((screenPointOfTouch.x-canvasW/2)/camera.getScale()+gameManager.getPlayer().getCenterX(),(screenPointOfTouch.y-canvasH/2)/camera.getScale()+gameManager.getPlayer().getCenterY());
        cursor.setWorldLocation(pointOfTouch);
        cursor.tick();
      	camera.tick(scale,canvasW,canvasH);
       	
		
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
        
        cursor.render(canvas);

        if (Options.drawDebugInfo.getBoolean())
        {
            camera.render(canvas);
			
		}
			
        if(pressedObject!=null)
        {
            pressedObject.drawDebugCollision(canvas);
        }

		canvas.drawRect(0,0,worldSize,worldSize,borderPaint);
		
        

        //UI
        

		
		
    }
	public void drawWorldBorder()
	{
		
	}
	public void drawPlayerMovement(Canvas canvas)
	{
		Player player=gameManager.getPlayer();
		String s=(""+(gameManager.getPlayer().getSpeed()));
		if(s.length()>4)
			s=s.substring(0,4);
		s+=(" m/s");

		canvas.drawText(""+s,canvasW/2+128,canvasH/2,speedPaint);
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
		
	}
	
	public void drawRadioPoints(Canvas canvas)
	{Player player=gameManager.getPlayer();
		
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
					//canvas.drawText
					canvas.drawText(""+s,f[0],f[1],debugPaint2);

				}
		}

        textFPS.render(canvas);
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
      		  	//Player player=gameManager.getPlayer();
				
            	switch (event.getAction())
                {
                	case MotionEvent.ACTION_DOWN:
                	    
						switch (gameManager.command)
						{
							case commandMoving:
							gameManager.checkJoystick(true,new PointF(x,y));
							
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
								gameManager.checkJoystick(false,new PointF(x,y));
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


	public PointF getPointOfTouch()
	{
		return screenPointOfTouch;
	}
	
    public Camera getCamera()
    {
        return camera;
    }



	public Cursor getCursor()
	{
		return cursor;
	}

	public float getWorldSize()
	{
		return worldSize;
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
