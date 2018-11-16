package com.sb9.foloke.sectorb9.game.Assets;
import android.graphics.*;

public class EffectsAsset {
    public static Bitmap yellow_pixel;

    public static void init(Bitmap sheet)
    {
        //SpriteSheet tsheet=new SpriteSheet(sheet);
		yellow_pixel=Bitmap.createBitmap(sheet,5*32,0,1,1);
    }
}
