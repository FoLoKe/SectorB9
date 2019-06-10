package com.sb9.foloke.sectorb9.game.Assets;

import android.graphics.Bitmap;

public class UIAsset {

        public static Bitmap hpBackground,hpLine,stunBackground,stunLine,progressBarBorder,destroyedText,noEnergySign,
		invFullSign,shootButton,uiBgBlur,turnedOffSign, interactionCancel,buildModeButton,collectDebrisButton,optionsButton,saveButton,loadButton,helpButton,exitButton,mapButton,
		AIStay,AIAttack,AIMove,AIFollow,AIMine,AIRepair,AIPatrol,AIControl,ToAI,AIAggressive,AIDefensive,AIPeaceful, AIRetreat,weaponOn,weaponOff,weaponsButton,interactionSign,
        interactionCargo,interactionConfig,interactionTechs,interactionShips,interactionProduction;

        public static void init(Bitmap sheet)
        {
            SpriteSheet tSheet=new SpriteSheet(sheet);

            hpBackground=Bitmap.createBitmap(tSheet.crop(0,0),0,0,32,12);
			stunBackground=Bitmap.createBitmap(tSheet.crop(1,0),0,12,32,12);
            hpLine=Bitmap.createBitmap(tSheet.crop(1,0),0,0,32,12);
			stunLine=Bitmap.createBitmap(tSheet.crop(0,0),0,12,32,12);
			progressBarBorder=Bitmap.createBitmap(tSheet.crop(2,0),0,0,2,12);
			destroyedText=Bitmap.createBitmap(sheet,0,32,64,32);
			
			noEnergySign=Bitmap.createScaledBitmap(tSheet.crop(3,0),20,20,true);
			invFullSign=Bitmap.createScaledBitmap(tSheet.crop(3,1),20,20,true);
			shootButton=tSheet.crop(1,2);
			uiBgBlur=tSheet.crop(2,2);

			buildModeButton=Bitmap.createBitmap(sheet,0,3*32,64,32,null,false);
			collectDebrisButton=Bitmap.createBitmap(sheet,64,3*32,32,32,null,false);
			
			///menu buttons
			optionsButton=Bitmap.createBitmap(sheet,0,4*32,96,32,null,false);
			saveButton=Bitmap.createBitmap(sheet,0,5*32,96,32,null,false);
			mapButton=Bitmap.createBitmap(sheet,0,6*32,96,32,null,false);
			helpButton=Bitmap.createBitmap(sheet,96,4*32,96,32,null,false);
			loadButton=Bitmap.createBitmap(sheet,96,5*32,96,32,null,false);
			exitButton=Bitmap.createBitmap(sheet,96,6*32,96,32,null,false);
			
			turnedOffSign=Bitmap.createScaledBitmap(tSheet.crop(4,0),20,20,true);
			
			AIStay=Bitmap.createBitmap(sheet,8*32,0,32,32,null,false);
			AIMove=Bitmap.createBitmap(sheet,8*32,32,32,32,null,false);
			AIAttack=Bitmap.createBitmap(sheet,8*32,64,32,32,null,false);
			AIFollow=Bitmap.createBitmap(sheet,8*32,96,32,32,null,false);
            AIMine=Bitmap.createBitmap(sheet,8*32,4*32,32,32,null,false);
            AIRepair=Bitmap.createBitmap(sheet,8*32,5*32,32,32,null,false);
            AIPatrol=Bitmap.createBitmap(sheet,8*32,6*32,32,32,null,false);
            AIControl=Bitmap.createBitmap(sheet,9*32,32,32,32,null,false);
            ToAI=Bitmap.createBitmap(sheet,9*32,0,32,32,null,false);
            AIAggressive=Bitmap.createBitmap(sheet,9*32,2*32,32,32,null,false);
            AIDefensive=Bitmap.createBitmap(sheet,9*32,3*32,32,32,null,false);
            AIRetreat =Bitmap.createBitmap(sheet,9*32,4*32,32,32,null,false);
            AIPeaceful=Bitmap.createBitmap(sheet,9*32,5*32,32,32,null,false);


			weaponOff= Bitmap.createBitmap(sheet,32,32+16,32,16,null,false);
			weaponOn=Bitmap.createBitmap(sheet,0,32+16,32,16,null,false);
			weaponsButton=Bitmap.createBitmap(sheet,64,32,17,32,null,false);
            interactionSign=Bitmap.createBitmap(sheet,128,32+16,64,16,null,false);

            interactionCargo=Bitmap.createBitmap(sheet,6*32,0,64,32,null,false);
            interactionTechs=Bitmap.createBitmap(sheet,6*32,32,64,32,null,false);
            interactionProduction=Bitmap.createBitmap(sheet,6*32,64,64,32,null,false);
            interactionConfig=Bitmap.createBitmap(sheet,6*32,96,64,32,null,false);
            interactionShips=Bitmap.createBitmap(sheet,6*32,128,64,32,null,false);
            interactionCancel =Bitmap.createBitmap(sheet,3*32,2*32,32,32,null,false);
        }

        


}
