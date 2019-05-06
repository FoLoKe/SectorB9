package com.sb9.foloke.sectorb9.game.Funtions;
import java.util.*;
import com.sb9.foloke.sectorb9.game.UI.TechUIs.*;

public class TechTree
{
	public static class Tech
	{
		private int techID;
		private	ArrayList<Integer> leadToTechID;
		private ArrayList<Integer> fromTechID;
		private ArrayList<Integer> opensObjects;
		private ArrayList<Integer> opensItems;
		private ArrayList<Integer> opensModules;
		private boolean opened=false;
		private boolean available=false;
		public Tech(int ID,int[] to,int[] from,int[] objects,int[] items,int[] mods,boolean opened,boolean available)
		{
			this.techID=ID;
			this.opened=opened;
			this.available=available;
			this.leadToTechID=new ArrayList<>();
			this.fromTechID=new ArrayList<>();
			this.opensObjects=new ArrayList<>();
			this.opensItems=new ArrayList<>();
			this.opensModules=new ArrayList<>();
			
			for(int i:to)
				leadToTechID.add(i);
			for(int i:from)
				fromTechID.add(i);
			
			for(int i:objects)
				opensObjects.add(i);
			for(int i:mods)
				opensModules.add(i);
			for(int i:items)
				opensItems.add(i);
		}

		public void setAvailable(boolean available)
		{
			this.available = available;
		}

		public boolean isAvailable()
		{
			return available;
		}

		public void setOpened(boolean opened)
		{
			this.opened = opened;
		}

		public boolean isOpened()
		{
			return opened;
		}
		public int getID()
		{
			return techID;
		}
		public ArrayList<Integer> getNext()
		{
			return leadToTechID;
		}
	}
	
	private static ArrayList<Tech> techs=new ArrayList<>();
	
	public static void init()
	{	
		/////TECHS LIST		ID		NEXT				PREVIOUS			New OBJECTS		new ITEMS		new MODS		opened	available
		techs.add(new Tech(	0		,new int[]{1,2}		,new int[]{}		,new int[]{}	,new int[]{}	,new int[]{}	,false	,true));
		techs.add(new Tech(	1		,new int[]{3}		,new int[]{}		,new int[]{}	,new int[]{}	,new int[]{}	,false	,false));
		techs.add(new Tech(	2		,new int[]{3}		,new int[]{}		,new int[]{}	,new int[]{}	,new int[]{}	,false	,false));
		techs.add(new Tech(	3		,new int[]{4}		,new int[]{}		,new int[]{}	,new int[]{}	,new int[]{}	,false	,false));
		techs.add(new Tech(	4		,new int[]{}		,new int[]{}		,new int[]{}	,new int[]{}	,new int[]{}	,false	,false));
		techs.add(new Tech(	5		,new int[]{1,2}		,new int[]{}		,new int[]{}	,new int[]{}	,new int[]{}	,false	,true));
		techs.add(new Tech(	6		,new int[]{4}		,new int[]{}		,new int[]{}	,new int[]{}	,new int[]{}	,false	,true));
		techs.add(new Tech(	7		,new int[]{1}		,new int[]{}		,new int[]{}	,new int[]{}	,new int[]{}	,false	,true));
	}
	public static ArrayList<Tech> getTechs()
	{
		return techs;
	}
	
	public static Tech getTech(int id)
	{
		for(Tech tech:techs)
		{
			if(tech.getID()==id)
				return tech;
		}
		return null;
	}
	
	public static void onActionWithTech(int techID)
	{		
		Tech t=getTech(techID);
		
		if(t.isAvailable())
		{
			t.setOpened(true);
			for(int next:t.getNext())
			{
				getTech(next).setAvailable(true);
			}
		}
		
	}
}
