package com.sb9.foloke.sectorb9.game.entities;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;

import static java.lang.Math.PI;
import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class Player extends DynamicEntity {
    Bitmap engine;
    float speed=3;
    boolean movable;
    private Text textdXdY;
    public Player(float x, float y, ImageAssets asset)
    {
        super(x,y,asset.player_mk1);
        this.engine=asset.engine_mk1;
        this.dx=this.dy=0;
        this.movable=false;
        textdXdY=new Text("",x-100,y-50);
    }

    @Override
    public void tick() {
        if(movable) {


            x += dx;
           y += dy;
           dx = dy = 0;

           this.collisionBox.set(x,y,x+image.getWidth(),y+image.getHeight());
        }
    }

    @Override
    public void render(Canvas canvas) {

        canvas.save();

        canvas.rotate(rotation,getCenterX(),getCenterY());
        if(movable)
        canvas.drawBitmap(engine,x,y,new Paint());

        canvas.drawBitmap(image,x,y,new Paint());

        canvas.restore();

        textdXdY.setString(dx+" "+dy);
        textdXdY.setWorldLocation(new PointF(x,y));
        //textdXdY.render(canvas);

    }

    @Override
    public void RotationToPoint(PointF targetPoint) {
      // rotation=(float)-Math.toDegrees(Math.PI+Math.atan2(targetPoint.x-x,targetPoint.y-y));   /coord rotation
    }

    public float getCenterX()
    {
        return x+image.getWidth()/2;
    }
    public float getCenterY()
    {
        return y+image.getHeight()/2;
    }

    public PointF getWorldLocation()
    {
        return new PointF(x,y);
    }


    public void addMovement(PointF screenPoint,float screenW, float screenH) {
        float hundredPercent=dx+dy;
        if (movable) {
            //dx = dx / hundredPercent * speed;
            //dy = dy / hundredPercent * speed;
            float mathRotation=(float)(PI/180*rotation);
            rotation=360-(float)Math.toDegrees(Math.PI+Math.atan2(-screenW/2+screenPoint.x,-screenH/2+screenPoint.y)); //screen relative rotation
            //this.dx=(float)(screenH/2*cos(-rotation)-(-screenW/2+screenPoint.x*sin(-rotation)));
            this.dy = -(float) (speed * cos(mathRotation));
            this.dx = (float) (speed * sin(mathRotation));


        }
    }

    public void setMovable(boolean movable) {
        this.movable = movable;
    }
}
