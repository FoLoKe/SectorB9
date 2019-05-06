package com.sb9.foloke.sectorb9.game.UI.TechUIs;
import android.widget.*;
import android.content.*;
import java.util.*;

public class TechUIObject extends Button
{
	public int techID;
	public int hLevel;
	public ArrayList<TechUILink> links=new ArrayList<>();
	
	public TechUIObject(Context context,int techID,int hLevel)
	{
		super(context);
		this.techID=techID;
		this.hLevel=hLevel;
	}
	
	public void updateLinks()
	{
		for(TechUILink tul:links)
		{
			tul.updatePos();
		}
	}
}
