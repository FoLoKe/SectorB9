package com.sb9.foloke.sectorb9.game.display;

import android.graphics.PointF;

import com.sb9.foloke.sectorb9.game.entities.DynamicEntity;
import com.sb9.foloke.sectorb9.game.entities.Entity;
import com.sb9.foloke.sectorb9.game.entities.Player;

import java.lang.annotation.Target;

public class Camera {
    private PointF location;
    private Entity pointOfLook;
    private float screenXcenter,screenYcenter;
    private float scale;
    public Camera(float x,float y,float scale,Entity target)
    {
        this.pointOfLook=target;
        this.location=new PointF(x,y);
        this.scale=scale;
    }

    public void tick(float scale)
    {
        this.scale=scale;
        this.location.x= ((Player) pointOfLook).getCenterX();
        this.location.y= ((Player) pointOfLook).getCenterY();
    }

    public float getxOffset()
    {
        return location.x;
    }

    public float getyOffset()
    {
        return location.y;
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

    public PointF getWorldLocation()
    {
        return location;
    }
}
