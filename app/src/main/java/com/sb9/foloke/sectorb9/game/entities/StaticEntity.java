package com.sb9.foloke.sectorb9.game.entities;

import android.graphics.Bitmap;
import android.graphics.PointF;
import android.graphics.Canvas;
import android.graphics.*;
import java.util.*;

public abstract class StaticEntity extends Entity {
	
	Text textName;
	private int debugCounter=0;
	String name;
    public StaticEntity(float x, float y, Bitmap image,String name)
    {
        super(x,y,image);
        this.name=name;
		textName=new Text("0",x,y-32);
    }
    abstract public void RotationToPoint(PointF targetPoint);

}
