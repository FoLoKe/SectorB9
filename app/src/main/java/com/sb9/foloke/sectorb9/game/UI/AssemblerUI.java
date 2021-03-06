package com.sb9.foloke.sectorb9.game.UI;
import com.sb9.foloke.sectorb9.*;
import android.widget.*;
import android.view.*;
import android.graphics.drawable.*;
import android.graphics.*;
import android.text.*;
import java.util.*;

import com.sb9.foloke.sectorb9.game.DataSheets.ItemsDataSheet;
import com.sb9.foloke.sectorb9.game.Entities.Buildings.*;
import com.sb9.foloke.sectorb9.game.Entities.*;

public class AssemblerUI
{
	private static Integer[] itemsIDs;
	private static MainActivity MA;
	private static ArrayList<Integer> inQueue;
	private static int inProduction=0;
	private static int newQueue=0;
	private static boolean opened;

	public  static void init(final MainActivity mainActivity,final Assembler target)
	{
		try
		{
			MA=mainActivity;
           // final ViewFlipper VF = MA.findViewById(R.id.UIFlipper);
		    inQueue=target.getQueue();
		
		    inProduction=target.getInProduction();
		    
			
		    //table
		    TableLayout TL=MA.findViewById(R.id.assembler_ui_TL_toProduceList);
		    TL.removeAllViews();
		
		    int j=0;
		    for(int i = 0; i<ItemsDataSheet.getLength(); i++)
		    {
			    if(!ItemsDataSheet.findById(i).madeFrom.containsKey(0))
			    {
				    j++;
			    }
		    }
		
		    itemsIDs=new Integer[j];
		    j=0;
		    for(int i = 0; i<ItemsDataSheet.getLength(); i++)
		    {
			    if(!ItemsDataSheet.findById(i).madeFrom.containsKey(0))
			    {
				    itemsIDs[j]=ItemsDataSheet.findById(i).ID;
				    j++;
			    }
		    }

		    updateQueue(target);

					
		    int maxRowElems=4;

		    for(int i=0; i<itemsIDs.length;)
		    {
					
		        final TableRow row=new TableRow(MA);
		        TableLayout.LayoutParams tableRowParams= new TableLayout.LayoutParams
					(TableLayout.LayoutParams.WRAP_CONTENT,TableLayout.LayoutParams.MATCH_PARENT);

		        int leftMargin=10;
		        int topMargin=10;
		        int rightMargin=10;
		        int bottomMargin=10;

		        tableRowParams.setMargins(leftMargin, topMargin, rightMargin, bottomMargin);
		        row.setLayoutParams(tableRowParams);

		        for(int rowPos=0;rowPos<maxRowElems;rowPos++)
		        {
		            if(i<itemsIDs.length)
		            {
		                ImageView icon=new ImageView(MA);
						icon.setId(itemsIDs[i]);

						row.addView(icon);
						icon.setOnClickListener(new View.OnClickListener()
						{
							public void onClick(View view)
							{
								initHelp(view.getId());
								newQueue=view.getId();
							}
						});
						TableRow.LayoutParams trp=new TableRow.LayoutParams();
						trp.setMargins(10,10,10,10);
								

						BitmapDrawable bdrawable= new BitmapDrawable(MA.getResources(),ItemsDataSheet.findById(itemsIDs[i]).image);
						icon.setImageDrawable(bdrawable);
						icon.setLayoutParams(trp);
						}
                    i++;
					}
					TL.addView(row);
				}

		    Button setProdButton=MA.findViewById(R.id.assembler_uiSetProdButton);
			setProdButton.setOnClickListener(new View.OnClickListener(){
				@Override
				public void onClick(View v)
				{
					
					target.addQueue(newQueue);
				}
			});
			
		
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}

	private static void initHelp(int pressedID)
    {
        LinearLayout LR=MA.findViewById(R.id.AssemblerUIHelpLayoutOfInfo);
        LR.removeAllViews();
        int textC=Color.WHITE;
        BitmapDrawable bdrawable;
				
        if(!ItemsDataSheet.findById(pressedID).madeFrom.containsKey(0))
        {
            //title
            TextView madeFromText = new TextView(MA);
            madeFromText.setText(Html.fromHtml("can be " + "<font color=\"#00c792\">"+"prodused "+"</font>"+"<font color=\"#ffffff\">"+"from: "));
            madeFromText.setTextColor(textC);
            madeFromText.setGravity(Gravity.LEFT|Gravity.TOP);
            madeFromText.setTextSize(25);
            LR.addView(madeFromText);

            for(Map.Entry e:ItemsDataSheet.findById(pressedID).madeFrom.entrySet())
            {
                //for each component create horizontal layout
                LinearLayout MLR=new LinearLayout(MA);
                MLR.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT));
                MLR.setOrientation(LinearLayout.HORIZONTAL);

                //component name
                TextView itemName = new TextView(MA);
                String stringItemName =ItemsDataSheet.findById((int)e.getKey()).name+" ";
                itemName.setText(stringItemName);
                itemName.setTextColor(textC);
                itemName.setGravity(Gravity.LEFT|Gravity.TOP);
                itemName.setTextSize(25);
                MLR.addView(itemName);

                //image of component
                bdrawable = new BitmapDrawable(MA.getResources(),ItemsDataSheet.findById((int)e.getKey()).image);
                ImageView imageOfNededItem=new ImageView(MA);
                imageOfNededItem.setImageDrawable(bdrawable);
                imageOfNededItem.setId((int)e.getKey());
						
                MLR.addView(imageOfNededItem);

                //count of components needed
                TextView itemCount = new TextView(MA);
                String stringItemCount =" x"+ e.getValue()+" ";
                itemCount.setText(stringItemCount);
                itemCount.setTextColor(textC);
                itemCount.setGravity(Gravity.LEFT|Gravity.TOP);
                itemCount.setTextSize(25);
                MLR.addView(itemCount);

                //to vertical layout
                LR.addView(MLR);
            }
        }
    }
	
	public static void updateQueue(Assembler caller)
	{
		inProduction=caller.getInProduction();
		MA.runOnUiThread(new Runnable(){public void run(){
		LinearLayout LLofQueue=MA.findViewById(R.id.assembler_uiImageInQueue);
		LLofQueue.removeAllViews();

		for(Integer e:inQueue)
		{
			ImageView icon=new ImageView(MA);
			icon.setId(e);

			LLofQueue.addView(icon);
			icon.setOnClickListener(new View.OnClickListener()
				{
					public void onClick(View view)
					{
						initHelp(view.getId());
						newQueue=view.getId();
					}
				});
			TableRow.LayoutParams trp=new TableRow.LayoutParams();
			trp.setMargins(10,10,10,10);


			BitmapDrawable bdrawable= new BitmapDrawable(MA.getResources(),ItemsDataSheet.findById(e).image);
			icon.setImageDrawable(bdrawable);
			icon.setLayoutParams(trp);
		}

		MA.findViewById(R.id.assembler_uiImageInProd).setBackgroundDrawable(new BitmapDrawable(MA.getResources(),ItemsDataSheet.findById(inProduction).image));
		}});
	}
	public static void updateCurrentProduction(Assembler caller)
	{
		inProduction=caller.getInProduction();
		MA.runOnUiThread(new Runnable(){public void run(){
		MA.findViewById(R.id.assembler_uiImageInProd).setBackgroundDrawable(new BitmapDrawable(MA.getResources(),ItemsDataSheet.findById(inProduction).image));
	}});
	}
    public static void setOpened(boolean state)
    {
        opened=state;
    }

    public static boolean getOpened()
    {
        return opened;
    }
}
