package com.sb9.foloke.sectorb9.game.Funtions;
import java.io.*;
import com.sb9.foloke.sectorb9.*;
import com.sb9.foloke.sectorb9.game.UI.CustomViews.*;
import android.os.*;

public enum Options
{
	drawDebugInfo("Draw debug info",typeOfElement.SWITCH,0,1,0),
	drawParticles("Draw particles",typeOfElement.SWITCH,0,1,0),
	debug2("debug",typeOfElement.SWITCH,0,1,0),
	debug3("debug",typeOfElement.SWITCH,0,1,0),
	debug4("debug",typeOfElement.SWITCH,0,1,0);
	
	public  enum typeOfElement{SWITCH,BAR,TEXT}
	private final String name;
	private final typeOfElement type;
	private int	value; // 1/0  for boolean
	private final int max;
	private final int min;
	//private MainActivity MA;
		
	Options(String in_name,typeOfElement in_type,int in_value,int in_max, int in_min)
	{
		name=in_name;
		type=in_type;
		value=in_value;
		max=in_max;
		min=in_min;
		
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
			GameLog.update(e.toString(),1);
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
            GameLog.update(e.toString(),1);
		}
	}
	
	public static void startupOptions()
	{
		try
		{
			String documentsFolderPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).toString();
			File documentsFolder  = new File(documentsFolderPath);

			if(!documentsFolder.exists())
			{
				documentsFolder.mkdir();
			}

			if(!documentsFolder.exists())
			{
				documentsFolder.mkdir();
				GameLog.update("no DOCUMENTS WRITE/READ PERMISSION",1);
				return;
			}

			String gameFolderPath=documentsFolderPath+File.separator+"sb9";
			File gameFolder=new File(gameFolderPath);

			if(!gameFolder.exists())
			{
				gameFolder.mkdir();
			}
			if(!gameFolder.exists())
			{
				gameFolder.mkdir();
				GameLog.update("no inside DOCUMENTS WRITE/READ PERMISSION",1);
				return;
			}

            File optionsFile = new File (gameFolder, "options.txt");

            if (optionsFile.exists ())
            {
                loadOptions();
			}
			else
			{
				//saveAsNew
				saveOptions();
				GameLog.update("options created",0);
			}

		}
		catch(Exception e)
		{
			GameLog.update(e.toString(),1);
		}
	}

	public static void loadOptions()
	{	
		try
		{
			String documentsFolderPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).toString();
			File documentsFolder  = new File(documentsFolderPath);
			if(!documentsFolder.exists())
			{
				documentsFolder.mkdir();
			}
			if(!documentsFolder.exists())
			{
				documentsFolder.mkdir();
				GameLog.update("no DOCUMENTS READ PERMISSION",1);
			}
			String gameFolderPath=documentsFolderPath+File.separator+"sb9";
			File gameFolder=new File(gameFolderPath);
			if(!gameFolder.exists())
			{
				gameFolder.mkdir();
			}
			if(!gameFolder.exists())
			{
				gameFolder.mkdir();
				GameLog.update("no inside DOCUMENTS READ PERMISSION",1);
			}

			File optionsFile = new File (gameFolder, "options.txt");
			if (optionsFile.exists ())
			{
				FileInputStream inputStream = new FileInputStream(optionsFile);
				if (inputStream != null)
				{
					InputStreamReader isr = new InputStreamReader(inputStream);
					BufferedReader reader = new BufferedReader(isr);
					////todo load
					reader.readLine();

					load(reader);
					inputStream.close();
					reader.close();
					isr.close();
					///options setup
					//setOptions();
					GameLog.update("Successfully loaded options",0);
				}
				else
				{
					GameLog.update("inputStream error",1);
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
			String documentsFolderPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).toString();
			File documentsFolder  = new File(documentsFolderPath);

			if(!documentsFolder.exists())
			{
				documentsFolder.mkdir();
			}
			if(!documentsFolder.exists())
			{
				documentsFolder.mkdir();
				GameLog.update("no DOCUMENTS WRITE PERMISSION",1);
				return;
			}

			String gameFolderPath=documentsFolderPath+File.separator+"sb9";
			File gameFolder=new File(gameFolderPath);

			if(!gameFolder.exists())
			{
				gameFolder.mkdir();
			}
			if(!gameFolder.exists())
			{
				gameFolder.mkdir();
				GameLog.update("no inside DOCUMENTS WRITE PERMISSION",1);
				return;
			}

			File optionsFile = new File (gameFolder, "options.txt");
			if (optionsFile.exists ())
			{
				FileOutputStream out = new FileOutputStream(optionsFile);
				OutputStreamWriter osw = new OutputStreamWriter(out);
				BufferedWriter writer = new BufferedWriter(osw);


				writer.write("game options SB9");
				writer.newLine();
				save(writer);
				writer.close();
				osw.close();
				out.close();
				//setOptions();
				GameLog.update("Successfully saved options",0);
			}
		}
		catch(Exception e)
		{
			GameLog.update(e.toString(),1);
		}
	}

}
