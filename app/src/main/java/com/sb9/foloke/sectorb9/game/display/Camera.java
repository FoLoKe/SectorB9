package com.sb9.foloke.sectorb9.game.display;

import android.graphics.PointF;

public class Camera {
    private PointF screenOffset;
    Camera(float x,float y)
    {
        this.screenOffset=new PointF(x,y);
    }

    public void tick()
    {

    }

    public void addScreenOffset(PointF screenOffset)
    {
        this.screenOffset.x+=screenOffset.x;
        this.screenOffset.y+=screenOffset.y;
    }

    public float getxOffset()
    {
        return screenOffset.x;
    }

    public float getyOffset()
    {
        return screenOffset.y;
    }

}
