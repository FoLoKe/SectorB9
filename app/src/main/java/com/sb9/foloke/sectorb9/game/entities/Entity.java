package com.sb9.foloke.sectorb9.game.entities;

import android.graphics.Bitmap;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;

public abstract class Entity {
    protected float x,y;
    protected RectF collisionBox;
    protected int Height,Width;
    protected Bitmap image;


    public Entity(float x,float y,Bitmap image)
    {
        this.x=x;
        this.y=y;
        this.collisionBox=new RectF(x,y,x+image.getWidth(),y+image.getHeight());
        this.image=image;

    }
    abstract public void render(Canvas canvas);
    abstract public void tick();

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void drawDebugBox(Canvas canvas)
    {
        Paint temppaint=new Paint();
        temppaint.setColor(Color.rgb(0,255,0));
        temppaint.setStyle(Paint.Style.STROKE);
        canvas.drawRect(collisionBox,temppaint);
    }

    public PointF getWorldLocation()
    {
        return new PointF(x,y);
    }
    public void setWorldLocation(PointF location)
    {
        x=location.x;
        y=location.y;
    }
}
