package com.sb9.foloke.sectorb9.game.entities;

import android.graphics.Bitmap;
import android.graphics.PointF;

public abstract class DynamicEntity extends Entity {

    float rotation,dx,dy;
    public DynamicEntity(float x, float y, Bitmap image)
    {
        super(x,y,image);
        rotation=dx=dy=0;
    }
    abstract public void RotationToPoint(PointF targetPoint);

}
