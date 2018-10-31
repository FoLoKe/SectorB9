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

public class MainActivity extends Activity {

    Game game;
	public boolean inventoryOpened=false;
	
	private BitmapFactory.Options options;
	private InventoryAsset invAsset;
	private ScrollView playerScrollView;
	
	private ScrollView objectScrollView;
	Button shootButton;
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.main_activity);
        game=findViewById(R.id.Game);
		options=new BitmapFactory.Options();
        options.inScaled=false;
		
		
		playerScrollView=findViewById(R.id.PlayerScrollView);
		playerScrollView.setVisibility(View.GONE);
		objectScrollView=findViewById(R.id.ObjectScrollView);
		objectScrollView.setVisibility(View.GONE);
		
		TableLayout playerTable=findViewById(R.id.PlayerTableLayout);
		TableLayout objectTable=findViewById(R.id.ObjectTableLayout);
		//objectInv=new UIinventory(invAsset,items,objectTable,(Context)this);
		//playerInv=new UIinventory(invAsset,items,playerTable,(Context)this);
// адаптер данных
		game.initInventoryUI(playerTable,objectTable,this);
		//listView.addView(new ImageButton(this));
		
    
		
        //if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED)
        //    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 10001);
		//Button button;
		shootButton = findViewById(R.id.shootButton);
		Button openInventoryButton = findViewById(R.id.openInventory);
        // Устанавливаем действие по нажатию
        openInventoryButton.setOnClickListener
		(new OnClickListener() 
		{
				@Override
				public void onClick(View v) 
				{
					switchPlayerInventory();
					//if(shootButton.getVisibility()==View.VISIBLE)
					
					//else
					//	shootButton.setVisibility(View.VISIBLE);
				}
			});
			openInventoryButton.setEnabled(true);
		
        // Устанавливаем действие по нажатию
        
		shootButton.setOnTouchListener
		(new OnTouchListener(){
			@Override
			public boolean onTouch(View view,MotionEvent e)
			{
				switch(e.getAction())
				{
					case MotionEvent.ACTION_DOWN:
						game.getPlayer().shootFlag=true;
						
						break;
						case MotionEvent.ACTION_UP:
						game.getPlayer().shootFlag=false;
						break;
				}
				return true;
			}
		});
			
    }
	public void switchPlayerInventory()
	{
		if(!inventoryOpened)
		{
			//mapPanel.openNewInventory(mapPanel.getPlayerUIInventory(),mapPanel.getplayer().getInventory());
			//playerScrollView.setActivated(true);
			playerScrollView.setVisibility(View.VISIBLE);
			if(game.getObjectUIInventory().getTarget()!=null)
			objectScrollView.setVisibility(View.VISIBLE);
			game.command=game.commandInteraction;
			inventoryOpened=true;
			shootButton.setVisibility(View.GONE);
		}
		else
		{
			//mapPanel.closeInventory(mapPanel.getPlayerUIInventory());
			//playerScrollView.setActivated(false);
			playerScrollView.setVisibility(View.GONE);	
			if(game.getObjectUIInventory().getTarget()!=null)
			objectScrollView.setVisibility(View.GONE);
			game.command=game.commandMoving;
			inventoryOpened=false;
			shootButton.setVisibility(View.VISIBLE);
		}
	}
	public void closeObjectInventory()
	{
		objectScrollView.setVisibility(View.GONE);
		
	}
	public void openObjectInventory()
	{
		objectScrollView.setVisibility(View.VISIBLE);
	}
}
