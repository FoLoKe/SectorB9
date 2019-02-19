package com.sb9.foloke.sectorb9.game.UI;
import com.sb9.foloke.sectorb9.*;
import com.sb9.foloke.sectorb9.game.Assets.UIAsset;
import com.sb9.foloke.sectorb9.game.Managers.GameManager;

import android.widget.*;
import android.view.View.*;
import android.view.*;
import android.graphics.drawable.*;

public class ActionUI
{
	
	public static void init(final MainActivity MA,final ViewFlipper VF)
	{
		
			
		Button interactionButton = MA.findViewById(R.id.Interaction);
      
        interactionButton.setOnClickListener
		(new OnClickListener() 
			{
				@Override
				public void onClick(View v) 
				{
					MA.getGameManager().command= GameManager.commandInteraction;
					VF.setDisplayedChild(VF.indexOfChild(MA.findViewById(R.id.interactionUI)));
					InteractionUI.init(MA,VF,null);
				}
			});
		ImageButton shootButton = MA.findViewById(R.id.shootButton);
		shootButton.setBackground(new BitmapDrawable(MA.getResources(),UIAsset.shootButton));
		shootButton.getLayoutParams().width=200;
		shootButton.getLayoutParams().height=200;
		
		shootButton.setOnTouchListener
		(new OnTouchListener(){
				@Override
				public boolean onTouch(View view,MotionEvent e)
				{
					switch(e.getAction())
					{
						case MotionEvent.ACTION_DOWN:
							MA.getGameManager().getPlayer().shootFlag=true;

							break;
						case MotionEvent.ACTION_UP:
							MA.getGameManager().getPlayer().shootFlag=false;
							break;
					}
					return false;
				}
			});
		

			
			
	}
}
