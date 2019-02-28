package com.sb9.foloke.sectorb9.game.Managers;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
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
import com.sb9.foloke.sectorb9.game.Display.Camera;
import com.sb9.foloke.sectorb9.game.Display.GamePanel;
import com.sb9.foloke.sectorb9.game.Entities.Buildings.Assembler;
import com.sb9.foloke.sectorb9.game.Entities.Entity;
import com.sb9.foloke.sectorb9.game.Entities.Player;
import com.sb9.foloke.sectorb9.game.Entities.StaticEntity;
import com.sb9.foloke.sectorb9.game.Funtions.Timer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.util.ArrayList;
import com.sb9.foloke.sectorb9.game.UI.*;
import org.apache.http.util.*;

public class GameManager {

    MainActivity MA;
    GamePanel gamePanel;
	String saveName="0";
    //UIs
    
    private InventoryExchangeInterface excInterface;
    public ProgressBarUI uIhp;
    
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
		destroyedTimer=new Timer(0);
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

        player=new Player(900,900,0,this);

        worldManager=new WorldManager(MA,this);
		worldManager.lodEmptyWorld();
     	//worldManager.loadDebugWorld();
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
		if(player!=null)
		{
		player.rotationToPoint(gamePanel.pointOfTouch);
		
			float minAcceleration=gamePanel.getHeight()/8/gamePanel.getCamera().getScale(); //0%
			float maxAcceleration=gamePanel.getHeight()/2/gamePanel.getCamera().getScale(); //100%
			
			PointF relativePoint=new PointF(player.getCenterX()-gamePanel.pointOfTouch.x,player.getCenterY()-gamePanel.pointOfTouch.y);
			float vectorLenght=(float)Math.sqrt(relativePoint.x*relativePoint.x+relativePoint.y*relativePoint.y);
			float targetAcceleration=(vectorLenght-minAcceleration)/(maxAcceleration-minAcceleration);
			if(targetAcceleration>1)
				targetAcceleration=1;
        player.addMovement(targetAcceleration);
       

        uIhp.set(player.getHp());
		}
        worldManager.updateWorld();
    }

    public void render(Canvas canvas)
    {
        worldManager.renderWorld(canvas);
        player.render(canvas);
        if(drawDebugInfo)
        player.drawVelocity(canvas);
    }
	public void spawnDestroyed(Entity e)
	{
		worldManager.spawnDestroyed(e);
	}
    public GamePanel getGamePanel() {
        return gamePanel;
    }

    public void interactionCheck(float x, float y)
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
        player=new Player(900,900,0,this);
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

    
    public void makeInventoryUI(TableLayout playerTable, TableLayout objectTable, MainActivity context)
    {

        InventoryUI.set(playerTable,player,objectTable,null,excInterface,context);
    }

    public void updateInventory(final Entity caller)
    {
        MA.runOnUiThread(new Runnable(){
            public void run()
            {
                InventoryUI.update(caller);
            }
        });
    }

    public void setPause(boolean state)
    {
        gamePause=state;
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

                AssemblerUI.init(MA,assembler);
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

    public Point getCurrentSector()
    {
        return worldManager.getSector();
    }

    public void warpToLocation(int x,int y)
    {

        worldManager.warpToSector(x,y);
    }
	
	public String getSaveName()
	{
		return saveName;
	}
	
	public void setSaveName(String s)
	{
		saveName=s;
	}
	public WorldManager getWorldManager()
	{
		return worldManager;
	}
	
	public Entity createBuildable(int id,Entity initiator)
	{
		return worldManager.getEntityManager().createBuildable(id,initiator);
	}
	
	public void onPlayerDestroyed()
	{
		destroyedTimer.setTimer(5);
		player.respawn();
		gamePanel.getCamera().setPointOfLook(player);
		
	}
}
