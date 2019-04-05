package com.sb9.foloke.sectorb9;

import android.graphics.Canvas;
import android.view.SurfaceHolder;
import com.sb9.foloke.sectorb9.game.Display.GamePanel;
import com.sb9.foloke.sectorb9.game.UI.CustomViews.*;
import com.sb9.foloke.sectorb9.game.Managers.*;

public class MainThread extends Thread
{
    private final int FPS =60;
    private double averageFPS;
    private SurfaceHolder surfaceHolder;
    private GameManager gameManager;
    private boolean running;
    private static Canvas canvas;


    public MainThread(GameManager gameManager)
    {
        super();
        this.surfaceHolder=gameManager.getGamePanel().getHolder();
        this.gameManager=gameManager;
    }

    @Override
    public void run()
    {
        long startTime;
        long timeMillis;
        long waitTime;
        long totalTime =0;
        int frameCount =0;


        while(running)
        {
            long targetTime =1000/FPS;
            startTime=System.nanoTime();
            canvas = null;

            try
            {
                canvas=this.surfaceHolder.lockCanvas();
                synchronized(surfaceHolder)
                {
                    this.gameManager.tick();
                    this.gameManager.render(canvas);
                }
            }
            catch(Exception e){
                
				GameLog.update("Main Thread: "+e,1);
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
				gameManager.getGamePanel().textFPS.setString("FPS: "+averageFPS);
                System.out.println(averageFPS);
            }
        }
    }
    public void setRunning(boolean b)
    {
        running=b;
    }

}
