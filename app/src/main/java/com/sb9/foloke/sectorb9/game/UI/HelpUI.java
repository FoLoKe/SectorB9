package com.sb9.foloke.sectorb9.game.UI;
import com.sb9.foloke.sectorb9.*;
import android.widget.*;
import android.graphics.*;
import android.graphics.drawable.*;
import android.view.View.*;
import android.view.*;
import android.widget.FrameLayout.*;

public class HelpUI
{
	static View prevPressed;
	static int pressedID;
	public void init(final MainActivity MA,final ViewFlipper VF,final int view)
	{
		BitmapFactory.Options options=new BitmapFactory.Options();
        options.inScaled=false;
		MA.findViewById(R.id.HelpSectionFrameLayout).setBackground(new BitmapDrawable(MA.getGame().mAcontext.getResources(),MA.getGame().uiAsset.uiBgBlur));
		MA.findViewById(R.id.helpSectionScrollView).setBackground(new BitmapDrawable(MA.getGame().mAcontext.getResources(),MA.getGame().uiAsset.uiBgBlur));
		((TableLayout)MA.findViewById(R.id.HelpSectionTableView)).removeAllViews();
		Button closeMenuButton=MA.findViewById(R.id.HelpSectionCloseButton);
		closeMenuButton.setOnClickListener
		(new OnClickListener() 
			{
				@Override
				public void onClick(View v) 
				{
					
					VF.setDisplayedChild(view);
					MA.getGame().setPause(false);
					MA.findViewById(R.id.Menu).setVisibility(View.VISIBLE);
				}
			});
		for(int i=1;i<MA.getGame().itemsData.getLenght();i++)
		{
			TextView text= new TextView(MA);
			text.setText(
			MA.getGame().itemsData.findById(i).name);
			text.setTextColor(Color.WHITE);
			TableRow row=new TableRow(MA);
			row.addView(text);
			row.setId(i);
			((TableLayout)MA.findViewById(R.id.HelpSectionTableView)).addView(row);
			row.setOnClickListener(new OnClickListener()
				{
					@Override
					public void onClick(View v)
					{
						

						v.setBackgroundColor(Color.RED);
						if(prevPressed!=null)
							prevPressed.setBackgroundColor(Color.parseColor("#00000000"));
						prevPressed=v;
						
						pressedID=v.getId();
						setInfo(MA);
					}
				});
			
		}
	}
	public void setInfo(MainActivity MA)
	{
		int textC=Color.WHITE;
		
		LinearLayout LR=MA.findViewById(R.id.help_sectionLinearLayoutOfInfo);
		LR.removeAllViews();
		LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		layoutParams.gravity=(Gravity.CENTER|Gravity.TOP);
		
		TextView text= new TextView(MA);
		text.setText( MA.getGame().itemsData.findById(pressedID).name);
		text.setTextColor(textC);
		text.setGravity(Gravity.CENTER|Gravity.TOP);
		text.setTextSize(25);
		LR.addView(text);
		
		ImageView imageOfitem=new ImageView(MA);
		BitmapDrawable bdrawable;
		bdrawable = new BitmapDrawable(MA.getResources(),MA.getGame().itemsData.findById(pressedID).image);

		imageOfitem.setImageDrawable(bdrawable);
		//imageOfitem.
		//imageOfitem.setGravity(Gravity.CENTER|Gravity.TOP);
		LR.addView(imageOfitem);
		//int g=MA.getGame().itemsData.findById(pressedID).;
		
		if(MA.getGame().itemsData.findById(pressedID).crushFromID!=0)
		{
		TextView crushedFromText = new TextView(MA);
			crushedFromText.setText("from: "+ MA.getGame().itemsData.findById(MA.getGame().itemsData.findById(pressedID).crushFromID).name);
		crushedFromText.setTextColor(textC);
		crushedFromText.setGravity(Gravity.CENTER|Gravity.TOP);
		crushedFromText.setTextSize(25);
		LR.addView(crushedFromText);
		
		}
		
	}
}
