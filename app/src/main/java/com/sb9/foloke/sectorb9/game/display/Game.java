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


import android.util.*;

import com.sb9.foloke.sectorb9.MainThread;
import com.sb9.foloke.sectorb9.R;
import com.sb9.foloke.sectorb9.game.Assets.UIAsset;
import com.sb9.foloke.sectorb9.game.entities.Player;
import com.sb9.foloke.sectorb9.game.Assets.ImageAssets;
import com.sb9.foloke.sectorb9.game.UI.Text;
import com.sb9.foloke.sectorb9.game.entities.Cursor;
import com.sb9.foloke.sectorb9.game.entities.*;


public class Game extends SurfaceView implements SurfaceHolder.Callback
{
    //thread
    private MainThread mainThread;

    //background
    private Bitmap background;

    //camera
    private Camera camera;

    //assets
    BitmapFactory.Options options;
    private Bitmap sheetOfShips;
    private ImageAssets shipAsset;
    private Bitmap sheetOfUI;
    private UIAsset uiAsset;

    //objects
    private Player player;
    private Cursor cursor;
	
	//objects arrays
	private DynamicEntity entities[];
	private Asteroid asteroids[];
	
    //UI
    private Text textPointOfPlayer;
    private Text textPointOfTouch;
	private Text textScreenWH;
    private float scale=4;

    private float canvasH,canvasW;
    private PointF pointOfTouch;
    private PointF screenPointOfTouch;

    boolean drawDebugInf=false;
	
	
    public Game(Context context, AttributeSet attributeSet)
    {
        super(context, attributeSet);

         options=new BitmapFactory.Options();
        options.inScaled=false;
        background=Bitmap.createBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.galactic_outflow,options));
        sheetOfShips=Bitmap.createBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.ships_sheet,options));
        sheetOfUI=Bitmap.createBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.ui_asset_sheet,options));
        shipAsset.init(sheetOfShips);
        uiAsset.init(sheetOfUI);
        screenPointOfTouch=new PointF(0,0);
        pointOfTouch=new PointF(0,0);
        player=new Player(900,900,shipAsset,uiAsset,this);
        cursor=new Cursor(900,900,shipAsset);
		asteroids=new Asteroid[5];
		for (int i=0;i<asteroids.length;i++)
		asteroids[i]=new Asteroid(800,900-100*i,shipAsset);
		canvasH=canvasW=100;
        textPointOfPlayer=new Text(""+player.getCenterX()+" "+player.getCenterY(),200,200);
        textPointOfTouch=new Text(""+0+" "+0,200,250);
		textScreenWH=new Text("",200,300);
        camera=new Camera(0,0,scale,player);



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
		
        cursor.setWorldLocation(pointOfTouch);
        player.addMovement(screenPointOfTouch,canvasW,canvasH);
        player.RotationToPoint(pointOfTouch);
        player.tick();
		for(int i=0;i<asteroids.length;i++)
		{
			asteroids[i].tick();
		}
        camera.tick(scale,canvasW,canvasH);
		textScreenWH.setString(canvasW+"x"+canvasH);

    }
    public void render(Canvas canvas)
    {
        super.draw(canvas);
		
        canvasW=canvas.getWidth();
        canvasH=canvas.getHeight();
		camera.setScreenRect(canvasW,canvasH);
        canvas.save();
        Paint tempPaint=new Paint();
        canvas.drawColor(Color.rgb(50,50,50));
		
        canvas.translate(-camera.getWorldLocation().x+canvas.getWidth()/2,-camera.getWorldLocation().y+canvas.getHeight()/2);
        canvas.scale(camera.getScale(),camera.getScale(),camera.getWorldLocation().x,camera.getWorldLocation().y);

        canvas.drawBitmap(background,0,0,tempPaint);



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
		if (drawDebugInf) {
            asteroids[i].drawVelocity(canvas);
            asteroids[i].drawDebugBox(canvas);
        }
		}
        if (drawDebugInf) {
            camera.render(canvas);
            player.drawVelocity(canvas);
        }
        canvas.restore();


		//UI
		if(drawDebugInf)
		textScreenWH.render(canvas);
		
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x=event.getX(),y=event.getY();
        screenPointOfTouch.set(x,y);
        pointOfTouch.set((x-canvasW/2)/camera.getScale()+player.getWorldLocation().x,(y-canvasH/2)/camera.getScale()+player.getWorldLocation().y);
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
					
                    pointOfTouch.set((x-canvasW/2)/camera.getScale()+player.getWorldLocation().x,(y-canvasH/2)/camera.getScale()+player.getWorldLocation().y);
					
                    textPointOfTouch.setString(pointOfTouch.x+" "+pointOfTouch.y);
					cursor.setDrawable(true);
                    player.setMovable(true);
                    break;
                case MotionEvent.ACTION_MOVE:
                    pointOfTouch.set((x-canvasW/2)/camera.getScale()+player.getWorldLocation().x,(y-canvasH/2)/camera.getScale()+player.getWorldLocation().y);

                    textPointOfTouch.setString(pointOfTouch.x+" "+pointOfTouch.y);
                    break;
                    case MotionEvent.ACTION_UP:
						cursor.setDrawable(false);
                        player.setMovable(false);
                    break;

                default:
                    break;
        }


                return true;
    }
	public Asteroid[] getAsteroids()
	{
		return asteroids;
	}
}
