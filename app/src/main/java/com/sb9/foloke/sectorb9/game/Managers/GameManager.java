package com.sb9.foloke.sectorb9.game.Managers;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.widget.TableLayout;

import com.sb9.foloke.sectorb9.MainActivity;
import com.sb9.foloke.sectorb9.R;
import com.sb9.foloke.sectorb9.game.Assets.EffectsAsset;
import com.sb9.foloke.sectorb9.game.Assets.InventoryAsset;
import com.sb9.foloke.sectorb9.game.Assets.ShipAsset;
import com.sb9.foloke.sectorb9.game.Assets.UIAsset;
import com.sb9.foloke.sectorb9.game.Assets.WeaponsAsset;
import com.sb9.foloke.sectorb9.game.UI.Inventory.InventoryExchangeInterface;
import com.sb9.foloke.sectorb9.game.UI.ProgressBarUI;
import com.sb9.foloke.sectorb9.game.UI.CustomImageUI;
import com.sb9.foloke.sectorb9.game.UI.InventoryUI;
import com.sb9.foloke.sectorb9.game.UI.ObjectOptionsUI;
import com.sb9.foloke.sectorb9.game.DataSheets.BuildingsDataSheet;
import com.sb9.foloke.sectorb9.game.DataSheets.ItemsDataSheet;
import com.sb9.foloke.sectorb9.game.display.Camera;
import com.sb9.foloke.sectorb9.game.display.GamePanel;
import com.sb9.foloke.sectorb9.game.Entities.Buildings.Assembler;
import com.sb9.foloke.sectorb9.game.Entities.Entity;
import com.sb9.foloke.sectorb9.game.Entities.Player;
import com.sb9.foloke.sectorb9.game.Entities.StaticEntity;
import com.sb9.foloke.sectorb9.game.Funtions.Timer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.util.ArrayList;

public class GameManager {

    MainActivity MA;
    GamePanel gamePanel;
    //UIs
    private InventoryUI inventoryUi;
    private InventoryExchangeInterface excInterface;
    public ProgressBarUI uIhp;
    private ObjectOptionsUI objOptionsUI=new ObjectOptionsUI();
    private BuildingsDataSheet buildingsData;
    private ItemsDataSheet itemsData;
    private CustomImageUI destroyedImage;

    //world manager
    private WorldManager worldManager;

    //booleans
    public boolean gamePause=false;
    public boolean playerDestroyed=false;
    public boolean drawDebugInfo=false;
    private Timer destroyedTimer;
    private Player player;


    public int command=0;
    public static final int commandInteraction=1,commandMoving=0;

    public GameManager(GamePanel gamePanel, MainActivity MA)
    {
        this.MA=MA;
        this.gamePanel=gamePanel;

        BitmapFactory.Options bitmapOptions=new BitmapFactory.Options();
        bitmapOptions.inScaled=false;

        InventoryAsset.init(Bitmap.createBitmap(BitmapFactory.decodeResource(MA.getResources(), R.drawable.ui_inventory_sheet,bitmapOptions)));
        ShipAsset.init(Bitmap.createBitmap(BitmapFactory.decodeResource(MA.getResources(),R.drawable.ships_sheet,bitmapOptions)));
        UIAsset.init(Bitmap.createBitmap(BitmapFactory.decodeResource(MA.getResources(),R.drawable.ui_asset_sheet,bitmapOptions)));
        WeaponsAsset.init(Bitmap.createBitmap(BitmapFactory.decodeResource(MA.getResources(),R.drawable.ships_sheet,bitmapOptions)));
        EffectsAsset.init(Bitmap.createBitmap(BitmapFactory.decodeResource(MA.getResources(),R.drawable.ships_sheet,bitmapOptions)));

        buildingsData=new BuildingsDataSheet(MA);
        itemsData=new ItemsDataSheet(MA);
        excInterface=new InventoryExchangeInterface(this);
        uIhp=new ProgressBarUI(this,gamePanel.canvasW/3,gamePanel.canvasH/20,50,50,UIAsset.hpBackground,UIAsset.hpLine,UIAsset.progressBarBorder,100);
        destroyedImage=new CustomImageUI(UIAsset.destroyedText);

        player=new Player(900,900,0,this,"player");

        worldManager=new WorldManager(MA,this);

        worldManager.loadDebugWorld();
    }

    public void tick()
    {
        if(gamePause)
            return;
        if(playerDestroyed)
        {
            if(destroyedTimer.tick())
            {
                destroyedTimer=null;
                createNewPlayer();
            }
            return;
        }


        player.addMovement(gamePanel.screenPointOfTouch,gamePanel.canvasW,gamePanel.canvasH);
        player.RotationToPoint(gamePanel.pointOfTouch);
        player.tick();

        uIhp.tick(player.getHp());

        worldManager.updateWorld();
    }
    public void render(Canvas canvas)
    {
        worldManager.renderWorld(canvas);
        player.render(canvas);
        if(drawDebugInfo)
        player.drawVelocity(canvas);
    }
    public void interactionCheck(float x,float y)
    {
        worldManager.interactionCheck(x,y);
    }
    public void setPlayerDestroyed(boolean condition)
    {
        playerDestroyed=condition;
        destroyedTimer=new Timer(2);
        gamePause=condition;
    }
    public void createNewPlayer()
    {
        player=null;
        gamePanel.getCamera().setPointOfLook(null);
        player=new Player(900,900,0,this,"player");
        playerDestroyed=false;
        getCamera().setPointOfLook(player);
        gamePause=false;
    }

    public ArrayList<Entity> getEntities()
    {
        return worldManager.getEntityManager().getArray();
    }


    public Player getPlayer()
    {
        return player;
    }

    public InventoryUI getInventoryUi()
    {
        return inventoryUi;
    }

    public void makeInventoryUI(TableLayout playerTable, TableLayout objectTable, MainActivity context)
    {

        inventoryUi=new InventoryUI(playerTable,player,objectTable,null,excInterface,context);
    }




    public void updateInventory(final Entity caller)
    {
        MA.runOnUiThread(new Runnable(){
            public void run()
            {
                inventoryUi.update(caller);
            }
        });
    }


    public void setPause(boolean state)
    {
        gamePause=state;
    }

    public ObjectOptionsUI getObjOptions()
    {
        return objOptionsUI;
    }

    public void save(BufferedWriter w)
    {
        worldManager.save(w);
    }
    public void load(BufferedReader r)
    {
        worldManager.load(r);
    }

    public void nullPressedObject()
    {
        gamePanel.pressedObject=null;
    }

    public void setPressedObject(StaticEntity pressedObject)
    {
        gamePanel.pressedObject=pressedObject;
    }

    public PointF getTouchPoint()
    {
        return gamePanel.pointOfTouch;
    }

    public void initAssemblerUI(final Assembler assembler)
    {
        MA.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                MA.assemblerUIi.init(MA,assembler);
            }});
    }
    public MainActivity getMainActivity()
    {
        return MA;
    }

    public Camera getCamera()
    {
        return gamePanel.getCamera();
    }

    public void addObject(Entity e)
    {
        worldManager.addObject(e);
    }
}
