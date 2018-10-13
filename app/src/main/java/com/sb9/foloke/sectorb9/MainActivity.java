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

public class MainActivity extends Activity {

    Game mapPanel;
	boolean inventoryOpened=false;
	
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.main_activity);
        mapPanel=findViewById(R.id.Game);
		
		
		/*TableLayout table = (TableLayout)findViewById(R.id.myTableLayout);
		String data[]={"a","b"};
		for(int i=0;i<data.length;i++)
		{
			TableRow row=new TableRow(this);
			String phone = data[i];
			String amount = data[i];
			TextView tv1=new TextView(this);
			tv1.setText(phone);
			TextView tv2=new TextView(this);
			tv2.setText(amount);
			row.addView(tv1);
			row.addView(tv2);
			table.addView(row);
		}*/
        //if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED)
        //    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 10001);
		//Button button;
		
		Button openInventoryButton = findViewById(R.id.openInventory);
        // Устанавливаем действие по нажатию
        openInventoryButton.setOnClickListener
		(new OnClickListener() 
		{
				@Override
				public void onClick(View v) 
				{
					switchPlayerInventory();
				}
			});
			openInventoryButton.setEnabled(true);
			
    }
	public void switchPlayerInventory()
	{
		if(!inventoryOpened)
		{
			mapPanel.openNewInventory(mapPanel.getPlayerUIInventory(),mapPanel.getplayer().getInventory());
			inventoryOpened=true;
		}
		else
		{
			mapPanel.closeInventory(mapPanel.getPlayerUIInventory());
			inventoryOpened=false;
		}
	}
}
