package com.sb9.foloke.sectorb9.game.entities.Buildings;
import com.sb9.foloke.sectorb9.game.UI.*;
import com.sb9.foloke.sectorb9.game.Assets.*;
import com.sb9.foloke.sectorb9.game.funtions.*;
import android.graphics.*;
import com.sb9.foloke.sectorb9.game.entities.*;
import com.sb9.foloke.sectorb9.game.display.*;
import java.util.Map;
import com.sb9.foloke.sectorb9.*;
import java.time.*;
import android.util.*;
import android.content.res.*;
import com.sb9.foloke.sectorb9.game.entities.Buildings.Components.*;
import java.util.ArrayList;

public class Assembler extends StaticEntity
{
	private final static int ID=6;
	private UIProgressBar prgBar;
	private UIcustomImage statusImage;
	private UIcustomImage statusImage2;
	private int prodTimeLength=10;
	private int inProduction=0;
	private Timer prodTimer;
	private boolean assembling=false;
	private ArrayList<Integer> productionQueue=new ArrayList<Integer>();
	private PointF collisionInitPoints[];
	private Animation assemblerAnim;
	//private Animation crusherAnim;

	
	EntitySocket[] arms=new EntitySocket[3];
	public Assembler(float x, float y,float rotation,ObjectsAsset  objAsset,String name,Game game)
	{
		super(x,y,rotation,objAsset.smelterCold,name,game,ID);
		//crusherAnim=new Animation(objAsset.crusherAnim,15);
		this.inventoryMaxCapacity=5;
		this.opened=true;
		inProduction=0;
		prodTimer=new Timer(0);
		prgBar=new UIProgressBar(this,50,8,-25,-20,game.uiAsset.stunBackground,game.uiAsset.stunLine,game.uiAsset.progressBarBorder,prodTimer.getTick());

		collisionInitPoints=new PointF[4];
		collisionInitPoints[0]=new PointF(-image.getWidth()/2,-image.getHeight()/2);
		collisionInitPoints[1]=new PointF(image.getWidth()/2,-image.getHeight()/2);
		collisionInitPoints[2]=new PointF(image.getWidth()/2,image.getHeight()/2);
		collisionInitPoints[3]=new PointF(-image.getWidth()/2,image.getHeight()/2);
		isUsingCustomCollision=true;
		setCustomCollisionObject(collisionInitPoints);

		statusImage=new UIcustomImage(game.uiAsset.noEnergySign,5);
		statusImage2=new UIcustomImage(game.uiAsset.noEnergySign,5);
		calculateCollisionObject();
		
		arms[0]=new EntitySocket(this,new AssemblerArm(x,y,rotation,"",game),rotation,new PointF(10,10));
		arms[1]=new EntitySocket(this,new AssemblerArm(x,y,rotation,"",game),rotation,new PointF(10,10));
		arms[2]=new EntitySocket(this,new AssemblerArm(x,y,rotation,"",game),rotation,new PointF(10,10));
		
	}

	public Assembler(float x, float y,float rotation,Game game)
	{
		super(x,y,rotation,game.buildingsData.findById(ID).image,game.buildingsData.findById(ID).name,game,ID);
		assemblerAnim=new Animation( game.buildingsData.findById(ID).animation,30);
		this.inventoryMaxCapacity=5;
		this.opened=true;
		inProduction=0;
		prodTimer=new Timer(0);
		prgBar=new UIProgressBar(this,50,8,-25,-20,game.uiAsset.stunBackground,game.uiAsset.stunLine,game.uiAsset.progressBarBorder,prodTimer.getTick());

		collisionInitPoints=new PointF[4];
		collisionInitPoints[0]=new PointF(-image.getWidth()/2,-image.getHeight()/2);
		collisionInitPoints[1]=new PointF(image.getWidth()/2,-image.getHeight()/2);
		collisionInitPoints[2]=new PointF(image.getWidth()/2,image.getHeight()/2);
		collisionInitPoints[3]=new PointF(-image.getWidth()/2,image.getHeight()/2);
		isUsingCustomCollision=true;
		setCustomCollisionObject(collisionInitPoints);

		statusImage=new UIcustomImage(game.uiAsset.noEnergySign,5);
		statusImage2=new UIcustomImage(game.uiAsset.turnedOffSign,5);
		calculateCollisionObject();
		arms[0]=new EntitySocket(this,new AssemblerArm(x,y,0,"",game),90,new PointF(10,0));
		arms[1]=new EntitySocket(this,new AssemblerArm(x,y,25,"",game),-90,new PointF(-10,-23));
		arms[2]=new EntitySocket(this,new AssemblerArm(x,y,15,"",game),-90,new PointF(-10,23));
		
		for(EntitySocket arm:arms)
			arm.tick();
		
	}

	@Override
	public void render(Canvas canvas)
	{
		
		if(!renderable)
			return;
		canvas.save();
		canvas.rotate(rotation,getCenterX(),getCenterY());
		//Bitmap debugBitmap=rotate(game.buildingsData.asset.smallCargoContainer,debugRotation);
		
		
		
		if(!assembling)
		canvas.drawBitmap(image,x,y,null);
		else
			canvas.drawBitmap(assemblerAnim.getImage(),x,y,null);
		//if(prodTimer.getTick()>0)
		prgBar.render(canvas);
		game.debugText.setString(prodTimer.getTick()+"");
		
		
		canvas.restore();

		if(energy==false)
			statusImage.render(canvas,new PointF(x,y));
		if(enabled==false)
			statusImage2.render(canvas,new PointF(x+16,y));
		if(game.drawDebugInf)
			drawDebugCollision(canvas);
		// TODO: Implement this method
		for(EntitySocket arm:arms)
		arm.render(canvas);
		
	}

	@Override
	public void tick()
	{
		/*try
		{
			game.textInProduction.setString(inProduction+"");
			game.textInQueue.setString(productionQueue+"");
		if(energy)
		{
			//error check
			
				//items search 
				if(!assembling)
				{
				for(EntitySocket arm:arms)
					arm.setRotation(0);
					if(productionQueue.size()>0)
					{
						
						if(inventory.size()>0)
						{
						int tProduction=productionQueue.get(0);
							boolean allOk=true;
							boolean toProduce[]=new boolean[game.itemsData.findById(tProduction).madeFrom.size()];
							int i=0;
							for(Map.Entry<Integer,Integer> e: game.itemsData.findById(tProduction).madeFrom.entrySet())
							{
								toProduce[i]=false;
								if(inventory.containsKey(e.getKey()))
									if(inventory.get(e.getKey())>=e.getValue())
									{
										toProduce[i]=true;
									}
								i++;
							}
						
							//all items found?
							for(int j=0;j<game.itemsData.findById(tProduction).madeFrom.size();j++)
							{
								if(toProduce[j]==false)
									allOk=false;
							}
			
							if(allOk)
							{
								for(Map.Entry<Integer,Integer> e: game.itemsData.findById(tProduction).madeFrom.entrySet())
								{
									if(inventory.get(e.getKey())>e.getValue())
									{
										inventory.put(e.getKey(),inventory.get(e.getKey())-e.getValue());
									}
									else
									{
										inventory.remove(e.getKey());
									}
								}
								game.initObjInventory();
								assembling=true;
								prodTimer.setTimer(prodTimeLength);
								inProduction=productionQueue.get(0);
								productionQueue.remove(0);
							
								if(game.mAcontext.assemblerUIi.getOpened())
									game.initAssemblerUI(this);
							}
						}
					}
				}
				//production
				if(assembling)
				{
					if(prodTimer.tick())
					{
						if(inventory.containsKey(inProduction))
						{
							inventory.put(inProduction,inventory.get(inProduction)+1);
						}
						else
							inventory.put(inProduction,1);
						game.initObjInventory();
						assembling=false;
						inProduction=0;
						if(game.mAcontext.assemblerUIi.getOpened())
							game.initAssemblerUI(this);
					}
				for(EntitySocket arm:arms)
					arm.tick();
					assemblerAnim.tick();
				}
				if(prodTimer.getTick()>0)
					prgBar.tick(prodTimer.getTick()/(prodTimeLength*0.6f));
		}
		super.calculateCollisionObject();
		
			}
			catch(Exception e)
			{
				System.out.println(e);
			}*/
	}

	@Override
	public void calculateCollisionObject()
	{
		// TODO: Implement this method
		super.calculateCollisionObject();
		calculateCustomCollisionObject();
	}

	@Override
	public void onAndOff()
	{
		super.onAndOff();
		if(!enabled&&this.powerSupplier!=null)
		{
			((Generator)this.powerSupplier).takeEnergy(this);
			this.powerSupplier=null;
		}
	}
	public void addQueue(int ID)
	{
		//Map<Integer,Integer> a=game.itemsData.findById(ID).madeFrom;
		if(!game.itemsData.findById(ID).madeFrom.containsValue(0))
			productionQueue.add(ID);
		else
			///error
			{}
		if(game.mAcontext.assemblerUIi.getOpened())
			game.initAssemblerUI(this);
	}
	public ArrayList<Integer> getQueue()
	{
		return productionQueue;
	}
	public int getInProduction()
	{
		return inProduction;
	}

	
	
	
}

