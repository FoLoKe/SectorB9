package com.sb9.foloke.sectorb9.game.display;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.view.SurfaceView;
import android.view.SurfaceHolder;
import android.content.Context;
import android.graphics.Canvas;


import android.util.*;

import com.sb9.foloke.sectorb9.MainThread;
import com.sb9.foloke.sectorb9.R;

public class Game extends SurfaceView implements SurfaceHolder.Callback
{
    //thread
    private MainThread mainThread;

    //background
    private Bitmap background;

    //camera
    private Camera camera;

    public Game(Context context, AttributeSet attributeSet)
    {
        super(context, attributeSet);

        background=Bitmap.createBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.galactic_outflow));


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
        camera.addScreenOffset(new PointF(1,1));
    }
    public void render(Canvas canvas)
    {
        super.draw(canvas);
        canvas.save();
        canvas.translate(camera.getxOffset(),camera.getyOffset());
        canvas.drawColor(Color.rgb(200,200,200));
        canvas.drawBitmap(background,0,0,new Paint());
        canvas.restore();

    }


}