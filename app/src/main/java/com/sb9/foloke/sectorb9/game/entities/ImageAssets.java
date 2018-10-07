package com.sb9.foloke.sectorb9.game.entities;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.sb9.foloke.sectorb9.R;

public class ImageAssets {
    public static Bitmap player_mk1,engine_mk1,testbox;

    public static void init(Bitmap sheet)
    {
        SpriteSheet tsheet=new SpriteSheet(sheet);
        player_mk1=tsheet.crop(0,0);
        engine_mk1=tsheet.crop(1,0);
        testbox=tsheet.crop(2,0);
    }
}
