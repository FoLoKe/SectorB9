package com.sb9.foloke.sectorb9.game.entities.Buildings;
import com.sb9.foloke.sectorb9.game.entities.*;
import android.graphics.*;
import com.sb9.foloke.sectorb9.game.display.*;
import com.sb9.foloke.sectorb9.game.funtions.*;
import java.util.Map;
import com.sb9.foloke.sectorb9.*;
import com.sb9.foloke.sectorb9.game.UI.*;
import com.sb9.foloke.sectorb9.game.Assets.*;
import com.sb9.foloke.sectorb9.game.UI.Inventory.*;

public class Crusher extends StaticEntity
{
	private final static int ID=2;
	private UIProgressBar prgBar;
	private UIcustomImage statusImage;
	
	private Inventory.InventoryItem inProduction;
	private int count;
	private Timer prodTimer;
	private PointF collisionInitPoints[];
	private Animation crusherAnim;
	
	private boolean noInventorySpaceBlock=false;
	private boolean noItemsBlock=false;
	
	
	public Crusher(float x, float y,float rotation,Game game)
	{
		super(x,y,rotation,game.buildingsData.findById(ID).image,game.buildingsData.findById(ID).name,game,ID);
		crusherAnim=new Animation(game.buildingsData.findById(ID).animation,15);
		this.inventoryMaxCapacity=3;
		this.opened=true;
		inProduction=new Inventory.InventoryItem(0,0,0,0);
		count=0;prodTimer=new Timer(0);
		prgBar=new UIProgressBar(this,50,8,-25,-20,UIAsset.stunBackground,UIAsset.stunLine,UIAsset.progressBarBorder,prodTimer.getTick());

		collisionInitPoints=new PointF[4];
		collisionInitPoints[0]=new PointF(-image.getWidth()/2,-image.getHeight()/2);
		collisionInitPoints[1]=new PointF(image.getWidth()/2,-image.getHeight()/2);
		collisionInitPoints[2]=new PointF(image.getWidth()/2,image.getHeight()/2);
		collisionInitPoints[3]=new PointF(-image.getWidth()/2,image.getHeight()/2);
		isUsingCustomCollision=true;
		setCustomCollisionObject(collisionInitPoints);

		statusImage=new UIcustomImage(UIAsset.invFullSign);
		calculateCollisionObject();
	}

	@Override
	public void render(Canvas canvas)
	{
		if(!renderable)
			return;
		canvas.save();
		canvas.rotate(rotation,getCenterX(),getCenterY());
		canvas.drawBitmap(crusherAnim.getImage(),x,y,null);
		//if(prodTimer.getTick()>0)
		prgBar.render(canvas);
		game.debugText.setString(prodTimer.getTick()+"");
		canvas.restore();
		
		if(energy==false)
		statusImage.render(canvas,new PointF(x,y));
		if(game.drawDebugInf)
		drawDebugCollision(canvas);
		// TODO: Implement this method
	}

	@Override
	public void tick()
	{
		if(energy)
		{
			if(!noItemsBlock)
				if(inProduction.getID()==0)
				{
					for(Inventory.InventoryItem e :inventory.getArray())
					{
						int crushableID=e.getID();
						int crushedInto=game.itemsData.findById(crushableID).crushToID;
						int crushedFromCount=2;
						if(crushedInto!=0)
						{				
							if(inventory.takeOneItemFromAllInventory(crushableID,crushedFromCount))
							{
								prodTimer.setTimer(2);
								game.updateInventory(this);
								inProduction.set(crushableID,2);
								break;
							}
						}
					}
				}

			if(!noInventorySpaceBlock)
				if(inProduction.getID()!=0)
				{	
					crusherAnim.tick();
					if(prodTimer.tick())
					{
						int crushableID=inProduction.getID();
						int crushedInto=game.itemsData.findById(crushableID).crushToID;
						int crushedIntoCount=1;
						
						inventory.addToExistingOrNull(crushedInto,crushedIntoCount);
						inProduction.set(0,0);
						game.updateInventory(this);
					}
				{	
						
				
				}
			
		}
		if(prodTimer.getTick()>0)
			prgBar.tick(prodTimer.getTick()/(1.2f));
		}
		
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
	
	
	


	
}
