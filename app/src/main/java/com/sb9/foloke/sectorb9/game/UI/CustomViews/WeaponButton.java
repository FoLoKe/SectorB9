package com.sb9.foloke.sectorb9.game.UI.CustomViews;
import android.widget.*;
import android.content.*;
import com.sb9.foloke.sectorb9.game.Entities.Weapons.*;

public class WeaponButton extends Button
{
	Weapon w;
	public WeaponButton(Context context)
	{
		super(context);
	}

	public void setWeapon(Weapon w)
	{
		this.w = w;
	}

	public Weapon getWeapon()
	{
		return w;
	}
	
}
