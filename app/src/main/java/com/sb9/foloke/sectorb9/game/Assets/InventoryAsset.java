package com.sb9.foloke.sectorb9.game.Assets;
import android.graphics.Bitmap;

import com.sb9.foloke.sectorb9.game.Assets.SpriteSheet;

public class InventoryAsset
{
		public static Bitmap inv_empty,inv_item_fe,inv_item_si,inv_item_au
		,inv_item_fe_dust,inv_item_au_dust,inv_item_si_dust,inv_item_steel_ingot;
		
		//public static 
		public static void init(Bitmap sheet)
		{
			SpriteSheet tsheet=new SpriteSheet(sheet);
			//inv_background=Bitmap.createBitmap(sheet,0,0,64,64);
			inv_empty=tsheet.crop(2,0);
			inv_item_fe=tsheet.crop(3,0);
			inv_item_si=tsheet.crop(4,0);
			inv_item_au=tsheet.crop(5,0);
			inv_item_fe_dust=tsheet.crop(6,0);
			inv_item_au_dust=tsheet.crop(7,0);
			inv_item_si_dust=tsheet.crop(8,0);
			inv_item_steel_ingot=tsheet.crop(3,1);
			
			
		}
		
}
