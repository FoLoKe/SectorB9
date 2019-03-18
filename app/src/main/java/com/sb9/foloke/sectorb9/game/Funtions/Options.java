package com.sb9.foloke.sectorb9.game.Funtions;
import java.util.*;
import java.io.*;
import com.sb9.foloke.sectorb9.*;
import com.sb9.foloke.sectorb9.game.UI.CustomViews.*;

public enum Options
{
	drawDebugInfo("Draw debug info",typeOfElement.SWITCH,0,1,0),
	drawParticles("Draw particles",typeOfElement.SWITCH,0,1,0),
	debug2("debug",typeOfElement.SWITCH,0,1,0),
	debug3("debug",typeOfElement.SWITCH,0,1,0),
	debug4("debug",typeOfElement.SWITCH,0,1,0);
	
	public final enum typeOfElement{SWITCH,BAR,TEXT}
	private final String name;
	private final typeOfElement type;
	private int	value; // 1/0  for boolean
	private final int max;
	private final int min;
	private static MainActivity MA;
		
	private Options(String in_name,typeOfElement in_type,int in_value,int in_max, int in_min)
	{
		name=in_name;
		type=in_type;
		value=in_value;
		max=in_max;
		min=in_min;
		
	}
	public static void inti(MainActivity tMA)
	{
		MA=tMA;
	}
	public void setValue(int in_value)
	{
		if(in_value>=min&&in_value<=max)
			value=in_value;
	}
	
	public int getValue()
	{
		return value;
	}
	
	public typeOfElement getType()
	{
		return type;
	}
	
	public String getName()
	{
		return name;
	}
	
	public static void save(BufferedWriter writer)
	{
		try
		{
			for(Options p:values())
			{
				writer.write(p.getName()+"="+p.getValue());
				writer.newLine();
			}
		}
		catch(Exception e)
		{
			MA.makeToast("write error",1);
		}
	}
	
	public boolean getBoolean()
	{
		return value!=0;
	}
	
	public static void load(BufferedReader reader)
	{
		try
		{
			GameLog.update("loading options"+values(),0);
			for(Options p:values())
			{
				GameLog.update("loading option: "+p,0);
				String s=reader.readLine();
				String elemWords[]=s.split("=");
				p.setValue(Integer.parseInt(elemWords[1]));
				GameLog.update("loaded: "+s,0);
			}
			GameLog.update("options loaded",0);
		}
		catch(Exception e)
		{
			MA.makeToast("options read error: "+e,1);
		}
	}

}
