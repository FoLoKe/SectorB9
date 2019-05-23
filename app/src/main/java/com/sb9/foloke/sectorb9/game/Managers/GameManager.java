package com.sb9.foloke.sectorb9.game.Managers;

import android.content.*;
import android.graphics.*;
import android.os.*;
import android.view.*;
import com.sb9.foloke.sectorb9.*;
import com.sb9.foloke.sectorb9.game.AI.*;
import com.sb9.foloke.sectorb9.game.Assets.*;
import com.sb9.foloke.sectorb9.game.DataSheets.*;
import com.sb9.foloke.sectorb9.game.Display.*;
import com.sb9.foloke.sectorb9.game.Entities.*;
import com.sb9.foloke.sectorb9.game.Entities.Buildings.*;
import com.sb9.foloke.sectorb9.game.Entities.Ships.*;
import com.sb9.foloke.sectorb9.game.Funtions.*;
import com.sb9.foloke.sectorb9.game.UI.*;
import com.sb9.foloke.sectorb9.game.UI.CustomViews.*;
import java.io.*;
import java.util.*;

import com.sb9.foloke.sectorb9.game.Display.Camera;
import com.sb9.foloke.sectorb9.game.Entities.Entity;
import com.sb9.foloke.sectorb9.game.Funtions.Timer;


public class GameManager {
	private MainThread mainThread;
    private MainActivity MA;
    private GamePanel gamePanel;
	private String saveName="0";

    //UIs not views 
    
    public static ProgressBarUI uIhp;
    public static ProgressBarUI uIsh;

    //world manager
    private WorldManager worldManager;
	private MapManager mapManager;

    //booleans
    public boolean isPaused=true;
    public boolean isLoading=false;
	public boolean isInMenu=false;
	
    private boolean collect=false;
    private boolean warpReady=false;

    private Timer destroyedTimer;
   
	private PlayerController playerController;
	public static Joystick joystick;
	public static PointF joystickTouchPoint=new PointF();
	
	
    
    public enum command{INTERACTION,CONTROL,ORDER,EXCHANGE};
	public  command currentCommand;
	
	private Point sectorToWarp=new Point();
	private PointF warpingLocation=new PointF();
    private  Point screenSize=new Point();
	
	
    public GameManager( MainActivity MA)
    {
        this.MA=MA;
        Options.startupOptions();
    }

    public void init(boolean state,String saveName)
    {
        GameLog.update("GameManager: preparing game",0);
        setSaveName(saveName);
        currentCommand=command.CONTROL;
        destroyedTimer=new Timer(0);
        BitmapFactory.Options bitmapOptions=new BitmapFactory.Options();
        bitmapOptions.inScaled=false;

        GameLog.update("GameManager: preparing assets",0);
        InventoryAsset.init(Bitmap.createBitmap(BitmapFactory.decodeResource(MA.getResources(), R.drawable.ui_inventory_sheet,bitmapOptions)));
        ShipAsset.init(Bitmap.createBitmap(BitmapFactory.decodeResource(MA.getResources(),R.drawable.ships_sheet,bitmapOptions)));
        UIAsset.init(Bitmap.createBitmap(BitmapFactory.decodeResource(MA.getResources(),R.drawable.ui_asset_sheet,bitmapOptions)));
        WeaponsAsset.init(Bitmap.createBitmap(BitmapFactory.decodeResource(MA.getResources(),R.drawable.ships_sheet,bitmapOptions)));
        EffectsAsset.init(Bitmap.createBitmap(BitmapFactory.decodeResource(MA.getResources(),R.drawable.ships_sheet,bitmapOptions)));
		
        GameLog.update("GameManager: preparing datasheets",0);
        ObjectsDataSheet.init(MA);
        ItemsDataSheet.init(MA);
		ModulesDataSheet.init(MA);
      	TechTree.init();
		

        WindowManager wm = ((WindowManager)
                MA.getSystemService(Context.WINDOW_SERVICE));
        Display display = wm.getDefaultDisplay();

        if(display!=null) {

            screenSize = new Point();
            display.getRealSize(screenSize);
        }
		joystick=new Joystick(new RectF(screenSize.x/2f,screenSize.y/2f,screenSize.x,screenSize.y));
		
        GameLog.update("GameManager: preparing action UI",0);
        uIhp=new ProgressBarUI(this,screenSize.x/3f,screenSize.y/20f,50,50,UIAsset.hpBackground,UIAsset.hpLine,UIAsset.progressBarBorder,100);
        uIsh=new ProgressBarUI(this,screenSize.x/3f,screenSize.y/40f,50,50+screenSize.y/20f,UIAsset.stunBackground,UIAsset.stunLine,UIAsset.progressBarBorder,100);

        GameLog.update("GameManager: preparing player controller",0);    
		playerController=new PlayerController();
		//player=new ControlledShip(0,0,0,this,Ship.createSimple());
		//player.setController(new PlayerController(player));
		
        GameLog.update("GameManager: preparing managers",0);
        mapManager=new MapManager();
        worldManager=new WorldManager(MA,this);
        worldManager.loadEmptyWorld();
		SaveManager.init(this);

        GameLog.update("GameManager: preparing canvas",0);
        
		//ONLY UI view OBJECT
		GameLog.update("GameManager: content set",0);
		MA.setContentView(R.layout.main_activity);
        this.gamePanel=MA.findViewById(R.id.Game);

        

        if(state)
        {
            ///new game state
            GameLog.update("GameManager: creating saves",0);
            SaveManager.createSaveFile();
            WorldGenerator.makeRandomSector(getWorldManager());
			ControlledShip startShip=new ControlledShip(0,0,0,this,Ship.createSimple());
			getEntityManager().addObject(startShip);
			playerController.setControlledEntity(startShip);
			startShip.setController(playerController);
			for(int i=0;i<startShip.getInventory().getCapacity();i++)
			{
				startShip.getInventory().addNewItem(i,64);
			}
        }
        else
            ///load state
            SaveManager.load();
		
        GameLog.update("GameManager: READY",0);
		mainThread= new MainThread(this);
		mainThread.setRunning(true);
        mainThread.start();
        
    }

    public void tick()
    {
		gamePanel.tick();
		worldManager.updateWorld();
		
        if(isLoading)
		{
			isPaused=true;
			LoadingScreen.tick();
            return;
		}
		
		if(isInMenu)
		{
			isPaused=true;
		}
		joystickTouchPoint.set(joystick.getPoint().x+gamePanel.getCamera().getWorldLocation().x,joystick.getPoint().y+gamePanel.getCamera().getWorldLocation().y);
		joystick.tick(gamePanel.getPointOfTouch());
		
		playerController.tick();
		
		
			
		if(playerController.getControlledEntity()!=null)
		{
		Iterator<Entity> iterUi = getEntities().iterator();
		boolean exist=false;
		while (iterUi.hasNext()) 
		{
			Entity e =  iterUi.next();
			if (e instanceof DroppedItems)
				if (distanceTo(playerController.getControlledEntity().getWorldLocation(), e.getWorldLocation()) < 200) 
				{
					exist=true;
					break;
				}
		}
		if(MA!=null)
		ActionUI.update(exist);
		
		if(collect)
        {
            boolean collected=true;
            Iterator<Entity> iter = getEntities().iterator();
            while (iter.hasNext()) 
			{
                Entity e = iter.next();
                if (e instanceof DroppedItems)
                    if (distanceTo(playerController.getControlledEntity().getWorldLocation(), e.getWorldLocation()) < 200) 
					{
                        boolean b = playerController.getControlledEntity().getInventory().collectFromInventory(e);
                        if(!b)
                            collected=false;
                    }
            }
			
            if (collected)
            {         
                GameLog.update("GameManager: items collected",0);     
            }
            else
                GameLog.update("GameManager: inventory full" ,0);
    	}
	
        	collect=false;
		}
        
		
		if(warpReady)
			if(distanceTo(playerController.getControlledEntity().getWorldLocation(),warpingLocation)<200)
				warp();
    }

    public void render(Canvas canvas)
    {	
		if(canvas==null)
			return;
		gamePanel.preRender(canvas);
        worldManager.renderWorld(canvas);
     
		
		Paint debugPaint= new Paint();
		debugPaint.setColor(Color.CYAN);
		debugPaint.setStyle(Paint.Style.STROKE);
		debugPaint.setStrokeWidth(10);
		if(Options.drawDebugInfo.getBoolean())
		canvas.drawCircle(joystickTouchPoint.x,joystickTouchPoint.y,5,debugPaint);
		
		if(warpReady)
       	canvas.drawCircle(warpingLocation.x,warpingLocation.y,200,debugPaint);
		
		gamePanel.postRender(canvas);
		
		gamePanel.drawPlayerMovement(canvas);
		if(Options.drawRadio.getValue()==1)
		gamePanel.drawRadioPoints(canvas);
		
		if(currentCommand==command.CONTROL) 
		{
            uIhp.render(canvas);
            uIsh.render(canvas);
        }
		
		joystick.render(canvas);
		
		if(Options.drawDebugInfo.getBoolean())
		canvas.drawRect(joystick.getActionZone(),debugPaint);
		
		Paint p=new Paint();
		p.setColor(Color.GREEN);
		p.setTextSize(90);
		if(isPaused)
		{
			
			canvas.drawText("PAUSE...",screenSize.x/2,screenSize.y/2,p);
		}
		if(isLoading)
		{
			LoadingScreen.render(canvas);
		}
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
		try
		{
        	worldManager.interactionCheck(x,y);
		}
		catch(Exception e)
		{
			GameLog.update(e.toString(),1);
		}
    }

    public ArrayList<Entity> getEntities()
    {
        return worldManager.getEntityManager().getArray();
    }

    public ControlledShip getPlayer()
    {
        return playerController.getControlledEntity();
    }
	
	public PlayerController getController()
	{
		return playerController;
	}

    public void updateInventory(final Entity caller)
    {GameLog.update("GameManager: update Inventory UI",0);
        MA.runOnUiThread(new Runnable(){
            public void run()
            {
                InventoryUI.update(caller);
            }
        });
    }

    public void setPause(boolean state)
    {
        isPaused=state;
    }

	public boolean getPause()
	{
		return	isPaused;
	}
   

    public void nullPressedObject()
    {
        gamePanel.pressedObject=null;
    }

    

    public void initAssemblerUI(final Assembler assembler)
    {
		GameLog.update("GameManager: init. Assembler UI",0);
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

    Camera getCamera()
    {
        return gamePanel.getCamera();
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
	
	private void warp()
	{
		warpReady=false;
		worldManager.warpToSector(sectorToWarp.x,sectorToWarp.y);
		playerController.getControlledEntity().setWorldLocation(new PointF(3000-warpingLocation.x,3000-warpingLocation.y));
	}
	
	public String getSaveName()
	{
		return saveName;
	}
	
	private void setSaveName(String s)
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
	
	
	
	public void collectDebris()
	{
	    collect=true;
	}
	
	private float distanceTo(PointF a,PointF b)
	{
		return (float)Math.sqrt((b.x-a.x)*(b.x-a.x)+(b.y-a.y)*(b.y-a.y));
	}

	public EntityManager getEntityManager()
	{
		return worldManager.getEntityManager();
	}
	
	public MapManager getMapManager()
	{
		return mapManager;
	}
	
	public static boolean loadSector(int x,int y)
	{
		return SaveManager.loadSector(x,y);
	}

    public void saveGame()
	{
		SaveManager.save();
		
	}
	
	
    public Point getScreenSize()
    {
        return screenSize;
    }
	
	public void shutdown()
	{
		GameLog.update("GameManager: shutdown",0);
		boolean retry = true;
        while(retry)
        {
            try{mainThread.setRunning(false);
                mainThread.join();
                retry=false;
				//mainThread.stop();
            }catch(InterruptedException e)
            {GameLog.update("GameManager: "+e.toString(),1);}
        }
		GameLog.update("GameManager: thread stopped",0);
	}

	public void resume()
	{
		GameLog.update("GameManager: resume",0);
		if(mainThread==null)
			return;
		//mainThread= new MainThread(this);
		mainThread.setRunning(true);
        mainThread.start();
        setPause(false);
		GameLog.update("GameManager: thread in progress",0);
	}
	
	public void checkJoystick(boolean touched,PointF screenPoint)
	{
		joystick.setTouched(touched,screenPoint);
	}
	
	public void interactionTouch(Entity e,PointF p)
	{
		
		switch(currentCommand)
		{
			case INTERACTION:
				getGamePanel().pressedObject=e;
				InteractionUI.init(MA,e);
				GameLog.update("interaction touch",2);
				break;
			case EXCHANGE:
				GameLog.update("exhange touch",2);
				InventoryUI.setRightSide(e);
				break;
			case ORDER:
				GameLog.update("order touch",2);
				switch(InteractionUI.currentOrder)
				{
					case MOVETO:
						((AI)((ControlledShip)getGamePanel().pressedObject).getController()).setDestination(p);
						((AI)((ControlledShip)getGamePanel().pressedObject).getController()).setCurrentOrder(AI.order.MOVETO);
						break;
					case ATTACK:
						((AI)((ControlledShip)getGamePanel().pressedObject).getController()).setTargetToAttack(e);
						((AI)((ControlledShip)getGamePanel().pressedObject).getController()).setCurrentOrder(AI.order.ATTACK);
						break;
					case FOLLOW:
						((AI)((ControlledShip)getGamePanel().pressedObject).getController()).setTargetToFollow(e);
						((AI)((ControlledShip)getGamePanel().pressedObject).getController()).setCurrentOrder(AI.order.FOLLOW);
						break;
				}
				currentCommand=command.INTERACTION;
				break;
		}
	}
}
