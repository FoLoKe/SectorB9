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
import android.content.*;
import android.view.View.*;
import com.sb9.foloke.sectorb9.game.UI.Inventory.*;
import java.util.*;
import android.widget.ActionMenuView.*;
//import java.util.*;
public class UIinventory
{
	int selectedItemID;
	private boolean visible;
	private TableLayout table;
	final private MainActivity context;
	private Entity target;
	InventoryExchangeInterface excInterface;
	
	ArrayList items;
	private class Element
	{
		
		public int ID;
		public int count;
		public Element(int ID,int count)
		{
			this.ID=ID;
			this.count=count;
		}
	}

	public UIinventory(TableLayout table,final MainActivity context,Entity target,InventoryExchangeInterface excInterface)
	{
		this.target=target;	
		this.table=table;
		this.context=context;
		this.excInterface=excInterface;
		ScrollView.LayoutParams lp= new ScrollView.LayoutParams(ScrollView.LayoutParams.WRAP_CONTENT,context.getResources().getDisplayMetrics().heightPixels);
		lp.setMargins(10,10,10,10);
		if(target!=null)
		init();
	}
	
	///UNDER REFACTORING
	public void init()
	{
		try
		{
		if(target==null)
			return;
			items=new ArrayList<Element>();
		table.setVisibility(View.GONE);
		table.removeAllViews();
		BitmapFactory.Options options=new BitmapFactory.Options();
        options.inScaled=false;
			table.setBackground(new BitmapDrawable(target.getGame().mAcontext.getResources(),target.getGame().uiAsset.uiBgBlur));
		
		
		int maxRowElems=3;
		
		if(target.getInventory().size()>0)
		for(HashMap.Entry<Integer,Integer> entry : target.getInventory().entrySet()) {		
			items.add(new Element(entry.getKey(),entry.getValue()));			
		}
		
		/////inv maker
		boolean overcapacity=false;
		int maxElems=target.getInventoryMaxCapacity();
			if(maxElems<items.size())
			{
				maxElems=items.size();
				overcapacity=true;
			}
			int elem=0;
		for(;elem<maxElems;)
		{
			final TableRow row=new TableRow(context);
			TableLayout.LayoutParams tableRowParams=
				new TableLayout.LayoutParams
			(TableLayout.LayoutParams.WRAP_CONTENT,TableLayout.LayoutParams.FILL_PARENT);

			int leftMargin=10;
			int topMargin=10;
			int rightMargin=10;
			int bottomMargin=10;

			tableRowParams.setMargins(leftMargin, topMargin, rightMargin, bottomMargin);
			row.setLayoutParams(tableRowParams);
			
			for(int rowPos=0;rowPos<maxRowElems;rowPos++)
			{
				//real
				if(elem <items.size())
				{
					final int itemId=((Element)items.get(elem)).ID;
					final int itemCount=((Element)items.get(elem)).count;
					
					ImageView sprite=new ImageView(context);
					TextView itemCountText=new TextView(context);
					
					TableRow.LayoutParams trp=new TableRow.LayoutParams();
					trp.setMargins(10,10,10,10);
					trp.height=100;
					itemCountText.setTextColor(Color.parseColor("#ffffffff"));
					//button1.setLayoutParams(trp);

					itemCountText.setText("x"+itemCount);
					itemCountText.setLayoutParams(trp);
					BitmapDrawable bdrawable;
					bdrawable = new BitmapDrawable(context.getResources(),context.getGame().itemsData.findById(itemId).image);

					sprite.setImageDrawable(bdrawable);
					LinearLayout LL=new LinearLayout(context);
					row.addView(LL);
					LL.setOrientation(LinearLayout.HORIZONTAL);
					//LL.setBackgroundColor(Color.CY);
					LayoutParams LLP=new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
					LLP.setMargins(10,10,10,10);
					//LLP.height=100;
					//LLP.width=100;
					//LL.setLayoutParams(LLP);
					//LL.getLayoutParams().width=100;
					//LL.getLayoutParams().height=100;
					
				
					LL.setOnTouchListener(new OnTouchListener()
						{
							@Override
							public boolean onTouch(View v,MotionEvent e)
							{
								ClipData.Item item = new ClipData.Item((CharSequence)v.getTag());
								String[] mimeTypes = {ClipDescription.MIMETYPE_TEXT_PLAIN};

								ClipData dragData = new ClipData("bug",mimeTypes, item);
								View.DragShadowBuilder myShadow = new View.DragShadowBuilder(v);
								excInterface.started(target,itemCount,itemId);
								selectedItemID=itemId;
								v.startDrag(dragData,myShadow,null,0);
								return true;
							}
						});
						//setDragAndDrop(sprite,itemId);
						
					LL.addView(sprite);
					
					LL.addView(itemCountText);
					
					//table.addView(row);
					setDragAndDrop(LL,itemId);
				}
				//empty
				else
				{
					if(!overcapacity)
					{
					ImageView sprite=new ImageView(context);
					TableRow.LayoutParams trp=new TableRow.LayoutParams();
					trp.setMargins(10,10,10,10);
					trp.height=100;

					BitmapDrawable bdrawable;

					bdrawable = new BitmapDrawable(context.getResources(),context.getGame().itemsData.findById(0).image);
					sprite.setImageDrawable(bdrawable);
					sprite.setLayoutParams(trp);

					sprite.setOnTouchListener(
						new OnTouchListener() 
						{
							@Override
							public boolean onTouch(View v,MotionEvent e) 
							{

								return true;
							}
						});
					//row.setId(0);
					row.addView(sprite);
					setDragAndDrop(sprite,0);
					}
				}
				elem++;
			}
			table.addView(row);
		}

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
	public void setDragAndDrop(View img,final int ID)
	{
	img.setOnDragListener(new View.OnDragListener() {
	@Override
	public boolean onDrag(View v, DragEvent event) {
		switch(event.getAction()) {
			case DragEvent.ACTION_DRAG_STARTED:
				if (ID==0||ID==excInterface.getItemID())
				v.setBackgroundColor(Color.GREEN);
				break;

			
			case DragEvent.ACTION_DRAG_ENDED   :
				
				v.setBackgroundColor(Color.TRANSPARENT);
				// Do nothing
				break;

			case DragEvent.ACTION_DROP:
				v.setBackgroundColor(Color.RED);
				excInterface.ended(target);
				
				// Do nothing
				break;
			default: break;
		}
		return true;
	}
	});}
}
