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

import static com.sb9.foloke.sectorb9.game.Managers.GameManager.command;
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
	private float worldSize=3000;//3km
	public Text textFPS;
	public Entity pressedObject;
	private Paint debugPaint=new Paint();
	
	public PointF cameraPoint=new PointF();
	private PointF lastTouchPoint=new PointF();
	private PointF cameraToOffset=new PointF();
	private boolean pressed=false;
	
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


        camera=new Camera(0,0,0);

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
        pointOfTouch.set((screenPointOfTouch.x-canvasW/2)/camera.getScale()+cameraPoint.x,(screenPointOfTouch.y-canvasH/2)/camera.getScale()+cameraPoint.y);
        cursor.setWorldLocation(pointOfTouch);
        cursor.tick();
		if(gameManager.getPlayer()!=null&&gameManager.currentCommand==command.CONTROL)
			cameraPoint.set(gameManager.getPlayer().getCenterX(),gameManager.getPlayer().getCenterY());
		else
		{
			cameraPoint.offset(cameraToOffset.x,cameraToOffset.y);
		}
      	camera.tick(scale,cameraPoint);
       	
		
    }

    public void preRender(Canvas canvas)
    {
		
			
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

		
    }
	
	public void postRender(Canvas canvas)
	{
		cursor.render(canvas);
		canvas.drawRect(0,0,worldSize,worldSize,borderPaint);
		canvas.restore();
	}
	
	
	public void drawWorldBorder()
	{
		
	}
	public void drawPlayerMovement(Canvas canvas)
	{
		DynamicEntity player=gameManager.getPlayer();
		if(player==null)
			return;
		String s=(""+(gameManager.getPlayer().getSpeed()));
		if(s.length()>4)
			s=s.substring(0,4);
		s+=(" m/s");

		canvas.drawText(""+s,canvasW/2+128,canvasH/2,speedPaint);
        if(player.getSpeed()>0) 
		{
            Path p = new Path();
			float size=player.getSpeed()/250+0.5f;
            p.moveTo(32*size, -160*size - player.getSpeed() * 2);
            p.lineTo(0, -192*size - player.getSpeed() * 2);
            p.lineTo(-32*size, -160*size - player.getSpeed() * 2);

            Paint debugPaint2=new Paint();
            debugPaint2.setColor(Color.parseColor("#ffff00"));
            Matrix m = new Matrix();
            m.postRotate((float)Math.toDegrees(Math.atan2(player.getDy(),player.getDx()))+90);
            m.postTranslate(canvasW / 2, canvasH / 2);
            p.transform(m);
            canvas.drawPath(p, debugPaint2);
        }
		
	}
	
	public void drawRadioPoints(Canvas canvas)
	{
		DynamicEntity player=gameManager.getPlayer();
		
		for(Entity e:gameManager.getEntities())
		{
			if(true)
				if(e!=gameManager.getPlayer())
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
					switch(e.getTeam())
					{
						case 0:
            				debugPaint2.setColor(Color.LTGRAY);
							break;
						case 1:
            				debugPaint2.setColor(Color.GREEN);
							break;
						case 2:
            				debugPaint2.setColor(Color.RED);
							break;
					}
					debugPaint2.setStyle(Paint.Style.STROKE);
					int saturation=(int)(dist/10);
					if(saturation>255)
						saturation=255;
					debugPaint2.setAlpha(255-saturation);
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
			pressed=false;
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
		
		if(!gameManager.isPaused)
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
                	    
						switch (gameManager.currentCommand)
						{
							case CONTROL:
								gameManager.checkJoystick(true,new PointF(x,y));
								break;
								
							case INTERACTION:
								if(event.getPointerCount()==1)
								{
									pressed=true;
									lastTouchPoint.set(screenPointOfTouch);
								}
								break;
						}
                    break;
					
                	case MotionEvent.ACTION_MOVE:
						if(pressed&&event.getPointerCount()==1)
						{
							cameraToOffset.set((lastTouchPoint.x-screenPointOfTouch.x)/scale,(lastTouchPoint.y-screenPointOfTouch.y)/scale);
							lastTouchPoint.set(screenPointOfTouch);
						}
                    	break;

                	case MotionEvent.ACTION_UP:
                	    
						switch (gameManager.currentCommand)
						{
							case CONTROL:
								gameManager.checkJoystick(false,new PointF(x,y));
								break;
							case INTERACTION:
								pressed=false;
								cameraToOffset.set(0,0);
								gameManager.interactionCheck(pointOfTouch.x,pointOfTouch.y);
								break;
							case EXCHANGE:
								pressed=false;
								cameraToOffset.set(0,0);
								gameManager.interactionCheck(pointOfTouch.x,pointOfTouch.y);
								break;
							case ORDER:
								pressed=false;
								cameraToOffset.set(0,0);
								gameManager.interactionCheck(pointOfTouch.x,pointOfTouch.y);
								break;
						}
                        break;
					case MotionEvent.ACTION_POINTER_1_UP:
						pressed=false;
						cameraToOffset.set(0,0);
						break;
                	default:
                        break;
                }
			}
		}
		else
		{
			if(!gameManager.isInMenu&&!gameManager.isLoading)
			gameManager.isPaused=false;
		}
		
		GameLog.update(pressed+"",3);
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
