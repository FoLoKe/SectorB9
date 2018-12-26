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
	private ViewFlipper VF;
	private BuildUI buildUI=new BuildUI();
	public assemblerUI assemblerUIi=new assemblerUI();
	private UImenu uiMenu=new UImenu();
	public UIinteraction uiInteraction=new UIinteraction();
	private UIaction uiAction =new UIaction();
	public HelpUI helpui=new HelpUI();
	public ShipUI shipUI;//=new ShipUI();
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.main_activity);
        game=findViewById(R.id.Game);
		options=new BitmapFactory.Options();
        options.inScaled=false;
		helpui.init(this,VF,1);
		TableLayout playerTable=findViewById(R.id.PlayerTableLayout);
		TableLayout objectTable=findViewById(R.id.ObjectTableLayout);

		game.initInventoryUI(playerTable,objectTable,this);
		
		VF = findViewById(R.id.UIFlipper);
		VF.setDisplayedChild(VF.indexOfChild(findViewById(R.id.actionUI)));
		
		buildUI.init(this,VF);
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
					VF.setDisplayedChild(VF.indexOfChild(menuUI));
					game.setPause(true);
					v.setVisibility(View.GONE);
				}
			});
    }
	
	public void initInvenories()
	{
		runOnUiThread(new Runnable() {  
				@Override
				public void run() {
					// TODO Auto-generated method stub

					game.initObjInventory();
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
	
	public void saveFile(String fileName) {
        try {
            String root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).toString()+File.separator+"sb9";
            File myDir = new File(root);
            if (!myDir.exists()) {
                myDir.mkdir();
            }

            String fname = "save "+fileName+".txt";
            File file = new File (myDir, fname);
            if (file.exists ())
                file.delete ();

            FileOutputStream out = new FileOutputStream(file);
            OutputStreamWriter osw = new OutputStreamWriter(out);
            BufferedWriter writer = new BufferedWriter(osw);
		
            writer.write("SB9 debug save");
			writer.newLine();
            game.getEntityManager().save(writer);
            
            writer.close();
            osw.close();
        } catch (Throwable e) {
           System.out.println(e);
        }
    }
	
	public String loadFile(String fileName) {
        
        try {

            String root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).toString()+File.separator+"sb9";
            File myDir = new File(root);
            if (!myDir.exists()) {
              return  "no directory";
            }

            String fname = "save "+fileName+".txt";
            File file = new File (myDir, fname);
            if (file.exists ())
            {
                //InputStream inputStream = openFileInput(fileName);
                FileInputStream inputStream = new FileInputStream(file);

                if (inputStream != null)
                {
					String line;
                    InputStreamReader isr = new InputStreamReader(inputStream);
                    BufferedReader reader = new BufferedReader(isr);
					line=reader.readLine();
                    game.getEntityManager().load(reader);
                    inputStream.close();
                    reader.close();
                    isr.close();
					
                    return line;
                }
            }
            else
            {
                return "no such file";
            }
        } catch (Throwable t)
        {
            return "error:"+ t.getLocalizedMessage();
        }
        return "200";
    }
}
