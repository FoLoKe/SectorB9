package com.sb9.foloke.sectorb9.game.UI;
import android.widget.*;
import com.sb9.foloke.sectorb9.*;
import android.view.View.*;
import android.view.*;
import android.graphics.*;
import android.graphics.drawable.*;

import com.sb9.foloke.sectorb9.game.Assets.UIAsset;
import com.sb9.foloke.sectorb9.game.entities.*;
import com.sb9.foloke.sectorb9.game.entities.Buildings.*;


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
		for(int i=1;i<MA.getGame().buildingsData.getLenght()+1;i++)
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
				bdrawable = new BitmapDrawable(MA.getResources(),MA.getGame().buildingsData.findById(ObjectID).image);

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
							MA.getGame().addObject(new SmallCargoContainer(MA.getGame().getTouchPoint().x-16,MA.getGame().getTouchPoint().y-16,10,MA.getGame()));
							break;
						case 2:
							MA.getGame().addObject(new Crusher(MA.getGame().getTouchPoint().x-16,MA.getGame().getTouchPoint().y-16,10,MA.getGame()));
							break;
						case 3:
							MA.getGame().addObject(new SolarPanel(MA.getGame().getTouchPoint().x-16,MA.getGame().getTouchPoint().y-16,10,MA.getGame()));
							break;
						case 4:
							MA.getGame().addObject(new FuelGenerator(MA.getGame().getTouchPoint().x-16,MA.getGame().getTouchPoint().y-16,10,MA.getGame()));
							break;
						case 5:
							MA.getGame().addObject(new BigSmelter(MA.getGame().getTouchPoint().x-16,MA.getGame().getTouchPoint().y-16,10,MA.getGame()));
							break;
						case 6:
							MA.getGame().addObject(new Assembler(MA.getGame().getTouchPoint().x-16,MA.getGame().getTouchPoint().y-16,10,MA.getGame()));
							break;
					}
				}
			});
	}
}
