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
import java.util.Iterator;

import com.sb9.foloke.sectorb9.game.UI.*;
import com.sb9.foloke.sectorb9.game.Entities.*;
import android.graphics.*;


public class GameManager {

    MainActivity MA;
    GamePanel gamePanel;
	String saveName="0";
    //UIs
    
    private InventoryExchangeInterface excInterface;
    public ProgressBarUI uIhp;
    public ProgressBarUI uIsh;
    
    private BuildingsDataSheet buildingsData;
    private ItemsDataSheet itemsData;
    private CustomImageUI destroyedImage;

    //world manager
    private WorldManager worldManager;

    //booleans
    public boolean gamePause=false;
    public boolean playerDestroyed=false;
    //public boolean drawDebugInfo=false;
    private Timer destroyedTimer;
    private Player player;
    private boolean collect=false;

    public int command;
    public static final int commandInteraction=1,commandMoving=0,commandBuild=2;
	private Point sectorToWarp=new Point();
	private PointF warpingLocation=new PointF();
	private boolean warpReady=false;
	
    public GameManager(GamePanel gamePanel, MainActivity MA)
    {
        command=0;
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
        uIsh=new ProgressBarUI(this,gamePanel.canvasW/3,gamePanel.canvasH/40,50,50+gamePanel.canvasH/20,UIAsset.stunBackground,UIAsset.stunLine,UIAsset.progressBarBorder,100);
        destroyedImage=new CustomImageUI(UIAsset.destroyedText);

        player=new Player(900,900,0,this);

        worldManager=new WorldManager(MA,this);
		worldManager.loadEmptyWorld();
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
			uIhp.set(player.getHp()/player.getMaxHP()*100);
			uIsh.set(player.getSH()/player.getMaxSH()*100);
			if(gamePanel.getTouched())
			{
				player.rotationToPoint(getGamePanel().getMovementPoint());
				float targetAcceleration=gamePanel.getMovementSpeed();
				
				if(targetAcceleration>1)
					targetAcceleration=1;
				if(targetAcceleration<0)
					targetAcceleration=0;
     			player.addMovement(targetAcceleration);
       		}

		}
		
		Iterator<Entity> iterUi = getEntities().iterator();
		boolean exist=false;
		while (iterUi.hasNext()) 
		{
			Entity e = (Entity) iterUi.next();
			if (e instanceof DroppedItems)
				if (distanceTo(player.getWorldLocation(), e.getWorldLocation()) < 200) 
				{
					exist=true;
					break;
				}
		}
		ActionUI.update(exist);
		
		if(collect)
        {
            boolean collected=true;
            Iterator<Entity> iter = getEntities().iterator();
            while (iter.hasNext()) 
			{
                Entity e = (Entity) iter.next();
                if (e instanceof DroppedItems)
                    if (distanceTo(player.getWorldLocation(), e.getWorldLocation()) < 200) 
					{
                        boolean b = player.getInventory().collectFromInventory(e);
                        if(!b)
                            collected=false;
                    }
            }
			
            if (collected)
            {         
                MA.makeToast("items collected",0);     
            }
            else
                MA.makeToast("inventory full" ,0);
    	}
	
        collect=false;
        worldManager.updateWorld();
		
		if(warpReady)
			if(distanceTo(player.getWorldLocation(),warpingLocation)<200)
				warp();
    }

    public void render(Canvas canvas)
    {
        worldManager.renderWorld(canvas);
        player.render(canvas);
		Paint debugPaint= new Paint();
		debugPaint.setColor(Color.CYAN);
		debugPaint.setStyle(Paint.Style.STROKE);
		debugPaint.setStrokeWidth(10);
		if(warpReady)
       	canvas.drawCircle(warpingLocation.x,warpingLocation.y,200,debugPaint);
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

    public void warpToLocation(int xs,int ys)
    {
		sectorToWarp.set(xs,ys);
		Point offset=new Point(worldManager.getSector().x-xs,worldManager.getSector().y-ys);
		float dist=(float)Math.sqrt(offset.x*offset.x+offset.y*offset.y);
		PointF vector=new PointF(offset.x/dist,offset.y/dist);
		PointF tvector=new PointF(-vector.x*3000,-vector.y*3000);
		warpingLocation.set((tvector.x+3000)/2,(tvector.y+3000)/2);
		
		warpReady=true;
    }
	
	public void warp()
	{
		warpReady=false;
		worldManager.warpToSector(sectorToWarp.x,sectorToWarp.y);
		player.setWorldLocation(new PointF(3000-warpingLocation.x,3000-warpingLocation.y));
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
	
	public void collectDebris()
	{
	    collect=true;
	}
	
	public float distanceTo(PointF a,PointF b)
	{
		return (float)Math.sqrt((b.x-a.x)*(b.x-a.x)+(b.y-a.y)*(b.y-a.y));
	}
	public EntityManager getEntityManager()
	{
		return worldManager.getEntityManager();
	}
}
