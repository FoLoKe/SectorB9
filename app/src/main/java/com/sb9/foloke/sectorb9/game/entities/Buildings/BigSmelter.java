package com.sb9.foloke.sectorb9.game.entities.Buildings;
import com.sb9.foloke.sectorb9.game.UI.*;
import com.sb9.foloke.sectorb9.game.Assets.*;
import com.sb9.foloke.sectorb9.game.funtions.*;
import android.graphics.*;
import com.sb9.foloke.sectorb9.game.entities.*;
import com.sb9.foloke.sectorb9.game.display.*;
import java.util.Map;
import com.sb9.foloke.sectorb9.*;
import com.sb9.foloke.sectorb9.game.UI.Inventory.*;

public class BigSmelter extends StaticEntity
{
	private final static int ID=5;
	private UIProgressBar prgBar;
	private UIcustomImage statusImageNoEnergy;
	private UIcustomImage statusImageEnebled;
	private int prodTimeLength=10;
	private Inventory.InventoryItem inProduction;
	private Timer prodTimer;
	
	private PointF collisionInitPoints[];
	private Bitmap smelterInWorkBitmap;

	

	public BigSmelter(float x, float y,float rotation,Game game)
	{
		super(x,y,rotation,game.buildingsData.findById(ID).image,game.buildingsData.findById(ID).name,game,ID);
		smelterInWorkBitmap= game.buildingsData.findById(ID).animation[0];
		this.inventoryMaxCapacity=3;
		this.opened=true;
		inProduction=new Inventory.InventoryItem(0,0,0,0);
		prodTimer=new Timer(0);
		prgBar=new UIProgressBar(this,50,8,-25,-20,UIAsset.stunBackground,UIAsset.stunLine,UIAsset.progressBarBorder,prodTimer.getTick());

		collisionInitPoints=new PointF[4];
		collisionInitPoints[0]=new PointF(-image.getWidth()/2,-image.getHeight()/2);
		collisionInitPoints[1]=new PointF(image.getWidth()/2,-image.getHeight()/2);
		collisionInitPoints[2]=new PointF(image.getWidth()/2,image.getHeight()/2);
		collisionInitPoints[3]=new PointF(-image.getWidth()/2,image.getHeight()/2);
		isUsingCustomCollision=true;
		setCustomCollisionObject(collisionInitPoints);

		statusImageNoEnergy=new UIcustomImage(UIAsset.noEnergySign);
		statusImageEnebled=new UIcustomImage(UIAsset.invFullSign);
		calculateCollisionObject();
	}

	@Override
	public void render(Canvas canvas)
	{
		if(!renderable)
			return;
		canvas.save();
		canvas.rotate(rotation,getCenterX(),getCenterY());
		if(inProduction.getID()==0)
		canvas.drawBitmap(image,x,y,null);
		else
		canvas.drawBitmap(smelterInWorkBitmap,x,y,null);
		prgBar.render(canvas);
		game.debugText.setString(prodTimer.getTick()+"");
		canvas.restore();

		if(!energy)
			statusImageNoEnergy.render(canvas,new PointF(x,y));
        if(!enabled)
        	statusImageEnebled.render(canvas,new PointF(x+16,y));
		if(game.drawDebugInf)
			drawDebugCollision(canvas);
	}

	@Override
	public void tick()
	{
		if(energy)
		{
			///make flag on inventory empty
			if(inProduction.getID()==0)
			{
				for(Inventory.InventoryItem e: inventory.getArray())
				{
					if(game.itemsData.findById(e.getID()).smeltToID!=0)
					{
						int toProdID=e.getID();
						int toProdCount=2;
						if(inventory.takeOneItemFromAllInventory(toProdID,toProdCount))
						{
							inProduction.set(toProdID,toProdCount);
							prodTimer.setTimer(prodTimeLength);
							game.updateInventory(this);
							break;
						}
					}
				}

			}
			//flag on inventory full
			if(inProduction.getID()!=0)
			{
				if(prodTimer.tick())
				{
					if(inventory.addToExistingOrNull(game.itemsData.findById(inProduction.getID()).smeltToID,1))
					{game.updateInventory(this);
					inProduction.set(0,0);}
				}
			}
			if(prodTimer.getTick()>0)
				prgBar.tick(prodTimer.getTick()/(prodTimeLength*0.6f));
		}
		super.calculateCollisionObject();

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
}
