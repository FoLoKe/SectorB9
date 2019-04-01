package com.sb9.foloke.sectorb9.game.UI.CustomViews;
import android.widget.*;
import android.content.*;
import android.graphics.*;
import android.view.*;
import com.sb9.foloke.sectorb9.*;
import android.util.*;

public class LoadingScreen extends FrameLayout
{
	ProgressBar PB;
	public LoadingScreen(Context context)
	{
		super(context);
		//GameLog.update("preparing loding screen",0);
        hide();
		
	}
	
	public LoadingScreen(Context context,AttributeSet attrSet)
	{
		super(context,attrSet);
		hide();
	}
	public void show()
	{
		setVisibility(View.VISIBLE);
	}
	
	public void hide()
	{
		setVisibility(View.GONE);
	}
}
