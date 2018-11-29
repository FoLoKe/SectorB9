package com.sb9.foloke.sectorb9.game.UI;
import android.widget.*;
import com.sb9.foloke.sectorb9.*;
import android.view.View.*;
import android.view.*;
//import java.lang.annotation.*;
import com.sb9.foloke.sectorb9.game.entities.*;
import android.graphics.*;
import com.sb9.foloke.sectorb9.game.entities.Buildings.*;
//mport java.lang.annotation.*;

public class UIinteraction
{
	public void init(final MainActivity MA,final ViewFlipper VF,final StaticEntity target)
	{
		final ViewFlipper IVF=MA.findViewById(R.id.interaction_uiViewFlipper);
		IVF.setDisplayedChild(0);
		Button closeButton = MA.findViewById(R.id.closeInteraction);
		final Button openInventoryButton = MA.findViewById(R.id.openInventory);
		final Button openInteraction=MA.findViewById(R.id.openInteraction);
		final Button openProduction=MA.findViewById(R.id.openProduction);
		openInventoryButton.setBackgroundColor(Color.BLACK);
		openInteraction.setBackgroundColor(Color.BLACK);
		openProduction.setBackgroundColor(Color.BLACK);
		openInventoryButton.setVisibility(View.GONE);
		openInteraction.setVisibility(View.GONE);
		openProduction.setVisibility(View.GONE);
		if(target!=null)
		{
			if (target.getOpened())
			{
				target.getGame().getObjectUIInventory().setTarget(target);
				MA.initInvenories();
				//MA.openObjectInventory();
				openInventoryButton.setVisibility(View.VISIBLE);
      			openInventoryButton.setOnClickListener(new OnClickListener() 
					{
						@Override
						public void onClick(View v) 
						{
							//MA.switchPlayerInventory();
							openInteraction.setBackgroundColor(Color.BLACK);
							openProduction.setBackgroundColor(Color.BLACK);
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
						openProduction.setBackgroundColor(Color.BLACK);
						openInventoryButton.setBackgroundColor(Color.BLACK);
					v.setBackgroundColor(Color.parseColor("#55ffffff"));
					target.getGame().getObjOptions().init(target,IVF,MA);
					IVF.setDisplayedChild(IVF.indexOfChild(MA.findViewById(R.id.obj_optionsUI)));
					}
				});
			}
			else
			{
				openInteraction.setVisibility(View.GONE);
			}
			
			if (target instanceof Crusher)
			{
				
				MA.assemblerUIi.init(MA);
				//MA.openObjectInventory();
				openProduction.setVisibility(View.VISIBLE);
      			openProduction.setOnClickListener(new OnClickListener() 
					{
						@Override
						public void onClick(View v) 
						{
							//MA.switchPlayerInventory();
							openInventoryButton.setBackgroundColor(Color.BLACK);
							openInteraction.setBackgroundColor(Color.BLACK);
							v.setBackgroundColor(Color.parseColor("#55ffffff"));
							IVF.setDisplayedChild(IVF.indexOfChild(MA.findViewById(R.id.assemblerUI)));
						}
					});
			}
			else
			{
				openInventoryButton.setVisibility(View.GONE);
			}
		}
		
		closeButton.setOnClickListener
		(new OnClickListener() 
			{
				@Override
				public void onClick(View v) 
				{
					MA.getGame().nullPressedObject();
					MA.getGame().command=MA.getGame().commandMoving;
					VF.setDisplayedChild(VF.indexOfChild(MA.findViewById(R.id.actionUI)));
					//MA.switchPlayerInventory();
					
				}
			});
			
		Button buildButton = MA.findViewById(R.id.openBuildUI);
		buildButton.setOnClickListener
		(new OnClickListener() 
			{
				@Override
				public void onClick(View v) 
				{
					MA.getGame().nullPressedObject();
					
					VF.setDisplayedChild(VF.indexOfChild(MA.findViewById(R.id.buildUI)));
					//MA.switchPlayerInventory();

				}
			});
	}
}
