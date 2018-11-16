package com.sb9.foloke.sectorb9.game.Assets;

import android.graphics.Bitmap;

public class UIAsset {

        public static Bitmap hpBackground,hpLine,stunBackground,stunLine,progressBarBorder,destroyedText,noEnergySign,
		invFullSign,shootButton,uiBgBlur;
		public static Bitmap indexedImages[]={hpBackground,hpLine,progressBarBorder,destroyedText};
        public static void init(Bitmap sheet)
        {
            SpriteSheet tsheet=new SpriteSheet(sheet);
            hpBackground=Bitmap.createBitmap(tsheet.crop(0,0),0,0,32,12);
			stunBackground=Bitmap.createBitmap(tsheet.crop(1,0),0,12,32,12);
            hpLine=Bitmap.createBitmap(tsheet.crop(1,0),0,0,32,12);
			stunLine=Bitmap.createBitmap(tsheet.crop(0,0),0,12,32,12);
			progressBarBorder=Bitmap.createBitmap(tsheet.crop(2,0),0,0,2,12); 
			destroyedText=Bitmap.createBitmap(sheet,0,32,64,32);
			
			noEnergySign=Bitmap.createScaledBitmap(tsheet.crop(3,0),20,20,true);
			invFullSign=Bitmap.createScaledBitmap(tsheet.crop(3,1),20,20,true);
			shootButton=tsheet.crop(1,2);
			uiBgBlur=tsheet.crop(2,2);
        }
		public static Bitmap getByIndex(int index)
		{
			if(index<indexedImages.length)
			return indexedImages[index];
			return indexedImages[0];
		}
        


}
