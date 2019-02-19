package com.sb9.foloke.sectorb9.game.UI;
import android.widget.*;
import android.view.View.*;
import com.sb9.foloke.sectorb9.*;
import android.view.*;
import android.widget.CompoundButton.*;
import android.app.*;
import android.content.*;

public class MenuUI
{
	private static int prevViewID=0;
	public static void init(final MainActivity MA,final ViewFlipper VF,final int view)
	{
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

		Button openHelpButton=MA.findViewById(R.id.menu_uiOpenHelpButton);
		openHelpButton.setOnClickListener
		(new OnClickListener() 
			{
				@Override
				public void onClick(View v) 
				{
					MA.helpui.init(MA,VF,view);
					VF.setDisplayedChild(VF.indexOfChild(MA.findViewById(R.id.help_sectionUI)));
				}
			});
		Button openMapButton=MA.findViewById(R.id.menu_openMapButton);
		openMapButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v)
			{
				MA.mapUI.init(MA,VF);
				VF.setDisplayedChild(VF.indexOfChild(MA.findViewById(R.id.map_ui)));
			}
		});
		
		Switch menuDebugSwitch = MA.findViewById(R.id.debug_switch);

		menuDebugSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener()
			{
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					MA.getGameManager().drawDebugInfo=isChecked;
				}
			});
		Switch frameLimmiterSwitch=MA.findViewById(R.id.framelimiter_switch);
		frameLimmiterSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener(){
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					MA.getGameManager().getGamePanel().setFrameLimiter(isChecked);
					}
		});
		Button saveButton=MA.findViewById(R.id.menu_save);
		saveButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v)
			{
				makeSaveQuestionDilaog(MA,"save");
			}});
			
		Button loadButton=MA.findViewById(R.id.menu_load);
		loadButton.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View v)
				{
					makeSaveQuestionDilaog(MA,"load");
				}});
	
		Button mainMenuButton=MA.findViewById(R.id.menu_ToMainMenu);
		mainMenuButton.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View v)
				{
					makeSaveQuestionDilaog(MA,"menu");
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
			case "menu":
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
						case "menu":
							MA.prepareMenu();
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
