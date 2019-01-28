package com.sb9.foloke.sectorb9.game.UI;
import android.widget.*;
import com.sb9.foloke.sectorb9.*;
import android.view.View.*;
import android.view.*;

import com.sb9.foloke.sectorb9.game.Assets.*;
import android.graphics.*;

import java.util.*;
import com.sb9.foloke.sectorb9.game.Funtions.*;
import com.sb9.foloke.sectorb9.game.UI.CustomViews.*;



public class MapUI
{
	private MapTile pastClickedView;
	private MapTile playerMapPositionView;
	private static LineView line;
	private MainActivity MA;
	private Point currentSector;
	private final int mapWidth=32;
	private final int mapHeight=32;
	private ArrayList<MapTile> mapTiles=new ArrayList<MapTile>();

	public MapUI(final MainActivity MA)
	{
		//line=new LineView(MA);
		this.MA=MA;
	}
	private class MapTile extends ImageView
	{
		int x,y;
		TableRow row;
		public MapTile(MainActivity context,int x,int y,TableRow row)
		{
			super(context);
			this.x=x;
			this.y=y;
			this.row=row;
		}
		public Point getPoint()
		{
			return new Point(x,y);
		}
		public TableRow getRow()
		{
			return row;
		}
	}

	public void init(final MainActivity MA ,final ViewFlipper VF)
	{
		currentSector=MA.getGameManager().getCurrentSector();

		MA.findViewById(R.id.map_closeButton).setOnClickListener(
		new OnClickListener(){
			@Override
			public void onClick(View v)
			{	
				VF.setDisplayedChild(VF.indexOfChild(MA.findViewById(R.id.menuUI)));
			}
		});
		
		MA.findViewById(R.id.map_uiWarpButton).setOnClickListener(
		new OnClickListener(){
			@Override
			public void onClick(View v)
			{
				warpToSector(pastClickedView);
				MA.toActionFast();

			}

		});
		FrameLayout TFR=MA.findViewById(R.id.map_uiSecondFrameLayout);
		
		TFR.removeViewInLayout(line);
		
		
		final LineView line2=new LineView(MA);

		TFR.addView(line2);
		///THE HELL IT IS NOT WORKING
		//line=line2;
		
		//line2.invalidate();
		TableLayout table=MA.findViewById(R.id.map_uiTilesTable);
		table.removeAllViews();
			
				for(int j=0;j<mapHeight+1;j++)
				{
					TableRow row=new TableRow(MA);
					TextView rowHead=new TextView(MA);
					rowHead.setText(j+"");
					rowHead.setTextColor(Color.WHITE);
					if(j!=0)
					{
						row.addView(rowHead);
						for(int i=1;i<mapWidth+1;i++)
						{
						MapTile tile=new MapTile(MA,i,j,row);
						mapTiles.add(tile);
						tile.setImageBitmap(InventoryAsset.inv_empty);
						tile.setOnClickListener(new OnClickListener(){
							@Override
							public void onClick(View v)
							{
								if(pastClickedView!=null)
									pastClickedView.setBackgroundColor(Color.TRANSPARENT);
								v.setBackgroundColor(Color.RED);
								pastClickedView=(MapTile)v;
								
										line2.setLine(getMapLine());
										line2.invalidate();
										line=line2;
								
								update();
							}
						});
						row.addView(tile);
					}
					}
					else
					{
						for(int i=0;i<mapWidth+1;i++)
						{
							
							TextView columnHead=new TextView(MA);
							columnHead.setText(i+"");
							columnHead.setTextColor(Color.WHITE);
							if(i!=0)
							{
								row.addView(columnHead);
							}
							else
								row.addView(new TextView(MA));
						}
					}
						table.addView(row);
				}
			for(MapTile tile:mapTiles)
			{
				if(tile.getPoint().equals(currentSector.x,currentSector.y))
				{
					tile.setBackgroundColor(Color.parseColor("#00ffff"));
					playerMapPositionView=tile;
				}
			}
	}
	private Line2D getMapLine()
	{
		int a[]=new int[2];
		playerMapPositionView.getLocationOnScreen(a);
		return new Line2D(playerMapPositionView.getX()+playerMapPositionView.getWidth()/2,playerMapPositionView.getRow().getY()+playerMapPositionView.getHeight()/2,pastClickedView.getX()+pastClickedView.getWidth()/2,pastClickedView.getRow().getY()+pastClickedView.getHeight()/2);
	}

	private void update()
	{

	}

	private void warpToSector(MapTile tile)
    {
		MA.getGameManager().warpToLocation(tile.x,tile.y);
    };
}
