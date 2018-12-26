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
	private TableLayout table;
	final private MainActivity context;
	private Entity target;
	InventoryExchangeInterface excInterface;
	
	
	

	public UIinventory(TableLayout table,final MainActivity context,Entity target,InventoryExchangeInterface excInterface)
	{
		this.target=target;	
		this.table=table;
		this.context=context;
		this.excInterface=excInterface;
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
			
		table.setVisibility(View.GONE);
		table.removeAllViews();
		BitmapFactory.Options options=new BitmapFactory.Options();
        options.inScaled=false;
		table.setBackground(new BitmapDrawable(target.getGame().mAcontext.getResources(),target.getGame().uiAsset.uiBgBlur));
		
		
			int height=target.getInventory().getHeight();
			int width=target.getInventory().getWidth();
			
		for(int i=0;i<height;i++)
		{
			final TableRow row=new TableRow(context);
			TableLayout.LayoutParams tableRowParams=
				new TableLayout.LayoutParams
			(TableLayout.LayoutParams.WRAP_CONTENT,TableLayout.LayoutParams.FILL_PARENT);

			
			row.setLayoutParams(tableRowParams);
			
			for(int j=0;j<width;j++)
			{
				final int x=j;
				final int y=i;
				final Inventory inventory=target.getInventory();
					ImageView sprite=new ImageView(context);
					TextView itemCountText=new TextView(context);
					
					itemCountText.setTextColor(Color.parseColor("#ffffffff"));
					int itemcount=target.getInventory().getItemOnPos(j,i).y;
					if(itemcount!=0)
					itemCountText.setText(itemcount+"");
					itemCountText.setTextSize(10);
					
					
					sprite.setImageDrawable(new BitmapDrawable(context.getResources(),context.getGame().itemsData.findById(target.getInventory().getItemOnPos(j,i).x).image));
					InventoryFrameLayout IFL=new InventoryFrameLayout(context);
					IFL.setX(j);
					IFL.setY(i);
					row.addView(IFL);
				
					IFL.setOnTouchListener(new OnTouchListener()
						{
							@Override
							public boolean onTouch(View v,MotionEvent e)
							{
								ClipData.Item item = new ClipData.Item((CharSequence)v.getTag());
								String[] mimeTypes = {ClipDescription.MIMETYPE_TEXT_PLAIN};

								ClipData dragData = new ClipData("bug",mimeTypes, item);
								View.DragShadowBuilder myShadow = new View.DragShadowBuilder(v);
								excInterface.started(inventory,x,y,inventory.getItemCountOnPos(x,y));
								
								v.startDrag(dragData,myShadow,null,0);
								return true;
							}
						});
						setDragAndDrop(IFL);
						
					IFL.addView(sprite);
					
					IFL.addView(itemCountText);
				}
				//empty
				
			table.addView(row);
		}

		if(table.getVisibility()==View.GONE)
		table.setVisibility(View.VISIBLE);
		target.getGame().errorText.setString("all good");
		}
		catch(Exception e)
		{ target.getGame().errorText.setString(e.toString());}
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
	public void setDragAndDrop(final InventoryFrameLayout IFL)
	{
		final int ID=target.getInventory().getItemOnPos(IFL.x,IFL.y).x;
		
	IFL.setOnDragListener(new View.OnDragListener() {
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
				excInterface.ended(target.getInventory(),IFL.x,IFL.y);
				
				// Do nothing
				break;
			default: break;
		}
		return true;
	}
	});}
}
