package com.sb9.foloke.sectorb9;


import android.app.Activity;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import java.io.File;
import android.widget.*;
import android.view.*;

import com.sb9.foloke.sectorb9.game.Funtions.WorldGenerator;
import com.sb9.foloke.sectorb9.game.Managers.GameManager;
import com.sb9.foloke.sectorb9.game.UI.*;
import com.sb9.foloke.sectorb9.game.Assets.*;
import com.sb9.foloke.sectorb9.game.Display.GamePanel;
import android.*;
import android.content.pm.*;
import android.os.*;
import com.sb9.foloke.sectorb9.game.UI.MainMenu.*;
import android.graphics.*;
import android.app.*;
import android.content.*;
import com.sb9.foloke.sectorb9.game.Funtions.*;
import com.sb9.foloke.sectorb9.game.UI.CustomViews.*;
import com.sb9.foloke.sectorb9.game.UI.Inventory.*;

public class MainActivity extends Activity {

    private GameManager gameManager;
    //private static GamePanel gamePanel;
	
	private ViewFlipper VF;
	
	private static final int PERMISSION_REQUEST_CODE = 123;
	private InventoryExchangeInterface excInterface;
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
		
        requestWindowFeature(Window.FEATURE_NO_TITLE);
		if(!hasPermissions())
			requestPerms();
		
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		int currentApiVersion;


		currentApiVersion = android.os.Build.VERSION.SDK_INT;

		final int flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
			| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
			| View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
			| View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
			| View.SYSTEM_UI_FLAG_FULLSCREEN
			| View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

		if(currentApiVersion >= Build.VERSION_CODES.KITKAT)
		{

			getWindow().getDecorView().setSystemUiVisibility(flags);
			final View decorView = getWindow().getDecorView();
			decorView
				.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener()
				{

					@Override
					public void onSystemUiVisibilityChange(int visibility)
					{
						if((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0)
						{
							decorView.setSystemUiVisibility(flags);
						}
					}
				});
		}

        setContentView(R.layout.main_menu);
		GameLog.delete();
		GameLog.update(" ",0);
		GameLog.update("Activity: Starting",0);
        gameManager=new GameManager(this);
		prepareMenu();
	}

	@Override
	protected void onResume()
	{
		// TODO: Implement this method
		super.onResume();
		gameManager.resume();
	}

	@Override
	protected void onPause()
	{
		// TODO: Implement this method
		super.onPause();
		gameManager.shutdown();
	}
	
	public void prepareMenu()
	{
		setContentView(R.layout.main_menu);
		GameLog.update("Activity: preparing menu",0);
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
			
		Button continueGameButton= findViewById(R.id.continue_game_button);

		continueGameButton.setOnClickListener(new OnClickListener()
			{
				public void onClick(View v)
				{
					continueGame();
				}
			});
		GameLog.update("Activity: menu created",0);

	}
	
	private void makeOnNewGameDialog()
	{
		GameLog.update("Activity: preparing new game alert Dialog",0);
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
					
					prepareNewGame(input.getText().toString(),true);
                    
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
		GameLog.update("Activity: preparing new game dialog successful",0);
	}
	
	private void prepareNewGame(String s,boolean state)
	{
	try{
		GameLog.update("Activity: preparing new game",0);
		if(s.equals("")||s.equals(" ")||s.length()==0||s.contains(" "))
		{
			GameLog.update("Activity: wrong save name",1);
			return;
		}

		if(state)
		{
			GameLog.update("Activity: checking for existing" ,0);
			String root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).toString()+File.separator+"sb9";
			File myDir = new File(root);
			if (!myDir.exists()) 
			{
				if(!myDir.mkdir()) {
                    GameLog.update("Activity: cant't create folder:" + myDir.getName(), 1);
                    return;
                }
			}

			File[] files = myDir.listFiles();

			if (files.length>0)
			{
				for(File f:files)
				{
					if(f.getName().equals(s))
						if (f.isDirectory())
						{
							GameLog.update("Activity: save name already used" ,0);
							return;
						}
				}
			}

			GameLog.update("Activity: creating folder",0);
			File saveDir=new File(myDir,s);

			if(saveDir.mkdir())
			{
				GameLog.update("Activity: folder created",0);
			}
			else
			{
                GameLog.update("Activity: folder creating error", 1);
                return;
            }
		}

		////ONLY PLACE TO START GAME THREAD
		
		GameLog.update("Activity: preparing GameManager",0);
        gameManager.init(state,s);
		GameLog.update("Activity: preparing UIs",0);
        TableLayout playerTable=findViewById(R.id.PlayerTableLayout);
        TableLayout objectTable=findViewById(R.id.ObjectTableLayout);

        makeInventoryUI(playerTable,objectTable,this);

        
        BuildUI.init( this);
        ActionUI.init( this);
        InteractionUI.init( this,null);
        HelpUI.init( this,1);
        MapUI.init( this);
        VF = findViewById(R.id.UIFlipper);
        VF.setDisplayedChild(VF.indexOfChild(findViewById(R.id.actionUI)));

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
				MenuUI.init(MA,a);
				VF.setDisplayedChild(VF.indexOfChild(menuUIFrame));
				gameManager.setPause(true);
				v.setVisibility(View.GONE);
			}
		});



		GameLog.update("Activity: preparing new game successful",0);
		}catch(Exception e){GameLog.update("Activity: "+e.toString(),1);}
	}
	
	public void prepareNewLoad(String s)
	{

		///ProgressDialog dialog = ProgressDialog.show(this,"loading","Loading. Please wait...");
		///dialog.show();

		GameLog.update("Activity: preparing load for save - "+s,0);
		
		prepareNewGame(s,false);


		GameLog.update("Activity: successfully loaded: "+s,0);
		//dialog.hide();
	}
    
	public void loadGame()
	{
		setContentView(R.layout.load_game_ui);
		LoadGameUI.init(this);
		
	}
	
	private void continueGame()
	{
		GameLog.update("Activity: preparing for continue" ,0);
		String root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).toString()+File.separator+"sb9";
		File myDir = new File(root);
		if (!myDir.exists()) {
            if (!myDir.mkdir())
            {
                GameLog.update("Activity: can't create directory:" + myDir.getName(), 1);
                return;
            }
		}

		File[] files = myDir.listFiles();
		File target=null;
		if (files.length>0)
		{
			
			for(File f:files)
			{
				GameLog.update("Activity: checking folder: "+f.getName(),0); 
				if (f.isDirectory())
				{
					if(target!=null)
					{
						if(f.lastModified()>target.lastModified())
							target=f;
					}
					else
					{
						target=f;
					}
				}
			}
		}
	
		if(target==null)
		{
			GameLog.update("Activity: no saves",0);
			return;
		}
		GameLog.update("Activity: file selected to continue: "+target.getName(),0);
		prepareNewLoad(target.getName());
		GameLog.update("Activity: continue successful",0);
	}
	
	public ViewFlipper getViewFlipper()
	{
		return VF;
	}

    public void toActionFast()
    {
       gameManager.setPause(false);
       findViewById(R.id.Menu).setVisibility(View.VISIBLE);
       VF.setDisplayedChild(VF.indexOfChild(findViewById(R.id.actionUI)));
    }
	
	private boolean hasPermissions(){
        int res;
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
	
	public Bitmap getBitmapFromView(View view) {
		
		GameLog.update("Activity: taking screen shoot",0);
        gameManager.getGamePanel().setDrawingCacheEnabled(true);
        gameManager.getGamePanel().buildDrawingCache(true);
		final Bitmap bitmap = Bitmap.createBitmap( view.getDrawingCache() );
		Canvas c=new Canvas(bitmap);
        gameManager.getGamePanel().tick();
        gameManager.getGamePanel().preRender(c);
        gameManager.getGamePanel().setDrawingCacheEnabled(false);
        gameManager.getGamePanel().destroyDrawingCache();
		GameLog.update("Activity: screen shoot created",0);
		
		
		return bitmap;
		
	}

	public GameManager getGameManager()
    {
        return gameManager;
    }
	
	private void makeInventoryUI(TableLayout playerTable, TableLayout objectTable, MainActivity context)
    {
		excInterface=new InventoryExchangeInterface(gameManager);
        InventoryUI.set(playerTable,gameManager.getPlayer(),objectTable,null,excInterface,context);
    }
}
