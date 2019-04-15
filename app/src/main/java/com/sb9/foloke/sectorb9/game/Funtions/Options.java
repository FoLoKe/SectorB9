package com.sb9.foloke.sectorb9.game.Funtions;
import java.io.*;
import com.sb9.foloke.sectorb9.game.UI.CustomViews.*;
import android.os.*;
import java.util.*;

public enum Options
{
	drawDebugInfo("Draw debug info",typeOfElement.SWITCH,0,1,0,null),
	drawParticles("Draw particles",typeOfElement.SWITCH,0,1,0,null),
	drawRadio("Radio options",typeOfElement.LIST,1,2,0,new ArrayList<String>(){{add("no");add("all");}}),
	debug3("debug",typeOfElement.SWITCH,0,1,0,null),
	debug4("debug",typeOfElement.SWITCH,0,1,0,null);
	
	public  enum typeOfElement{SWITCH,BAR,TEXT,LIST}
	private final String name;
	private final typeOfElement type;
	private int	value; // 1/0  for boolean
	private final int max;
	private final int min;
	private ArrayList<String> names=new ArrayList<>();
	//private MainActivity MA;
		
	Options(String in_name,typeOfElement in_type,int in_value,int in_max, int in_min,ArrayList<String> names)
	{
		this.name=in_name;
		this.type=in_type;
		this.value=in_value;
		this.max=in_max;
		this.min=in_min;
		this.names=names;
	}
	
	public ArrayList<String> getStrings()
	{
		return names;
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
	
	public int getMax()
	{
		return max;
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
			GameLog.update("Options: "+e.toString(),1);
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

			for(Options p:values())
			{
				GameLog.update("Options: "+"loading option: "+p,0);
				String s=reader.readLine();
				String elemWords[]=s.split("=");
				p.setValue(Integer.parseInt(elemWords[1]));
				GameLog.update("Options: "+"loaded: "+s,0);
			}
			GameLog.update("Options: "+"options loaded",0);
		}
		catch(Exception e)
		{
            GameLog.update("Options: "+ e.toString(),1);
		}
	}
	
	public static void startupOptions()
	{
	    GameLog.update("Options: "+"startup options",0);
		try
		{
		    File gameFolder=checkFolders();
			if(gameFolder==null)
                return;

            File optionsFile = new File (gameFolder, "options.txt");

            if (optionsFile.exists ())
            {
                GameLog.update("Options: "+"loading existing options",0);
                loadOptions();
			}
			else
			{
				//saveAsNew
				saveOptions();
				GameLog.update("Options: "+"options created",0);
			}

		}
		catch(Exception e)
		{
			GameLog.update("Options: "+ e.toString(),1);
		}
	}

	public static void loadOptions()
	{	
		try
		{
            File gameFolder=checkFolders();
            if(gameFolder==null)
                return;

			File optionsFile = new File (gameFolder, "options.txt");
			if (optionsFile.exists ())
			{
				FileInputStream inputStream = new FileInputStream(optionsFile);
				if (inputStream != null)
				{
					InputStreamReader isr = new InputStreamReader(inputStream);
					BufferedReader reader = new BufferedReader(isr);

					reader.readLine();

					load(reader);
					inputStream.close();
					reader.close();
					isr.close();

					GameLog.update("Options: "+"successfully loaded options",0);
				}
				else
				{
					GameLog.update("Options: "+"inputStream error",1);
				}
			}
		}
		catch(Exception e)
		{ 
			GameLog.update(e.toString(),1);
		}
	}

	public static void saveOptions()
	{
		try
		{
            File gameFolder=checkFolders();
            if(gameFolder==null) {
                GameLog.update("Options: "+"saving options interrupted",1);
                return;
            }
			File optionsFile = new File (gameFolder, "options.txt");

				FileOutputStream out = new FileOutputStream(optionsFile);
				OutputStreamWriter osw = new OutputStreamWriter(out);
				BufferedWriter writer = new BufferedWriter(osw);


				writer.write("Options: "+"game options SB9");
				writer.newLine();
				save(writer);
				writer.close();
				osw.close();
				out.close();
				//setOptions();
				GameLog.update("Options: "+"successfully saved options",0);

		}
		catch(Exception e)
		{
			GameLog.update("Options: "+e.toString(),1);
		}
	}

	private static File checkFolders()
    {
        String documentsFolderPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).toString();
        File documentsFolder  = new File(documentsFolderPath);

        if(!documentsFolder.exists())
        {
            GameLog.update("Options: "+"creating docs folder",0);
            if(!documentsFolder.mkdir()) {
                GameLog.update("Options: "+"no DOCUMENTS WRITE/READ PERMISSION for documents directory", 1);
                return null;
            }
            GameLog.update("Options: "+"docs folder created",0);
        }

        String gameFolderPath=documentsFolderPath+File.separator+"sb9";
        File gameFolder=new File(gameFolderPath);

        if(!gameFolder.exists())
        {
            GameLog.update("Options: "+"creating game folder",0);
            if(!gameFolder.mkdir())
            {
                GameLog.update("Options: "+"no inside DOCUMENTS WRITE/READ PERMISSION for game directory", 1);
                return null;
            }
            GameLog.update("Options: "+"game folder created",0);
        }

        return gameFolder;
    }
}
