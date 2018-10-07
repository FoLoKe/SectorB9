package com.sb9.foloke.sectorb9.game.entities;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;

public class Player extends DynamicEntity {
    Bitmap engine;
    public Player(float x, float y, ImageAssets asset)
    {
        super(x,y,asset.player_mk1);
        engine=asset.engine_mk1;
    }

    @Override
    public void tick() {

    }

    @Override
    public void render(Canvas canvas) {

        canvas.save();

        canvas.rotate(rotation,x,y);
        canvas.drawBitmap(engine,x,y,new Paint());
        canvas.drawBitmap(image,x,y,new Paint());

        canvas.restore();

    }

    @Override
    public void RotationToPoint(PointF targetPoint) {
       rotation= (float)Math.atan((targetPoint.y-y)/(targetPoint.x-x));
    }

    public float getCenterX()
    {
        return x+image.getWidth()/2;
    }
    public float getCenterY()
    {
        return y+image.getHeight()/2;
    }
}
