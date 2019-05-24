package com.sb9.foloke.sectorb9.game.Assets;
import android.graphics.*;

public class AIAsset
{
	public static Bitmap defensive,aggressive,peaceful,retreatful;
	public static void init(Bitmap sheet)
    {
		defensive=Bitmap.createBitmap(sheet,0,0,16,16,null,false);
		aggressive=Bitmap.createBitmap(sheet,16,0,16,16,null,false);
		peaceful=Bitmap.createBitmap(sheet,32,0,16,16,null,false);
		retreatful=Bitmap.createBitmap(sheet,48,0,16,16,null,false);
		
	}
}
