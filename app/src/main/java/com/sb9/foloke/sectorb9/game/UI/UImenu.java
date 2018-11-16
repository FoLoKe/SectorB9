package com.sb9.foloke.sectorb9.game.UI;
import android.widget.*;
import android.view.View.*;
import com.sb9.foloke.sectorb9.*;
import android.view.*;
import android.widget.CompoundButton.*;

public class UImenu
{
	public void init(final MainActivity MA,final ViewFlipper VF,final int view)
	{
		
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

		Switch menuDebugSwitch = MA.findViewById(R.id.debug_switch);

		menuDebugSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener()
			{
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					MA.getGame().drawDebugInf=isChecked;
				}
			});
	}
}
