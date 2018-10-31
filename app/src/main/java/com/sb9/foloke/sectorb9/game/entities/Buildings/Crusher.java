package com.sb9.foloke.sectorb9.game.entities.Buildings;
import com.sb9.foloke.sectorb9.game.entities.*;
import android.graphics.*;
import com.sb9.foloke.sectorb9.game.display.*;
import com.sb9.foloke.sectorb9.game.funtions.*;
import java.util.Map;
import com.sb9.foloke.sectorb9.*;
import com.sb9.foloke.sectorb9.game.UI.*;

public class Crusher extends StaticEntity
{
	private UIProgressBar prgBar;
	private int inProduction;
	private int count;
	private Timer prodTimer;
	public Crusher(float x, float y,float rotation, Bitmap image,String name,Game game)
	{
		super(x,y,rotation,image,name,game);
		this.inventoryMaxCapacity=3;
		this.opened=true;
		inProduction=0;
		count=0;prodTimer=new Timer(0);
		prgBar=new UIProgressBar(this,50,8,-25,-20,game.uiAsset.hpBackground,game.uiAsset.hpLine,game.uiAsset.progressBarBorder,prodTimer.getTick());
		
	}

	@Override
	public void render(Canvas canvas)
	{
		canvas.save();
		canvas.rotate(rotation,getCenterX(),getCenterY());
		canvas.drawBitmap(image,x,y,null);
		if(prodTimer.getTick()>0)
		prgBar.render(canvas);
		canvas.restore();
		// TODO: Implement this method
	}

	@Override
	public void tick()
	{
		if(inventory.size()>0&&inProduction==0)
		{
			//inProduction= inventory..;
			for(Map.Entry<Integer,Integer> e: inventory.entrySet())
			{
				if(e.getKey()==1)
					inProduction=e.getKey();
			}
			
			//keySet().toArray()[0];
			//count=inventory.get(inProduction);
		
			/*if(count==1)
			{
				
				inventory.remove(inProduction);
					//inProduction=0;
			}*/
			prodTimer.setTimer(1);
			
		}
		if(inProduction!=0)
		{
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
					inProduction=0;
				}
			//if(getGame().command==getGame().commandInteraction)
				
			
				MainActivity tAct=(MainActivity)game.mAcontext;
				tAct.initInvenories();
			
			}
		}
		if(prodTimer.getTick()>0)
		prgBar.tick(1/prodTimer.getTick());
		// TODO: Implement this method
	}


	
}
