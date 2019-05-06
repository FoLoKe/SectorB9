package com.sb9.foloke.sectorb9.game.UI;
import com.sb9.foloke.sectorb9.*;
import com.sb9.foloke.sectorb9.game.Assets.UIAsset;
import com.sb9.foloke.sectorb9.game.Managers.GameManager;

import android.widget.*;
import android.view.View.*;
import android.view.*;
import android.graphics.drawable.*;
import com.sb9.foloke.sectorb9.game.Entities.Weapons.*;
import com.sb9.foloke.sectorb9.game.UI.CustomViews.*;
import android.graphics.*;

public class ActionUI
{
	//TODO: BUTTONS SIZE FROM SCREEN SIZE
	private static boolean weaponsOpened=false;
	private static ImageButton collectAllButton;
	private static float scaleX=1;
	private static float scaleY=1;
	private static MainActivity MA;
	public static void init(final MainActivity MA_in)
	{
        //
	    MA=MA_in;
        final ViewFlipper VF = MA.findViewById(R.id.UIFlipper);
		//to prevent texture overblurring scale by one axis scaleX=MA.getGameManager().getGamePanel().canvasW/2500;
		scaleY=MA.getGameManager().getScreenSize().y/1600f;
		if(scaleX>2)
			scaleX=2;
			if(scaleX<0.5f)
				scaleX=0.5f;
		scaleX=scaleY;
		
		
		Button interactionButton = MA.findViewById(R.id.Interaction);
      
        interactionButton.setOnClickListener
		(new OnClickListener() 
			{
				@Override
				public void onClick(View v) 
				{
					MA.getGameManager().command= GameManager.commandInteraction;
					VF.setDisplayedChild(VF.indexOfChild(MA.findViewById(R.id.interactionUI)));
					InteractionUI.init(MA,null);
				}
			});
		ImageButton shootButton = MA.findViewById(R.id.shootButton);
		shootButton.setBackground(null);
		shootButton.setImageBitmap(Bitmap.createScaledBitmap(UIAsset.shootButton,(int)(100*scaleX),(int)(100*scaleY),false));
		
		
		shootButton.setOnTouchListener
		(new OnTouchListener(){
				@Override
				public boolean onTouch(View view,MotionEvent e)
				{
					switch(e.getAction())
					{
						case MotionEvent.ACTION_DOWN:
							MA.getGameManager().getPlayer().shootFlag=true;

							break;
						case MotionEvent.ACTION_UP:
							MA.getGameManager().getPlayer().shootFlag=false;
							break;
					}
					return false;
				}
			});
			
		ImageButton weaponsButton = MA.findViewById(R.id.action_ui_weapons_button);
		BitmapFactory.Options bitmapOptions=new BitmapFactory.Options();
        bitmapOptions.inScaled=false;
		///2500 screenW
		///100 normal size for 32 px
		//so button size is scaled 3.125 for 2500 it is 100%
		
		//for 1800 scale =(int) 3.125*1800/2500
		
		
		weaponsButton.setBackgroundDrawable(new BitmapDrawable(MA.getResources(),Bitmap.createScaledBitmap(BitmapFactory.decodeResource(MA.getResources(),R.drawable.ui_weaponsbutton,bitmapOptions),(int)(50*scaleX),(int)(100*scaleY),false)));
		//weaponsButton.getLayoutParams().width=(int)(150*scaleX);
		weaponsButton.setOnClickListener(new OnClickListener()
		{
			public void onClick(View p)
			{
				initWeapons(MA);
			}
		});

		collectAllButton=MA.findViewById(R.id.action_u_collectAllButton);
		collectAllButton.setBackgroundDrawable(new BitmapDrawable(MA.getResources(),Bitmap.createScaledBitmap(UIAsset.collectDebrisButton,(int)(50*scaleX),(int)(50*scaleY),false)));
		collectAllButton.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				MA.getGameManager().collectDebris();
			}
		});
			
	}
	
	public static void update(final boolean collectButtonState)
	{
		if(MA!=null)
		MA.runOnUiThread(new Runnable()
		{
			public void run()
			{
				if (collectButtonState)
                    MA.findViewById(R.id.action_u_collectAllButton).setVisibility(View.VISIBLE);
				else
                    MA.findViewById(R.id.action_u_collectAllButton).setVisibility(View.GONE);
			}
		});
	}
	
	private static void initWeapons(final MainActivity MA)
	{
		LinearLayout LL=MA.findViewById(R.id.action_ui_WeaponsList);
		
		//LL.scal
		if(weaponsOpened)
		{
			weaponsOpened=false;
			LL.removeAllViews();
			LL.setVisibility(View.GONE);
			
		}
		else
		{
			LL.setVisibility(View.VISIBLE);
			weaponsOpened=true;
			Weapon[] weapons= MA.getGameManager().getPlayer().getShip().getWeapons();
			for(Weapon w:weapons)
			{
				WeaponButton wb=new WeaponButton(MA);
				wb.setWeapon(w);
				wb.setText(w.getName());
				wb.setTextColor(Color.WHITE);
				setWeaponButtonState(wb,MA);
				
				wb.setOnClickListener(new OnClickListener()
				{
					public void onClick(View v)
					{
						((WeaponButton)v).getWeapon().setEnabled(!((WeaponButton)v).getWeapon().getEnabled());
						setWeaponButtonState((WeaponButton)v,MA);
					}
				});
				LL.addView(wb);
			}
		}
	}
	
	private static void setWeaponButtonState(WeaponButton wb,MainActivity MA)
	{
		BitmapFactory.Options bitmapOptions=new BitmapFactory.Options();
        bitmapOptions.inScaled=false;
		if(!wb.getWeapon().getEnabled())
		{
			wb.setBackgroundDrawable(new BitmapDrawable(MA.getResources(),Bitmap.createScaledBitmap(BitmapFactory.decodeResource(MA.getResources(),R.drawable.ui_weapon_enabled,bitmapOptions),(int)(100*scaleX),(int)(50*scaleY),false)));
		}
		else
			wb.setBackgroundDrawable(new BitmapDrawable(MA.getResources(),Bitmap.createScaledBitmap(BitmapFactory.decodeResource(MA.getResources(),R.drawable.ui_weapon_disabled,bitmapOptions),(int)(100*scaleX),(int)(50*scaleY),false)));
	}
}
