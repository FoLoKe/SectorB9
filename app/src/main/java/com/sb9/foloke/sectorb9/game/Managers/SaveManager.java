package com.sb9.foloke.sectorb9.game.Managers;
import java.io.*;
import com.sb9.foloke.sectorb9.game.UI.CustomViews.*;
import com.sb9.foloke.sectorb9.game.Entities.*;
import android.os.*;
import android.graphics.*;

public class SaveManager
{
	private static LoadThread loadThread;
	public static SaveThread saveThread;
	private static GameManager gameManager;
	public static void init(GameManager gm)
	{
		gameManager=gm;
		loadThread=new LoadThread(gm);
		saveThread=new SaveThread(gm);
	}
	
	private static class LoadThread extends Thread
	{
		GameManager gm;
		public LoadThread(GameManager gm)
		{
			this.gm=gm;
		}

		@Override
		public void run()
		{
			// TODO: Implement this method
			super.run();
			gm.isLoading=true;
			loadGame();
			
			gm.isLoading=false;
			
		}
	}
	
	private static class SaveThread extends Thread
	{
		GameManager gm;
		public SaveThread(GameManager gm)
		{
			this.gm=gm;
		}

		@Override
		public void run()
		{
			// TODO: Implement this method
			super.run();
			gm.isLoading=true;
			saveGame();
			
			gm.isLoading=false;

		}
	}
	//TODO:
	public static void load()
	{
		loadThread.start();
	}
	
	public static 
	void save()
	{
		try
		{
		saveThread.start();
		}
		catch(Exception e)
		{
			GameLog.update(e.toString(),1);
		}
	}
	
	private static void loadMeta()
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
				gameManager.getWorldManager().setSector(x, y);

//				

//                
               	gameManager.getGamePanel().cameraPoint.x=(Float.parseFloat(metareader.readLine()));
			   	gameManager.getGamePanel().cameraPoint.y=(Float.parseFloat(metareader.readLine()));

				gameManager.getController().setControlledEntity(null);

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

	private static boolean loadGame()
	{
		WorldManager worldManager=gameManager.getWorldManager();
		EntityManager entityManager=gameManager.getEntityManager();
		MapManager mapManager=gameManager.getMapManager();
		
		try
		{
			GameLog.update("GameManager: starting game load",0);

			entityManager.reload();

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
									loadThread.sleep(1);
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
										Entity createdEntity=entityManager.createObject(Integer.parseInt(entityParams[0]));
										createdEntity.loadFromStrings(entityParams);
										entityManager.addObject(createdEntity);

									}
								}
							}
						}
					}

					ins.close();
					isr.close();
					reader.close();

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
	
	public static File checkSaveFolder()
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
		File saveFolder=new File(gameFolderPath,gameManager.getSaveName());
		if(!saveFolder.exists())
		{
			GameLog.update("GameManager: no inside DOCUMENTS WRITE PERMISSION",1);
			return null;
		}
	    return saveFolder;
	}
	
	private static void saveMeta()
    {
        try {
            File saveFolder =SaveManager.checkSaveFolder();
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

                metaWriter.write("" + gameManager.getWorldManager().getSector().x);
                metaWriter.newLine();
                metaWriter.write("" + gameManager.getWorldManager().getSector().y);
                metaWriter.newLine();

                metaWriter.write("" + gameManager.getGamePanel().cameraPoint.x);
                metaWriter.newLine();
                metaWriter.write("" + gameManager.getGamePanel().cameraPoint.y);
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



	private static void saveGame()
	{
		
		try
		{
			GameLog.update("GameManager: starting game save",0);
			File saveFolder=SaveManager.checkSaveFolder();
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
							if(Integer.parseInt(head[1])==gameManager.getWorldManager().getSector().x &&
							   Integer.parseInt(head[2])==gameManager.getWorldManager().getSector().y)
							{
								MapManager.Sector sector=gameManager.getMapManager().getSector(gameManager.getWorldManager().getSector().x,gameManager.getWorldManager().getSector().y);

								writer.write("< "+sector.x+" "+sector.y+" "+sector.discovered+" "+sector.explored+" ");
								gameManager.getEntityManager().save(writer);
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

					Bitmap img=gameManager.getMainActivity().getBitmapFromView(gameManager.getGamePanel());
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
		
	}

	public static void createSaveFile()
	{
		try
		{
			GameLog.update("GameManger: creating files",0);
			File saveFolder=SaveManager.checkSaveFolder();
			if(saveFolder!=null)
			{
				File mapFile = new File (saveFolder,"map.txt");
				if (!mapFile.exists ())
				{
					FileOutputStream out = new FileOutputStream(mapFile);
					OutputStreamWriter osw = new OutputStreamWriter(out);
					BufferedWriter writer = new BufferedWriter(osw);

					for(MapManager.Sector s:gameManager.getMapManager().getSectors())
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
	
	public static boolean loadSector(int x,int y) {

        gameManager.getWorldManager().setSector(x,y);
        //set new sector in meta
        saveMeta();

        //load sector with new meta
		boolean state=loadGame();
        return state;
		
    }
	
}
