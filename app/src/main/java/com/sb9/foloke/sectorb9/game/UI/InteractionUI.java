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

    ///////////////REFACTOR: INIT() UPDATE() COMMAND() OPEN()
	private static float scaleX=1,scaleY=1;
	public static AI.order currentOrder;

	public static void init(final MainActivity MA,final Entity target)
	{
		GameLog.update("InteractionUI: init by "+target,0);
        final ViewFlipper VF = MA.findViewById(R.id.UIFlipper);
		scaleX=MA.getGameManager().getScreenSize().y/1600f;

		if(scaleX>2)
		{
            scaleX = 2;
            scaleY = 2;
        }
		if(scaleX<0.5f)
		{
            scaleX = 0.5f;
            scaleY = 0.5f;
        }


		int sizeX=128;
		int sizeY=64;
		GameLog.update("InteractionUI: buttons set",0);
		final ViewFlipper IVF=MA.findViewById(R.id.interaction_uiViewFlipper);
		IVF.setDisplayedChild(0);
		ImageButton buttonCloseInteraction = MA.findViewById(R.id.closeInteraction);
		final ImageButton buttonOpenCargo = MA.findViewById(R.id.openCargo);
		final ImageButton buttonOpenConfig=MA.findViewById(R.id.openConfig);
		final ImageButton buttonOpenProduction=MA.findViewById(R.id.openProduction);
		final ImageButton buttonOpenTechTree=MA.findViewById(R.id.openTechTree);
		final ImageButton buttonOpenShipsConstructor=MA.findViewById(R.id.openConstructor);

		buttonOpenCargo.setBackground(new BitmapDrawable(MA.getResources(),Bitmap.createScaledBitmap(UIAsset.interactionCargo,(int)(sizeX*scaleX),(int)(sizeY*scaleY),false)));
		buttonOpenConfig.setBackground(new BitmapDrawable(MA.getResources(),Bitmap.createScaledBitmap(UIAsset.interactionConfig,(int)(sizeX*scaleX),(int)(sizeY*scaleY),false)));
		buttonOpenProduction.setBackground(new BitmapDrawable(MA.getResources(),Bitmap.createScaledBitmap(UIAsset.interactionProduction,(int)(sizeX*scaleX),(int)(sizeY*scaleY),false)));
		buttonOpenTechTree.setBackground(new BitmapDrawable(MA.getResources(),Bitmap.createScaledBitmap(UIAsset.interactionTechs,(int)(sizeX*scaleX),(int)(sizeY*scaleY),false)));
		buttonOpenShipsConstructor.setBackground(new BitmapDrawable(MA.getResources(),Bitmap.createScaledBitmap(UIAsset.interactionShips,(int)(sizeX*scaleX),(int)(sizeY*scaleY),false)));
        buttonCloseInteraction.setBackground(new BitmapDrawable(MA.getResources(),Bitmap.createScaledBitmap(UIAsset.interactionCancel,(int)(sizeY*scaleX),(int)(sizeY*scaleY),false)));

		buttonOpenCargo.setVisibility(View.GONE);
		buttonOpenConfig.setVisibility(View.GONE);
		buttonOpenProduction.setVisibility(View.GONE);
		buttonOpenShipsConstructor.setVisibility(View.GONE);
		
		
		GameLog.update("InteractionUI: spaceDockUI set",0);
		if(target!=null)
		if(target instanceof SpaceDock)
		{
			buttonOpenShipsConstructor.setVisibility(View.VISIBLE);
			buttonOpenShipsConstructor.setOnClickListener(new OnClickListener()
			{
				public void onClick(View v)
				{
					MA.getGameManager().currentCommand=GameManager.command.INTERACTION;
					AssemblerUI.setOpened(false);

					ConstructorUI.update((SpaceDock)target);
					IVF.setDisplayedChild(IVF.indexOfChild(MA.findViewById(R.id.ship_constructor_ui)));
				}
			});
		}
		
		GameLog.update("InteractionUI: techTreeUI set",0);
		buttonOpenTechTree.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				MA.getGameManager().currentCommand=GameManager.command.INTERACTION;
				AssemblerUI.setOpened(false);

				TechUI.updatePositions();
				IVF.setDisplayedChild(IVF.indexOfChild(MA.findViewById(R.id.tech_ui)));
			}
		});
		
		GameLog.update("InteractionUI: inventoryUI set"+target,0);
		if(target!=null)
		{
			if (target.getOpened())
			{
				MA.getGameManager().currentCommand=GameManager.command.EXCHANGE;
				InventoryUI.setLeftSide(target);
				MA.getGameManager().updateInventory(null);
				buttonOpenCargo.setVisibility(View.VISIBLE);
      			buttonOpenCargo.setOnClickListener(new OnClickListener()
					{
						@Override
						public void onClick(View v) 
						{
							AssemblerUI.setOpened(false);

							
							IVF.setDisplayedChild(IVF.indexOfChild(MA.findViewById(R.id.inventoryUI)));
						}
					});
			}
			else
			{
				buttonOpenCargo.setVisibility(View.GONE);
			}
			
			GameLog.update("InteractionUI: optionsUI set",0);
			if (target.getInteractable()&&(target instanceof StaticEntity))
			{
				MA.getGameManager().currentCommand=GameManager.command.INTERACTION;
				buttonOpenConfig.setVisibility(View.VISIBLE);
				buttonOpenConfig.setOnClickListener(new OnClickListener()
				{
					@Override
					public void onClick(View v)
					{
						AssemblerUI.setOpened(false);

						ObjectOptionsUI.init((StaticEntity)target,IVF,MA);
						IVF.setDisplayedChild(IVF.indexOfChild(MA.findViewById(R.id.obj_optionsUI)));
					}
				});
			}
			else
			{
				buttonOpenConfig.setVisibility(View.GONE);
			}
			
			GameLog.update("InteractionUI: assemblerUI set",0);
			if (target instanceof Assembler)
			{
				MA.getGameManager().currentCommand=GameManager.command.INTERACTION;
				AssemblerUI.init(MA,(Assembler)target);
				buttonOpenProduction.setVisibility(View.VISIBLE);
      			buttonOpenProduction.setOnClickListener(new OnClickListener()
					{
						@Override
						public void onClick(View v) 
						{
							AssemblerUI.setOpened(true);
							MA.getGameManager().initAssemblerUI((Assembler)target);

							IVF.setDisplayedChild(IVF.indexOfChild(MA.findViewById(R.id.assemblerUI)));
						}
					});
			}
			else
			{
				buttonOpenProduction.setVisibility(View.GONE);
			}
		}
		//32x32==160x160 1:5
		
		GameLog.update("InteractionUI: close button set",0);

		//buttonCloseInteraction.getLayoutParams().width=(int)(1000*scaleX);
		buttonCloseInteraction.setOnClickListener
		(new OnClickListener() 
			{
				@Override
				public void onClick(View v) 
				{
					MA.getGameManager().currentCommand=GameManager.command.INTERACTION;
					AssemblerUI.setOpened(false);				
					MA.getGameManager().nullPressedObjects();
					MA.getGameManager().currentCommand= GameManager.command.CONTROL;
					VF.setDisplayedChild(VF.indexOfChild(MA.findViewById(R.id.actionUI)));
				}
			});
			
		GameLog.update("InteractionUI: build button set",0);
		ImageButton buildButton = MA.findViewById(R.id.openBuildUI);
		buildButton.setBackground(new BitmapDrawable(null,Bitmap.createScaledBitmap(UIAsset.buildModeButton,(int)(320*scaleX),(int)(160*scaleY),false)));
		
		buildButton.setOnClickListener
		(new OnClickListener() 
			{
				@Override
				public void onClick(View v) 
				{
					MA.getGameManager().currentCommand=GameManager.command.INTERACTION;
					BuildUI.init(MA);
					MA.getGameManager().nullPressedObjects();
					AssemblerUI.setOpened(false);
					VF.setDisplayedChild(VF.indexOfChild(MA.findViewById(R.id.buildUI)));
				}
			});
			
			
		Button openShipButton=MA.findViewById(R.id.openShip);
		openShipButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v)
			{
				MA.getGameManager().currentCommand=GameManager.command.INTERACTION;
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


		img.setImageBitmap(Bitmap.createScaledBitmap(UIAsset.interactionSign,(int)(200*scaleX),(int)(50*scaleY),false));
		//weaponsButton.getLayoutParams().width=(int)(150*scaleX);
		
		setAICommands(target,MA);
		GameLog.update("InteractionUI: ready",0);
	}
	
	private static void setAICommands(final Entity e,final MainActivity MA)
	{
		if(e!=null)
		GameLog.update("InteractionUI: orders set by"+e,0);
		else
			GameLog.update("InteractionUI: orders hide",0);
		//CONTROLLABLE ONLY 
		if(e instanceof ControlledShip)
		{
			//AI ONLY ORDERS
			if(((ControlledShip)e).getController() instanceof AI)
			{
				MA.findViewById(R.id.interaction_ui_LL_AIOptions).setVisibility(View.VISIBLE);
				MA.findViewById(R.id.interaction_ui_LL_buildOptions).setVisibility(View.GONE);
				
				//buttons
                ImageButton buttonAggressiveBehaviour=MA.findViewById(R.id.interaction_ui_b_behaviour_aggressive);
                ImageButton buttonDefensiveBehaviour=MA.findViewById(R.id.interaction_ui_b_behaviour_defensive);
                ImageButton buttonPeacefulBehaviour=MA.findViewById(R.id.interaction_ui_b_behaviour_peaceful);
                ImageButton buttonRetreatBehaviour=MA.findViewById(R.id.interaction_ui_b_behaviour_retreat);
				
				ImageButton buttonMoveToCommand=MA.findViewById(R.id.interaction_ui_b_order_move_to);
				ImageButton buttonAttackCommand=MA.findViewById(R.id.interaction_ui_b_order_attack);
				ImageButton buttonFollowCommand=MA.findViewById(R.id.interaction_ui_b_order_follow);
                ImageButton buttonRepairCommand=MA.findViewById(R.id.interaction_ui_b_order_repair);
                ImageButton buttonMineCommand=MA.findViewById(R.id.interaction_ui_b_order_mine);
				ImageButton buttonHoldCommand=MA.findViewById(R.id.interaction_ui_b_order_stay);
                ImageButton buttonPatrolCommand=MA.findViewById(R.id.interaction_ui_b_order_patrol);
				
				float size=64;
                buttonAggressiveBehaviour.setBackground(new BitmapDrawable(MA.getResources(),Bitmap.createScaledBitmap(UIAsset.AIAggressive,(int)(size*scaleX),(int)(size*scaleY),false)));
                buttonDefensiveBehaviour.setBackground(new BitmapDrawable(MA.getResources(),Bitmap.createScaledBitmap(UIAsset.AIDefensive,(int)(size*scaleX),(int)(size*scaleY),false)));
                buttonPeacefulBehaviour.setBackground(new BitmapDrawable(MA.getResources(),Bitmap.createScaledBitmap(UIAsset.AIPeaceful,(int)(size*scaleX),(int)(size*scaleY),false)));
                buttonRetreatBehaviour.setBackground(new BitmapDrawable(MA.getResources(),Bitmap.createScaledBitmap(UIAsset.AIRetreat,(int)(size*scaleX),(int)(size*scaleY),false)));

				buttonMoveToCommand.setBackground(new BitmapDrawable(MA.getResources(),Bitmap.createScaledBitmap(UIAsset.AIMove,(int)(size*scaleX),(int)(size*scaleY),false)));
				buttonAttackCommand.setBackground(new BitmapDrawable(MA.getResources(),Bitmap.createScaledBitmap(UIAsset.AIAttack,(int)(size*scaleX),(int)(size*scaleY),false)));
                buttonFollowCommand.setBackground(new BitmapDrawable(MA.getResources(),Bitmap.createScaledBitmap(UIAsset.AIFollow,(int)(size*scaleX),(int)(size*scaleY),false)));
                buttonRepairCommand.setBackground(new BitmapDrawable(MA.getResources(),Bitmap.createScaledBitmap(UIAsset.AIRepair,(int)(size*scaleX),(int)(size*scaleY),false)));
                buttonMineCommand.setBackground(new BitmapDrawable(MA.getResources(),Bitmap.createScaledBitmap(UIAsset.AIMine,(int)(size*scaleX),(int)(size*scaleY),false)));
				buttonHoldCommand.setBackground(new BitmapDrawable(MA.getResources(),Bitmap.createScaledBitmap(UIAsset.AIStay,(int)(size*scaleX),(int)(size*scaleY),false)));
                buttonPatrolCommand.setBackground(new BitmapDrawable(MA.getResources(),Bitmap.createScaledBitmap(UIAsset.AIPatrol,(int)(size*scaleX),(int)(size*scaleY),false)));

				//actions for behaviour
                buttonAggressiveBehaviour.setOnClickListener(new OnClickListener(){
						public void onClick(View v)
						{
							((AI)((ControlledShip)e).getController()).setCurrentBehaviour(AI.behaviour.AGGRESSIVE);
						}
					});
				
				buttonDefensiveBehaviour.setOnClickListener(new OnClickListener(){
						public void onClick(View v)
						{
							((AI)((ControlledShip)e).getController()).setCurrentBehaviour(AI.behaviour.DEFENSIVE);
						}
					});
					
				buttonPeacefulBehaviour.setOnClickListener(new OnClickListener(){
						public void onClick(View v)
						{
							((AI)((ControlledShip)e).getController()).setCurrentBehaviour(AI.behaviour.PEACEFUL);
						}
					});
				
				buttonRetreatBehaviour.setOnClickListener(new OnClickListener(){
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
					
				buttonHoldCommand.setOnClickListener(new OnClickListener(){
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
				
				
				
				
			}
			
			//CHANGE CONTROLLER MANUAL/AI COMMON ORDER
			ImageButton buttonControlCommand=MA.findViewById(R.id.interaction_ui_b_order_control);


            float size=64;
            buttonControlCommand.setBackground(new BitmapDrawable(MA.getResources(),Bitmap.createScaledBitmap(UIAsset.AIControl,(int)(size*scaleX),(int)(size*scaleY),false)));


			buttonControlCommand.setOnClickListener(new OnClickListener(){
					public void onClick(View v)
					{
						try
						{
							PlayerController playerController=MA.getGameManager().getController();
							ControlledShip controlled=playerController.getControlledEntity();
							ControlledShip pressed=(ControlledShip)e;

							if(controlled!=null)
							{
								playerController.setControlledEntity(null);
								controlled.setController(new AI(controlled));
								GameLog.update("ai set",3);
							}

							if(controlled!=pressed)
							{
								playerController.setControlledEntity(pressed);
								pressed.setController(playerController);
								GameLog.update("manual set",3);
							}
							setAICommands(e,MA);
						}
						catch(Exception e)
						{
							GameLog.update(e.toString(),1);
						}
					}
				});
			
		}
		else
		{
			MA.findViewById(R.id.interaction_ui_LL_buildOptions).setVisibility(View.VISIBLE);
			MA.findViewById(R.id.interaction_ui_LL_AIOptions).setVisibility(View.GONE);
		}
	}
	
}
