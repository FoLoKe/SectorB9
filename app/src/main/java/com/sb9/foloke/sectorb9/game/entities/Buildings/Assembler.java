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
import com.sb9.foloke.sectorb9.game.UI.Inventory.*;

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

	
	EntitySocket[] arms=new EntitySocket[3];


	public Assembler(float x, float y,float rotation,Game game)
	{
		super(x,y,rotation,game.buildingsData.findById(ID).image,game.buildingsData.findById(ID).name,game,ID);
		assemblerAnim=new Animation( game.buildingsData.findById(ID).animation,30);
		this.inventoryMaxCapacity=5;
		this.opened=true;
		inProduction=0;
		prodTimer=new Timer(0);
		prgBar=new UIProgressBar(this,50,8,-25,-20,UIAsset.stunBackground,UIAsset.stunLine,UIAsset.progressBarBorder,prodTimer.getTick());

		collisionInitPoints=new PointF[4];
		collisionInitPoints[0]=new PointF(-image.getWidth()/2,-image.getHeight()/2);
		collisionInitPoints[1]=new PointF(image.getWidth()/2,-image.getHeight()/2);
		collisionInitPoints[2]=new PointF(image.getWidth()/2,image.getHeight()/2);
		collisionInitPoints[3]=new PointF(-image.getWidth()/2,image.getHeight()/2);
		isUsingCustomCollision=true;
		setCustomCollisionObject(collisionInitPoints);

		statusImage=new UIcustomImage(UIAsset.noEnergySign);
		statusImage2=new UIcustomImage(UIAsset.invFullSign);
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
		
		if(!assembling)
		canvas.drawBitmap(image,x,y,null);
		else
			canvas.drawBitmap(assemblerAnim.getImage(),x,y,null);
		prgBar.render(canvas);
		game.debugText.setString(prodTimer.getTick()+"");
		
		
		canvas.restore();

		if(energy==false)
			statusImage.render(canvas,new PointF(x,y));
		if(enabled==false)
			statusImage2.render(canvas,new PointF(x+16,y));
		if(game.drawDebugInf)
			drawDebugCollision(canvas);
		
		for(EntitySocket arm:arms)
		arm.render(canvas);
		
	}

	@Override
	public void tick()
	{
		try
		{
			game.textInProduction.setString(inProduction+"");
			game.textInQueue.setString(productionQueue+"");
			if(energy)
			{
				//if not working
				if(!assembling)
				{
				for(EntitySocket arm:arms)
					arm.setRotation(0);
					if(productionQueue.size()>0)
					{
						//not every tick!
						//make flag for update button
						if(true)
						{
						int tProduction=productionQueue.get(0);
						ArrayList<Inventory.InventoryItem> tItems=new ArrayList<Inventory.InventoryItem>();
							for(Map.Entry<Integer,Integer> e : game.itemsData.findById(tProduction).madeFrom.entrySet())
							{
								tItems.add(new Inventory.InventoryItem(0,0,e.getKey(),e.getValue()));
							}
							if(inventory.takeArrayItemFromAllInventory(tItems))
							{
								
								game.updateInventory(this);
								assembling=true;
								prodTimer.setTimer(prodTimeLength);
								inProduction=tProduction;
								productionQueue.remove(0);
							
								if(game.mContext.assemblerUIi.getOpened())
									game.initAssemblerUI(this);
							}
						}
					}
				}
				
				//if in work
				if(assembling)
				{
					if(prodTimer.tick())
					{
						inventory.addToExistingOrNull(inProduction,1);
						game.updateInventory(this);
						assembling=false;
						inProduction=0;
						if(game.mContext.assemblerUIi.getOpened())
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
		catch(Exception e){System.out.println(e);}
	}

	@Override
	public void calculateCollisionObject()
	{
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
		if(!game.itemsData.findById(ID).madeFrom.containsValue(0))
			productionQueue.add(ID);
		else
			///error
			{}
		if(game.mContext.assemblerUIi.getOpened())
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

