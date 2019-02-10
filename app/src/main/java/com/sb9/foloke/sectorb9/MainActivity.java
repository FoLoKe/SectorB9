package com.sb9.foloke.sectorb9;


import android.app.Activity;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
//import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;


import android.widget.*;
import android.view.*;


import com.sb9.foloke.sectorb9.game.Managers.GameManager;
import com.sb9.foloke.sectorb9.game.UI.*;
import com.sb9.foloke.sectorb9.game.Assets.*;
import com.sb9.foloke.sectorb9.game.Display.GamePanel;
import android.*;
import android.content.pm.*;
import android.os.*;

public class MainActivity extends Activity {

    private GamePanel gamePanel;

	

	private ViewFlipper VF;
	private BuildUI buildUI=new BuildUI();
	public AssemblerUI assemblerUIi=new AssemblerUI();
	public MenuUI menuUI=new MenuUI();
	public InteractionUI uiInteraction=new InteractionUI();
	private ActionUI uiAction =new ActionUI();
	public HelpUI helpui=new HelpUI();
	public ShipUI shipUI;
	public MapUI mapUI;
	private static final int PERMISSION_REQUEST_CODE = 123;
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
		if(!hasPermissions())
			requestPerms();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.main_activity);
        gamePanel =findViewById(R.id.Game);
        BitmapFactory.Options options=new BitmapFactory.Options();
		
		
		
        options.inScaled=false;
		helpui.init(this,VF,1);
		
		
		TableLayout playerTable=findViewById(R.id.PlayerTableLayout);
		TableLayout objectTable=findViewById(R.id.ObjectTableLayout);

		gamePanel.getGameManager().makeInventoryUI(playerTable,objectTable,this);
		
		VF = findViewById(R.id.UIFlipper);
		VF.setDisplayedChild(VF.indexOfChild(findViewById(R.id.actionUI)));
		
		buildUI.init(this,VF);
		uiAction.init(this,VF);
		uiInteraction.init(this,VF,null);
			
		findViewById(R.id.menuUILinearLayout).setBackground(new BitmapDrawable(this.getResources(),UIAsset.uiBgBlur));
		
		final FrameLayout menuUIFrame=findViewById(R.id.menuUI);
		final MainActivity MA=this;
		mapUI=new MapUI(this);
		mapUI.init(this,VF);
		Button menuButton=findViewById(R.id.Menu);
		menuButton.setOnClickListener
		
		(new OnClickListener() 
			{
				@Override
				public void onClick(View v) 
				{
					final int a=VF.getDisplayedChild();
					menuUI.init(MA,VF,a);
					VF.setDisplayedChild(VF.indexOfChild(menuUIFrame));
					gamePanel.getGameManager().setPause(true);
					v.setVisibility(View.GONE);
				}
			});
    }
	
	
	public ViewFlipper getViewFlipper()
	{
		return VF;
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
            gamePanel.save(writer);
            
            writer.close();
            osw.close();
        } catch (Throwable e) {
           System.err.print(e);
        }
    }
	
	public int loadFile(String fileName) {
        
        try {

            String root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).toString()+File.separator+"sb9";
            File myDir = new File(root);
            if (!myDir.exists()) {
              return -1; // "no directory";
            }

            String fname = "save "+fileName+".txt";
            File file = new File (myDir, fname);
            if (file.exists ())
            {
                //InputStream inputStream = openFileInput(fileName);
                FileInputStream inputStream = new FileInputStream(file);

                if (inputStream != null)
                {
                    InputStreamReader isr = new InputStreamReader(inputStream);
                    BufferedReader reader = new BufferedReader(isr);
                    gamePanel.load(reader);
                    inputStream.close();
                    reader.close();
                    isr.close();
					
                    return 0; //all ok;
                }
            }
            else
            {
                return 1;// "no such file";
            }
        } catch (Throwable t)
        {
            return -2;// "error:"+ t.getLocalizedMessage();
        }
        return -3;
    }

    public GameManager getGameManager()
    {
        return gamePanel.getGameManager();
    }

    public void toActionFast()
    {
        getGameManager().setPause(false);
        findViewById(R.id.Menu).setVisibility(View.VISIBLE);
        VF.setDisplayedChild(VF.indexOfChild(findViewById(R.id.actionUI)));
    }
	
	private boolean hasPermissions(){
        int res = 0;
        //string array of permissions,
        String[] permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        for (String perms : permissions){
            res = checkCallingOrSelfPermission(perms);
            if (!(res == PackageManager.PERMISSION_GRANTED)){
                return false;
            }
        }
        return true;
    }

    private void requestPerms(){
        String[] permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            requestPermissions(permissions,PERMISSION_REQUEST_CODE);
        }
    }
}
