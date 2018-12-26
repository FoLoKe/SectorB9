package com.sb9.foloke.sectorb9.game.entities.Buildings;
import com.sb9.foloke.sectorb9.game.UI.*;
import com.sb9.foloke.sectorb9.game.Assets.*;
import com.sb9.foloke.sectorb9.game.funtions.*;
import android.graphics.*;
import com.sb9.foloke.sectorb9.game.entities.*;
import com.sb9.foloke.sectorb9.game.display.*;
import java.util.Map;
import com.sb9.foloke.sectorb9.*;

public class BigSmelter extends StaticEntity
{
	private final static int ID=5;
	private UIProgressBar prgBar;
	private UIcustomImage statusImage;
	private UIcustomImage statusImage2;
	private int prodTimeLength=10;
	private int inProduction;
	private int count;
	private Timer prodTimer;
	
	private PointF collisionInitPoints[];
	//private Animation crusherAnim;
	Bitmap smelterInWorkBitmap;

	public BigSmelter(float x, float y,float rotation,ObjectsAsset  objAsset,String name,Game game)
	{
		super(x,y,rotation,objAsset.smelterCold,name,game,ID);
		//crusherAnim=new Animation(objAsset.crusherAnim,15);
		this.inventoryMaxCapacity=3;
		this.opened=true;
		inProduction=0;
		count=0;prodTimer=new Timer(0);
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
	}

	public BigSmelter(float x, float y,float rotation,Game game)
	{
		super(x,y,rotation,game.buildingsData.findById(ID).image,game.buildingsData.findById(ID).name,game,ID);
		smelterInWorkBitmap= game.buildingsData.findById(ID).animation[0];
		this.inventoryMaxCapacity=3;
		this.opened=true;
		inProduction=0;
		count=0;prodTimer=new Timer(0);
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
	}

	@Override
	public void render(Canvas canvas)
	{
		if(!renderable)
			return;
		canvas.save();
		canvas.rotate(rotation,getCenterX(),getCenterY());
		if(inProduction==0)
		canvas.drawBitmap(image,x,y,null);
		else
		canvas.drawBitmap(smelterInWorkBitmap,x,y,null);
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
	}

	@Override
	public void tick()
	{
		/*if(energy)
		{
			if(inventory.size()>0&&inProduction==0)
			{
				for(Map.Entry<Integer,Integer> e: inventory.entrySet())
				{
					if(game.itemsData.findById(e.getKey()).smeltToID!=0)
					{
						if(e.getValue()>=2)
						{
						inProduction=e.getKey();
						prodTimer.setTimer(prodTimeLength);
						if(inventory.get(inProduction)>2)
							inventory.put(inProduction,inventory.get(inProduction)-2);
						else
							inventory.remove(inProduction);
							game.mAcontext.initInvenories();
						break;
						}
					}
				}

			}
			if(inProduction!=0)
			{
				
				if(prodTimer.tick())
				{

						if(inventory.containsKey(game.itemsData.findById(inProduction).smeltToID))
						{
							inventory.put(game.itemsData.findById(inProduction).smeltToID,inventory.get(game.itemsData.findById(inProduction).smeltToID)+1);
						}
						else
							inventory.put(game.itemsData.findById(inProduction).smeltToID,1);

					inProduction=0;
					MainActivity tAct=game.mAcontext;
					tAct.initInvenories();
				}
			}
			if(prodTimer.getTick()>0)
				prgBar.tick(prodTimer.getTick()/(prodTimeLength*0.6f));
		}
		super.calculateCollisionObject();*/

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
