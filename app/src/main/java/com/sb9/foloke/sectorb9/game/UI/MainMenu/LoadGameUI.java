package com.sb9.foloke.sectorb9.game.UI.MainMenu;
import com.sb9.foloke.sectorb9.*;
import android.widget.*;
import android.os.*;
import java.io.*;
import android.view.*;
import android.graphics.*;
import android.widget.AbsListView.*;
import java.text.*;
import android.graphics.drawable.*;
import com.sb9.foloke.sectorb9.game.UI.CustomViews.*;
import android.app.*;
import android.content.*;

public class LoadGameUI
{
	private static String saveName;
	public static void init(final MainActivity MA)
	{
		saveName="";
		TableLayout TL=MA.findViewById(R.id.load_game_saves_table);
		TL.setBackgroundColor(Color.DKGRAY);
		TL.removeAllViews();
		String root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).toString()+File.separator+"sb9";
		File myDir = new File(root);
		if (!myDir.exists()) {
			myDir.mkdir();
		}

		MA.findViewById(R.id.back_from_load).setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				MA.prepareMenu();
			}
		});
		
		MA.findViewById(R.id.load_menu_load_game).setOnClickListener(new View.OnClickListener()
			{
				public void onClick(View v)
				{
					MA.prepareNewLoad(saveName);
				}
			});
			
		MA.findViewById(R.id.load_menu_delete_game).setOnClickListener(new View.OnClickListener()
			{
				public void onClick(View v)
				{
					makeConfirmDilaog(MA);
				}
			});
		
		File[] files = myDir.listFiles();
		if (files.length<1)
		{
			TableRow TR=new TableRow(MA);
			
			
			TextView TV=new TextView(MA);
			TV.setText("NO SAVES");
			TV.setGravity(Gravity.CENTER_HORIZONTAL);
			TV.setTextColor(Color.WHITE);
			TR.addView(TV);
			TL.addView(TR);
			
		}
		else
		{
			for(File f:files)
			{
				String metaRoot = f.getAbsolutePath();
				
				String metaFileName="meta.txt";
				File metaFile = new File (metaRoot, metaFileName);
				if (metaFile.exists ())
				{
					LoadMenuRow TR=new LoadMenuRow(MA);
					TR.setSaveName(f.getName());
					TextView TV=new TextView(MA);
					SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
					TV.setText(f.getName()+" "+sdf.format(f.lastModified()));
					TV.setGravity(Gravity.CENTER_HORIZONTAL);
					TV.setTextColor(Color.WHITE);
					TR.addView(TV);
					
					File imgFile = new  File(f.getAbsolutePath()+File.separator+"image.jpg");
				
					if(imgFile.exists()){

						Bitmap image = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
						Matrix matrix=new Matrix();
						matrix.postScale(0.15f,0.15f);
						Bitmap scaledBitmap = Bitmap.createBitmap(image, 0, 0, image.getWidth(), image.getHeight(), matrix, true);
					
						ImageView IV = new ImageView(MA);

						IV.setImageBitmap(scaledBitmap);
						TR.addView(IV);
					}
				
					TL.addView(TR);
					//TR.setBackgroundResource(R.drawable.background_ui);
					TR.setBackgroundColor(Color.BLACK);
					TableLayout.LayoutParams TLLP=new TableLayout.LayoutParams();
					TLLP.setMargins(10,10,10,10);
				
					TR.setLayoutParams(TLLP);
					TR.setGravity(Gravity.CENTER);
					TR.setOnClickListener(new View.OnClickListener()
					{
						@Override
						public void onClick(View v)
						{
							
							onItemPress((LoadMenuRow)v,MA);
						}
					});
				}
			}
		}
	}
	private static void onItemPress(LoadMenuRow v,MainActivity MA)
	{
		TableLayout TL=MA.findViewById(R.id.load_game_saves_table);
		for(int index=0; index<TL.getChildCount(); ++index) {
			TL.getChildAt(index).setBackgroundColor(Color.BLACK);
		}
		v.setBackgroundColor(Color.LTGRAY);
		saveName=v.getSaveName();
	}
	
	private static void  makeConfirmDilaog(final MainActivity MA)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(MA);

		LinearLayout LL=new LinearLayout(MA);
		LL.setGravity(Gravity.CENTER);

		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
			LinearLayout.LayoutParams.WRAP_CONTENT,
			LinearLayout.LayoutParams.WRAP_CONTENT);
		LL.setLayoutParams(lp);
		LL.setOrientation(LinearLayout.VERTICAL);


		final TextView text = new TextView(MA);  
		text.setLayoutParams(lp);


		text.setText("ARE YOU SURE?");

// Add the buttons
		builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					// User clicked OK button

					String root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).toString()+File.separator+"sb9";
					File myDir = new File(root);
					if (myDir.exists()&&saveName.length()>0) {
						File save=new File(root+File.separator+saveName);
						if(save.exists())
						{
							try
							{
								String[] children = save.list();
								for (int i = 0; i < children.length; i++)
								{
									new File(save, children[i]).delete();
								}
							if(save.delete())
									GameLog.update(saveName+" deleted",0);
							}
							catch(Throwable t)
							{
								GameLog.update("ERROR: write error"+t,1);
							}
						}
							else
							GameLog.update("ERROR: save did not exist",1);
					}
					else
						GameLog.update("ERROR: dir not exist or name wrong",1);
					LoadGameUI.init(MA);
				}
			});
		builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					// User cancelled the dialo

				}
			});
// Set other dialog properties


// Create the AlertDialog
		AlertDialog dialog = builder.create();
		LL.addView(text);


		dialog.setView(LL);
		//dialog.setView(input); 

		dialog.show();
	}
	
}
