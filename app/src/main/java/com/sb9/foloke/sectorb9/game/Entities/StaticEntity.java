package com.sb9.foloke.sectorb9.game.Entities;

import android.graphics.Bitmap;

import com.sb9.foloke.sectorb9.game.Managers.GameManager;
import com.sb9.foloke.sectorb9.game.UI.Text;
import android.graphics.*;
import com.sb9.foloke.sectorb9.game.Funtions.*;

public abstract class StaticEntity extends Entity 
{
	private String name;
	protected StaticEntity powerSupplier=null;
	
    public StaticEntity(float x, float y, float rotation, GameManager gameManager, int ID)
    {
        super(x,y,rotation, gameManager,ID);	
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
        if((SP<maxSP||HP<maxHP)&&drawHp)
        {
            uIsh.render(canvas);
			uIhp.render(canvas);
		}
		if(Options.drawDebugInfo.getBoolean())
			drawDebugCollision(canvas);
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
