package com.sb9.foloke.sectorb9.game.UI;
import android.widget.*;
import com.sb9.foloke.sectorb9.*;
import android.view.View.*;
import android.view.*;
import com.sb9.foloke.sectorb9.game.Entities.*;
import android.graphics.*;
import com.sb9.foloke.sectorb9.game.Entities.Buildings.*;
import com.sb9.foloke.sectorb9.game.Assets.*;
import com.sb9.foloke.sectorb9.game.Managers.GameManager;

import android.graphics.drawable.*;
import com.sb9.foloke.sectorb9.game.Funtions.*;
import com.sb9.foloke.sectorb9.game.UI.TechUIs.*;

public class InteractionUI
{
	private static float scaleX=1,scaleY=1;
	public static void init(final MainActivity MA,final StaticEntity target)
	{
        final ViewFlipper VF = MA.findViewById(R.id.UIFlipper);
		scaleY=MA.getGameManager().getScreenSize().y/1600f;
		if(scaleX>2)
			scaleX=2;
		if(scaleX<0.5f)
			scaleX=0.5f;
		scaleX=scaleY;
		
		final ViewFlipper IVF=MA.findViewById(R.id.interaction_uiViewFlipper);
		IVF.setDisplayedChild(0);
		ImageButton closeButton = MA.findViewById(R.id.closeInteraction);
		final Button openInventoryButton = MA.findViewById(R.id.openInventory);
		final Button openInteraction=MA.findViewById(R.id.openInteraction);
		final Button openProduction=MA.findViewById(R.id.openProduction);
		final Button openTechTree=MA.findViewById(R.id.openTechTree);
		final Button openConstructor=MA.findViewById(R.id.openConstructor);
		openInventoryButton.setBackgroundColor(Color.parseColor("#33ffffff"));
		openInteraction.setBackgroundColor(Color.parseColor("#33ffffff"));
		openProduction.setBackgroundColor(Color.parseColor("#33ffffff"));
		openTechTree.setBackgroundColor(Color.parseColor("#33ffffff"));
		openConstructor.setBackgroundColor(Color.parseColor("#33ffffff"));
		openInventoryButton.setVisibility(View.GONE);
		openInteraction.setVisibility(View.GONE);
		openProduction.setVisibility(View.GONE);
		openConstructor.setVisibility(View.GONE);
		
		if(target!=null)
		if(target instanceof SpaceDock)
		{
			openConstructor.setVisibility(View.VISIBLE);
			openConstructor.setOnClickListener(new OnClickListener()
			{
				public void onClick(View v)
				{
					AssemblerUI.setOpened(false);
					openInteraction.setBackgroundColor(Color.parseColor("#22ffffff"));
					openProduction.setBackgroundColor(Color.parseColor("#22ffffff"));
					v.setBackgroundColor(Color.parseColor("#55ffffff"));
					ConstructorUI.update((SpaceDock)target);
					IVF.setDisplayedChild(IVF.indexOfChild(MA.findViewById(R.id.ship_constructor_ui)));
				}
			});
		}
		openTechTree.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				AssemblerUI.setOpened(false);
				openInteraction.setBackgroundColor(Color.parseColor("#22ffffff"));
				openProduction.setBackgroundColor(Color.parseColor("#22ffffff"));
				v.setBackgroundColor(Color.parseColor("#55ffffff"));
				TechUI.updatePositions();
				IVF.setDisplayedChild(IVF.indexOfChild(MA.findViewById(R.id.tech_ui)));
			}
		});
		
		if(target!=null)
		{
			if (target.getOpened())
			{
				InventoryUI.setObjectTarget(target);
				MA.getGameManager().updateInventory(null);
				openInventoryButton.setVisibility(View.VISIBLE);
      			openInventoryButton.setOnClickListener(new OnClickListener() 
					{
						@Override
						public void onClick(View v) 
						{
							AssemblerUI.setOpened(false);
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
						AssemblerUI.setOpened(false);
						openProduction.setBackgroundColor(Color.parseColor("#22ffffff"));
						openInventoryButton.setBackgroundColor(Color.parseColor("#22ffffff"));
						v.setBackgroundColor(Color.parseColor("#55ffffff"));
						ObjectOptionsUI.init(target,IVF,MA);
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
				
				AssemblerUI.init(MA,(Assembler)target);
				openProduction.setVisibility(View.VISIBLE);
      			openProduction.setOnClickListener(new OnClickListener() 
					{
						@Override
						public void onClick(View v) 
						{
							AssemblerUI.setOpened(true);
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
		//32x32==160x160 1:5
		closeButton.setBackgroundDrawable(new BitmapDrawable(Bitmap.createScaledBitmap(UIAsset.cancelButton,(int)(160*scaleX),(int)(160*scaleY),false)));
		//closeButton.getLayoutParams().width=(int)(1000*scaleX);
		closeButton.setOnClickListener
		(new OnClickListener() 
			{
				@Override
				public void onClick(View v) 
				{
					AssemblerUI.setOpened(false);				
					MA.getGameManager().nullPressedObject();
					MA.getGameManager().command= GameManager.commandMoving;
					VF.setDisplayedChild(VF.indexOfChild(MA.findViewById(R.id.actionUI)));
				}
			});
			
		ImageButton buildButton = MA.findViewById(R.id.openBuildUI);
		buildButton.setBackgroundDrawable(new BitmapDrawable(Bitmap.createScaledBitmap(UIAsset.buildModeButton,(int)(320*scaleX),(int)(160*scaleY),false)));
		
		buildButton.setOnClickListener
		(new OnClickListener() 
			{
				@Override
				public void onClick(View v) 
				{
					BuildUI.init(MA);
					MA.getGameManager().nullPressedObject();
					AssemblerUI.setOpened(false);
					VF.setDisplayedChild(VF.indexOfChild(MA.findViewById(R.id.buildUI)));
				}
			});
		Button openShipButton=MA.findViewById(R.id.openShip);
		openShipButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v)
			{
				ShipUI.init();
				VF.setDisplayedChild(VF.indexOfChild(MA.findViewById(R.id.shipui)));
			}
		});
		
		ImageView img=MA.findViewById(R.id.interaction_uiImageSign);
		BitmapFactory.Options bitmapOptions=new BitmapFactory.Options();
        bitmapOptions.inScaled=false;
		///2500 screenW
		///100 normal size for 32 px
		//so button size is scaled 3.125 for 2500 it is 100%

		//for 1800 scale =(int) 3.125*1800/2500


		img.setImageBitmap(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(MA.getResources(),R.drawable.ui_interaction_sign,bitmapOptions),(int)(200*scaleX),(int)(50*scaleY),false));
		//weaponsButton.getLayoutParams().width=(int)(150*scaleX);
	}
	
}
