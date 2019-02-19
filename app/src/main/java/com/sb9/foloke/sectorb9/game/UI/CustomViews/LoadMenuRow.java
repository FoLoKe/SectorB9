package com.sb9.foloke.sectorb9.game.UI.CustomViews;
import android.widget.*;
import android.content.*;

public class LoadMenuRow extends TableRow
{
	String saveName="";
	public LoadMenuRow(Context context)
	{
		super(context);
	}

	public void setSaveName(String saveName)
	{
		this.saveName = saveName;
	}

	public String getSaveName()
	{
		return saveName;
	}
}
