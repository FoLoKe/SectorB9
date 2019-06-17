package com.sb9.foloke.sectorb9.game.Display;

import android.content.*;
import android.graphics.*;
import android.util.*;
import android.view.*;
import android.view.ScaleGestureDetector.*;
import com.sb9.foloke.sectorb9.*;
import com.sb9.foloke.sectorb9.game.Entities.*;
import com.sb9.foloke.sectorb9.game.Funtions.*;
import com.sb9.foloke.sectorb9.game.Managers.*;
import com.sb9.foloke.sectorb9.game.UI.*;
import com.sb9.foloke.sectorb9.game.UI.CustomViews.*;

import com.sb9.foloke.sectorb9.game.Entities.Entity;

import static com.sb9.foloke.sectorb9.game.Managers.GameManager.command;
import android.view.GestureDetector.*;
import java.util.*;




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
    
	public RectF interactionBox=new RectF();
	
	private float worldSize=3000; //3km
	public Text textFPS;
	//public Entity pressedObject;
	private Paint debugPaint=new Paint();
	
	public PointF cameraPoint=new PointF();
	
	private PointF cameraToOffset=new PointF();
	
	
	private Paint borderPaint = new Paint();
    private boolean touched=false;
	
	private Paint speedPaint=new Paint();
	
	private enum touchType{MOVE,TOUCH,GROUP}

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
		
		debugPaint.setStyle(Paint.Style.FILL_AND_STROKE);

		
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
        pointOfTouch.set((currentTouchPoint.x-canvasW/2)/camera.getScale()+cameraPoint.x,(currentTouchPoint.y-canvasH/2)/camera.getScale()+cameraPoint.y);
        cursor.setWorldLocation(pointOfTouch);
        cursor.tick();
		if(gameManager.getPlayer()!=null&&gameManager.currentCommand==command.CONTROL)
			cameraPoint.set(gameManager.getPlayer().getCenterX(),gameManager.getPlayer().getCenterY());
		else
		{
			cameraPoint.offset(cameraToOffset.x,cameraToOffset.y);
			cameraToOffset.set(0,0);
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
    }
	
	public void postRender(Canvas canvas)
	{
		Paint p=new Paint();
		p.setStrokeWidth(3);
		p.setColor(Color.GREEN);
		p.setStyle(Paint.Style.STROKE);
		cursor.render(canvas);
		
		if(action==screenAction.select)
		canvas.drawRect(selectBox,p);
		canvas.drawRect(interactionBox,debugPaint);
		canvas.drawRect(0,0,worldSize,worldSize,borderPaint);
		canvas.restore();
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
			
			return true;
		}

		@Override
		public boolean onScale(ScaleGestureDetector detector)
		{
			scale *= detector.getScaleFactor();
			scale = Math.max(0.5f, Math.min(scale, 4.0f));
			action=screenAction.scale;
			return true;
		}

		@Override
		public void onScaleEnd(ScaleGestureDetector detector)
		{
			super.onScaleEnd(detector);
			
		}	
	};

    private ScaleGestureDetector gestureDetector = new ScaleGestureDetector(getContext(), gestureListener);
	
	/////TODO:
	// onLongTouch start "select group"
	// onActionUP on touch type select action
	
	
	private float touchOffset=0;
	private PointF firstTouchPoint=new PointF();
	private PointF currentTouchPoint=new PointF();
	private long firstTouchTime;
	private long currentTouchTime;
	private enum screenAction{simple,select,scale,move}
	private screenAction action=screenAction.simple;
	private RectF selectBox=new RectF();
	
    @Override
	public boolean onTouchEvent(MotionEvent event)
	{
		if(!gameManager.isPaused)								//if no pause
		{
			float x=event.getX(),y=event.getY();
			
			if(event.getPointerCount()>1)
			{
				action=screenAction.scale;
			}
			
			if(action==screenAction.scale)
				gestureDetector.onTouchEvent(event);
			//TODO CHECK TO POINTERS
            {
					
				
				switch(event.getAction())
				{
					case MotionEvent.ACTION_DOWN:
							
						firstTouchPoint.set(x,y);
						firstTouchTime=System.nanoTime();
						action=screenAction.simple;
						cameraToOffset.set(0,0);
						currentTouchPoint.set(firstTouchPoint);
						
						switch (gameManager.currentCommand)
						{
							case CONTROL:
								gameManager.checkJoystick(true,new PointF(x,y));
								break;
						}
						break;
						
					case MotionEvent.ACTION_UP:
						GameLog.update("unpressed "+action+" "+ gameManager.currentCommand,3);
						
						switch (gameManager.currentCommand)
						{
							case CONTROL:
								gameManager.checkJoystick(false,new PointF(x,y));
								break;
						}
						
						if(action==screenAction.simple)
						{
							switch (gameManager.currentCommand)
							{
								case INTERACTION:
									gameManager.interactionCheck(pointOfTouch.x,pointOfTouch.y);
									break;
									
								case EXCHANGE:
									gameManager.interactionCheck(pointOfTouch.x,pointOfTouch.y);
									break;
									
								case ORDER:
									gameManager.interactionCheck(pointOfTouch.x,pointOfTouch.y);
									break;
							}
						}
							if(action==screenAction.select)
							{
								switch (gameManager.currentCommand)
								{
									case INTERACTION:
										gameManager.interactionCheck(selectBox);
										break;
									
									case ORDER:
										gameManager.interactionCheck(selectBox);
										break;
								}
							}
                        
						action=screenAction.simple;
						break;
						
					case MotionEvent.ACTION_MOVE:
						if(action!=screenAction.scale)
						{
							touchOffset=distanceTo(firstTouchPoint,currentTouchPoint);
							if(touchOffset>32&&action!=screenAction.select)
								action=screenAction.move;
							else
							{
								if(currentTouchTime-firstTouchTime>100000000&&gameManager.currentCommand==GameManager.command.INTERACTION)
								{
									action=screenAction.select;
								}
							}
								
							if(action==screenAction.move)
							{
								cameraToOffset.set((-event.getX()+currentTouchPoint.x)/scale,(-event.getY()+currentTouchPoint.y)/scale);
							}
							
							if(action==screenAction.select)
							{
								float x1=(firstTouchPoint.x-canvasW/2)/camera.getScale()+camera.getWorldLocation().x;
								float y1=(firstTouchPoint.y-canvasH/2)/camera.getScale()+camera.getWorldLocation().y;
								float x2=(currentTouchPoint.x-canvasW/2)/camera.getScale()+camera.getWorldLocation().x;
								float y2=(currentTouchPoint.y-canvasH/2)/camera.getScale()+camera.getWorldLocation().y;
								
								if(x1>x2)
								{
									float t=x2;
									x2=x1;
									x1=t;
								}
								
								if(y1>y2)
								{
									float t=y2;
									y2=y1;
									y1=t;
								}
								
								
								selectBox.set(x1,y1,x2,y2);
								
							}
						}
					break;
			    }
			    currentTouchTime=System.nanoTime();
			    currentTouchPoint.set(x,y);
			}
			/*gestureDetector.onTouchEvent(event);				//check gesture
			if(!gestureInProgress)								//if not a jesture
			{
      		  	float x=event.getX(),y=event.getY();			//screen touch point
      		  	screenPointOfTouch.set(x,y);
				
            	switch (event.getAction())
                {
                	case MotionEvent.ACTION_DOWN:
						surfaceTouched=true;
						touchOffset=0;
						firstTouchTime=System.nanoTime();
						
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
						surfaceTouched=true;
						currentTouchPoint.set(x,y);
						touchTime=System.nanoTime()-firstTouchTime;
						touchOffset=distanceTo(firstTouchPoint,currentTouchPoint);
						
						if(pressed&&event.getPointerCount()==1)
						{
							cameraToOffset.set((lastTouchPoint.x-screenPointOfTouch.x)/scale,(lastTouchPoint.y-screenPointOfTouch.y)/scale);
							lastTouchPoint.set(screenPointOfTouch);
							GameLog.update("screen move",3);
						}
						
                    	break;

                	case MotionEvent.ACTION_UP:
						surfaceTouched=false;
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
			}*/
		}
		else
		{
			if(!gameManager.isInMenu&&!gameManager.isLoading)
			gameManager.isPaused=false;
		}
		return true;
		
    }
	
	public PointF getPointOfTouch()
	{
		return currentTouchPoint;
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
