package com.sb9.foloke.sectorb9.game.Entities;

import android.graphics.Bitmap;

import com.sb9.foloke.sectorb9.game.Managers.GameManager;
import com.sb9.foloke.sectorb9.game.UI.Text;
import android.graphics.*;

public abstract class StaticEntity extends Entity {
	
	
	private String name;
	protected boolean energy=false;
	protected boolean enabled=true;
	protected boolean isInteractable=true;
	protected StaticEntity powerSupplier=null;
	
    public StaticEntity(float x, float y, float rotation, Bitmap image, String name, GameManager gameManager, int ID)
    {
        super(x,y,rotation,image,name, gameManager,ID);	
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
	

	@Override
	public void render(Canvas canvas)
	{
		if(HP<maxHp)
		{
			uIhp.render(canvas);
		}
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
        ///STATIC NO COLLISION DIFFERENCE
        super.tick();
    }

		
}
