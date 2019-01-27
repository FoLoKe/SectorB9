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
	
	public void init(final MainActivity MA,final ViewFlipper VF)
	{
		Button debugButton = MA.findViewById(R.id.DEBUG);
        // Устанавливаем действие по нажатию
        debugButton.setOnClickListener
		(new OnClickListener() 
			{
				@Override
				public void onClick(View v) 
				{
					//switchPlayerInventory();
					//if(shootButton.getVisibility()==View.VISIBLE)
					MA.getGameManager().getPlayer().setShip();
					//else
					//	shootButton.setVisibility(View.VISIBLE);
				}
			});
			
		Button interactionButton = MA.findViewById(R.id.Interaction);
        // Устанавливаем действие по нажатию
        interactionButton.setOnClickListener
		(new OnClickListener() 
			{
				@Override
				public void onClick(View v) 
				{
					//switchPlayerInventory();
					//if(shootButton.getVisibility()==View.VISIBLE)
					//MA.getGame().getPlayer().setShip();
					//else
					
					MA.getGameManager().command= GameManager.commandInteraction;
					VF.setDisplayedChild(VF.indexOfChild(MA.findViewById(R.id.interactionUI)));
					//VF.setDisplayedChild(VF.indexOfChild(MA.findViewById(R.id.assembler_uiFrameLayout)));
					//	shootButton.setVisibility(View.VISIBLE);
					MA.uiInteraction.init(MA,VF,null);
				}
			});
        // Устанавливаем действие по нажатию
		ImageButton shootButton = MA.findViewById(R.id.shootButton);
		//shootButton.setImageBitmap();
		shootButton.setBackground(new BitmapDrawable(MA.getResources(),UIAsset.shootButton));
		/*LinearLayout.LayoutParams params = */shootButton.getLayoutParams().width=200;
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
			//shootButton.setonto
			VF.setOnTouchListener(new OnTouchListener(){
				@Override
				public boolean onTouch(View v,MotionEvent e)
				{
					return false;
				}
			});
		MA.findViewById(R.id.actionUI).setOnTouchListener(new OnTouchListener(){
				@Override
				public boolean onTouch(View v,MotionEvent e)
				{
					return false;
				}
			});
			
			
			
	}
}
