package com.sb9.foloke.sectorb9;


import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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

import com.sb9.foloke.sectorb9.game.display.Game;
import android.widget.*;
import android.util.*;
import android.view.*;
import com.sb9.foloke.sectorb9.game.UI.*;
import com.sb9.foloke.sectorb9.game.Assets.*;
import android.content.*;
import android.opengl.*;
import android.view.View.*;
import android.view.InputDevice.*;
import android.os.*;
import android.widget.ViewSwitcher.*;
import android.widget.CompoundButton.*;
import android.app.*;

public class MainActivity extends Activity {

    private Game game;
	public boolean inventoryOpened=false;
	
	private BitmapFactory.Options options;
	//private InventoryAsset invAsset;
	//private ScrollView playerScrollView;
	private ViewFlipper VF;
	//private ScrollView objectScrollView;
	
	private UImenu uiMenu=new UImenu();
	public UIinteraction uiInteraction=new UIinteraction();
	private UIaction uiAction =new UIaction();
	
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.main_activity);
        game=findViewById(R.id.Game);
		options=new BitmapFactory.Options();
        options.inScaled=false;
		//findViewById(R.id.UIFlipper).setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
		//findViewById(R.id.UIFlipper).setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
		//ActionBar actionBar = getActionBar();
		//actionBar.hide();
		//playerScrollView=findViewById(R.id.PlayerScrollView);
		//playerScrollView.setVisibility(View.GONE);
		//objectScrollView=findViewById(R.id.ObjectScrollView);
		//objectScrollView.setVisibility(View.GONE);
		
		TableLayout playerTable=findViewById(R.id.PlayerTableLayout);
		TableLayout objectTable=findViewById(R.id.ObjectTableLayout);

		game.initInventoryUI(playerTable,objectTable,this);
		
		VF = findViewById(R.id.UIFlipper);
		VF.setDisplayedChild(VF.indexOfChild(findViewById(R.id.actionUI)));
		
		
		uiAction.init(this,VF);
		uiInteraction.init(this,VF,null);
			
		
		
		
		
		findViewById(R.id.menuUILinearLayout).setBackground(new BitmapDrawable(this.getResources(),game.uiAsset.uiBgBlur));
		
		final FrameLayout menuUI=findViewById(R.id.menuUI);
		final MainActivity MA=this;
		
		Button menuButton=findViewById(R.id.Menu);
		menuButton.setOnClickListener
		
		(new OnClickListener() 
			{
				@Override
				public void onClick(View v) 
				{
					final int a=VF.getDisplayedChild();
					uiMenu.init(MA,VF,a);
					//uiMenu.init(this,VF,a);
					VF.setDisplayedChild(VF.indexOfChild(menuUI));
					game.setPause(true);
					v.setVisibility(View.GONE);
				}
			});
    }
	
	
	
	
	/*public void switchPlayerInventory()
	{
		if(!inventoryOpened)
		{
			//mapPanel.openNewInventory(mapPanel.getPlayerUIInventory(),mapPanel.getplayer().getInventory());
			//playerScrollView.setActivated(true);
			playerScrollView.setVisibility(View.VISIBLE);
			if(game.getObjectUIInventory().getTarget()!=null)
			objectScrollView.setVisibility(View.VISIBLE);
			
			inventoryOpened=true;
			
			game.getPlayer().setDrawInteractionCicle(true);
		}
		else
		{
			//mapPanel.closeInventory(mapPanel.getPlayerUIInventory());
			//playerScrollView.setActivated(false);
			playerScrollView.setVisibility(View.GONE);	
			if(game.getObjectUIInventory().getTarget()!=null)
			objectScrollView.setVisibility(View.GONE);
			
			inventoryOpened=false;
			
			game.getPlayer().setDrawInteractionCicle(false);
		}
	}*/
	/*public void closeObjectInventory()
	{
		runOnUiThread(new Runnable() {  
				@Override
				public void run() {
		objectScrollView.setVisibility(View.GONE);}});
		
	}
	public void openObjectInventory()
	{
		runOnUiThread(new Runnable() {  
				@Override
				public void run() {
		objectScrollView.setVisibility(View.VISIBLE);
		}});
	}*/
	public void initInvenories()
	{
		runOnUiThread(new Runnable() {  
				@Override
				public void run() {
					// TODO Auto-generated method stub

					game.getObjectUIInventory().init();
				}});
	}
	public ViewFlipper getViewFlipper()
	{
		return VF;
	}
	public Game getGame()
	{
		return game;
	}
}
