package com.sb9.foloke.sectorb9.game.Entities.Buildings;

import com.sb9.foloke.sectorb9.game.Managers.GameManager;
import com.sb9.foloke.sectorb9.game.UI.*;
import com.sb9.foloke.sectorb9.game.Assets.*;
import com.sb9.foloke.sectorb9.game.DataSheets.BuildingsDataSheet;
import com.sb9.foloke.sectorb9.game.DataSheets.ItemsDataSheet;
import com.sb9.foloke.sectorb9.game.Funtions.*;
import android.graphics.*;
import com.sb9.foloke.sectorb9.game.Entities.*;
import java.util.Map;
import com.sb9.foloke.sectorb9.game.Entities.Buildings.Components.*;
import java.util.ArrayList;
import com.sb9.foloke.sectorb9.game.UI.Inventory.*;

public class Assembler extends StaticEntity
{
	private final static int ID=6;
	private ProgressBarUI prgBar;
	private CustomImageUI statusImage;
	private CustomImageUI statusImage2;
	private int prodTimeLength=10;
	private int inProduction=0;
	private Timer prodTimer;
	private boolean assembling=false;
	private ArrayList<Integer> productionQueue=new ArrayList<Integer>();

	private Animation assemblerAnim;

	
	EntitySocket[] arms=new EntitySocket[3];


	public Assembler(float x, float y, float rotation, GameManager gameManager)
	{
		super(x,y,rotation, BuildingsDataSheet.findById(ID).image,BuildingsDataSheet.findById(ID).name, gameManager,ID);
		assemblerAnim=new Animation( BuildingsDataSheet.findById(ID).animation,30);
		this.inventoryMaxCapacity=5;
		this.opened=true;
		inProduction=0;
		prodTimer=new Timer(0);
		prgBar=new ProgressBarUI(this,50,8,-25,-20,UIAsset.stunBackground,UIAsset.stunLine,UIAsset.progressBarBorder,prodTimer.getTick());



		statusImage=new CustomImageUI(UIAsset.noEnergySign);
		statusImage2=new CustomImageUI(UIAsset.invFullSign);

		arms[0]=new EntitySocket(this,new AssemblerArm(x,y,0,"", gameManager),90,new PointF(10,0));
		arms[1]=new EntitySocket(this,new AssemblerArm(x,y,25,"", gameManager),-90,new PointF(-10,-23));
		arms[2]=new EntitySocket(this,new AssemblerArm(x,y,15,"", gameManager),-90,new PointF(-10,23));
		
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
		
		canvas.restore();

		if(!energy)
			statusImage.render(canvas,new PointF(x,y));
		if(!enabled)
			statusImage2.render(canvas,new PointF(x+16,y));
		if(gameManager.drawDebugInfo)
			drawDebugCollision(canvas);
		
		for(EntitySocket arm:arms)
		arm.render(canvas);
		
	}

	@Override
	public void tick()
	{
	    super.tick();
		try
		{
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
							for(Map.Entry<Integer,Integer> e : ItemsDataSheet.findById(tProduction).madeFrom.entrySet())
							{
								tItems.add(new Inventory.InventoryItem(0,0,e.getKey(),e.getValue()));
							}
							if(inventory.takeArrayItemFromAllInventory(tItems))
							{
								
								gameManager.updateInventory(this);
								assembling=true;
								prodTimer.setTimer(prodTimeLength);
								inProduction=tProduction;
								productionQueue.remove(0);
							
								if(gameManager.getMainActivity().assemblerUIi.getOpened())
									gameManager.initAssemblerUI(this);
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
						gameManager.updateInventory(this);
						assembling=false;
						inProduction=0;
						if(gameManager.getMainActivity().assemblerUIi.getOpened())
							gameManager.initAssemblerUI(this);
					}
					for(EntitySocket arm:arms)
						arm.tick();
					assemblerAnim.tick();
				}
				if(prodTimer.getTick()>0)
					prgBar.tick(prodTimer.getTick()/(prodTimeLength*0.6f));
			}

		}
		catch(Exception e){System.out.println(e);}
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
		if(!ItemsDataSheet.findById(ID).madeFrom.containsValue(0))
			productionQueue.add(ID);
		else
			///error
			{}
		if(gameManager.getMainActivity().assemblerUIi.getOpened())
			gameManager.initAssemblerUI(this);
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

