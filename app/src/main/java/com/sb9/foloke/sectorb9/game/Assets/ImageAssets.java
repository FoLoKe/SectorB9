package com.sb9.foloke.sectorb9.game.Assets;

import android.graphics.Bitmap;

        import com.sb9.foloke.sectorb9.game.Assets.SpriteSheet;

public class ImageAssets {
    public static Bitmap player_mk1,engine_mk1,cursor,asteroid_1,shell;

    public static void init(Bitmap sheet)
    {
        SpriteSheet tsheet=new SpriteSheet(sheet);
        player_mk1=tsheet.crop(0,0);
        engine_mk1=tsheet.crop(1,0);
        cursor=tsheet.crop(2,0);
        asteroid_1=tsheet.crop(3,0);
		shell=tsheet.crop(4,0);
    }
}
