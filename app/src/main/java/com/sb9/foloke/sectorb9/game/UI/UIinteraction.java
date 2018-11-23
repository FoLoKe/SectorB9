package com.sb9.foloke.sectorb9.game.UI;
import android.widget.*;
import com.sb9.foloke.sectorb9.*;
import android.view.View.*;
import android.view.*;
//import java.lang.annotation.*;
import com.sb9.foloke.sectorb9.game.entities.*;
//mport java.lang.annotation.*;

public class UIinteraction
{
	public void init(final MainActivity MA,final ViewFlipper VF,final StaticEntity target)
	{
		Button openInventoryButton = MA.findViewById(R.id.openInventory);
		Button openInteraction=MA.findViewById(R.id.openInteraction);
		openInventoryButton.setVisibility(View.GONE);
		openInteraction.setVisibility(View.GONE);
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
							VF.setDisplayedChild(VF.indexOfChild(MA.findViewById(R.id.inventoryUI)));
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
						target.getGame().getObjOptions().init(target,VF,MA);
					VF.setDisplayedChild(VF.indexOfChild(MA.findViewById(R.id.objectOptionsUI)));
					}
				});
			}
			else
			{
				openInteraction.setVisibility(View.GONE);
			}
		}
		Button closeButton = MA.findViewById(R.id.closeInteraction);
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
