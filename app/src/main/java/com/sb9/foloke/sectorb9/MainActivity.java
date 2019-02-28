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
import com.sb9.foloke.sectorb9.game.UI.MainMenu.*;
import android.graphics.*;
import android.view.View.*;
import android.app.*;
import android.content.*;

public class MainActivity extends Activity {

    private static GamePanel gamePanel;

	

	private ViewFlipper VF;
	
	private static final int PERMISSION_REQUEST_CODE = 123;
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
		if(!hasPermissions())
			requestPerms();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		prepareMenu();
	}
	
	public void prepareMenu()
	{
		BuildUI.deinit(this);
		setContentView(R.layout.main_menu);
		
		Button startNewGameButton= findViewById(R.id.new_game_button);
	
		startNewGameButton.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				makeOnNewGameDialog();
			}
		});
		
		Button loadGameButton= findViewById(R.id.load_game_button);
		
		loadGameButton.setOnClickListener(new OnClickListener()
			{
				public void onClick(View v)
				{
					loadGame();
				}
			});
	}
	
	private void makeOnNewGameDialog()
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		
		LinearLayout LL=new LinearLayout(this);
		LL.setGravity(Gravity.CENTER);
		final EditText input = new EditText(MainActivity.this);  
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
			LinearLayout.LayoutParams.WRAP_CONTENT,
			LinearLayout.LayoutParams.WRAP_CONTENT);
		LL.setLayoutParams(lp);
		LL.setOrientation(LinearLayout.VERTICAL);
		input.setLayoutParams(lp);
		
		final TextView text = new TextView(MainActivity.this);  
		text.setLayoutParams(lp);
		text.setText("Enter save name");

		builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					
					prepareNewGame(input.getText().toString());
				}
			});
		builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					
					
				}
			});
		
		AlertDialog dialog = builder.create();
		
		LL.addView(text);
		LL.addView(input);
		
		dialog.setView(LL);
		dialog.show();
	}
	
	private void prepareNewGame(String s)
	{
		if(s==""||s==null||s==" "||s.length()==0)
		{
			makeToast("wrong savename");
			return;
		}
		
        setContentView(R.layout.main_activity);
        gamePanel =findViewById(R.id.Game);
		gamePanel.getGameManager().setSaveName(s);
        BitmapFactory.Options options=new BitmapFactory.Options();
		
        options.inScaled=false;
		
		
		TableLayout playerTable=findViewById(R.id.PlayerTableLayout);
		TableLayout objectTable=findViewById(R.id.ObjectTableLayout);

		gamePanel.getGameManager().makeInventoryUI(playerTable,objectTable,this);
		
		VF = findViewById(R.id.UIFlipper);
		VF.setDisplayedChild(VF.indexOfChild(findViewById(R.id.actionUI)));
		
		BuildUI.init(this,VF);
		ActionUI.init(this,VF);
		InteractionUI.init(this,VF,null);
		HelpUI.init(this,VF,1);
		MapUI.init(this,VF);
		findViewById(R.id.menuUILinearLayout).setBackground(new BitmapDrawable(this.getResources(),UIAsset.uiBgBlur));
		
		final FrameLayout menuUIFrame=findViewById(R.id.menuUI);
		final MainActivity MA=this;
		
		
		
		Button menuButton=findViewById(R.id.Menu);
		menuButton.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				final int a=VF.getDisplayedChild();
				MenuUI.init(MA,VF,a);
				VF.setDisplayedChild(VF.indexOfChild(menuUIFrame));
				gamePanel.getGameManager().setPause(true);
				v.setVisibility(View.GONE);
			}
		});
	}
	
	public int prepareNewLoad(String s)
	{
		prepareNewGame(s);
		String fileName="";
		String playerSave="";
		String root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).toString()+File.separator+"sb9"+File.separator+s;
		File myDir = new File(root);
		if (!myDir.exists()) {
			makeToast("Error: no directory");
			return -1;
		}
		String metaFileName="meta.txt";
		File metaFile = new File (myDir, metaFileName);
		if (!metaFile.exists ())
		{makeToast("Error: no metaFile");
			return -4;}
		else
		{
			try
			{
				FileInputStream inputStream = new FileInputStream(metaFile);	
				if (inputStream != null)
				{
					InputStreamReader isr = new InputStreamReader(inputStream);
					BufferedReader reader = new BufferedReader(isr);
					fileName=reader.readLine();
					gamePanel.getGameManager().getWorldManager().setSector(Integer.parseInt(reader.readLine()),Integer.parseInt(reader.readLine()));
					playerSave=reader.readLine();
					String[] words = playerSave.split("\\s"); 
					gamePanel.getGameManager().getPlayer().load(words);
					gamePanel.pointOfTouch=gamePanel.getGameManager().getPlayer().getWorldLocation();
					inputStream.close();
					reader.close();
					isr.close();
				}
			}
			catch(Exception e){
				makeToast(e.toString());
				return -5;}
		}
		gamePanel.getGameManager().getPlayer().initShip();
		loadFile(fileName,s);
		makeToast("Successfully loaded");
		return 0;
	}
    
	public void loadGame()
	{
		setContentView(R.layout.load_game_ui);
		LoadGameUI.init(this);
		
	}
	
	public ViewFlipper getViewFlipper()
	{
		return VF;
	}
	
	public void saveFile(String fileName,String saveName) {
        try {
			String fname = "save"+fileName+".txt";
            String root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).toString()+File.separator+"sb9"+File.separator+saveName;
            File myDir = new File(root);
            if (!myDir.exists()) {
                myDir.mkdir();
            }
			
			String metaFileName="meta.txt";
			File metaFile = new File (myDir, metaFileName);
				if (metaFile.exists ())
					metaFile.delete ();
			FileOutputStream mout = new FileOutputStream(metaFile);
            OutputStreamWriter mosw = new OutputStreamWriter(mout);
            BufferedWriter mwriter = new BufferedWriter(mosw);

            mwriter.write(fname);
			mwriter.newLine();
			Point p=gamePanel.getGameManager().getWorldManager().getSector();
			mwriter.write(p.x+"");
			mwriter.newLine();
			mwriter.write(p.y+"");
			mwriter.newLine();
            gamePanel.getGameManager().getPlayer().save(mwriter);

            mwriter.close();
            mosw.close();
					
            
            File file = new File (myDir, fname);
            if (file.exists ())
                file.delete ();
			Bitmap image=getBitmapFromView(findViewById(R.id.Game));
			image.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(new File(root,"image.jpg")));
            FileOutputStream out = new FileOutputStream(file);
            OutputStreamWriter osw = new OutputStreamWriter(out);
            BufferedWriter writer = new BufferedWriter(osw);
		
            writer.write("SB9 debug save");
			writer.newLine();
            gamePanel.save(writer);
            
            writer.close();
            osw.close();
			makeToast("Successfully saved");
        } catch (Throwable e) {
			makeToast(e.toString());
           System.err.print(e);
        }
    }
	
	public int loadFile(String fileName,String SaveName) {
        
        try {
			String root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).toString()+File.separator+"sb9"+File.separator+SaveName;
			File myDir = new File(root);
			if (!myDir.exists()) {
				makeToast("Error: no directory");
				return -1;
			}
               
            File file = new File (myDir, fileName);
            if (file.exists ())
            {
               
                FileInputStream inputStream = new FileInputStream(file);

                if (inputStream != null)
                {
                    InputStreamReader isr = new InputStreamReader(inputStream);
                    BufferedReader reader = new BufferedReader(isr);
                    gamePanel.load(reader);
                    inputStream.close();
                    reader.close();
                    isr.close();
					makeToast("Successfully loaded file");
                    return 0;
                }
            }
            else
            {
				makeToast("Error: no such file from meta");
                return 1;
            }
        } catch (Throwable t)
        {
			makeToast(t.toString());
            return -2;
        }
		makeToast("Cant reach that!");
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
	
	public static Bitmap getBitmapFromView(View view) {
		
		view.setDrawingCacheEnabled(true);
		view.buildDrawingCache(true);	
		final Bitmap bitmap = Bitmap.createBitmap( view.getDrawingCache() );
		Canvas c=new Canvas(bitmap);
		gamePanel.render(c);
		view.setDrawingCacheEnabled(false);
		view.destroyDrawingCache();
		return bitmap;
		
	}
	
	public void makeToast(String toastText)
	{
		Context context = getApplicationContext();
		int duration = Toast.LENGTH_LONG;
		Toast toast = Toast.makeText(context, toastText, duration);
		toast.show();
	}
}
