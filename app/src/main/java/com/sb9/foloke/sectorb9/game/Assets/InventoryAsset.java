package com.sb9.foloke.sectorb9.game.Assets;
import android.graphics.Bitmap;

import com.sb9.foloke.sectorb9.game.Assets.SpriteSheet;

public class InventoryAsset
{
		public static Bitmap inv_background,inv_empty,inv_steel_ingot;

		public static void init(Bitmap sheet)
		{
			SpriteSheet tsheet=new SpriteSheet(sheet);
			inv_background=Bitmap.createBitmap(sheet,0,0,64,64);
			inv_empty=tsheet.crop(2,0);
			inv_steel_ingot=tsheet.crop(3,0);
		}
}
