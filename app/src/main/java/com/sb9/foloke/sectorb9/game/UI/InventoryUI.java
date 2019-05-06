package com.sb9.foloke.sectorb9.game.UI;
import com.sb9.foloke.sectorb9.game.Assets.*;

import android.widget.*;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
//import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.sb9.foloke.sectorb9.R;

import android.view.*;

import com.sb9.foloke.sectorb9.game.DataSheets.ItemsDataSheet;
import com.sb9.foloke.sectorb9.game.Entities.Entity;
import com.sb9.foloke.sectorb9.*;
import android.content.*;
import android.view.View.*;
import com.sb9.foloke.sectorb9.game.UI.Inventory.*;

//import java.util.*;
public class InventoryUI
{
	private static TableLayout playerTable,objectTable;
	//final private MainActivity context;
	private static Entity playerTarget,objectTarget;
	private static InventoryExchangeInterface excInterface;
	private static int countToTransfer=1;
	private static MainActivity MA;
	

	public static void set(TableLayout pTable, Entity pTarget, TableLayout oTable, Entity oTarget, InventoryExchangeInterface eInterface, MainActivity mainActivity)
	{
		MA=mainActivity;
		playerTarget=pTarget;	
		playerTable=pTable;
		objectTable=oTable;
		objectTarget=oTarget;
		
		excInterface=eInterface;
		
		
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
	public static void update(Entity caller)
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
	
	public static void resetButtonColor(View v)
	{
		MA.findViewById(R.id.InventoryUI_halfItemButton).setBackgroundColor(Color.parseColor("#22ffffff"));
		MA.findViewById(R.id.InventoryUI_allItemButton).setBackgroundColor(Color.parseColor("#22ffffff"));
		MA.findViewById(R.id.InventoryUI_oneItemButton).setBackgroundColor(Color.parseColor("#22ffffff"));
		if(v!=null)
		v.setBackgroundColor(Color.parseColor("#55ffffff"));
		else
		switch (countToTransfer)
		{
			case 1:
				
				MA.findViewById(R.id.InventoryUI_oneItemButton).setBackgroundColor(Color.parseColor("#55ffffff"));
				break;
			case 2:
				MA.findViewById(R.id.InventoryUI_halfItemButton).setBackgroundColor(Color.parseColor("#55ffffff"));
				break;
			default:
				MA.findViewById(R.id.InventoryUI_allItemButton).setBackgroundColor(Color.parseColor("#55ffffff"));
				break;
		}
		
		}
	///UNDER REFACTORING
	public static void init(TableLayout table,Entity target)
	{
		try
		{
		if(target==null)
			return;
			
		table.setVisibility(View.GONE);
		table.removeAllViews();
		BitmapFactory.Options options=new BitmapFactory.Options();
        options.inScaled=false;
		table.setBackground(new BitmapDrawable(MA.getResources(),UIAsset.uiBgBlur));
		
		
			int height=target.getInventory().getHeight();
			int width=target.getInventory().getWidth();
			int c=0;
			int maxC=target.getInventory().getCapacity();
			
		for(int i=0;i<height;i++)
		{
			if(c>=maxC)
				break;
			final TableRow row=new TableRow(MA);
			TableLayout.LayoutParams tableRowParams=new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT,TableLayout.LayoutParams.FILL_PARENT);
			row.setLayoutParams(tableRowParams);
			
			for(int j=0;j<width;j++)
			{
				if(c<maxC)
				{
				final int x=j;
				final int y=i;
				final Inventory inventory=target.getInventory();
				
				ImageView sprite=new ImageView(MA);
				TextView itemCountText=new TextView(MA);
				itemCountText.setTextColor(Color.parseColor("#ffffffff"));
				int itemcount=target.getInventory().getItemOnPos(j,i).y;
				if(itemcount!=0)
					itemCountText.setText(itemcount+"");
				itemCountText.setTextSize(10);
					
				sprite.setImageDrawable(new BitmapDrawable(MA.getResources(), ItemsDataSheet.findById(target.getInventory().getItemOnPos(j,i).x).image));
				InventoryFrameLayout IFL=new InventoryFrameLayout(MA);
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
							excInterface.started(inventory,x,y,calculateCountToTransfer(inventory.getItemCountOnPos(x,y)));
								
							v.startDrag(dragData,myShadow,null,0);
							return true;
						}
					});
					setDragAndDrop(IFL,target);
						
					IFL.addView(sprite);
					IFL.addView(itemCountText);
					}
					c++;
				}
			table.addView(row);
			}
			
			if(table.getVisibility()==View.GONE)
				table.setVisibility(View.VISIBLE);
		}
		catch(Exception e){
			System.out.println(e);

		}
	}
	
	private static int calculateCountToTransfer(int count)
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
	public static int getCountToTransfer()
	{
		
		return countToTransfer;
	}
	public static TableLayout getTableOfPlayer()
	{
		return playerTable;
	}
	public static void setPlayerTarget(Entity target)
	{
		playerTarget=target;
	}
	public static Entity getPlayerTarget()
	{
		return playerTarget;
	}
	
	public static void setObjectTarget(Entity target)
	{
		objectTarget=target;
	}
	public static Entity getObjectTarget()
	{
		return objectTarget;
	}
	
	private static void setDragAndDrop(final InventoryFrameLayout IFL,final Entity target)
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
