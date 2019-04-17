package com.sb9.foloke.sectorb9.game.Entities.Buildings;

import com.sb9.foloke.sectorb9.game.Managers.GameManager;
import com.sb9.foloke.sectorb9.game.UI.*;
import com.sb9.foloke.sectorb9.game.Assets.*;
import com.sb9.foloke.sectorb9.game.DataSheets.ObjectsDataSheet;
import com.sb9.foloke.sectorb9.game.DataSheets.ItemsDataSheet;
import com.sb9.foloke.sectorb9.game.Funtions.*;
import android.graphics.*;
import com.sb9.foloke.sectorb9.game.Entities.*;
import java.util.Map;
import com.sb9.foloke.sectorb9.game.Entities.Buildings.Components.*;
import java.util.ArrayList;
import com.sb9.foloke.sectorb9.game.UI.Inventory.*;
import com.sb9.foloke.sectorb9.game.UI.Inventory.Inventory.*;

public class Assembler extends ProductionBuilding
{
	private final static int ID=6;
	
	

	private Animation assemblerAnim;

	
	EntitySocket[] arms=new EntitySocket[3];


	public Assembler(float x, float y, float rotation, GameManager gameManager)
	{
		super(x,y,rotation, gameManager,ID);
		assemblerAnim=new Animation( ObjectsDataSheet.findById(ID).animation,5);
		
		
		



		

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
		super.render(canvas);
		canvas.save();
		canvas.rotate(rotation,getCenterX(),getCenterY());
		
		
			canvas.drawBitmap(assemblerAnim.getImage(),x,y,null);
		
		
		canvas.restore();

		
		
		
		for(EntitySocket arm:arms)
		arm.render(canvas);
		
	}

	@Override
	public void tick()
	{
	    super.tick();
		production();
		if(inProgress)
		{	
			for(EntitySocket arm:arms)
				arm.tick();
			assemblerAnim.tick();
		}
		else
		{
			for(EntitySocket arm:arms)
				arm.setRotation(0);
		}
				

	}

	@Override
	protected int chooseItemToProduce(ArrayList<Inventory.InventoryItem> items)
	{
		// TODO: Implement this method
		int toProduction=-1;
		if(productionQueue.size()>0)
		{
			//not every tick!
			//make flag for update button
			
				toProduction=productionQueue.get(0);
				
				for(Map.Entry<Integer,Integer> e : ItemsDataSheet.findById(toProduction).madeFrom.entrySet())
				{
					items.add(new Inventory.InventoryItem(0,0,e.getKey(),e.getValue()));
				}
		}		
		return toProduction;
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
		if(AssemblerUI.getOpened())
			AssemblerUI.updateQueue(this);
	}

	@Override
	protected void onProductionEnded()
	{
		// TODO: Implement this method
		productionQueue.remove(0);
		if(AssemblerUI.getOpened())
		{
			AssemblerUI.updateQueue(this);
		}
	}

	


	
	
}

