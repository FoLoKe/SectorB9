package com.sb9.foloke.sectorb9.game.UI.Inventory;

import android.widget.*;
import android.content.*;
import android.util.*;
import android.content.res.Resources;
import com.sb9.foloke.sectorb9.game.Assets.*;
import android.graphics.*;
import android.widget.*;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.content.res.*;

public class InventoryFrameLayout extends FrameLayout
{
	public int x=0,y=0;
	public InventoryFrameLayout(Context context, AttributeSet set) {
		super(context, set);

	}
	public InventoryFrameLayout(Context context) {
		super(context);

	}

	public void setX(int x){this.x=x;}
	public void setY(int y){this.y=y;}
}
