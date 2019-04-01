package com.sb9.foloke.sectorb9.game.UI;
import android.widget.*;
import com.sb9.foloke.sectorb9.*;
import android.view.View.*;
import android.view.*;
import android.graphics.*;
import android.graphics.drawable.*;

import com.sb9.foloke.sectorb9.game.Assets.UIAsset;
import com.sb9.foloke.sectorb9.game.DataSheets.BuildingsDataSheet;
import com.sb9.foloke.sectorb9.game.Entities.*;
import com.sb9.foloke.sectorb9.game.Assets.*;

import java.util.Random;
import com.sb9.foloke.sectorb9.game.DataSheets.*;


public class BuildUI
{
	private static int ObjectID=0;
	private static View prevPressed;

	public static void init(final MainActivity MA)
	{
        final ViewFlipper VF = MA.findViewById(R.id.UIFlipper);
		ScrollView.LayoutParams lp= new ScrollView.LayoutParams(ScrollView.LayoutParams.WRAP_CONTENT,MA.getResources().getDisplayMetrics().heightPixels);
		lp.setMargins(10,10,10,10);
		MA.findViewById(R.id.buildTableLayout).setVisibility(View.VISIBLE);
		TableLayout table=MA.findViewById(R.id.buildTableLayout);
		int count = table.getChildCount();
		for (int i = 0; i < count; i++) {
			View child = table.getChildAt(i);
			if (child instanceof TableRow) ((ViewGroup) child).removeAllViews();
		}
		table.removeAllViews();
		table.removeAllViewsInLayout();
		BitmapFactory.Options options=new BitmapFactory.Options();
        options.inScaled=false;
		table.setBackground(new BitmapDrawable(MA.getResources(),UIAsset.uiBgBlur));
		for(int i = 1; i< BuildingsDataSheet.getLength(); i++)
		{
			if(!BuildingsDataSheet.findById(i).buildable)
				continue;
			TableRow row=new TableRow(MA);
			ImageView sprite=new ImageView(MA);
			TextView testText=new TextView(MA);
				
			TableRow.LayoutParams trp=new TableRow.LayoutParams();
			trp.setMargins(10,10,10,10);
				
			testText.setTextColor(Color.parseColor("#ffffffff"));
				
			testText.setLayoutParams(trp);
			ObjectID=i;
				
			BitmapDrawable bdrawable;
			bdrawable = new BitmapDrawable(MA.getResources(),BuildingsDataSheet.findById(ObjectID).image);

			sprite.setImageDrawable(bdrawable);
				
			row.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					ObjectID=(v).getId();
					v.setBackgroundColor(Color.RED);
					if(prevPressed!=null&&prevPressed!=v)
					{
						prevPressed.setBackgroundColor(Color.parseColor("#00000000"));
						
					}
					initInfo(MA);
					prevPressed=v;
					MA.getGameManager().getGamePanel().getCursor().setImage(BuildingsDataSheet.findById(ObjectID).image);
				}
			});

			row.setId(ObjectID);
			
			row.addView(sprite);
			row.addView(testText);
			
			table.addView(row);
			ObjectID=0;
		}
		
		Button closeButton=MA.findViewById(R.id.closeBuildButton);
		closeButton.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				deinit(MA);
				VF.setDisplayedChild(VF.indexOfChild(MA.findViewById(R.id.interactionUI)));
			}
		});
	
		Button buildBotton=MA.findViewById(R.id.buildObject);
		buildBotton.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
			    if(MA.getGameManager().getGamePanel().getCursor().onBuild())
			    {
                    Entity e = MA.getGameManager().createBuildable(ObjectID, MA.getGameManager().getPlayer());
					if(e!=null)
					{
						initInfo(MA);
                    	MA.getGameManager().getWorldManager().getEntityManager().addObject(e);
                    	e.setCenterX(MA.getGameManager().getGamePanel().pointOfTouch.x);
                    	e.setCenterY(MA.getGameManager().getGamePanel().pointOfTouch.y);
                    	e.setWorldRotation(new Random().nextInt(360));
                    	e.calculateCollisionObject();
					}
                }
			}
		});
	}
	public static void deinit(MainActivity MA)
	{
		TableLayout table=MA.findViewById(R.id.buildTableLayout);
		if(table==null)
			return;
			
		MA.getGameManager().getGamePanel().getCursor().setImage(ShipAsset.cursor);
		int count = table.getChildCount();

		for (int i = 0; i < count; i++) {
			
			TableRow child = (TableRow)table.getChildAt(i);

			child.removeAllViews();
			child.removeAllViewsInLayout();
		}
		table.removeAllViews();
		table.removeAllViewsInLayout();

	}
	
	private static void initInfo(MainActivity MA)
	{
		
		TableLayout TL=MA.findViewById(R.id.buildInfoTableLayout);
		TL.setBackground(new BitmapDrawable(MA.getResources(),UIAsset.uiBgBlur));
		TL.removeAllViews();
		int i=0;
		for(int id:BuildingsDataSheet.findById(ObjectID).resToBuild)
		{
			TableRow row=new TableRow(MA);
			ImageView IV=new ImageView(MA);
			TextView TV=new TextView(MA);

			TableRow.LayoutParams trp=new TableRow.LayoutParams();
			trp.setMargins(10,10,10,10);

			TV.setTextColor(Color.parseColor("#ffffffff"));

			TV.setLayoutParams(trp);
			

			BitmapDrawable bdrawable;
			bdrawable = new BitmapDrawable(MA.getResources(),ItemsDataSheet.findById(id).image);

			IV.setImageDrawable(bdrawable);
			row.addView(IV);
			
			TV.setText(ItemsDataSheet.findById(id).name+" "+BuildingsDataSheet.findById(ObjectID).countToBuild[i]+"/"+MA.getGameManager().getPlayer().getInventory().countItem(BuildingsDataSheet.findById(ObjectID).resToBuild[i]));
			row.addView(TV);
			TL.addView(row);
			i++;
		}
		
	}
}
