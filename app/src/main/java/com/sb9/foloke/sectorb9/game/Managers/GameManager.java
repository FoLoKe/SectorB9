package com.sb9.foloke.sectorb9.game.Managers;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.PointF;
import android.view.Display;
import android.view.WindowManager;
import android.widget.TableLayout;

import com.sb9.foloke.sectorb9.MainActivity;
import com.sb9.foloke.sectorb9.R;
import com.sb9.foloke.sectorb9.game.Assets.EffectsAsset;
import com.sb9.foloke.sectorb9.game.Assets.InventoryAsset;
import com.sb9.foloke.sectorb9.game.Assets.ShipAsset;
import com.sb9.foloke.sectorb9.game.Assets.UIAsset;
import com.sb9.foloke.sectorb9.game.Assets.WeaponsAsset;
import com.sb9.foloke.sectorb9.game.Funtions.Options;
import com.sb9.foloke.sectorb9.game.Funtions.WorldGenerator;
import com.sb9.foloke.sectorb9.game.UI.Inventory.InventoryExchangeInterface;
import com.sb9.foloke.sectorb9.game.UI.ProgressBarUI;
import com.sb9.foloke.sectorb9.game.UI.InventoryUI;
import com.sb9.foloke.sectorb9.game.DataSheets.ObjectsDataSheet;
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
import com.sb9.foloke.sectorb9.game.UI.CustomViews.*;
import android.os.*;
import android.widget.ViewFlipper;

import java.io.*;
import com.sb9.foloke.sectorb9.*;
import com.sb9.foloke.sectorb9.game.Funtions.*;
import com.sb9.foloke.sectorb9.game.Entities.Buildings.*;
import com.sb9.foloke.sectorb9.game.UI.TechUIs.*;
import com.sb9.foloke.sectorb9.game.DataSheets.*;


public class GameManager {
	private MainThread mainThread;
    private MainActivity MA;
    private GamePanel gamePanel;
	private String saveName="0";

    //UIs not views 
    
    public ProgressBarUI uIhp;
    public ProgressBarUI uIsh;

    //world manager
    private WorldManager worldManager;
	private MapManager mapManager;

    //booleans
    public boolean gamePause=true;
    private boolean playerDestroyed=false;
    private boolean collect=false;
    private boolean warpReady=false;

    private Timer destroyedTimer;
    private Player player;


    public int command;
    public static final int commandInteraction=1,commandMoving=0;
	private Point sectorToWarp=new Point();
	private PointF warpingLocation=new PointF();
    private  Point screenSize=new Point();
	public Joystick joystick;
	private PointF joystickTouchPoint=new PointF();
	
    public GameManager( MainActivity MA)
    {
        this.MA=MA;
        Options.startupOptions();
    }

    public void init(boolean state,String saveName)
    {
        GameLog.update("GameManager: preparing game",0);
        setSaveName(saveName);
        command=0;
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

        GameLog.update("GameManager: preparing action UI",0);
        uIhp=new ProgressBarUI(this,screenSize.x/3f,screenSize.y/20f,50,50,UIAsset.hpBackground,UIAsset.hpLine,UIAsset.progressBarBorder,100);
        uIsh=new ProgressBarUI(this,screenSize.x/3f,screenSize.y/40f,50,50+screenSize.y/20f,UIAsset.stunBackground,UIAsset.stunLine,UIAsset.progressBarBorder,100);

        GameLog.update("GameManager: preparing player",0);
        player=new Player(900,900,0,this);

        GameLog.update("GameManager: preparing managers",0);
        mapManager=new MapManager();
        worldManager=new WorldManager(MA,this);
        worldManager.loadEmptyWorld();

        GameLog.update("GameManager: preparing canvas",0);
        
		//ONLY UI view OBJECT
		GameLog.update("GameManager: content set",0);
		MA.setContentView(R.layout.main_activity);
        this.gamePanel=MA.findViewById(R.id.Game);

        

        if(state)
        {
            ///new game state
            GameLog.update("GameManager: creating saves",0);
            createSaveFile();
            WorldGenerator.makeRandomSector(getWorldManager());
        }
        else
            ///load state
            loadGame();
		joystick=new Joystick(new RectF(screenSize.x/1.5f,screenSize.y/2,screenSize.x,screenSize.y));
        GameLog.update("GameManager: READY",0);
		mainThread= new MainThread(this);
		mainThread.setRunning(true);
        mainThread.start();
        setPause(false);
    }

    public void tick()
    {
		gamePanel.tick();
        if(gamePause)
            return;
		joystickTouchPoint.set(joystick.getPoint().x+gamePanel.getCamera().getWorldLocation().x,joystick.getPoint().y+gamePanel.getCamera().getWorldLocation().y);
		joystick.tick(gamePanel.getPointOfTouch());
		
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
			player.setMovable(joystick.getTouched());
			
			if(joystick.getTouched())
			{
				
				player.rotationToPoint(joystickTouchPoint);
				float targetAcceleration=joystick.getAcceleration();
				
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
			Entity e =  iterUi.next();
			if (e instanceof DroppedItems)
				if (distanceTo(player.getWorldLocation(), e.getWorldLocation()) < 200) 
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
                    if (distanceTo(player.getWorldLocation(), e.getWorldLocation()) < 200) 
					{
                        boolean b = player.getInventory().collectFromInventory(e);
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
        worldManager.updateWorld();
		
		if(warpReady)
			if(distanceTo(player.getWorldLocation(),warpingLocation)<200)
				warp();
    }

    public void render(Canvas canvas)
    {	
		if(canvas==null)
			return;
		gamePanel.preRender(canvas);
        worldManager.renderWorld(canvas);
        player.render(canvas);
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
		
		if(command==commandMoving) 
		{
            uIhp.render(canvas);
            uIsh.render(canvas);
        }
		
		joystick.render(canvas);
		
		if(Options.drawDebugInfo.getBoolean())
		canvas.drawRect(joystick.getActionZone(),debugPaint);
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

    private void createNewPlayer()
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

    void setPressedObject(StaticEntity pressedObject)
    {
        gamePanel.pressedObject=pressedObject;
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
		player.setWorldLocation(new PointF(3000-warpingLocation.x,3000-warpingLocation.y));
	}
	
	private String getSaveName()
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
	
	public void onPlayerDestroyed()
	{
		destroyedTimer.setTimer(5);
		SpaceDock sd=(SpaceDock)worldManager.getEntityManager().findRespawnPoint(player.getTeam());
		if(sd!=null)
		player.respawn(sd);
		else
		{
			GameLog.update("respawning in homeworld",2);
			loadSector(1,1);
			sd=(SpaceDock)worldManager.getEntityManager().findRespawnPoint(player.getTeam());
			if(sd!=null)
			player.respawn(sd);
			else
			{
				GameLog.update("no respawn point",2);
				player.forceRespawn();
			}
		}
		gamePanel.getCamera().setPointOfLook(player);
		
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
	
	boolean loadSector(int x,int y) {

        worldManager.setSector(x,y);
        //set new sector in meta
        saveMeta();

        //load sector with new meta
        boolean state=loadGame();
        return state;
    }

    private  void saveMeta()
    {
        try {
            File saveFolder = checkSaveFolder();
            if (saveFolder != null) {
                File meta = new File(saveFolder, "meta.txt");
                if (meta.exists())
                    if (!meta.delete()) {
                        GameLog.update("GameManager: meta deleting error", 1);
                        return;
                    }
                FileOutputStream mots = new FileOutputStream(meta);
                OutputStreamWriter mos = new OutputStreamWriter(mots);
                BufferedWriter metaWriter = new BufferedWriter(mos);

                metaWriter.write("" + worldManager.getSector().x);
                metaWriter.newLine();
                metaWriter.write("" + worldManager.getSector().y);
                metaWriter.newLine();
                metaWriter.write("" + getPlayer().getHp());
                metaWriter.newLine();
                metaWriter.write("" + getPlayer().getSH());
                metaWriter.newLine();
                metaWriter.write("" + getPlayer().getCenterX());
                metaWriter.newLine();
                metaWriter.write("" + getPlayer().getCenterY());
                metaWriter.newLine();
				metaWriter.write("" + getPlayer().getWorldRotation());
                metaWriter.newLine();
				metaWriter.write("" + getPlayer().getInvSaveString());
				metaWriter.newLine();
                metaWriter.close();
                mos.close();
                mots.close();
            }
        }
        catch (Exception e)
        {
            GameLog.update("GameManager: "+e.toString(),1);
        }
    }

    private void loadMeta()
    {
        try {
            File saveFolder = checkSaveFolder();
            if (saveFolder != null) {
                //TO Read meta
                File meta = new File(saveFolder, "meta.txt");
                if (!meta.exists()) {
                    GameLog.update("GameManger: meta did not exist", 1);
                    return;
                }

                FileInputStream mits = new FileInputStream(meta);
                InputStreamReader mis = new InputStreamReader(mits);
                BufferedReader metareader = new BufferedReader(mis);

                int x = Integer.parseInt(metareader.readLine());
                int y = Integer.parseInt(metareader.readLine());
				worldManager.setSector(x, y);
				
                player.setHP(Float.parseFloat(metareader.readLine()));
                player.setSH(Float.parseFloat(metareader.readLine()));
                
                player.setWorldLocation(new PointF(Float.parseFloat(metareader.readLine()),Float.parseFloat(metareader.readLine())));
				player.setWorldRotation(Float.parseFloat(metareader.readLine()));
				player.LoadInvFromString(metareader.readLine());
                metareader.close();
                mis.close();
                mits.close();
                //Meta readed
				GameLog.update("GameManager: meta loaded",0);
            }
        }
        catch (Exception e)
        {
            GameLog.update("GameManager: "+e.toString(),1);
        }
    }
		
	private boolean loadGame()
	{
		try
		{
			GameLog.update("GameManager: starting game load",0);

			getEntityManager().reload();
			getEntityManager().addObject(player);
			File saveFolder=checkSaveFolder();
            if(saveFolder!=null)
            {
                File mapFile = new File (saveFolder,"map.txt");
                if (mapFile.exists ())
                {GameLog.update("GameManager: starting load meta",0);
					
					loadMeta();
					//TO read map
					FileInputStream ins=new FileInputStream(mapFile);
					InputStreamReader isr=new InputStreamReader(ins);
					BufferedReader reader=new BufferedReader(isr);

					String s;

					//while there rows
					GameLog.update("GameManager: reading lines for "+worldManager.getSector(),0);
					while((s=reader.readLine())!=null)
					{
						//if new sector
						if(s.startsWith("<"))
						{
							String[] words;
							s=s.replace("<","");
							s=s.replace(">","");
							words=s.split(" ");
							int sX=Integer.parseInt(words[1]);
							int sY=Integer.parseInt(words[2]);
							mapManager.getSector(sX,sY).discovered=Boolean.parseBoolean(words[3]);
							mapManager.getSector(sX,sY).explored=Boolean.parseBoolean(words[4]);
							//equals loading one?
							if(Integer.parseInt(words[1])==worldManager.getSector().x &&
							   Integer.parseInt(words[2])==worldManager.getSector().y)
							{
							//explored?
								GameLog.update("GameManager: sector founded "+worldManager.getSector(),0);
                                if(!Boolean.parseBoolean(words[4]))
                                    return false;
								MapManager.Sector sector=mapManager.getSector(worldManager.getSector().x,worldManager.getSector().y);
								GameLog.update("GameManger: "+sector.x+" "+sector.y+"loading objects",0);
								String toLoadEntity="";

								for(String object:words)
								{
									if (object.contains("["))
									{
										toLoadEntity="";
									}
									
									toLoadEntity+=object+" ";
									if(object.contains("]"))
									{
										toLoadEntity=toLoadEntity.replace("[","");
										toLoadEntity=toLoadEntity.replace("]","");
									
										String[] entityParams=toLoadEntity.split(" ");
										Entity createdEntity=getEntityManager().createObject(Integer.parseInt(entityParams[0]));
										createdEntity.loadFromStrings(entityParams);
										getEntityManager().addObject(createdEntity);

									}
								}
							}
						}
					}
				
					ins.close();
					isr.close();
					reader.close();
                    getPlayer().initShip();
					GameLog.update("GameMnager: successfully loaded game",0);
					return true;
				}
				else
				{
					GameLog.update("GameManger: save file did not exist",1);
				}
			}
			else
			{
				GameLog.update("GameManager: save folder did not exist",1);
			}
		}
		catch(Exception e)
		{
			GameLog.update("GameManager: "+e.toString(),1);
		}
		return false;
	}
	
	public void saveGame()
	{
		shutdown();
		try
		{
			GameLog.update("GameManager: starting game save",0);
			File saveFolder=checkSaveFolder();
			if(saveFolder!=null)
			{
				File mapFile = new File (saveFolder,"map.txt");
				if (mapFile.exists ())
				{
					//TO WRITE
					File tempMapFile = new File (saveFolder,"tempMap.txt");
					FileOutputStream out = new FileOutputStream(tempMapFile);
					OutputStreamWriter osw = new OutputStreamWriter(out);
					BufferedWriter writer = new BufferedWriter(osw);

					//TO COPY
					FileInputStream ins=new FileInputStream(mapFile);
					InputStreamReader isr=new InputStreamReader(ins);
					BufferedReader reader=new BufferedReader(isr);

					String s;

					//while there rows
					while((s=reader.readLine())!=null)
					{
						//if new sector
						if(s.startsWith("<"))
						{
							String[] head;

							head=s.split(" ");
							
							//equals saving one?
							if(Integer.parseInt(head[1])==worldManager.getSector().x &&
							   Integer.parseInt(head[2])==worldManager.getSector().y)
							{
								MapManager.Sector sector=mapManager.getSector(worldManager.getSector().x,worldManager.getSector().y);
					
								writer.write("< "+sector.x+" "+sector.y+" "+sector.discovered+" "+sector.explored+" ");
								getEntityManager().save(writer);
								writer.write(">");
								writer.newLine();
							}
							else
							{
								writer.write(s);
								writer.newLine();
							}
						}
					}

                    ins.close();
                    isr.close();
                    reader.close();

                    writer.close();
                    osw.close();
                    out.close();

                    saveMeta();

					Bitmap img=MA.getBitmapFromView(gamePanel);
					String imgName="image.jpg";
					FileOutputStream imgOut = new FileOutputStream(saveFolder+File.separator+imgName);
					img.compress(Bitmap.CompressFormat.JPEG, 100, imgOut);

					if(!mapFile.delete())
					{
                        GameLog.update("GameManager: map deleting error",1);
                        return;
                    }

					if(!tempMapFile.renameTo(mapFile))
                    {
                        GameLog.update("GameManager: temp map renaming error",1);
                        return;
                    }

					GameLog.update("GameManger: successfully saved game",0);
				}
				else
				{
					GameLog.update("GameManager: save file did not exist",1);
				}
			}
			else
			GameLog.update("GameManager: save folder did not exist",0);
		}
		catch(Exception e)
		{
			GameLog.update("GameManager: "+e.toString(),1);
		}
		resume();
	}
	
	private void createSaveFile()
	{
		try
		{
			GameLog.update("GameManger: creating files",0);
			File saveFolder=checkSaveFolder();
			if(saveFolder!=null)
			{
				File mapFile = new File (saveFolder,"map.txt");
				if (!mapFile.exists ())
				{
					FileOutputStream out = new FileOutputStream(mapFile);
					OutputStreamWriter osw = new OutputStreamWriter(out);
					BufferedWriter writer = new BufferedWriter(osw);
					
					for(MapManager.Sector s:mapManager.getSectors())
					{
						writer.write("< "+s.x+" "+s.y+" "+s.discovered+" "+s.explored+" >");
						writer.newLine();
					}
					
					writer.close();
					osw.close();
					out.close();
					GameLog.update("GameMnager: successfully created saves for map",0);
				}
				else
				{
					GameLog.update("GameMnager: "+mapFile.getName()+" already exists",1);
					return;
				}

				GameLog.update("GameManager: files created",0);
			}
			else
			{
				GameLog.update("GameManager: permissions not granted or folder has been deleted",1);
			}
		}
		catch(Exception e)
		{
			GameLog.update(e.toString(),1);
		}
		
	}
	
	private File checkSaveFolder()
	{
		String documentsFolderPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).toString();
		File documentsFolder  = new File(documentsFolderPath);

		if(!documentsFolder.exists())
		{
			GameLog.update("GameManager: no DOCUMENTS WRITE PERMISSION",1);
			return null;
		}

		String gameFolderPath=documentsFolderPath+File.separator+"sb9";
		File gameFolder=new File(gameFolderPath);

		if(!gameFolder.exists())
		{
			GameLog.update("GameManager: no inside DOCUMENTS WRITE PERMISSION",1);
			return null;
		}
		File saveFolder=new File(gameFolderPath,getSaveName());
		if(!saveFolder.exists())
		{
			GameLog.update("GameManager: no inside DOCUMENTS WRITE PERMISSION",1);
			return null;
		}
	    return saveFolder;
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
}
