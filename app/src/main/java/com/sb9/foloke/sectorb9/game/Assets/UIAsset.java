package com.sb9.foloke.sectorb9.game.Assets;

import android.graphics.Bitmap;

public class UIAsset {

        public static Bitmap hpBackground,hpLine;

        public static void init(Bitmap sheet)
        {
            SpriteSheet tsheet=new SpriteSheet(sheet);
            hpBackground=Bitmap.createBitmap(tsheet.crop(0,0),0,0,32,12);
            hpLine=Bitmap.createBitmap(tsheet.crop(1,0),0,0,32,12);
        }


}
