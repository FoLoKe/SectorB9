package com.sb9.foloke.sectorb9.game.entities;

import android.graphics.Bitmap;



public class SpriteSheet {
    private Bitmap sheet;
    private final int SPRITE_SHEET_WIDTH=32;
    private final int SPRITE_SHEET_HEIGHT=32;
    public SpriteSheet(Bitmap sheet)
    {
        this.sheet=sheet;
    }
    public Bitmap crop(int x,int y)
    {
        return Bitmap.createBitmap(sheet,x*SPRITE_SHEET_WIDTH,y*SPRITE_SHEET_HEIGHT,SPRITE_SHEET_WIDTH,SPRITE_SHEET_HEIGHT,null,false);
    }
}
