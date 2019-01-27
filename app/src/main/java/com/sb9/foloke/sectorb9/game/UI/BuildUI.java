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
import com.sb9.foloke.sectorb9.game.Entities.Buildings.*;


public class BuildUI
{
	private static int ObjectID;
	private View prevPressed;
	public void init(final MainActivity MA,final ViewFlipper VF)
	{
		
		ScrollView.LayoutParams lp= new ScrollView.LayoutParams(ScrollView.LayoutParams.WRAP_CONTENT,MA.getResources().getDisplayMetrics().heightPixels);
		lp.setMargins(10,10,10,10);
		MA.findViewById(R.id.buildTableLayout).setVisibility(View.VISIBLE);
		TableLayout table=MA.findViewById(R.id.buildTableLayout);
		table.removeAllViews();
		BitmapFactory.Options options=new BitmapFactory.Options();
        options.inScaled=false;
		table.setBackground(new BitmapDrawable(MA.getResources(),UIAsset.uiBgBlur));
		for(int i = 1; i< BuildingsDataSheet.getLenght()+1; i++)
		{
				TableRow row=new TableRow(MA);
				
				
				ImageView sprite=new ImageView(MA);
				TextView testText=new TextView(MA);
				
				TableRow.LayoutParams trp=new TableRow.LayoutParams();
				trp.setMargins(10,10,10,10);
				
				testText.setTextColor(Color.parseColor("#ffffffff"));
				
				testText.setLayoutParams(trp);
				ObjectID=i;
				testText.setText(ObjectID+"");
				
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
							if(prevPressed!=null)
							prevPressed.setBackgroundColor(Color.parseColor("#00000000"));
							prevPressed=v;
						}
					});
				row.setId(ObjectID);
			
				row.addView(sprite);
				row.addView(testText);
				table.addView(row);
		ObjectID=0;
	}
		
	
	Button closeButton=MA.findViewById(R.id.closeBuildButton);
	closeButton.setOnClickListener
	(new OnClickListener() 
	{
	@Override
	public void onClick(View v) 
	{
		VF.setDisplayedChild(VF.indexOfChild(MA.findViewById(R.id.interactionUI)));
	}
	});
	
		Button buildBotton=MA.findViewById(R.id.buildObject);
		buildBotton.setOnClickListener
		(new OnClickListener() 
			{
				@Override
				public void onClick(View v) 
				{
					switch (ObjectID)
					{
						case 0:
							break;
						case 1:
							MA.getGameManager().addObject(new SmallCargoContainer(MA.getGameManager().getTouchPoint().x-16,MA.getGameManager().getTouchPoint().y-16,10,MA.getGameManager()));
							break;
						case 2:
							MA.getGameManager().addObject(new Crusher(MA.getGameManager().getTouchPoint().x-16,MA.getGameManager().getTouchPoint().y-16,10,MA.getGameManager()));
							break;
						case 3:
							MA.getGameManager().addObject(new SolarPanel(MA.getGameManager().getTouchPoint().x-16,MA.getGameManager().getTouchPoint().y-16,10,MA.getGameManager()));
							break;
						case 4:
							MA.getGameManager().addObject(new FuelGenerator(MA.getGameManager().getTouchPoint().x-16,MA.getGameManager().getTouchPoint().y-16,10,MA.getGameManager()));
							break;
						case 5:
							MA.getGameManager().addObject(new BigSmelter(MA.getGameManager().getTouchPoint().x-16,MA.getGameManager().getTouchPoint().y-16,10,MA.getGameManager()));
							break;
						case 6:
							MA.getGameManager().addObject(new Assembler(MA.getGameManager().getTouchPoint().x-16,MA.getGameManager().getTouchPoint().y-16,10,MA.getGameManager()));
							break;
					}
				}
			});
	}
}
