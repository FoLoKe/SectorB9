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
	private TableLayout playerTable,objectTable;
	final private MainActivity context;
	private Entity playerTarget,objectTarget;
	private InventoryExchangeInterface excInterface;
	private int countToTransfer=1;
	
	

	public UIinventory(TableLayout playerTable,Entity playerTarget,TableLayout objectTable,Entity objectTarget,InventoryExchangeInterface excInterface,MainActivity MA)
	{
		this.playerTarget=playerTarget;	
		this.playerTable=playerTable;
		this.objectTable=objectTable;
		this.objectTarget=objectTarget;
		this.context=MA;
		this.excInterface=excInterface;
		
		
		update(null);
		Button but1=MA.findViewById(R.id.InventoryUI_oneItemButton);
		but1.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){countToTransfer=1;
			resetButtonColor(v);
			}});
			
		MA.findViewById(R.id.InventoryUI_halfItemButton).setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View v){countToTransfer=2;
				resetButtonColor(v);
		}});
				
		MA.findViewById(R.id.InventoryUI_allItemButton).setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View v){countToTransfer=3;
					resetButtonColor(v);
				}});
	}
	public void update(Entity caller)
	{
		if(caller==null||caller==playerTarget||caller==objectTarget)
		{
			if(playerTarget!=null||playerTable!=null)
			init(playerTable,playerTarget);
			if(objectTable!=null||objectTarget!=null)
			init(objectTable,objectTarget);
			resetButtonColor(null);
		}
	}
	
	public void resetButtonColor(View v)
	{
		context.findViewById(R.id.InventoryUI_halfItemButton).setBackgroundColor(Color.parseColor("#22ffffff"));
		context.findViewById(R.id.InventoryUI_allItemButton).setBackgroundColor(Color.parseColor("#22ffffff"));
		context.findViewById(R.id.InventoryUI_oneItemButton).setBackgroundColor(Color.parseColor("#22ffffff"));
		if(v!=null)
		v.setBackgroundColor(Color.parseColor("#55ffffff"));
		else
		switch (countToTransfer)
		{
			case 1:
				
				context.findViewById(R.id.InventoryUI_oneItemButton).setBackgroundColor(Color.parseColor("#55ffffff"));
				break;
			case 2:
				context.findViewById(R.id.InventoryUI_halfItemButton).setBackgroundColor(Color.parseColor("#55ffffff"));
				break;
			default:
				context.findViewById(R.id.InventoryUI_allItemButton).setBackgroundColor(Color.parseColor("#55ffffff"));
				break;
		}
		
		}
	///UNDER REFACTORING
	public void init(TableLayout table,Entity target)
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
			TableLayout.LayoutParams tableRowParams=new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT,TableLayout.LayoutParams.FILL_PARENT);
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
							excInterface.started(inventory,x,y,calculateCountTotransfer(inventory.getItemCountOnPos(x,y)));
								
							v.startDrag(dragData,myShadow,null,0);
							return true;
						}
					});
					setDragAndDrop(IFL,target);
						
					IFL.addView(sprite);
					IFL.addView(itemCountText);
				}
			table.addView(row);
			}
			
			if(table.getVisibility()==View.GONE)
				table.setVisibility(View.VISIBLE);
		}
		catch(Exception e){ target.getGame().errorText.setString(e.toString());}
	}
	
	public int calculateCountTotransfer(int count)
	{
		switch(countToTransfer)
		{
			case 1:
				count=1;
				break;
			case 2:
				if(count!=1)
				count=count/2;
				else
					count=1;
				break;
			default:
			break;
		}
		return count;
	}
	public int getCountToTransfer()
	{
		
		return countToTransfer;
	}
	public TableLayout getTableOfPlayer()
	{
		return playerTable;
	}
	public void setPlayerTarget(Entity target)
	{
		this.playerTarget=target;
	}
	public Entity getPlayerTarget()
	{
		return playerTarget;
	}
	
	public void setObjectTarget(Entity target)
	{
		this.objectTarget=target;
	}
	public Entity getObjectTarget()
	{
		return objectTarget;
	}
	
	public void setDragAndDrop(final InventoryFrameLayout IFL,final Entity target)
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
