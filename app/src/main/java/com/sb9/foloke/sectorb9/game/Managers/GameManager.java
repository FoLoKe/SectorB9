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
import com.sb9.foloke.sectorb9.game.UI.CustomViews.*;
import android.os.*;
import java.io.*;


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
	private MapManager mapManager;
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

		mapManager=new MapManager(MA,this);
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
                GameLog.update("items collected",0);     
            }
            else
                GameLog.update("inventory full" ,0);
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
	
	public MapManager getMapManager()
	{
		return mapManager;
	}
	
	public boolean loadSector(int x,int y) {return false;};

		
		
	public void  loadGame()
	{
		try
		{
			GameLog.update("satrting game load",2);
			File saveFolder=checkSaveFolder();
			if(saveFolder!=null)
			{
				File mapFile = new File (saveFolder,"map.txt");
				if (mapFile.exists ())
				{
					//TO Read meta
					File meta=new File(saveFolder,"meta.txt");
					if(!meta.exists())
					{
						GameLog.update("meta did not exist",1);
						return;
					}
					
					FileInputStream mits=new FileInputStream(meta);
					InputStreamReader mis=new InputStreamReader(mits);
					BufferedReader metareader=new BufferedReader(mis);

					int x=Integer.parseInt(metareader.readLine());
					int y=Integer.parseInt(metareader.readLine());
					worldManager.setSector(x,y);
					metareader.close();
					mis.close();
					mits.close();
					//Meta readed
					
					//TO read map
					FileInputStream ins=new FileInputStream(mapFile);
					InputStreamReader isr=new InputStreamReader(ins);
					BufferedReader reader=new BufferedReader(isr);

					String s;

					GameLog.update(mapFile.getAbsolutePath(),2);
					GameLog.update(mapFile.canRead()+"",2);
					GameLog.update(mapFile.length()+"",2);
					//while there rows
					while((s=reader.readLine())!=null)
					{
						//if new sector
						if(s.startsWith("<"))
						{
							String[] words;
							s=s.replace("<","");
							s=s.replace(">","");
							words=s.split(" ");
						
							//equals loading one?
							if(Integer.parseInt(words[1])==worldManager.getSector().x &&
							   Integer.parseInt(words[2])==worldManager.getSector().y)
							{
							
								MapManager.Sector sector=mapManager.getSector(worldManager.getSector().x,worldManager.getSector().y);
								GameLog.update(sector.x+" "+sector.y+"loading objects",2);
								String toLoadEntity="";
								int objectsCount=0;
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
										createdEntity.load(entityParams);
										getEntityManager().addObject(createdEntity);
										objectsCount++;
									}
								}
							}
						}
					}
				
					ins.close();
					isr.close();
					reader.close();

				
					GameLog.update("Successfully loaded game",0);
				}
				else
				{
					GameLog.update("save file did not exist",1);
					return;
				}
			}
			else
			{
				GameLog.update("save folder did not exist",2);
				return;
			}
		}
		catch(Exception e)
		{
			GameLog.update(e.toString(),1);
			return;
		}
	}
	
	public void saveGame()
	{
		try
		{
			GameLog.update("satrting game save",2);
			File saveFolder=checkSaveFolder();
			if(saveFolder!=null)
			{
				File mapFile = new File (saveFolder,"map.txt");
				if (mapFile.exists ())
				{
					//TO WRITE
					File tempMapFile = new File (saveFolder,"tmap.txt");
					FileOutputStream out = new FileOutputStream(tempMapFile);
					OutputStreamWriter osw = new OutputStreamWriter(out);
					BufferedWriter writer = new BufferedWriter(osw);

					//TO COPY
					FileInputStream ins=new FileInputStream(mapFile);
					InputStreamReader isr=new InputStreamReader(ins);
					BufferedReader reader=new BufferedReader(isr);

					String s;
					
					GameLog.update(mapFile.getAbsolutePath(),2);
					GameLog.update(mapFile.canRead()+"",2);
					GameLog.update(mapFile.length()+"",2);
					//while there rows
					while((s=reader.readLine())!=null)
					{
						//if new sector
						if(s.startsWith("<"))
						{
							String[] head;
							s.replace("<","");
							s.replace(">","");

							head=s.split(" ");
							
							//equals saving one?
							if(Integer.parseInt(head[1])==worldManager.getSector().x &&
							   Integer.parseInt(head[2])==worldManager.getSector().y)
							{
								GameLog.update(s,2);
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

					File meta=new File(saveFolder,"meta.txt");
					if(meta.exists())
						meta.delete();
					FileOutputStream mots=new FileOutputStream(meta);
					OutputStreamWriter mos=new OutputStreamWriter(mots);
					BufferedWriter metawriter=new BufferedWriter(mos);
					
					metawriter.write(""+worldManager.getSector().x);
					metawriter.newLine();
					metawriter.write(""+worldManager.getSector().y);
					metawriter.newLine();
					metawriter.write(""+getPlayer().getHp());
					metawriter.newLine();
					metawriter.write(""+getPlayer().getSH());
					metawriter.newLine();
					metawriter.write(""+getPlayer().getCenterX());
					metawriter.newLine();
					metawriter.write(""+getPlayer().getCenterY());
					metawriter.newLine();
					
					metawriter.close();
					mos.close();
					mots.close();
					
					Bitmap img=MA.getBitmapFromView(gamePanel);
					String imgname="image.jpg";
					FileOutputStream imgout = new FileOutputStream(saveFolder+File.separator+imgname);
					img.compress(Bitmap.CompressFormat.JPEG, 100, imgout); // bmp is your Bitmap instance
						// PNG is a lossless format, the compression factor (100) is ignored
					
					ins.close();
					isr.close();
					reader.close();
					
					writer.close();
					osw.close();
					out.close();
					
					mapFile.delete();
					tempMapFile.renameTo(mapFile);
					GameLog.update("Successfully saved game",0);
				}
				else
				{
					GameLog.update("save file did not exist",1);
					return;
				}
			}
			else
			GameLog.update("save folder did not exist",2);
		}
		catch(Exception e)
		{
			GameLog.update(e.toString(),1);
		}
	}
	
	public void createSaveFile()
	{
		try
		{
			GameLog.update("creating files",2);
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
					GameLog.update("Successfully created saves for map",0);
				}
				else
				{
					GameLog.update(mapFile.getName()+" already exists",1);
					return;
				}

				GameLog.update("files created",2);
			}
			else
			{
				GameLog.update("permissions not granted or folder has been deleted",1);
			}
		}
		catch(Exception e)
		{
			GameLog.update(e.toString(),1);
		}
		
	}
	
	public File checkSaveFolder()
	{
		String documentsFolderPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).toString();
		File documentsFolder  = new File(documentsFolderPath);

		if(!documentsFolder.exists())
		{
			GameLog.update("no DOCUMENTS WRITE PERMISSION",1);
			return null;
		}

		String gameFolderPath=documentsFolderPath+File.separator+"sb9";
		File gameFolder=new File(gameFolderPath);

		if(!gameFolder.exists())
		{
			GameLog.update("no inside DOCUMENTS WRITE PERMISSION",1);
			return null;
		}
		File saveFolder=new File(gameFolderPath,getSaveName());
		if(!saveFolder.exists())
		{
			GameLog.update("no inside DOCUMENTS WRITE PERMISSION",1);
			return null;
		}
	return saveFolder;
	}
}
