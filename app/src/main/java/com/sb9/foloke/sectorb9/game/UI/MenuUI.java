package com.sb9.foloke.sectorb9.game.UI;
import android.widget.*;
import android.view.View.*;
import com.sb9.foloke.sectorb9.*;
import android.view.*;
import android.widget.CompoundButton.*;

public class MenuUI
{
	int prevViewID=0;
	public void init(final MainActivity MA,final ViewFlipper VF,final int view)
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
					MA.getGame().setPause(false);
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
					MA.getGame().drawDebugInf=isChecked;
				}
			});
			
		Button saveButton=MA.findViewById(R.id.menu_save);
		saveButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v)
			{
				MA.saveFile("test");
			}});
		Button loadButton=MA.findViewById(R.id.menu_load);
		loadButton.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View v)
				{
					MA.loadFile("test");
				}});
	}
	public int getPrevViewID()
	{
		return prevViewID;
	}
}
