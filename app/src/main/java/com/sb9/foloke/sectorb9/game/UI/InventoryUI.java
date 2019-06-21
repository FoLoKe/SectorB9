package com.sb9.foloke.sectorb9.game.UI;
import android.content.*;
import android.graphics.*;
import android.graphics.drawable.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import com.sb9.foloke.sectorb9.*;
import com.sb9.foloke.sectorb9.game.Assets.*;
import com.sb9.foloke.sectorb9.game.DataSheets.*;
import com.sb9.foloke.sectorb9.game.Entities.*;
import com.sb9.foloke.sectorb9.game.UI.Inventory.*;

import com.sb9.foloke.sectorb9.game.Entities.Entity;

//import java.util.*;
public class InventoryUI
{
	private static TableLayout leftTable,rightTable;
	//final private MainActivity context;
	private static Entity leftObject,rightObject;
	private static InventoryExchangeInterface excInterface;
	private static int countToTransfer=1;
	private static MainActivity MA;
	
	private static PointF frameOffset=new PointF();
	private static PointF initialResizePoint=new PointF();
	
	public static void init(MainActivity mainActivity)
	{
		MA=mainActivity;
		
		leftTable=MA.findViewById(R.id.PlayerTableLayout);
        rightTable=MA.findViewById(R.id.ObjectTableLayout);
		
		excInterface=new InventoryExchangeInterface(MA.getGameManager());
		
		setWindowDragAndDrop(MA.findViewById(R.id.inventoryLeftInvLayout),MA.findViewById(R.id.inventoryLeftInvLayoutHead));
		setWindowDragAndDrop(MA.findViewById(R.id.inventoryRightInvLayout),MA.findViewById(R.id.inventoryRightInvLayout));
		setResizeButtons();
		
		MA.findViewById(R.id.InventoryUI_oneItemButton).setOnClickListener(new OnClickListener(){
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
		if(caller==null||caller==leftObject||caller==rightObject)
		{
			if(leftObject!=null)
			{
				((TextView)MA.findViewById(R.id.inventoryLeftTextView)).setText(leftObject.getName());
				update(leftTable,leftObject);
			}
			else
				leftTable.setVisibility(View.GONE);
				
			if(rightObject!=null)
			{
				((TextView)MA.findViewById(R.id.inventoryRightTextView)).setText(rightObject.getName());
				update(rightTable,rightObject);
			}
			else
				rightTable.setVisibility(View.GONE);
			resetButtonColor(null);
		}
	}
	
	private static void resetButtonColor(View v)
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
	
	private static void update(TableLayout table,Entity target)
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
			//setWindowDragAndDrop(table);
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
	
	public static void setRightSide(Entity side)
	{
		rightObject=side;
		update(null);
	}
	
	public static void setLeftSide(Entity side)
	{
			leftObject=side;
			rightObject=null;
			update(null);
	}
	
	
	
	private static void setResizeButtons()
	{
		MA.findViewById(R.id.inventoryLeftResizeButton).setOnTouchListener(new OnTouchListener(){
			
			@Override
			public boolean onTouch(View v, MotionEvent event)
			{
				switch(event.getAction())
				{
					case event.ACTION_DOWN:
						initialResizePoint.set(event.getRawX(),event.getRawY());
						break;
					case event.ACTION_MOVE:
						
						AbsoluteLayout.LayoutParams lp=(AbsoluteLayout.LayoutParams)MA.findViewById(R.id.inventoryLeftInvLayout).getLayoutParams();
						
						lp.height=lp.height-(int)(initialResizePoint.y-event.getRawY());
						lp.width=lp.width-(int)(initialResizePoint.x-event.getRawX());
						
						if(lp.width<600)
							lp.width=600;
						if(lp.height<400)
							lp.height=400;
						
						MA.findViewById(R.id.inventoryLeftInvLayout).setLayoutParams(lp);
						
						initialResizePoint.set(event.getRawX(),event.getRawY());
						break;
						
					
				}
				return true;
			}
		});
	}
	
	private static void setWindowDragAndDrop(final View window,final View windowHead)
	{
		
		
		window.setZ(1);
		windowHead.setOnTouchListener(new OnTouchListener()
			{
				@Override
				public boolean onTouch(View v,MotionEvent e)
				{
					switch(e.getAction())
					{
						case e.ACTION_DOWN:
							int[] location=new int[2];
							window.getLocationOnScreen(location);
							
							frameOffset.set(e.getRawX()-location[0],e.getRawY()-location[1]);
							break;
							
						case e.ACTION_MOVE:
							
						int[] framelocation=new int[2];
						MA.findViewById(R.id.inventoryAbsoluteLayout).getLocationOnScreen(framelocation);
						window.setX(e.getRawX()-frameOffset.x);
						window.setY(e.getRawY()-framelocation[1]-frameOffset.y);
						break;
					}
					
					return true;
				}
			});
		
		
		
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
