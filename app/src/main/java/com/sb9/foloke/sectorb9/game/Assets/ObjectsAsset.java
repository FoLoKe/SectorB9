package com.sb9.foloke.sectorb9.game.Assets;
import android.graphics.*;
import com.sb9.foloke.sectorb9.game.entities.*;

public class ObjectsAsset
{
	public static Bitmap smallCargoContainer,asteroidDust_1,asteroidDust_2,asteroidDust_3,crusher;
	public static Bitmap crusherAnim[];
    public static void init(Bitmap sheet)
    {
        SpriteSheet tsheet=new SpriteSheet(sheet);
        smallCargoContainer=tsheet.crop(0,0);
		asteroidDust_1=tsheet.crop(0,1);
		asteroidDust_2=tsheet.crop(1,0);
		asteroidDust_3=tsheet.crop(1,1);
		crusher=Bitmap.createBitmap(sheet,2*32,0,32,64);
		crusherAnim=new Bitmap[]{crusher,Bitmap.createBitmap(sheet,3*32,0,32,64)
		,Bitmap.createBitmap(sheet,4*32,0,32,64)};
        //engine_mk1=tsheet.crop(1,0);
        //cursor=tsheet.crop(2,0);
       // asteroid_1=tsheet.crop(3,0);
    }
}
