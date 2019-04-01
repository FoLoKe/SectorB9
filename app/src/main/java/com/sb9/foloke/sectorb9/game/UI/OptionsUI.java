package com.sb9.foloke.sectorb9.game.UI;
import com.sb9.foloke.sectorb9.*;

import com.sb9.foloke.sectorb9.game.Funtions.*;
import android.widget.*;

import android.widget.*;
import android.view.View.*;
import com.sb9.foloke.sectorb9.*;
import android.view.*;
import android.widget.CompoundButton.*;
import android.app.*;
import android.content.*;
import android.graphics.drawable.*;
import com.sb9.foloke.sectorb9.game.Assets.*;
import android.graphics.*;

public class OptionsUI
{
	public static void init(final MainActivity MA)
	{
        final ViewFlipper VF = MA.findViewById(R.id.UIFlipper);
		LinearLayout LL=MA.findViewById(R.id.option_ui_linear_layout);
		LL.removeAllViews();
		MA.findViewById(R.id.options_ui).setBackground(new BitmapDrawable(MA.getResources(),UIAsset.uiBgBlur));
		
		MA.findViewById(R.id.options_ui_close_button).setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v)
			{	
				VF.setDisplayedChild(VF.indexOfChild(MA.findViewById(R.id.menuUI)));
			}
		});
		
		for(Options p:Options.values())
		{
			LinearLayout TLL=new LinearLayout(MA);
			TextView TV=new TextView(MA);
			TV.setText(p.getName());
			TV.setTextColor(Color.WHITE);
			TLL.addView(TV);
			
			switch(p.getType())
			{
					case SWITCH:
					{
						final Options option=p;
						Switch SW=new Switch(MA);
						if(p.getValue()==0)
						SW.setChecked(false);
						else
							SW.setChecked(true);
						SW.setOnCheckedChangeListener(new OnCheckedChangeListener()
						{
							public void onCheckedChanged(CompoundButton view,boolean state)
							{
								if(state)
								option.setValue(1);
								else
								option.setValue(0);
								
								option.saveOptions();
							}
						});
						TLL.addView(SW);
					}
			}
			LL.addView(TLL);
		}
	}
}
