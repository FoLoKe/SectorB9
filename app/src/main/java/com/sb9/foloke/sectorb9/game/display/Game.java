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


        player=new Player(900,900,shipAsset);
       testBox=new Player(800,900,shipAsset);
        textPointOfPlayer=new Text(""+player.getCenterX()+player.getCenterY(),200,200);
        camera=new Camera(0,0,1,player);



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
        camera.tick();
    }
    public void render(Canvas canvas)
    {
        super.draw(canvas);

        camera.setScreenXcenter(getWidth()/2);
        camera.setScreenYcenter(getHeight()/2);

        canvas.save();
        Paint tpaint=new Paint();
        //tpaint.setAntiAlias(false);
        //tpaint.setFilterBitmap(false);
        //tpaint.setDither(false);
        canvas.drawColor(Color.rgb(200,200,200));


        canvas.translate(-camera.getxOffset(),-camera.getyOffset());
        //canvas.scale(camera.getScale(),camera.getScale());
        canvas.scale(camera.getScale(),camera.getScale(),player.getCenterX(),player.getCenterY());




        // tempCanvas.setBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.box_tile));





        canvas.drawBitmap(background,0,0,tpaint);
        player.render(canvas);
        player.drawDebugBox(canvas);
        testBox.render(canvas);
        testBox.drawDebugBox(canvas);



        canvas.restore();
        textPointOfPlayer.render(canvas);


    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        float x=event.getX(),y=event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                player.RotationToPoint(new PointF(x/camera.getScale()+camera.getxOffset()+camera.getScreenXcenter()/camera.getScale(),y/camera.getScale()+camera.getyOffset()+camera.getScreenYcenter()/camera.getScale()));
                testBox.setX(x/camera.getScale()+camera.getxOffset()+camera.getScreenXcenter()/camera.getScale());
                testBox.setY(y/camera.getScale()+camera.getyOffset()+camera.getScreenYcenter()/camera.getScale());
                break;

                case MotionEvent.ACTION_MOVE:
                    player.RotationToPoint(new PointF(x/camera.getScale()+camera.getxOffset()+camera.getScreenXcenter()/camera.getScale(),y/camera.getScale()+camera.getyOffset()+camera.getScreenYcenter()/camera.getScale()));
                    testBox.setX(x/camera.getScale()+camera.getxOffset()/camera.getScale()+camera.getScreenXcenter()/camera.getScale());
                    testBox.setY(y/camera.getScale()+camera.getyOffset()/camera.getScale()+camera.getScreenYcenter()/camera.getScale());
                   break;
            default:
                break;
        }
                return true;
    }
}