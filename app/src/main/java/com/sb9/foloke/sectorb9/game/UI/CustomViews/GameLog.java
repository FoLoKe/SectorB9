package com.sb9.foloke.sectorb9.game.UI.CustomViews;
import android.widget.*;
import android.content.*;
import android.graphics.*;
import java.sql.*;
import java.util.*;
import android.util.*;
import android.text.*;
import com.sb9.foloke.sectorb9.*;
import android.os.*;
import java.io.*;
import com.sb9.foloke.sectorb9.game.Funtions.*;
import android.view.*;

public class GameLog extends LinearLayout
{
	
	/*
	TODO:
	-TEXTVIEW
	-INPUTLINE VITH ENTER BUTTON
	
	*/
	
	private static ArrayList<String> text=new ArrayList<String>();
	private static int maxCount=20;
	private static boolean folderlog=true;
	private static TextView TV;
	private static MainActivity MA;
	
	public GameLog(Context context)
	{
		super(context);
		init(context);
	}
	
	public void init(Context in_context)
	{
		MA=(MainActivity)in_context;
		TV=new TextView(in_context);
		 LinearLayout.LayoutParams LP=new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.FILL_PARENT);
		 TV.setLayoutParams(LP);
		 TV.setTextColor(Color.GREEN);
		 TV.setTextSize(10);
		 TV.setText("LOG");
		 this.addView(TV);
		 setZ(10);
	}
	
	public GameLog(Context context, AttributeSet attrs)
	{
        super(context, attrs);
        init(context);
    }
	
	@Override
	protected void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);		
	}
	
	public static void update(final String s,final int state)
	{
		
			
		MA.runOnUiThread(new Runnable()
		{
			
			public void run()
			{
				if(Options.drawDebugInfo.getBoolean())
					TV.setVisibility(VISIBLE);
				else
					TV.setVisibility(GONE);
				if(folderlog)
				newLine(state+" "+s);
				String ts="";
				switch(state)
				{
					case 0:
						ts="<font color=\"green\">"+s+"</font><br>";
						break;
					case 1:
						ts=("<font color=\"red\">"+s+"</font><br>");
						break;
					case 2:
						ts="<font color=\"yellow\">"+s+"</font><br>";
						break;
				}
				StringBuffer sb=new StringBuffer();
				text.add(ts);
				if(text.size()>maxCount)
					text.remove(0);
				for(String t:text)
				{
					sb.append(t);
				}
				Spanned finalString=Html.fromHtml(sb.toString());
				TV.setText(finalString);
			}
		});
		}
		
	private static void newLine(String s)
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
				folderlog=false;
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
				folderlog=false;
				GameLog.update("no inside DOCUMENTS WRITE PERMISSION",1);
				return;
			}

			File logFile = new File (gameFolder, "log.txt");
			if(!logFile.exists())
			{
				logFile.createNewFile();
			}
			if (logFile.exists ())
			{
				FileOutputStream out = new FileOutputStream(logFile,true);
				OutputStreamWriter osw = new OutputStreamWriter(out);
				BufferedWriter writer = new BufferedWriter(osw);


				
				writer.newLine();
				writer.write(s);
				writer.close();
				osw.close();
				out.close();
				
				


			}
		}
		catch(Exception e)
		{
			GameLog.update(e.toString(),1);
		}
		}
	}
