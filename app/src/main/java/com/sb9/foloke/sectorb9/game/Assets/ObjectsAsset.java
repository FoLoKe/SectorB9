package com.sb9.foloke.sectorb9.game.Assets;
import android.graphics.*;

public class ObjectsAsset
{
	public static Bitmap smallCargoContainer,asteroidDust_1,asteroidDust_2,asteroidDust_3,crusher,solarPanel,
	fuelGenerator,smelterCold,smelterHot[],assembler,assembler_arm1,lab_mk1,labreader_mk1,smallAsteroid;
	public static Bitmap crusherAnim[];
	public static Bitmap fuelGeneratorAnim[];
	public static Bitmap assemblerAnim[];
	
    public static void init(Bitmap sheet)
    {
        SpriteSheet tsheet=new SpriteSheet(sheet);
        smallCargoContainer=tsheet.crop(0,0);
		asteroidDust_1=tsheet.crop(0,1);
		asteroidDust_2=tsheet.crop(1,0);
		asteroidDust_3=tsheet.crop(1,1);
		crusher=Bitmap.createBitmap(sheet,2*32,0,32,64);
		solarPanel=Bitmap.createBitmap(sheet,5*32,0,32,64);
		fuelGenerator=Bitmap.createBitmap(sheet,0,2*32,32,64);
		crusherAnim=new Bitmap[]{crusher,Bitmap.createBitmap(sheet,3*32,0,32,64)
		,Bitmap.createBitmap(sheet,4*32,0,32,64)};
		fuelGeneratorAnim=new Bitmap[]{fuelGenerator,Bitmap.createBitmap(sheet,1*32,2*32,32,64),
			Bitmap.createBitmap(sheet,2*32,2*32,32,64),Bitmap.createBitmap(sheet,1*32,2*32,32,64),
			};
		smelterCold=Bitmap.createBitmap(sheet,6*32,0,64,64);
		smelterHot=new Bitmap[]{Bitmap.createBitmap(sheet,6*32,2*32,64,64)};
		assembler=Bitmap.createBitmap(sheet,0*32,4*32,32,64);
		assemblerAnim=new Bitmap[]{Bitmap.createBitmap(sheet,1*32,4*32,32,64),
		Bitmap.createBitmap(sheet,2*32,4*32,32,64),
		Bitmap.createBitmap(sheet,3*32,4*32,32,64),
		Bitmap.createBitmap(sheet,4*32,4*32,32,64),
		};
		assembler_arm1=Bitmap.createBitmap(sheet,0,6*32,32,32);
		lab_mk1=Bitmap.createBitmap(sheet,3*32,2*32,64,64);
		labreader_mk1=Bitmap.createBitmap(sheet,5*32,2*32,32,32);
		smallAsteroid=Bitmap.createBitmap(sheet,5*32,3*32,32,32);
    }
}
