package com.sb9.foloke.sectorb9;

import android.graphics.Canvas;
import android.view.SurfaceHolder;
import com.sb9.foloke.sectorb9.game.Display.GamePanel;

public class MainThread extends Thread
{
    private int FPS =30;
    private double averageFPS;
    private SurfaceHolder surfaceHolder;
    private GamePanel mapPanel;
    private boolean running;
    private static Canvas canvas;


    public MainThread(SurfaceHolder surfaceHolder, GamePanel mapPanel)
    {
        super();
        this.surfaceHolder=surfaceHolder;
        this.mapPanel=mapPanel;
    }

    @Override
    public void run()
    {
        long startTime;
        long timeMillis;
        long waitTime;
        long totalTime =0;
        int frameCount =0;
        long targetTime =1000/FPS;

        while(running)
        {
            startTime=System.nanoTime();
            canvas = null;

            try
            {
                canvas=this.surfaceHolder.lockCanvas();
                synchronized(surfaceHolder)
                {
                    this.mapPanel.tick();
                    this.mapPanel.render(canvas);
                }
            }
            catch(Exception e){
                System.out.print(e);
            }

            finally{if(canvas!=null)
                try{
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }catch(Exception e){e.printStackTrace();}

            }
            timeMillis=(System.nanoTime()-startTime)/1000000;
            waitTime=targetTime-timeMillis;

            try{
                this.sleep(waitTime);
            }catch(Exception e){}

            totalTime += System.nanoTime()-startTime;
            frameCount++;
            if(frameCount==FPS)
            {
                averageFPS= 1000/((totalTime/frameCount)/1000000);
                frameCount=0;
                totalTime=0;
				mapPanel.textFPS.setString("FPS: "+averageFPS);
                System.out.println(averageFPS);
            }
        }
    }
    public void setRunning(boolean b)
    {
        running=b;
    }
}
