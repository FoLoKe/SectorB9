package com.sb9.foloke.sectorb9.game.entities.Buildings;
import com.sb9.foloke.sectorb9.game.entities.*;
import android.graphics.*;
import com.sb9.foloke.sectorb9.game.display.*;
import com.sb9.foloke.sectorb9.game.funtions.*;
import java.util.Map;
import com.sb9.foloke.sectorb9.*;
import com.sb9.foloke.sectorb9.game.UI.*;
import com.sb9.foloke.sectorb9.game.Assets.*;

public class Crusher extends StaticEntity
{
	private UIProgressBar prgBar;
	private UIcustomImage statusImage;
	
	private int inProduction;
	private int count;
	private Timer prodTimer;
	//private PointF collisionPoints[];
	private PointF collisionInitPoints[];
	//private Line2D collisionlines[];
	private Animation crusherAnim;
	
	public Crusher(float x, float y,float rotation,ObjectsAsset  objAsset,String name,Game game)
	{
		super(x,y,rotation,objAsset.crusher,name,game);
		crusherAnim=new Animation(objAsset.crusherAnim,15);
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
		if(inventory.size()>0&&inProduction==0)
		{
			//inProduction= inventory..;
			for(Map.Entry<Integer,Integer> e: inventory.entrySet())
			{
				if(e.getKey()==1)
				{
					inProduction=e.getKey();
				prodTimer.setTimer(2);
				}
			}
			
		}
		if(inProduction!=0)
		{
			crusherAnim.tick();
			if(prodTimer.tick())
			{
				
				if(inventory.containsKey(inProduction))
				{
				//int v=
				//inventory.remove(inProduction);
				if(inventory.get(inProduction)>1)
					inventory.put(inProduction,inventory.get(inProduction)-1);
				else
					inventory.remove(inProduction);
					if(inventory.containsKey(4))
					{
						inventory.put(4,inventory.get(4)+1);
					}
					else
						inventory.put(4,1);
					
				}
				inProduction=0;
				MainActivity tAct=(MainActivity)game.mAcontext;
				tAct.initInvenories();
			//if(getGame().command==getGame().commandInteraction)
				
			
				
			
			}
		}
		if(prodTimer.getTick()>0)
			prgBar.tick(prodTimer.getTick()/(1.2f));
		}
		calculateCollisionObject();
		// TODO: Implement this method
	}

	@Override
	public void calculateCollisionObject()
	{
		// TODO: Implement this method
		super.calculateCollisionObject();
		calculateCustomCollisionObject();
	}
	//@Override
	


	
}
