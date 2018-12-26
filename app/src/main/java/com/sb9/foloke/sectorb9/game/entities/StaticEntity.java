package com.sb9.foloke.sectorb9.game.entities;

import android.graphics.Bitmap;
import android.graphics.PointF;

import com.sb9.foloke.sectorb9.game.UI.Text;
import com.sb9.foloke.sectorb9.game.display.*;

public abstract class StaticEntity extends Entity {
	
	private Text textName;
	
	private String name;
	protected boolean energy=false;
	protected boolean enabled=true;
	protected boolean isInteractable=true;
	protected StaticEntity powerSupplier=null;
	
    public StaticEntity(float x, float y,float rotation, Bitmap image,String name,Game game,int ID)
    {
        super(x,y,rotation,image,name,game,ID);
        //this.name=name;
		textName=new Text("0",x,y-32);
    }
	public String getName()
	{
		return name;
	}
    public boolean getEnergy()
	{
		return energy;
	}
	public void setEnergy(boolean energy)
	{
		this.energy=energy;
	}
	public void onAndOff()
	{
		if(enabled)
			enabled=false;
			else
				enabled=true;
		}
		public boolean getEnabled()
		{
			return enabled;
		}
		public boolean getInteractable()
		{
			return isInteractable;
		}
		public void setPowerSupplier(StaticEntity entity)
		{
			powerSupplier=entity;
		}

		@Override
		public void tick()
		{
			// TODO: Implement this method
			calculateCollisionObject();
		}

		
}
