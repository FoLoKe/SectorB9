package com.sb9.foloke.sectorb9.game.entities.Buildings;
import com.sb9.foloke.sectorb9.game.entities.*;
import android.graphics.*;
import com.sb9.foloke.sectorb9.game.display.*;
import com.sb9.foloke.sectorb9.game.funtions.*;
import java.util.Map;

public class Crusher extends StaticEntity
{
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
	}

	@Override
	public void render(Canvas canvas)
	{
		canvas.drawBitmap(image,x,y,null);
		// TODO: Implement this method
	}

	@Override
	public void tick()
	{
		if(inventory.size()>0&&inProduction==0)
		{
			//inProduction= inventory..;
			
			
			inProduction=inventory.keySet().toArray()[0];
			count=inventory.get(inProduction);
		
			/*if(count==1)
			{
				
				inventory.remove(inProduction);
					//inProduction=0;
			}*/
			prodTimer.setTimer(1);
		}
		if(inProduction!=0&&inProduction!=0)
		{
			if(prodTimer.tick())
			{
				inProduction=0;
				if(inventory.containsKey(inProduction))
				{
				//int v=
				//inventory.remove(inProduction);
				if(inventory.get(inProduction)>1)
					inventory.put(inProduction,inventory.get(inProduction)-1);
				else
					inventory.remove(inProduction);
					if(inventory.containsKey(inProduction+1))
					{
						inventory.put(inProduction+1,inventory.get(inProduction+1)+1);
					}
					else
						inventory.put(inProduction+1,1);
				}
			//if(getGame().command==getGame().commandInteraction)
				
			game.initObjInventory();
			
			
			}
		}
		
		// TODO: Implement this method
	}


	
}
