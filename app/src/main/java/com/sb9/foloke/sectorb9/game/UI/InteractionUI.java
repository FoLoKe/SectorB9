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
import com.sb9.foloke.sectorb9.game.UI.CustomViews.*;
import com.sb9.foloke.sectorb9.game.AI.*;

public class InteractionUI
{
	private static float scaleX=1,scaleY=1;
	public static AI.order currentOrder;
	
	public static void init(final MainActivity MA,final Entity target)
	{
		GameLog.update("InteractionUI: init by "+target,0);
        final ViewFlipper VF = MA.findViewById(R.id.UIFlipper);
		scaleY=MA.getGameManager().getScreenSize().y/1600f;
		if(scaleX>2)
			scaleX=2;
		if(scaleX<0.5f)
			scaleX=0.5f;
		scaleX=scaleY;
		
		GameLog.update("InteractionUI: buttons set",0);
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
		
		
		GameLog.update("InteractionUI: spaceDockUI set",0);
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
		
		GameLog.update("InteractionUI: techTreeUI set",0);
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
		
		GameLog.update("InteractionUI: inventoryUI set"+target,0);
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
			
			GameLog.update("InteractionUI: optionsUI set",0);
			if (target.getInteractable()&&(target instanceof StaticEntity))
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
						ObjectOptionsUI.init((StaticEntity)target,IVF,MA);
						IVF.setDisplayedChild(IVF.indexOfChild(MA.findViewById(R.id.obj_optionsUI)));
					}
				});
			}
			else
			{
				openInteraction.setVisibility(View.GONE);
			}
			
			GameLog.update("InteractionUI: assemblerUI set",0);
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
		
		GameLog.update("InteractionUI: close button set",0);
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
					MA.getGameManager().currentCommand= GameManager.command.CONTROL;
					VF.setDisplayedChild(VF.indexOfChild(MA.findViewById(R.id.actionUI)));
				}
			});
			
		GameLog.update("InteractionUI: build button set",0);
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
		setAICommands(target,MA);
		GameLog.update("InteractionUI: ready",0);
	}
	
	public static void setAICommands(final Entity e,final MainActivity MA)
	{
		if(e instanceof ControlledShip)
			if(((ControlledShip)e).getController() instanceof AI)
			{
				MA.findViewById(R.id.interaction_ui_LL_AIOptions).setVisibility(View.VISIBLE);
				MA.findViewById(R.id.interaction_ui_LL_buildOptions).setVisibility(View.GONE);
				
				//buttons
				Button buttonAgressiveBehaviout=MA.findViewById(R.id.interaction_ui_b_behaviour_agressive);
				Button buttonDefensiveBehaviout=MA.findViewById(R.id.interaction_ui_b_behaviour_defensive);
				Button buttonPeacefulBehaviout=MA.findViewById(R.id.interaction_ui_b_behaviour_peaceful);
				Button buttonRetreatBehaviout=MA.findViewById(R.id.interaction_ui_b_behaviour_retreat);
				
				Button buttonMoveToCommand=MA.findViewById(R.id.interaction_ui_b_order_move_to);
				Button buttonAttackCommand=MA.findViewById(R.id.interaction_ui_b_order_attack);
				Button buttonFollowCommand=MA.findViewById(R.id.interaction_ui_b_order_follow);
				Button buttonRepairCommand=MA.findViewById(R.id.interaction_ui_b_order_repair);
				Button buttonMineCommand=MA.findViewById(R.id.interaction_ui_b_order_mine);
				Button buttoHoldCommand=MA.findViewById(R.id.interaction_ui_b_order_stay);
				Button buttonPatrolCommand=MA.findViewById(R.id.interaction_ui_b_behaviour_patrol);
				
				
				//actions for behaviour
				buttonAgressiveBehaviout.setOnClickListener(new OnClickListener(){
						public void onClick(View v)
						{
							((AI)((ControlledShip)e).getController()).setCurrentBehaviour(AI.behaviour.AGGRESSIVE);
						}
					});
				
				buttonDefensiveBehaviout.setOnClickListener(new OnClickListener(){
						public void onClick(View v)
						{
							((AI)((ControlledShip)e).getController()).setCurrentBehaviour(AI.behaviour.DEFENSIVE);
						}
					});
					
				buttonPeacefulBehaviout.setOnClickListener(new OnClickListener(){
						public void onClick(View v)
						{
							((AI)((ControlledShip)e).getController()).setCurrentBehaviour(AI.behaviour.PEACEFUL);
						}
					});
				
				buttonRetreatBehaviout.setOnClickListener(new OnClickListener(){
						public void onClick(View v)
						{
							((AI)((ControlledShip)e).getController()).setCurrentBehaviour(AI.behaviour.RETREAT);
						}
					});
					
				//actions for orders
				buttonMoveToCommand.setOnClickListener(new OnClickListener(){
						public void onClick(View v)
						{
							MA.getGameManager().currentCommand= GameManager.command.ORDER;
							currentOrder=AI.order.MOVETO;
						}
					});
					
				buttonAttackCommand.setOnClickListener(new OnClickListener(){
						public void onClick(View v)
						{
							MA.getGameManager().currentCommand= GameManager.command.ORDER;
							currentOrder=AI.order.ATTACK;
						}
					});
					
				buttoHoldCommand.setOnClickListener(new OnClickListener(){
						public void onClick(View v)
						{
							((AI)((ControlledShip)e).getController()).setCurrentOrder(AI.order.STAY);
						}
					});
				buttonPatrolCommand.setOnClickListener(new OnClickListener(){
						public void onClick(View v)
						{
							((AI)((ControlledShip)e).getController()).setCurrentOrder(AI.order.PATROL);
						}
					});
					
				buttonFollowCommand.setOnClickListener(new OnClickListener(){
						public void onClick(View v)
						{
							MA.getGameManager().currentCommand= GameManager.command.ORDER;
							currentOrder=AI.order.FOLLOW;
						}
					});
					
				buttonMineCommand.setOnClickListener(new OnClickListener(){
						public void onClick(View v)
						{
							((AI)((ControlledShip)e).getController()).setCurrentOrder(AI.order.MINE);
						}
					});
					
				buttonRepairCommand.setOnClickListener(new OnClickListener(){
						public void onClick(View v)
						{
							((AI)((ControlledShip)e).getController()).setCurrentOrder(AI.order.REPAIR);
						}
					});
				
				
				
				return;
			}
			
		MA.findViewById(R.id.interaction_ui_LL_buildOptions).setVisibility(View.VISIBLE);
		MA.findViewById(R.id.interaction_ui_LL_AIOptions).setVisibility(View.GONE);
	}
	
}
