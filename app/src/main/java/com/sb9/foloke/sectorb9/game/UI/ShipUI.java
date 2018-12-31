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
import com.sb9.foloke.sectorb9.game.UI.ShipUis.*;
//import java.util.*;
public class ShipUI
{
	int selectedItemID;
	
	private TableLayout table;
	final private MainActivity context;
	private Entity target;
	InventoryExchangeInterface excInterface;
	ShipArrangement shipArrangement;
	

	public ShipUI(Entity player,final MainActivity context)
	{
		this.target=player;
		this.context=context;
		//this.excInterface=excInterface;
		this.table=this.context.findViewById(R.id.shipUI_InvTable);
		shipArrangement=new ShipArrangement(context,this);
		if(target!=null&&table!=null)
			init();
		
	}

	///UNDER REFACTORING
	public void init()
	{
		try
		{
			Button closeButton=context.findViewById(R.id.shipuiCloseButton);
			closeButton.setOnClickListener(new OnClickListener(){

					public void onClick(View v)
					{
						context.getViewFlipper().setDisplayedChild(context.getViewFlipper().indexOfChild(context.findViewById(R.id.interactionUI)));
					}});
			
			this.table=this.context.findViewById(R.id.shipUI_InvTable);
			if(target==null||table==null)
				return;
			
			table.setVisibility(View.GONE);
			table.removeAllViews();
			BitmapFactory.Options options=new BitmapFactory.Options();
			options.inScaled=false;
			table.setBackground(new BitmapDrawable(target.getGame().mAcontext.getResources(),target.getGame().uiAsset.uiBgBlur));
			
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
						BitmapDrawable bdrawable;
						bdrawable = new BitmapDrawable(context.getResources(),context.getGame().itemsData.findById(0).image);

						sprite.setImageDrawable(bdrawable);
						InventoryLinearLayout LL=new InventoryLinearLayout(context);
						row.addView(LL);
						LL.setOrientation(LinearLayout.HORIZONTAL);
						//LL.setBackgroundColor(Color.CY);
						LayoutParams LLP=new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
						LLP.setMargins(10,10,10,10);

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
	public void setDragAndDrop(View img,final int ID,final boolean right)
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
