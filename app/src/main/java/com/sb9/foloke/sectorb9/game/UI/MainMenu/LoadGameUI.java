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

		MA.findViewById(R.id.back_from_load).setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				MA.prepareMenu();
			}
		});
		
		MA.findViewById(R.id.load_menu_load_game).setOnClickListener(new OnClickListener()
			{
				public void onClick(View v)
				{
					MA.prepareNewLoad(saveName);
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
					TR.setOnClickListener(new OnClickListener()
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
}
