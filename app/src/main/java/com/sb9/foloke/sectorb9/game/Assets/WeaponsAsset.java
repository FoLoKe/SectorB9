package com.sb9.foloke.sectorb9.game.Assets;
import android.graphics.*;

public class WeaponsAsset {
    public static Bitmap shell,plasm;

    public static void init(Bitmap sheet)
    {
        SpriteSheet tsheet=new SpriteSheet(sheet);
        
		shell=Bitmap.createBitmap(sheet,5*32,0,1,1);
		plasm=tsheet.crop(4,0);
		
    }
}

