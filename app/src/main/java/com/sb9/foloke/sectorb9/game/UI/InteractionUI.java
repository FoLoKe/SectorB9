package com.sb9.foloke.sectorb9.game.UI;
import android.widget.*;
import com.sb9.foloke.sectorb9.*;
import android.view.View.*;
import android.view.*;
import com.sb9.foloke.sectorb9.game.Entities.*;
import android.graphics.*;
import com.sb9.foloke.sectorb9.game.Entities.Buildings.*;

public class InteractionUI
{
	public void init(final MainActivity MA,final ViewFlipper VF,final StaticEntity target)
	{
		final ViewFlipper IVF=MA.findViewById(R.id.interaction_uiViewFlipper);
		IVF.setDisplayedChild(0);
		Button closeButton = MA.findViewById(R.id.closeInteraction);
		final Button openInventoryButton = MA.findViewById(R.id.openInventory);
		final Button openInteraction=MA.findViewById(R.id.openInteraction);
		final Button openProduction=MA.findViewById(R.id.openProduction);
		openInventoryButton.setBackgroundColor(Color.parseColor("#33ffffff"));
		openInteraction.setBackgroundColor(Color.parseColor("#33ffffff"));
		openProduction.setBackgroundColor(Color.parseColor("#33ffffff"));
		openInventoryButton.setVisibility(View.GONE);
		openInteraction.setVisibility(View.GONE);
		openProduction.setVisibility(View.GONE);
		if(target!=null)
		{
			if (target.getOpened())
			{
				target.getGameManager().getInventoryUi().setObjectTarget(target);
				MA.getGameManager().updateInventory(null);
				openInventoryButton.setVisibility(View.VISIBLE);
      			openInventoryButton.setOnClickListener(new OnClickListener() 
					{
						@Override
						public void onClick(View v) 
						{
							MA.assemblerUIi.setOpened(false);
							openInteraction.setBackgroundColor(Color.parseColor("#22ffffff"));
							openProduction.setBackgroundColor(Color.parseColor("#22ffffff"));
							v.setBackgroundColor(Color.parseColor("#55ffffff"));
							IVF.setDisplayedChild(IVF.indexOfChild(MA.findViewById(R.id.inventoryUI)));
						}
					});
			}
			else
			{
				openInventoryButton.setVisibility(View.GONE);
			}
			if (target.getInteractable())
			{
				openInteraction.setVisibility(View.VISIBLE);
				openInteraction.setOnClickListener(new OnClickListener()
				{
					@Override
					public void onClick(View v)
					{
						MA.assemblerUIi.setOpened(false);
						openProduction.setBackgroundColor(Color.parseColor("#22ffffff"));
						openInventoryButton.setBackgroundColor(Color.parseColor("#22ffffff"));
						v.setBackgroundColor(Color.parseColor("#55ffffff"));
						target.getGameManager().getObjOptions().init(target,IVF,MA);
						IVF.setDisplayedChild(IVF.indexOfChild(MA.findViewById(R.id.obj_optionsUI)));
					}
				});
			}
			else
			{
				openInteraction.setVisibility(View.GONE);
			}
			
			if (target instanceof Assembler)
			{
				
				MA.assemblerUIi.init(MA,(Assembler)target);
				openProduction.setVisibility(View.VISIBLE);
      			openProduction.setOnClickListener(new OnClickListener() 
					{
						@Override
						public void onClick(View v) 
						{
							MA.assemblerUIi.setOpened(true);
							MA.getGameManager().initAssemblerUI((Assembler)target);
							openInventoryButton.setBackgroundColor(Color.parseColor("#22ffffff"));
							openInteraction.setBackgroundColor(Color.parseColor("#22ffffff"));
							v.setBackgroundColor(Color.parseColor("#55ffffff"));
							IVF.setDisplayedChild(IVF.indexOfChild(MA.findViewById(R.id.assemblerUI)));
						}
					});
			}
			else
			{
				openProduction.setVisibility(View.GONE);
			}
		}
		
		closeButton.setOnClickListener
		(new OnClickListener() 
			{
				@Override
				public void onClick(View v) 
				{
					MA.assemblerUIi.setOpened(false);				
					MA.getGameManager().nullPressedObject();
					MA.getGameManager().command=MA.getGameManager().commandMoving;
					VF.setDisplayedChild(VF.indexOfChild(MA.findViewById(R.id.actionUI)));
				}
			});
			
		Button buildButton = MA.findViewById(R.id.openBuildUI);
		buildButton.setOnClickListener
		(new OnClickListener() 
			{
				@Override
				public void onClick(View v) 
				{
					MA.getGameManager().nullPressedObject();
					MA.assemblerUIi.setOpened(false);
					VF.setDisplayedChild(VF.indexOfChild(MA.findViewById(R.id.buildUI)));
				}
			});
		Button openShipButton=MA.findViewById(R.id.openShip);
		openShipButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v)
			{
				MA.shipUI.init();
				VF.setDisplayedChild(VF.indexOfChild(MA.findViewById(R.id.shipui)));
			}
		});
	}
}
