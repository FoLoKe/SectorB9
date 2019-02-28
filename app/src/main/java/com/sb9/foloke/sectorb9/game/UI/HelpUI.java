package com.sb9.foloke.sectorb9.game.UI;
import com.sb9.foloke.sectorb9.*;
import android.widget.*;
import android.graphics.*;
import android.graphics.drawable.*;
import android.view.View.*;
import android.view.*;
import android.widget.FrameLayout.*;

import com.sb9.foloke.sectorb9.game.Assets.UIAsset;
import com.sb9.foloke.sectorb9.game.DataSheets.ItemsDataSheet;

import java.util.*;

import android.text.*;
import android.util.*;

public class HelpUI
{
	private static View prevPressed;
	private static int pressedID;
	
	
	public static void init(final MainActivity MA,final ViewFlipper VF,final int view)
	{
		///list init
		BitmapFactory.Options options=new BitmapFactory.Options();
        options.inScaled=false;
		MA.findViewById(R.id.HelpSectionFrameLayout).setBackground(new BitmapDrawable(MA.getResources(),UIAsset.uiBgBlur));
		MA.findViewById(R.id.helpSectionScrollView).setBackground(new BitmapDrawable(MA.getResources(),UIAsset.uiBgBlur));
		((TableLayout)MA.findViewById(R.id.HelpSectionTableView)).removeAllViews();
		
		//close button
		Button closeMenuButton=MA.findViewById(R.id.HelpSectionCloseButton);
		closeMenuButton.setOnClickListener
		(new OnClickListener() 
			{
				@Override
				public void onClick(View v) 
				{
					VF.setDisplayedChild(view);
					MA.getGameManager().setPause(false);
					MA.findViewById(R.id.Menu).setVisibility(View.VISIBLE);
				}
			});

		//list containment
		for(int i = 1; i<ItemsDataSheet.getLenght(); i++)
		{
			TextView text= new TextView(MA);
			text.setText(
			ItemsDataSheet.findById(i).name);
			text.setTextColor(Color.WHITE);
			TableRow row=new TableRow(MA);
			row.addView(text);
			row.setId(i);
			((TableLayout)MA.findViewById(R.id.HelpSectionTableView)).addView(row);
			
			//row listeners
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
	
	//on row click - display info:
	public static void setInfo(final MainActivity MA)
	{
		//standart text color
		int textC=Color.WHITE;
		
		//main layout
		LinearLayout LR=MA.findViewById(R.id.help_sectionLinearLayoutOfInfo);
		LR.removeAllViews();
		LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		layoutParams.gravity=(Gravity.CENTER|Gravity.TOP);
		
		//name of selected item
		TextView text= new TextView(MA);
		text.setText( ItemsDataSheet.findById(pressedID).name+" ");
		text.setTextColor(textC);
		text.setGravity(Gravity.CENTER|Gravity.TOP);
		text.setTextSize(25);
		LR.addView(text);
		
		//image of selected item
		ImageView imageOfitem=new ImageView(MA);
		BitmapDrawable bdrawable;
		bdrawable = new BitmapDrawable(MA.getResources(),ItemsDataSheet.findById(pressedID).image);
		imageOfitem.setImageDrawable(bdrawable);
		LR.addView(imageOfitem);
		
		//description of selected item
		TextView infoText= new TextView(MA);
		infoText.setText( ItemsDataSheet.findById(pressedID).info);
		infoText.setTextColor(textC);
		infoText.setGravity(Gravity.LEFT|Gravity.TOP);
		infoText.setTextSize(25);
		LR.addView(infoText);
		
		//can be prodused by crushing item:
		if(ItemsDataSheet.findById(pressedID).crushFromID!=0)
		{
			//title
			TextView crushedFromText = new TextView(MA);
			crushedFromText.setText(Html.fromHtml("can be " + "<font color=\"#81abc7\">"+"crushed "+"</font>"+"from: "));
			crushedFromText.setTextColor(textC);
			crushedFromText.setGravity(Gravity.LEFT|Gravity.TOP);
			crushedFromText.setTextSize(25);
			LR.addView(crushedFromText);
		
			//name of crushable item
			TextView crushObjectName = new TextView(MA);
			int crushid=ItemsDataSheet.findById(pressedID).crushFromID;
			crushObjectName.setText( ItemsDataSheet.findById(crushid).name+" ");
			crushObjectName.setTextColor(textC);
			crushObjectName.setGravity(Gravity.LEFT|Gravity.TOP);
			crushObjectName.setTextSize(25);
			
			
			bdrawable = new BitmapDrawable(MA.getResources(),ItemsDataSheet.findById(crushid).image);
			ImageView imageOfCrushableitem=new ImageView(MA);
			imageOfCrushableitem.setImageDrawable(bdrawable);
			imageOfCrushableitem.setId(crushid);
		
			//listener for image
			onSearch(imageOfCrushableitem,MA);
			
			//new horizontal linearLayout
			LinearLayout MLR=new LinearLayout(MA);
			MLR.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
			MLR.setOrientation(LinearLayout.HORIZONTAL);
			MLR.addView(crushObjectName);
			MLR.addView(imageOfCrushableitem);
			//added to vertical linearLayout
			LR.addView(MLR);
		
		}
		
		//can be prodused by smelting an item:
		if(ItemsDataSheet.findById(pressedID).smeltFromID!=0)
		{
			//title
			int smeltId=ItemsDataSheet.findById(pressedID).smeltFromID;
			String text1="can be ";
			String text2="smelted ";
			TextView smeltFromText = new TextView(MA);
			smeltFromText.setText(Html.fromHtml(text1 + "<font color=\"#c77800\">"+text2+"</font>"+"from: "));
			smeltFromText.setTextColor(textC);
			smeltFromText.setGravity(Gravity.LEFT|Gravity.TOP);
			smeltFromText.setTextSize(25);
			LR.addView(smeltFromText);
			
			//object name
			TextView smeltFromObjectName = new TextView(MA);
			smeltFromObjectName.setText(ItemsDataSheet.findById(ItemsDataSheet.findById(pressedID).smeltFromID).name+" ");
			smeltFromObjectName.setTextColor(textC);
			smeltFromObjectName.setGravity(Gravity.LEFT|Gravity.TOP);
			smeltFromObjectName.setTextSize(25);
			
			//image of an item
			bdrawable = new BitmapDrawable(MA.getResources(),ItemsDataSheet.findById(smeltId).image);
			ImageView imageOfSmeeltableItem=new ImageView(MA);
			imageOfSmeeltableItem.setImageDrawable(bdrawable);
			imageOfSmeeltableItem.setId(smeltId);
			onSearch(imageOfSmeeltableItem,MA);
			
			//horizontal layout
			LinearLayout MLR=new LinearLayout(MA);
			MLR.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
			MLR.setOrientation(LinearLayout.HORIZONTAL);
			MLR.addView(smeltFromObjectName);
			MLR.addView(imageOfSmeeltableItem);
			//to vertical
			LR.addView(MLR);
		}
		
		//can be produsedbfrom components:
		if(!ItemsDataSheet.findById(pressedID).madeFrom.containsKey(0))
		{
			//title
			TextView madeFromText = new TextView(MA);
			madeFromText.setText(Html.fromHtml("can be " + "<font color=\"#00c792\">"+"prodused "+"</font>"+"<font color=\"#ffffff\">"+"from: "));
			madeFromText.setTextColor(textC);
			madeFromText.setGravity(Gravity.LEFT|Gravity.TOP);
			madeFromText.setTextSize(25);
			LR.addView(madeFromText);
			
			for(Map.Entry e:ItemsDataSheet.findById(pressedID).madeFrom.entrySet())
			{
				//for each component create horizontal layout
				LinearLayout MLR=new LinearLayout(MA);
				MLR.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
				MLR.setOrientation(LinearLayout.HORIZONTAL);
			
				//component name
				TextView itemName = new TextView(MA);
				String stringItemName =ItemsDataSheet.findById((int)e.getKey()).name+" ";
				itemName.setText(stringItemName);
				itemName.setTextColor(textC);
				itemName.setGravity(Gravity.LEFT|Gravity.TOP);
				itemName.setTextSize(25);
				MLR.addView(itemName);
				
				//image of component
				bdrawable = new BitmapDrawable(MA.getResources(),ItemsDataSheet.findById((int)e.getKey()).image);
				ImageView imageOfNededItem=new ImageView(MA);
				imageOfNededItem.setImageDrawable(bdrawable);
				imageOfNededItem.setId((int)e.getKey());
				onSearch(imageOfNededItem,MA);
				MLR.addView(imageOfNededItem);
				
				//count of components needed
				TextView itemCount = new TextView(MA);
				String stringItemCount =" x"+ e.getValue()+" ";
				itemCount.setText(stringItemCount);
				itemCount.setTextColor(textC);
				itemCount.setGravity(Gravity.LEFT|Gravity.TOP);
				itemCount.setTextSize(25);
				MLR.addView(itemCount);
				
				//to vertical layout
				LR.addView(MLR);
			}
		}
			//recycling
			if(ItemsDataSheet.findById(pressedID).crushToID!=0||ItemsDataSheet.findById(pressedID).smeltToID!=0)
			{
				ImageView iv=new ImageView(MA);
				LR.addView(iv);
				int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, MA.getResources().getDisplayMetrics());
				iv.setBackgroundColor(Color.BLACK);
				LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(layoutParams.FILL_PARENT,height);
				
				
				
				iv.setLayoutParams(lp);
				String text1="can be ";
				String text2="recycled ";
				TextView title = new TextView(MA);
				title.setText(Html.fromHtml(text1 + "<font color=\"#00ff78\">"+text2+"</font>"+"by: "));
				title.setTextColor(textC);
				title.setGravity(Gravity.LEFT|Gravity.TOP);
				title.setTextSize(25);
				LR.addView(title);
				
				if(ItemsDataSheet.findById(pressedID).crushToID!=0)
				{
					TextView crushedFromText = new TextView(MA);
					crushedFromText.setText(Html.fromHtml("<font color=\"#81abc7\">"+"crusher "+"</font>"+"to: "));
					crushedFromText.setTextColor(textC);
					crushedFromText.setGravity(Gravity.LEFT|Gravity.TOP);
					crushedFromText.setTextSize(25);
					LR.addView(crushedFromText);

					//name of crushable item
					TextView crushObjectName = new TextView(MA);
					int crushid=ItemsDataSheet.findById(pressedID).crushToID;
					crushObjectName.setText( ItemsDataSheet.findById(crushid).name+" ");
					crushObjectName.setTextColor(textC);
					crushObjectName.setGravity(Gravity.LEFT|Gravity.TOP);
					crushObjectName.setTextSize(25);


					bdrawable = new BitmapDrawable(MA.getResources(),ItemsDataSheet.findById(crushid).image);
					ImageView imageOfCrushableitem=new ImageView(MA);
					imageOfCrushableitem.setImageDrawable(bdrawable);
					imageOfCrushableitem.setId(crushid);

					//listener for image
					onSearch(imageOfCrushableitem,MA);

					//new horizontal linearLayout
					LinearLayout MLR=new LinearLayout(MA);
					MLR.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
					MLR.setOrientation(LinearLayout.HORIZONTAL);
					MLR.addView(crushObjectName);
					MLR.addView(imageOfCrushableitem);
					//added to vertical linearLayout
					LR.addView(MLR);
				}
				if(ItemsDataSheet.findById(pressedID).smeltToID!=0)
				{
					int smeltId=ItemsDataSheet.findById(pressedID).smeltToID;
					
					TextView smeltFromText = new TextView(MA);
					smeltFromText.setText(Html.fromHtml("<font color=\"#c77800\">"+"smelter "+"</font>"+"to: "));
					smeltFromText.setTextColor(textC);
					smeltFromText.setGravity(Gravity.LEFT|Gravity.TOP);
					smeltFromText.setTextSize(25);
					LR.addView(smeltFromText);

					//object name
					TextView smeltFromObjectName = new TextView(MA);
					smeltFromObjectName.setText(ItemsDataSheet.findById(smeltId).name+" ");
					smeltFromObjectName.setTextColor(textC);
					smeltFromObjectName.setGravity(Gravity.LEFT|Gravity.TOP);
					smeltFromObjectName.setTextSize(25);

					//image of an item
					bdrawable = new BitmapDrawable(MA.getResources(),ItemsDataSheet.findById(smeltId).image);
					ImageView imageOfSmeeltableItem=new ImageView(MA);
					imageOfSmeeltableItem.setImageDrawable(bdrawable);
					imageOfSmeeltableItem.setId(smeltId);
					onSearch(imageOfSmeeltableItem,MA);

					//horizontal layout
					LinearLayout MLR=new LinearLayout(MA);
					MLR.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
					MLR.setOrientation(LinearLayout.HORIZONTAL);
					MLR.addView(smeltFromObjectName);
					MLR.addView(imageOfSmeeltableItem);
					//to vertical
					LR.addView(MLR);
				}
			}
	}

	public static void onSearch(View v,final MainActivity MA)
	{
		//lister for image
		v.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if(prevPressed!=null)
				prevPressed.setBackgroundColor(Color.parseColor("#00000000"));
				prevPressed=v;
				pressedID=v.getId();
				setInfo(MA);
			}
		});
	}
	public static void deinit(MainActivity MA)
	{
		//
		TableLayout table=MA.findViewById(R.id.HelpSectionTableView);
		if(table==null)
			return;

		
		int count = table.getChildCount();
	
		for (int i = 0; i < count; i++) {

			TableRow child = (TableRow)table.getChildAt(i);
			
			child.removeAllViews();
			child.removeAllViewsInLayout();

			
		}
		table.removeAllViews();
		table.removeAllViewsInLayout();
		
	}
}
