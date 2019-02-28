package com.sb9.foloke.sectorb9.game.UI;

import com.sb9.foloke.sectorb9.game.Assets.*;
import android.graphics.*;
import android.widget.*;
import android.graphics.Bitmap;
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
import com.sb9.foloke.sectorb9.game.UI.ShipUis.*;
//import java.util.*;
public class ShipUI
{
	int selectedItemID;
	
	private static TableLayout table;
	private	static MainActivity context;
	private static Entity target;
	private static InventoryExchangeInterface excInterface;
	private static ShipArrangement shipArrangement;
	

	public static void setUI(Entity player,final MainActivity con)
	{
		target=player;
		context=con;
		//this.excInterface=excInterface;
		table=context.findViewById(R.id.shipUI_InvTable);
		shipArrangement=new ShipArrangement(context);
		if(target!=null&&table!=null)
			init();
		
	}

	///UNDER REFACTORING
	public static void init()
	{
		try
		{
			Button closeButton=context.findViewById(R.id.shipuiCloseButton);
			closeButton.setOnClickListener(new OnClickListener(){

					public void onClick(View v)
					{
						context.getViewFlipper().setDisplayedChild(context.getViewFlipper().indexOfChild(context.findViewById(R.id.interactionUI)));
					}});
			
			table=context.findViewById(R.id.shipUI_InvTable);
			if(target==null||table==null)
				return;
			
			table.setVisibility(View.GONE);
			table.removeAllViews();
			BitmapFactory.Options options=new BitmapFactory.Options();
			options.inScaled=false;
			table.setBackground(new BitmapDrawable(context.getResources(),UIAsset.uiBgBlur));
			
			ImageView imageViewForBackground=context.findViewById(R.id.shipUI_shipImage);
			Bitmap bitmap=target.getSprite();
			imageViewForBackground.setImageBitmap(bitmap);
			imageViewForBackground.setScaleType(ImageView.ScaleType.MATRIX);
			Matrix m = imageViewForBackground.getImageMatrix();
		
			RectF drawableRect = new RectF(0, 0, bitmap.getWidth(), bitmap.getHeight());
			RectF viewRect = new RectF(0, 0, 200, 200);
			m.setRectToRect(drawableRect, viewRect, Matrix.ScaleToFit.CENTER);
			imageViewForBackground.setImageMatrix(m);

			// add ImageView to the Layout
			
			// set LinearLayout as ContentView
			
			
			//spriteForBackground.setImageBitmap();

			int maxRowElems=3;

		
				

			
			for(int i=0;i<10;)
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
						ImageView sprite=new ImageView(context);
						TextView itemCountText=new TextView(context);

						TableRow.LayoutParams trp=new TableRow.LayoutParams();
						trp.setMargins(10,10,10,10);
						
						itemCountText.setTextColor(Color.parseColor("#ffffffff"));
						//button1.setLayoutParams(trp);

						itemCountText.setText("x"+0);
						itemCountText.setLayoutParams(trp);
						BitmapDrawable bDrawable;
						bDrawable = new BitmapDrawable(context.getResources(), ItemsDataSheet.findById(0).image);

						sprite.setImageDrawable(bDrawable);
						InventoryLinearLayout LL=new InventoryLinearLayout(context);
						row.addView(LL);
						LL.setOrientation(LinearLayout.HORIZONTAL);
						//LL.setBackgroundColor(Color.CY);
						//LayoutParams LLP=new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
						//LLP.setMargins(10,10,10,10);

						LL.setOnTouchListener(new OnTouchListener()
							{
								@Override
								public boolean onTouch(View v,MotionEvent e)
								{
									ClipData.Item item = new ClipData.Item((CharSequence)v.getTag());
									String[] mimeTypes = {ClipDescription.MIMETYPE_TEXT_PLAIN};

									ClipData dragData = new ClipData("bug",mimeTypes, item);
									View.DragShadowBuilder myShadow = new View.DragShadowBuilder(v);
									//excInterface.started(target,itemCount,itemId);
									//selectedItemID=itemId;
									v.startDrag(dragData,myShadow,null,0);
									return true;
								}
							});
						LL.addView(sprite);
						LL.addView(itemCountText);
						i++;
						//table.addView(row);
						//setDragAndDrop(sprite,itemId,false);
					}
		
				table.addView(row);
				}
				
			

			if(table.getVisibility()==View.GONE)
				table.setVisibility(View.VISIBLE);
			shipArrangement.init();

		}
		catch(Exception e)
		{ System.out.println(e); }
	}
	
	public static TableLayout getTable()
	{
		return table;
	}
	public static void setTarget(Entity t)
	{
		target=t;
	}
	public static Entity getTarget()
	{
		return target;
	}
	public static void setDragAndDrop(View img,final int ID,final boolean right)
	{
		img.setOnDragListener(new View.OnDragListener() {
				@Override
				public boolean onDrag(View v, DragEvent event) {
					switch(event.getAction()) {
						case DragEvent.ACTION_DRAG_STARTED:
							shipArrangement.started(target,1,ID);
								v.setBackgroundColor(Color.GREEN);
							break;


						case DragEvent.ACTION_DRAG_ENDED   :

							v.setBackgroundColor(Color.TRANSPARENT);
							// Do nothing
							break;

						case DragEvent.ACTION_DROP:
							v.setBackgroundColor(Color.RED);
							
							shipArrangement.ended(right,v.getId());
							// Do nothing
							break;
						default: break;
					}
					return true;
				}
			});}
}
