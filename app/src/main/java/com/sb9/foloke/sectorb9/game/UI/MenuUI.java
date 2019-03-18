package com.sb9.foloke.sectorb9.game.UI;
import android.widget.*;
import android.view.View.*;
import com.sb9.foloke.sectorb9.*;
import android.view.*;
import android.widget.CompoundButton.*;
import android.app.*;
import android.content.*;
import android.graphics.*;
import com.sb9.foloke.sectorb9.game.Assets.*;
import android.graphics.drawable.*;

public class MenuUI
{
	private static int prevViewID=0;
	public static float scaleX=1;
	public static float scaleY=1;
	public static final int size=64;
	public static void init(final MainActivity MA,final ViewFlipper VF,final int view)
	{
		scaleY=MA.getGameManager().getGamePanel().canvasH/1600;
		if(scaleX>2)
			scaleX=2;
		if(scaleX<0.5f)
			scaleX=0.5f;
		scaleX=scaleY;
		prevViewID=view;
		Button closeMenuButton=MA.findViewById(R.id.closeMenu);
		closeMenuButton.setOnClickListener
		(new OnClickListener() 
			{
				@Override
				public void onClick(View v) 
				{
					
					VF.setDisplayedChild(view);
					MA.getGameManager().setPause(false);
					MA.findViewById(R.id.Menu).setVisibility(View.VISIBLE);
				}
			});
		//optimal 50=32
		
		
		BitmapFactory.Options bitmapOptions=new BitmapFactory.Options();
        bitmapOptions.inScaled=false;
		///2500 screenW
		///100 normal size for 32 px
		//so button size is scaled 3.125 for 2500 it is 100%

		//for 1800 scale =(int) 3.125*1800/2500


		
		ImageButton openHelpButton=MA.findViewById(R.id.menu_uiOpenHelpButton);
		openHelpButton.setBackgroundDrawable(new BitmapDrawable(MA.getResources(),Bitmap.createScaledBitmap(UIAsset.helpButton,(int)(size*3*scaleX),(int)(size*scaleY),false)));
		openHelpButton.setOnClickListener
		(new OnClickListener() 
			{
				@Override
				public void onClick(View v) 
				{
					HelpUI.init(MA,VF,view);
					VF.setDisplayedChild(VF.indexOfChild(MA.findViewById(R.id.help_sectionUI)));
				}
			});
			
		ImageButton openOptionsButton=MA.findViewById(R.id.menu_ui_open_options);
		openOptionsButton.setBackgroundDrawable(new BitmapDrawable(MA.getResources(),Bitmap.createScaledBitmap(UIAsset.optionsButton,(int)(size*3*scaleX),(int)(size*scaleY),false)));
		openOptionsButton.setOnClickListener
		(new OnClickListener() 
			{
				@Override
				public void onClick(View v) 
				{
					OptionsUI.init(MA,VF);
					VF.setDisplayedChild(VF.indexOfChild(MA.findViewById(R.id.options_ui)));
				}
			});
		ImageButton openMapButton=MA.findViewById(R.id.menu_openMapButton);
		openMapButton.setBackgroundDrawable(new BitmapDrawable(MA.getResources(),Bitmap.createScaledBitmap(UIAsset.mapButton,(int)(size*3*scaleX),(int)(size*scaleY),false)));
		openMapButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v)
			{
				MapUI.init(MA,VF);
				VF.setDisplayedChild(VF.indexOfChild(MA.findViewById(R.id.map_ui)));
			}
		});
		
		ImageButton saveButton=MA.findViewById(R.id.menu_save);
		saveButton.setBackgroundDrawable(new BitmapDrawable(MA.getResources(),Bitmap.createScaledBitmap(UIAsset.saveButton,(int)(size*3*scaleX),(int)(size*scaleY),false)));
		saveButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v)
			{
				makeSaveQuestionDilaog(MA,"save");
			}});
			
		ImageButton loadButton=MA.findViewById(R.id.menu_load);
		loadButton.setBackgroundDrawable(new BitmapDrawable(MA.getResources(),Bitmap.createScaledBitmap(UIAsset.loadButton,(int)(size*3*scaleX),(int)(size*scaleY),false)));
		loadButton.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View v)
				{
					makeSaveQuestionDilaog(MA,"load");
				}});
	
		ImageButton exitMenuButton=MA.findViewById(R.id.menu_ToMainMenu);
		exitMenuButton.setBackgroundDrawable(new BitmapDrawable(MA.getResources(),Bitmap.createScaledBitmap(UIAsset.exitButton,(int)(size*3*scaleX),(int)(size*scaleY),false)));
		exitMenuButton.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View v)
				{
					makeSaveQuestionDilaog(MA,"exit");
				}});





        Button sh1=MA.findViewById(R.id.menu_ui_set_first_ship);
        sh1.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v)
            {
                MA.getGameManager().getPlayer().setShip(1);
            }});

        Button sh2=MA.findViewById(R.id.menu_ui_set_second_ship);
        sh2.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v)
            {
                MA.getGameManager().getPlayer().setShip(2);
            }});

        Button sh3=MA.findViewById(R.id.menu_ui_set_third_ship);
       sh3.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v)
            {
                MA.getGameManager().getPlayer().setShip(3);
            }});
	}
	
	public int getPrevViewID(){return prevViewID;}
	
	private static void makeSaveQuestionDilaog(final MainActivity MA,final String actionName)
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
		
		switch(actionName)
		{
			case "save":
				text.setText("Overwrite this save?");
				break;
			case "load":
				text.setText("Save current game?");
				break;
			case "exit":
				text.setText("Save current game?");
				break;
		}
		


// Add the buttons
		builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					// User clicked OK button

					MA.saveFile("sector-"+MA.getGameManager().getCurrentSector().x+"-"+MA.getGameManager().getCurrentSector().y,MA.getGameManager().getSaveName());
				
					switch(actionName)
					{
						case "save":
							
							break;
						case "load":
							MA.loadGame();
							break;
						case "exit":
							MainThread mainThread=MA.getGameManager().getGamePanel().getMainThread();
							boolean retry = true;
							while(retry)
							{
								try{mainThread.setRunning(false);
									mainThread.join();
									retry=false;
								}catch(InterruptedException e)
								{MA.makeToast(""+e,1);}
							}
							MA.prepareMenu();
							Intent homeIntent = new Intent(Intent.ACTION_MAIN);
							homeIntent.addCategory( Intent.CATEGORY_HOME );
							homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							
							android.os.Process.killProcess(android.os.Process.myPid());
							//MA.finish();
							MA.startActivity(homeIntent); 
							break;
					}
				}
			});
		builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					// User cancelled the dialo
					makeConfirmDilaog(MA,actionName);
				}
			});
// Set other dialog properties

		builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					// User cancelled the dialo
					switch(actionName)
					{
						case "save":

							break;
						case "load":
							
							break;
					}
				}
			});
// Create the AlertDialog
		AlertDialog dialog = builder.create();
		LL.addView(text);
		

		dialog.setView(LL);
		//dialog.setView(input); 

		dialog.show();
	}
	


	private static void  makeConfirmDilaog(final MainActivity MA,final String actionName)
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

					switch(actionName)
					{
						case "save":

							break;
						case "load":
							MA.loadGame();
							break;
						case "menu":
							MA.prepareMenu();
							break;
					}
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
