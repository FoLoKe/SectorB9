package com.sb9.foloke.sectorb9.game.Assets;

import android.graphics.Bitmap;

        import com.sb9.foloke.sectorb9.game.Assets.SpriteSheet;

public class ShipAsset {
    public static Bitmap player_mk1,engine_mk1,cursor,asteroid_1,player_mk2,engine_mk2,player_mk2_engine_shield;

    public static void init(Bitmap sheet)
    {
        SpriteSheet tsheet=new SpriteSheet(sheet);
        player_mk1=tsheet.crop(0,0);
        engine_mk1=tsheet.crop(1,0);
        cursor=tsheet.crop(2,0);
        asteroid_1=tsheet.crop(3,0);
		
		
		player_mk2=Bitmap.createBitmap(sheet,0,32,32,64);
		engine_mk2=tsheet.crop(1,2);
		player_mk2_engine_shield=tsheet.crop(2,2);
    }
}
