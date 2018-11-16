package com.sb9.foloke.sectorb9.game.UI;
import com.sb9.foloke.sectorb9.game.entities.*;
import android.widget.*;
import com.sb9.foloke.sectorb9.R;
import com.sb9.foloke.sectorb9.*;
import android.view.View.*;
import android.view.*;
import android.widget.CompoundButton.*;
import android.graphics.drawable.*;
import android.widget.Toolbar.*;

public class objectOptionsUI
{
	//StaticEntity target=null;
	
	public void init(final StaticEntity target,final ViewFlipper VF,MainActivity MA)
	{
		LinearLayout LL=(MA.findViewById(R.id.object_options_uiLinearLayout));
		if(LL.getChildCount()>0)
		LL.removeAllViews();
		Button closeMenuButton=MA.findViewById(R.id.closeObjectOptions);
		//final ViewFlipper VF=vf;
		closeMenuButton.setOnClickListener(new OnClickListener() 
			{
				@Override
				public void onClick(View v) 
				{
					//switchPlayerInventory();
					//if(shootButton.getVisibility()==View.VISIBLE)
					VF.setDisplayedChild(VF.indexOfChild(VF.findViewById(R.id.interactionUI)));
					
					//else
					//	shootButton.setVisibility(View.VISIBLE);
				}
			});
		//this.target=target;
		LinearLayout LR=VF.findViewById(R.id.object_options_uiLinearLayout);
		LR.setBackground(new BitmapDrawable(MA.getResources(),target.getGame().uiAsset.uiBgBlur));
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
