package com.sb9.foloke.sectorb9.game.UI.TechUIs;
import android.widget.*;
import com.sb9.foloke.sectorb9.*;
import android.graphics.*;
import com.sb9.foloke.sectorb9.game.Funtions.*;
import java.util.*;
import com.sb9.foloke.sectorb9.game.UI.CustomViews.*;
import android.view.*;

public class TechUI
{
	public static ArrayList<Integer> existing=new ArrayList<>();
	public static int maxHorizontalLevel=0;
	public static ArrayList<TechUIObject> techsButtons=new ArrayList<>();
	static MainActivity MA;
	
	public static void init(MainActivity in_MA)
	{
		MA=in_MA;
		AbsoluteLayout MAL=(MA.findViewById(R.id.tech_ui_AbsoluteLayout));
		if(MAL.getChildCount()>0)
			MAL.removeAllViews();


		
	
		MAL.setBackgroundColor(Color.parseColor("#55ffffff"));
		for(TechTree.Tech t:TechTree.getTechs())
		{
			stackButtonsAdd(t,0);			
		}
		updatePositions();
		
	}
	
	private static void updateStates()
	{
		for(TechUIObject ib:techsButtons)
		{
			TechTree.Tech t=TechTree.getTech(ib.techID);
			if(t.isAvailable())
			{
				if(t.isOpened())
					ib.setBackgroundColor(Color.parseColor("#5500ff00"));
				else
					ib.setBackgroundColor(Color.parseColor("#55ffff00"));
			}
			else
			ib.setBackgroundColor(Color.parseColor("#55ffffff"));
		}
		
		for(TechUIObject t:techsButtons)
		{
			t.updateLinks();
			for(TechUILink tul:t.links)
			{

				tul.updateState();
			}
		}
	}
	
	public static void updatePositions()
	{
		int maxVerticalLevel=0;
		ArrayList<Integer> objectsPerLevel=new ArrayList<>();
		//find max count
		for(int level=0;level<=maxHorizontalLevel;level++)
		{
			int objForLevel=0;
			for(TechUIObject t:techsButtons)
			{
				if(t.hLevel==level)
				{						
					objForLevel++;
				}
			}
			if(objForLevel>maxVerticalLevel)
				maxVerticalLevel=objForLevel;
				
			objectsPerLevel.add(objForLevel);
		}
		
		for(int level=0;level<=maxHorizontalLevel;level++)
		{
			
	
			//apply cords
			int countOfObjects=0;
			for(TechUIObject t:techsButtons)
			{
				if(t.hLevel==level)
				{
					AbsoluteLayout.LayoutParams params = (AbsoluteLayout.LayoutParams)t.getLayoutParams();
					params.x = level*320;
					
					//if maxV=5 and in level only one cord shold be 1/2*5*320
					//if maxV=7 and in level two	  cord shold be 1/(2*2)*7*320
					float y=(1f/2f*(maxVerticalLevel)*320f-1f/2f*objectsPerLevel.get(level)*320f+countOfObjects*320f);
					params.y = (int)y;
					params.height=100;
					params.width=100;
					countOfObjects++;
					t.setLayoutParams(params);
				}
			}
		}
		
		for(TechUIObject t:techsButtons)
		{
			t.updateLinks();
			for(TechUILink tul:t.links)
			{
				
				tul.updateState();
			}
		}
	}
	
	private static TechUIObject stackButtonsAdd(TechTree.Tech t,int level)
	{
		if(t==null)
		{
			GameLog.update("wrong tech link",1);
			return null;
		}
		
		
		
		TechUIObject ib=null;
		
		AbsoluteLayout MAL=(MA.findViewById(R.id.tech_ui_AbsoluteLayout));
		if(!existing.contains(t.getID()))
		{
			if(maxHorizontalLevel<=level)
				maxHorizontalLevel=level;
			ib=new TechUIObject(MA,t.getID(),level);
			techsButtons.add(ib);
			MAL.addView(ib);
			BitmapFactory.Options bitmapOptions=new BitmapFactory.Options();
			bitmapOptions.inScaled=false;
			///2500 screenW
			///100 normal size for 32 px
			//so button size is scaled 3.125 for 2500 it is 100%
			
			//for 1800 scale =(int) 3.125*1800/2500
			float scaleY=0;
			float scaleX=MA.getGameManager().getScreenSize().y/1600f;
			if(scaleX>2)
				scaleX=2;
			if(scaleX<0.5f)
				scaleX=0.5f;
			scaleX=scaleY;

			existing.add(t.getID());
			ib.setText(t.getID()+"");
			ib.setTextSize(15);
			if(t.isAvailable())
			{
				if(t.isOpened())
					ib.setBackgroundColor(Color.parseColor("#1100ff00"));
				else
					ib.setBackgroundColor(Color.parseColor("#55ffff00"));
			}
			else
				ib.setBackgroundColor(Color.parseColor("#55ffffff"));
				
						ib.setOnClickListener(new View.OnClickListener()
						{
							public void onClick(View v)
							{
								TechTree.onActionWithTech(((TechUIObject)v).techID);
								updateStates();
							};
						});
			for(int ntid:t.getNext())
			{
				
				stackButtonsAdd(TechTree.getTech(ntid),level+1);
				
			}
			
		
		
		for(int ntid:t.getNext())
		{
			
		TechUIObject tstart=findTechObject(t.getID());
		TechUIObject tfinish=findTechObject(ntid);
		if(tfinish!=null)
			if(tstart!=null)
			{
				TechUILink tul =new TechUILink(MA,tfinish,tstart);
				tstart.links.add(tul);
				MAL.addView(tul);
			}
		}
		}
			
		
		return ib; ///TODO: FIND EXISTING
	}
	
	public static TechUIObject findTechObject(int id)
	{
		
		for(TechUIObject tb:techsButtons)
		{
			if(tb.techID==id)
				return tb;
		}
		return null;
	}
	
}
