package com.sb9.foloke.sectorb9.game.UI;
import com.sb9.foloke.sectorb9.game.Assets.*;
import android.graphics.*;
import android.widget.*;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
//import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import com.sb9.foloke.sectorb9.R;

import com.sb9.foloke.sectorb9.game.display.Game;
import android.widget.*;
import android.util.*;
import android.view.*;
import android.content.Context;
import com.sb9.foloke.sectorb9.game.entities.Entity;
import com.sb9.foloke.sectorb9.*;
//import java.util.*;
public class UIinventory
{
	private InventoryAsset asset;
	//private HashMap<int,int> inv_indexes; //item_index //count
	//private  inv_items[][]; //index,count
	private boolean visible;
	private TableLayout table;
	final private MainActivity context;
	//private int capacity;
	//private HashMap<Integer,Integer> inventoryItems;
	private Entity target;
	
	private UICommInterface exchangeInterface;

	public UIinventory(TableLayout table,final MainActivity context,Entity target,UICommInterface exchangeInterface)
	{
		this.target=target;
		//this.capacity=target.getInventoryMaxCapacity();
		//table.setStretchAllColumns(true);this.inventoryItems=inv_items;
		this.exchangeInterface=exchangeInterface;
		
		this.table=table;
		
		this.context=context;
		ScrollView.LayoutParams lp= new ScrollView.LayoutParams(ScrollView.LayoutParams.WRAP_CONTENT,context.getResources().getDisplayMetrics().heightPixels);
		lp.setMargins(10,10,10,10);
		/*Button closeButton=((MainActivity)context).findViewById(R.id.closeInventoryButton);
		closeButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v)
			{
				((MainActivity)context).getViewFlipper().setDisplayedChild(((MainActivity)context).getViewFlipper().indexOfChild(((MainActivity)context).findViewById(R.id.interactionUI)));
			}
		});*/
		if(target!=null)
		init();
	}
	public void init()
	{
		try
		{
		if(target==null)
			return;
		table.setVisibility(View.GONE);
		table.removeAllViews();
		BitmapFactory.Options options=new BitmapFactory.Options();
        options.inScaled=false;
			table.setBackground(new BitmapDrawable(target.getGame().mAcontext.getResources(),target.getGame().uiAsset.uiBgBlur));
		
		
		if(target.getInventory().size()>0)
		for(HashMap.Entry<Integer,Integer> entry : target.getInventory().entrySet()) {
			Integer key = entry.getKey();
			Integer value = entry.getValue();
			TableRow row=new TableRow(context);
			TableLayout.LayoutParams tableRowParams=
				new TableLayout.LayoutParams
			(TableLayout.LayoutParams.WRAP_CONTENT,TableLayout.LayoutParams.FILL_PARENT);
			
			int leftMargin=10;
			int topMargin=10;
			int rightMargin=10;
			int bottomMargin=10;
			
			tableRowParams.setMargins(leftMargin, topMargin, rightMargin, bottomMargin);
			row.setLayoutParams(tableRowParams);
			ImageView sprite=new ImageView(context);
			
			TextView testText2=new TextView(context);
			TableRow.LayoutParams trp=new TableRow.LayoutParams();
			trp.setMargins(10,10,10,10);
			trp.height=100;
			testText2.setTextColor(Color.parseColor("#ffffffff"));
			//button1.setLayoutParams(trp);
			
			testText2.setText(""+value);
			testText2.setLayoutParams(trp);
			BitmapDrawable bdrawable;
				bdrawable = new BitmapDrawable(context.getResources(),context.getGame().itemsData.findById(key).image);
			
			sprite.setImageDrawable(bdrawable);
			final int itemId=key;
			final int itemCount=value;
			row.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					
					if(exchangeInterface.setSide(target,itemId,itemCount))
					{
					//
						v.setBackgroundColor(Color.RED);
						
					}
					else
					{
						exchangeInterface.game.getObjectUIInventory().init();
						exchangeInterface.game.getPlayerUIInventory().init();
					}
					if(!exchangeInterface.flagStartedExchange)
					{
						exchangeInterface.game.getObjectUIInventory().init();
						exchangeInterface.game.getPlayerUIInventory().init();
					}
					//v.setBackground();
					
				}
			});
			//button1.setScaleType(ImageView.ScaleType.FIT_XY);
			row.setId(key);
			
			row.addView(sprite);
			
			row.addView(testText2);
			table.addView(row);
		}
		for (int i=0;i<(target.getInventoryMaxCapacity()-target.getInventory().size());i++)
		{
			TableRow row=new TableRow(context);
			TableLayout.LayoutParams tableRowParams=
				new TableLayout.LayoutParams
			(TableLayout.LayoutParams.WRAP_CONTENT,TableLayout.LayoutParams.FILL_PARENT);

			int leftMargin=10;
			int topMargin=10;
			int rightMargin=10;
			int bottomMargin=10;

			tableRowParams.setMargins(leftMargin, topMargin, rightMargin, bottomMargin);
			row.setLayoutParams(tableRowParams);

			ImageView sprite=new ImageView(context);
			TableRow.LayoutParams trp=new TableRow.LayoutParams();
			trp.setMargins(10,10,10,10);
			trp.height=100;
		
			BitmapDrawable bdrawable;
			
			bdrawable = new BitmapDrawable(context.getResources(),context.getGame().itemsData.findById(0).image);
			sprite.setImageDrawable(bdrawable);
			
			row.setOnClickListener
			(new OnClickListener() 
				{
					@Override
					public void onClick(View v) 
					{
						if(exchangeInterface.flagStartedExchange==true)
						{
							if(exchangeInterface.setSide(target,0,0))
								exchangeInterface.game.getObjectUIInventory().init();
							exchangeInterface.game.getPlayerUIInventory().init();
						}
					}
				});
			row.setId(0);
			row.addView(sprite);
			table.addView(row);
		}	
		//TableLayout ttable=table;
		if(table.getVisibility()==View.GONE)
		table.setVisibility(View.VISIBLE);
		target.getGame().errorText.setString("all good");
		}
		catch(Exception e)
		{ target.getGame().errorText.setString(e.toString());}
	}
	public void setVisability(boolean visability)
	{
		this.visible=visability;
	}
	public TableLayout getTable()
	{
		return table;
	}
	public void setTarget(Entity target)
	{
		this.target=target;
	}
	public Entity getTarget()
	{
		return target;
	}
}
