package com.sb9.foloke.sectorb9.game.UI;
import android.widget.*;
import com.sb9.foloke.sectorb9.*;
import android.view.View.*;
import android.view.*;
import com.sb9.foloke.sectorb9.game.Entities.*;
import android.graphics.*;
import com.sb9.foloke.sectorb9.game.Entities.Buildings.*;
import com.sb9.foloke.sectorb9.game.Assets.*;
import com.sb9.foloke.sectorb9.game.Funtions.PlayerController;
import com.sb9.foloke.sectorb9.game.Managers.GameManager;

import android.graphics.drawable.*;
import com.sb9.foloke.sectorb9.game.UI.TechUIs.*;
import com.sb9.foloke.sectorb9.game.UI.CustomViews.*;
import com.sb9.foloke.sectorb9.game.AI.*;

public class InteractionUI
{

    ///////////////REFACTOR: INIT() UPDATE() COMMAND()

	public static AI.order currentOrder;
    private static Entity pressedEntity;

	public static void init(final MainActivity MA)
    {
        GameLog.update("InteractionUI: init",0);

        final ViewFlipper IVF=MA.findViewById(R.id.interaction_uiViewFlipper);
        IVF.setDisplayedChild(0);

        float scale;

        scale=MA.getGameManager().getScreenSize().y/1600f;

        if(scale>2)
            scale = 2;
        else if(scale<0.5f)
            scale = 0.5f;

        int sizeX=128;
        int sizeY=64;

        final ImageView imageViewInteractionSign=MA.findViewById(R.id.interaction_uiImageSign);
        imageViewInteractionSign.setImageBitmap(Bitmap.createScaledBitmap(UIAsset.interactionSign,(int)(sizeX*2*scale),(int)(sizeY*scale),false));

        GameLog.update("InteractionUI: buttons set",0);
        final Button buttonOpenShip=MA.findViewById(R.id.openShip);
        final ImageButton buttonOpenBuildMenu = MA.findViewById(R.id.openBuildUI);
        final ImageButton buttonCloseInteraction = MA.findViewById(R.id.closeInteraction);
        final ImageButton buttonOpenCargo = MA.findViewById(R.id.openCargo);
        final ImageButton buttonOpenConfig = MA.findViewById(R.id.openConfig);
        final ImageButton buttonOpenProduction = MA.findViewById(R.id.openProduction);
        final ImageButton buttonOpenTechTree = MA.findViewById(R.id.openTechTree);
        final ImageButton buttonOpenShipsConstructor = MA.findViewById(R.id.openConstructor);

        final ImageButton buttonAggressiveBehaviour=MA.findViewById(R.id.interaction_ui_b_behaviour_aggressive);
        final ImageButton buttonDefensiveBehaviour=MA.findViewById(R.id.interaction_ui_b_behaviour_defensive);
        final ImageButton buttonPeacefulBehaviour=MA.findViewById(R.id.interaction_ui_b_behaviour_peaceful);
        final ImageButton buttonRetreatBehaviour=MA.findViewById(R.id.interaction_ui_b_behaviour_retreat);

        final ImageButton buttonMoveToCommand=MA.findViewById(R.id.interaction_ui_b_order_move_to);
        final ImageButton buttonAttackCommand=MA.findViewById(R.id.interaction_ui_b_order_attack);
        final ImageButton buttonFollowCommand=MA.findViewById(R.id.interaction_ui_b_order_follow);
        final ImageButton buttonRepairCommand=MA.findViewById(R.id.interaction_ui_b_order_repair);
        final ImageButton buttonMineCommand=MA.findViewById(R.id.interaction_ui_b_order_mine);
        final ImageButton buttonHoldCommand=MA.findViewById(R.id.interaction_ui_b_order_stay);
        final ImageButton buttonPatrolCommand=MA.findViewById(R.id.interaction_ui_b_order_patrol);

        final ImageButton buttonControlCommand=MA.findViewById(R.id.interaction_ui_b_order_control);

        buttonOpenBuildMenu.setBackground(new BitmapDrawable(MA.getResources(),Bitmap.createScaledBitmap(UIAsset.buildModeButton,(int)(sizeX*scale),(int)(sizeY*scale),false)));
        buttonOpenCargo.setBackground(new BitmapDrawable(MA.getResources(),Bitmap.createScaledBitmap(UIAsset.interactionCargo,(int)(sizeX*scale),(int)(sizeY*scale),false)));
        buttonOpenConfig.setBackground(new BitmapDrawable(MA.getResources(),Bitmap.createScaledBitmap(UIAsset.interactionConfig,(int)(sizeX*scale),(int)(sizeY*scale),false)));
        buttonOpenProduction.setBackground(new BitmapDrawable(MA.getResources(),Bitmap.createScaledBitmap(UIAsset.interactionProduction,(int)(sizeX*scale),(int)(sizeY*scale),false)));
        buttonOpenTechTree.setBackground(new BitmapDrawable(MA.getResources(),Bitmap.createScaledBitmap(UIAsset.interactionTechs,(int)(sizeX*scale),(int)(sizeY*scale),false)));
        buttonOpenShipsConstructor.setBackground(new BitmapDrawable(MA.getResources(),Bitmap.createScaledBitmap(UIAsset.interactionShips,(int)(sizeX*scale),(int)(sizeY*scale),false)));
        buttonCloseInteraction.setBackground(new BitmapDrawable(MA.getResources(),Bitmap.createScaledBitmap(UIAsset.interactionCancel,(int)(sizeY*scale),(int)(sizeY*scale),false)));

        float size=64;

        buttonAggressiveBehaviour.setBackground(new BitmapDrawable(MA.getResources(),Bitmap.createScaledBitmap(UIAsset.AIAggressive,(int)(size*scale),(int)(size*scale),false)));
        buttonDefensiveBehaviour.setBackground(new BitmapDrawable(MA.getResources(),Bitmap.createScaledBitmap(UIAsset.AIDefensive,(int)(size*scale),(int)(size*scale),false)));
        buttonPeacefulBehaviour.setBackground(new BitmapDrawable(MA.getResources(),Bitmap.createScaledBitmap(UIAsset.AIPeaceful,(int)(size*scale),(int)(size*scale),false)));
        buttonRetreatBehaviour.setBackground(new BitmapDrawable(MA.getResources(),Bitmap.createScaledBitmap(UIAsset.AIRetreat,(int)(size*scale),(int)(size*scale),false)));

        buttonMoveToCommand.setBackground(new BitmapDrawable(MA.getResources(),Bitmap.createScaledBitmap(UIAsset.AIMove,(int)(size*scale),(int)(size*scale),false)));
        buttonAttackCommand.setBackground(new BitmapDrawable(MA.getResources(),Bitmap.createScaledBitmap(UIAsset.AIAttack,(int)(size*scale),(int)(size*scale),false)));
        buttonFollowCommand.setBackground(new BitmapDrawable(MA.getResources(),Bitmap.createScaledBitmap(UIAsset.AIFollow,(int)(size*scale),(int)(size*scale),false)));
        buttonRepairCommand.setBackground(new BitmapDrawable(MA.getResources(),Bitmap.createScaledBitmap(UIAsset.AIRepair,(int)(size*scale),(int)(size*scale),false)));
        buttonMineCommand.setBackground(new BitmapDrawable(MA.getResources(),Bitmap.createScaledBitmap(UIAsset.AIMine,(int)(size*scale),(int)(size*scale),false)));
        buttonHoldCommand.setBackground(new BitmapDrawable(MA.getResources(),Bitmap.createScaledBitmap(UIAsset.AIStay,(int)(size*scale),(int)(size*scale),false)));
        buttonPatrolCommand.setBackground(new BitmapDrawable(MA.getResources(),Bitmap.createScaledBitmap(UIAsset.AIPatrol,(int)(size*scale),(int)(size*scale),false)));
        buttonControlCommand.setBackground(new BitmapDrawable(MA.getResources(),Bitmap.createScaledBitmap(UIAsset.AIControl,(int)(size*scale),(int)(size*scale),false)));

        buttonOpenShip.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v) {
                openShipUI(MA);
            }
        });

        buttonOpenBuildMenu.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                openBuildUI(MA);
            }
        });

        buttonOpenShipsConstructor.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                openSpaceDockUI(MA);
            }
        });

        buttonOpenTechTree.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                openTechTreeUI(MA);
            }
        });

        buttonOpenCargo.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                openInventoryUI(MA);
            }
        });

        buttonOpenConfig.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                openConfigUI(MA);
            }
        });

        buttonOpenProduction.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                openAssemblerUI(MA);
            }
        });

        buttonCloseInteraction.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                closeInteractionUI(MA);
            }
        });

        buttonAggressiveBehaviour.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                ((AI)((ControlledShip)pressedEntity).getController()).setCurrentBehaviour(AI.behaviour.AGGRESSIVE);
            }
        });

        buttonDefensiveBehaviour.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                ((AI)((ControlledShip)pressedEntity).getController()).setCurrentBehaviour(AI.behaviour.DEFENSIVE);
            }
        });

        buttonPeacefulBehaviour.setOnClickListener(new OnClickListener(){
            public void onClick(View v) {
                ((AI)((ControlledShip)pressedEntity).getController()).setCurrentBehaviour(AI.behaviour.PEACEFUL);
            }
        });

        buttonRetreatBehaviour.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                ((AI)((ControlledShip)pressedEntity).getController()).setCurrentBehaviour(AI.behaviour.RETREAT);
            }
        });

        //actions for orders
        buttonMoveToCommand.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                MA.getGameManager().currentCommand= GameManager.command.ORDER;
                currentOrder=AI.order.MOVE;
            }
        });

        buttonAttackCommand.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                MA.getGameManager().currentCommand= GameManager.command.ORDER;
                currentOrder=AI.order.ATTACK;
            }
        });

        buttonHoldCommand.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                ((AI)((ControlledShip)pressedEntity).getController()).setCurrentOrder(AI.order.STAY);
            }
        });

        buttonPatrolCommand.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                ((AI)((ControlledShip)pressedEntity).getController()).setCurrentOrder(AI.order.PATROL);
            }
        });

        buttonFollowCommand.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                MA.getGameManager().currentCommand= GameManager.command.ORDER;
                currentOrder=AI.order.FOLLOW;
            }
        });

        buttonMineCommand.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                ((AI)((ControlledShip)pressedEntity).getController()).setCurrentOrder(AI.order.MINE);
            }
        });

        buttonRepairCommand.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                ((AI)((ControlledShip)pressedEntity).getController()).setCurrentOrder(AI.order.REPAIR);
            }
        });

        buttonControlCommand.setOnClickListener(new OnClickListener() {
            public void onClick(View v)
            {
                changeController(MA);
            }
        });

        update(MA,null);

        GameLog.update("InteractionUI: ready",0);
    }

    private static void changeController(MainActivity MA)
    {
        try {
            PlayerController playerController=MA.getGameManager().getController();
            ControlledShip controlled=playerController.getControlledEntity();
            ControlledShip pressed=(ControlledShip)pressedEntity;

            if(controlled!=null) {
                playerController.setControlledEntity(null);
                controlled.setController(new AI(controlled));
                GameLog.update("ai set",3);
            }

            if(controlled!=pressed) {
                playerController.setControlledEntity(pressed);
                pressed.setController(playerController);
                GameLog.update("manual set",3);
            }
            update(MA,pressedEntity);
        }
        catch(Exception e) {
            GameLog.update(e.toString(),1);
        }
    }

    private static void openShipUI(MainActivity MA)
    {
        GameLog.update("InteractionUI: OPEN ShipUI",0);
        final ViewFlipper VF = MA.findViewById(R.id.UIFlipper);
        MA.getGameManager().currentCommand=GameManager.command.INTERACTION;
        ShipUI.init();
        VF.setDisplayedChild(VF.indexOfChild(MA.findViewById(R.id.shipui)));
    }

    private static void closeInteractionUI(MainActivity MA)
    {
        GameLog.update("InteractionUI: CLOSE",0);
        final ViewFlipper VF = MA.findViewById(R.id.UIFlipper);
        MA.getGameManager().currentCommand= GameManager.command.CONTROL;
        MA.getGameManager().nullPressedObjects();
        VF.setDisplayedChild(VF.indexOfChild(MA.findViewById(R.id.actionUI)));
    }

    private static void openBuildUI(MainActivity MA)
    {
        GameLog.update("InteractionUI: OPEN BuildUI",0);
        final ViewFlipper VF = MA.findViewById(R.id.UIFlipper);
        MA.getGameManager().currentCommand=GameManager.command.INTERACTION;
        BuildUI.init(MA);
        MA.getGameManager().nullPressedObjects();
        AssemblerUI.setOpened(false);
        VF.setDisplayedChild(VF.indexOfChild(MA.findViewById(R.id.buildUI)));
    }

    private static void openAssemblerUI(MainActivity MA)
    {
        GameLog.update("InteractionUI: OPEN assemblerUI",0);
        final ViewFlipper IVF=MA.findViewById(R.id.interaction_uiViewFlipper);
        MA.getGameManager().currentCommand=GameManager.command.INTERACTION;
        AssemblerUI.init(MA,(Assembler)pressedEntity);
        MA.getGameManager().initAssemblerUI((Assembler)pressedEntity);
        IVF.setDisplayedChild(IVF.indexOfChild(MA.findViewById(R.id.assemblerUI)));
    }

    private static void openConfigUI(MainActivity MA)
    {
        GameLog.update("InteractionUI: OPEN optionsUI",0);
        final ViewFlipper IVF=MA.findViewById(R.id.interaction_uiViewFlipper);
        MA.getGameManager().currentCommand = GameManager.command.INTERACTION;
        ObjectOptionsUI.init((StaticEntity)pressedEntity,IVF,MA);
        IVF.setDisplayedChild(IVF.indexOfChild(MA.findViewById(R.id.obj_optionsUI)));
    }

    private static void openInventoryUI(MainActivity MA)
    {
        GameLog.update("InteractionUI: OPEN inventoryUI",0);
        final ViewFlipper IVF=MA.findViewById(R.id.interaction_uiViewFlipper);
        MA.getGameManager().currentCommand = GameManager.command.EXCHANGE;
        InventoryUI.setLeftSide(pressedEntity);
        MA.getGameManager().updateInventory(null);
        IVF.setDisplayedChild(IVF.indexOfChild(MA.findViewById(R.id.inventoryUI)));
    }

    private static void openTechTreeUI(MainActivity MA)
    {
        GameLog.update("InteractionUI: OPEN techTreeUI",0);
        final ViewFlipper IVF=MA.findViewById(R.id.interaction_uiViewFlipper);
        MA.getGameManager().currentCommand=GameManager.command.INTERACTION;
        TechUI.updatePositions();
        IVF.setDisplayedChild(IVF.indexOfChild(MA.findViewById(R.id.tech_ui)));
    }

    private static void openSpaceDockUI(MainActivity MA)
    {
        GameLog.update("InteractionUI: OPEN SpaceDockUI",0);
        final ViewFlipper IVF=MA.findViewById(R.id.interaction_uiViewFlipper);
        MA.getGameManager().currentCommand=GameManager.command.INTERACTION;
        ConstructorUI.update((SpaceDock)pressedEntity);
        IVF.setDisplayedChild(IVF.indexOfChild(MA.findViewById(R.id.ship_constructor_ui)));
    }

    public static void update(MainActivity MA,Entity e)
    {
        final ImageButton buttonOpenCargo = MA.findViewById(R.id.openCargo);
        final ImageButton buttonOpenConfig=MA.findViewById(R.id.openConfig);
        final ImageButton buttonOpenProduction=MA.findViewById(R.id.openProduction);
        final ImageButton buttonOpenShipsConstructor=MA.findViewById(R.id.openConstructor);

        MA.findViewById(R.id.interaction_ui_LL_buildOptions).setVisibility(View.VISIBLE);
        MA.findViewById(R.id.interaction_ui_LL_AIOptions).setVisibility(View.GONE);

        buttonOpenProduction.setVisibility(View.GONE);
        buttonOpenShipsConstructor.setVisibility(View.GONE);
        buttonOpenCargo.setVisibility(View.GONE);
        buttonOpenConfig.setVisibility(View.GONE);
        pressedEntity=e;

        if(pressedEntity!=null)
        {
            if (pressedEntity instanceof SpaceDock)
                buttonOpenShipsConstructor.setVisibility(View.VISIBLE);

            if (pressedEntity.getOpened())
                buttonOpenCargo.setVisibility(View.VISIBLE);

            if (pressedEntity.getInteractable() && (pressedEntity instanceof StaticEntity))
                buttonOpenConfig.setVisibility(View.VISIBLE);

            if (pressedEntity instanceof Assembler)
                buttonOpenProduction.setVisibility(View.VISIBLE);

            if (pressedEntity instanceof ControlledShip)
                if (((ControlledShip) pressedEntity).getController() instanceof AI)
                {
                    MA.findViewById(R.id.interaction_ui_LL_AIOptions).setVisibility(View.VISIBLE);
                    MA.findViewById(R.id.interaction_ui_LL_buildOptions).setVisibility(View.GONE);
                }
        }
    }
}
