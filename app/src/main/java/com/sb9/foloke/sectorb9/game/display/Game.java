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
import com.sb9.foloke.sectorb9.game.entities.Entity;
import com.sb9.foloke.sectorb9.game.entities.Player;
import com.sb9.foloke.sectorb9.game.entities.SpriteSheet;
import com.sb9.foloke.sectorb9.game.entities.ImageAssets;
import com.sb9.foloke.sectorb9.game.entities.Text;

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

    //objects
    private Player player;
    private Player testBox;

    //UI
    private Text textPointOfPlayer;
    private Text textPointOfTouch;
    private float scale=4;

    private float canvasH,canvasW;
    private PointF pointOfTouch;
    private PointF screenPointOfTouch;
    public Game(Context context, AttributeSet attributeSet)
    {
        super(context, attributeSet);

         options=new BitmapFactory.Options();
        //options.inPreferredConfig=Bitmap.Config.ARGB_8888;
        //options.inDither=false;
        //options.inDensity=0;
        options.inScaled=false;
        background=Bitmap.createBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.galactic_outflow,options));
        sheetOfShips=Bitmap.createBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.ships_sheet,options));

        shipAsset.init(sheetOfShips);

        screenPointOfTouch=new PointF(0,0);
        pointOfTouch=new PointF(0,0);
        player=new Player(900,900,shipAsset);
       testBox=new Player(800,900,shipAsset);
        textPointOfPlayer=new Text(""+player.getCenterX()+" "+player.getCenterY(),200,200);
        textPointOfTouch=new Text(""+0+" "+0,200,250);
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

        testBox.setWorldLocation(pointOfTouch);
        player.addMovement(screenPointOfTouch,canvasW,canvasH);
        player.RotationToPoint(pointOfTouch);
        player.tick();
        camera.tick(scale);

    }
    public void render(Canvas canvas)
    {
        super.draw(canvas);

        canvasW=canvas.getWidth();
        canvasH=canvas.getHeight();

        canvas.save();
        Paint tpaint=new Paint();
        canvas.drawColor(Color.rgb(200,200,200));

        canvas.translate(-camera.getWorldLocation().x+canvas.getWidth()/2,-camera.getWorldLocation().y+canvas.getHeight()/2);
        canvas.scale(camera.getScale(),camera.getScale(),camera.getWorldLocation().x,camera.getWorldLocation().y);

        canvas.drawBitmap(background,0,0,tpaint);
        player.render(canvas);
        //player.drawDebugBox(canvas);
        //testBox.render(canvas);
        //testBox.drawDebugBox(canvas);

        canvas.restore();
        //textPointOfPlayer.render(canvas);
        //textPointOfTouch.render(canvas);

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
                    player.setMovable(true);
                    break;
                case MotionEvent.ACTION_MOVE:
                    pointOfTouch.set((x-canvasW/2)/camera.getScale()+player.getWorldLocation().x,(y-canvasH/2)/camera.getScale()+player.getWorldLocation().y);

                    textPointOfTouch.setString(pointOfTouch.x+" "+pointOfTouch.y);
                    break;
                    case MotionEvent.ACTION_UP:
                        player.setMovable(false);
                    break;

                default:
                    break;
        }


                return true;
    }
}