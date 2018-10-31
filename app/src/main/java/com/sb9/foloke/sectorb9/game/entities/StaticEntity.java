package com.sb9.foloke.sectorb9.game.entities;

import android.graphics.Bitmap;
import android.graphics.PointF;

import com.sb9.foloke.sectorb9.game.UI.Text;
import com.sb9.foloke.sectorb9.game.display.*;

public abstract class StaticEntity extends Entity {
	
	Text textName;
	private int debugCounter=0;
	String name;
    public StaticEntity(float x, float y,float rotation, Bitmap image,String name,Game game)
    {
        super(x,y,rotation,image,name,game);
        //this.name=name;
		textName=new Text("0",x,y-32);
    }
	public String getName()
	{
		return name;
	}
    

}
