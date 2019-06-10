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
import com.sb9.foloke.sectorb9.game.Managers.*;



public class MapUI
{
	private static MapTile pastClickedView;
	private static MapTile playerMapPositionView;
	private static LineView line;
	private	static MainActivity MA;
	private static MapManager MM;
	
	private static Point currentSector;
	
	private static ArrayList<MapTile> mapTiles=new ArrayList<MapTile>();

	
	private static class MapTile extends ImageView
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

	public static void init(final MainActivity mainActivity)
	{MA=mainActivity;
        final ViewFlipper VF = MA.findViewById(R.id.UIFlipper);
		GameLog.update("Map UI: preparing",0);

		MM=MA.getGameManager().getMapManager();
		currentSector=MA.getGameManager().getCurrentSector();
		MM.getSector(currentSector.x,currentSector.y).discovered=true;
		MM.getSector(currentSector.x,currentSector.y).explored=true;
		int scanDepth=1;
		if(true)
		for(int i=-scanDepth;i<=scanDepth;i++)
			for(int j=-scanDepth;j<=scanDepth;j++)
			{	
				MapManager.Sector sector=MM.getSector(currentSector.x+i,currentSector.y+j);
				if(sector!=null)
					sector.discovered=true;
				
			}
			
		MM.getSector(currentSector.x,currentSector.y).discovered=true;
		MM.getSector(currentSector.x,currentSector.y).explored=true;
		
		GameLog.update("Map UI: discovered set",0);
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
		GameLog.update("Map UI: views set",0);
		TableLayout table=MA.findViewById(R.id.map_uiTilesTable);
		table.removeAllViews();
			
				for(int j=0;j<mainActivity.getGameManager().getMapManager().getMapHeight()+1;j++)
				{
					TableRow row=new TableRow(MA);
					TextView rowHead=new TextView(MA);
					rowHead.setText(j+"");
					rowHead.setTextColor(Color.WHITE);
					if(j!=0)
					{
						row.addView(rowHead);
						for(int i=1;i<mainActivity.getGameManager().getMapManager().getMapWidth()+1;i++)
						{
						MapTile tile=new MapTile(MA,i,j,row);
						mapTiles.add(tile);
						tile.setImageBitmap(null);  //////NO BITMAP
						MapManager.Sector sector=MM.getSector(i,j);
						if(sector!=null)
							{
							if(sector.discovered)
								tile.setBackgroundColor(Color.BLACK);
							if(sector.explored)
								tile.setBackgroundColor(Color.GREEN);
								if(MA.getGameManager().getMapManager().getSector(i,j).discovered)
							switch(MA.getGameManager().getMapManager().getSector(i,j).type)
							{
								case EMPTY:
									tile.setBackgroundColor(Color.BLUE);
									break;
								case STATION:
									tile.setBackgroundColor(Color.RED);
									break;
								case ASTEROID_CLUSTER:
									tile.setBackgroundColor(Color.YELLOW);
									break;
								case ASTEROID_BELT:
									tile.setBackgroundColor(Color.MAGENTA);
									break;
							}
							}
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
						for(int i=0;i<mainActivity.getGameManager().getMapManager().getMapWidth()+1;i++)
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
		GameLog.update("Map UI: tiles set",0);
			for(MapTile tile:mapTiles)
			{
				if(tile.getPoint().equals(currentSector.x,currentSector.y))
				{
					tile.setBackgroundColor(Color.parseColor("#00ffff"));
					playerMapPositionView=tile;
				}
			}
			GameLog.update("Map UI: READY",0);
	}
	private static Line2D getMapLine()
	{
		int a[]=new int[2];
		playerMapPositionView.getLocationOnScreen(a);
		return new Line2D(playerMapPositionView.getX()+playerMapPositionView.getWidth()/2,playerMapPositionView.getRow().getY()+playerMapPositionView.getHeight()/2,pastClickedView.getX()+pastClickedView.getWidth()/2,pastClickedView.getRow().getY()+pastClickedView.getHeight()/2);
	}

	private static void update()
	{

	}

	private static void warpToSector(MapTile tile)
    {
		MA.getGameManager().warpToLocation(tile.x,tile.y);
    };
}
