package com.sb9.foloke.sectorb9.game.Assets;
import android.graphics.*;

public class EffectsAsset {
    public static Bitmap yellow_pixel,blue_pixel,dust_1,dust_2;

    public static void init(Bitmap sheet)
    {
        //SpriteSheet tsheet=new SpriteSheet(sheet);
		yellow_pixel=Bitmap.createBitmap(sheet,5*32,0,1,1);
		blue_pixel=Bitmap.createBitmap(sheet,5*32,2,1,1);
		dust_1=Bitmap.createBitmap(sheet,6*32,0,32,32);
		dust_2=Bitmap.createBitmap(sheet,7*32,0,32,32);

    }
}
