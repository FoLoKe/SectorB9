package com.sb9.foloke.sectorb9.game.display;

import android.graphics.PointF;

import com.sb9.foloke.sectorb9.game.entities.Entity;

import java.lang.annotation.Target;

public class Camera {
    private PointF screenOffset;
    private Entity pointOfLook;
    private float screenXcenter,screenYcenter;
    private float scale;
    public Camera(float x,float y,float scale,Entity target)
    {
        this.pointOfLook=target;
        this.screenOffset=new PointF(x,y);
        this.scale=scale;
    }

    public void tick()
    {
       if(pointOfLook!=null)
       {
           screenOffset.x=(pointOfLook.getX()-screenXcenter);
           if (screenOffset.x<0)
               screenOffset.x+=0;

           screenOffset.y=(pointOfLook.getY()-screenYcenter);
           if (screenOffset.y<0)
               screenOffset.y+=0;
       }
    }

    public float getxOffset()
    {
        return screenOffset.x;
    }

    public float getyOffset()
    {
        return screenOffset.y;
    }

    public void setPointOfLook(Entity pointOfLook) {
        this.pointOfLook = pointOfLook;
    }

    public void setScreenXcenter(float screenXcenter) {
        this.screenXcenter = screenXcenter;
    }

    public void setScreenYcenter(float screenYcenter)
    {
        this.screenYcenter=screenYcenter;
    }

    public float getScale()
    {
        return scale;
    }

    public float getScreenXcenter() {
        return screenXcenter;
    }

    public float getScreenYcenter() {
        return screenYcenter;
    }
}
