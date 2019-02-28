package com.sb9.foloke.sectorb9.game.UI;
import com.sb9.foloke.sectorb9.game.Entities.*;
import android.widget.*;
import com.sb9.foloke.sectorb9.R;
import com.sb9.foloke.sectorb9.*;

import android.widget.CompoundButton.*;
import android.widget.Toolbar.*;
import android.graphics.*;

public class ObjectOptionsUI
{
	//StaticEntity target=null;
	
	public static void init(final StaticEntity target,final ViewFlipper VF,MainActivity MA)
	{
		LinearLayout LL=(MA.findViewById(R.id.object_options_uiLinearLayout));
		if(LL.getChildCount()>0)
		LL.removeAllViews();
		
		
		LinearLayout LR=VF.findViewById(R.id.object_options_uiLinearLayout);
		//LR.setBackground(new BitmapDrawable(MA.getResources(),target.getGame().uiAsset.uiBgBlur));
		LR.setBackgroundColor(Color.parseColor("#55ffffff"));
		Switch SW1=new Switch(MA);
		SW1.setChecked(target.getEnabled());
		
		LR.addView(SW1);
		SW1.setOnCheckedChangeListener(new OnCheckedChangeListener()
			{
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					target.onAndOff();
				}
			});
			LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
			params.gravity=0;
			SW1.setLayoutParams(params);
		
		
	}
	
}
