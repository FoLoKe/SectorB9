package com.sb9.foloke.sectorb9.game.UI;
import com.sb9.foloke.sectorb9.*;
import android.widget.*;
import android.graphics.*;
import android.view.*;
import com.sb9.foloke.sectorb9.game.DataSheets.*;
import android.content.*;
import com.sb9.foloke.sectorb9.game.Entities.Buildings.*;
import android.view.View.*;
import android.graphics.drawable.*;
import com.sb9.foloke.sectorb9.game.Entities.Ships.*;


public class ConstructorUI
{
	
    public static ModulesDataSheet.HullModule selectedHull;
	public static ModulesDataSheet.EngineModule selectedEngine;
	public static ModulesDataSheet.TurretModule selectedTurrets[];
	public static ModulesDataSheet.WeaponModule selectedWeapons[];
	public static ModulesDataSheet.GeneratorModule selectedGenerator;
	public static ModulesDataSheet.Module selectedShield;
	private static float scaleX,scaleY;
	
	private static class CustomAdapter extends ArrayAdapter
	{
		ModulesDataSheet.Module[] modules;
		
		Context mContext;

		private static class ViewHolder 
		{
			ImageView mImage;
			TextView mName;
			TextView mInfo;
		}
		
		public CustomAdapter(Context context, ModulesDataSheet.Module[] modules) 
		{
			super(context, R.layout.spinner_row_for_constructor_ui);
			this.modules = modules;
			
			this.mContext = context;
		}
		
		@Override
		public int getCount() 
		{
			return modules.length;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) 
		{
			ViewHolder mViewHolder = new ViewHolder();
			if (convertView == null) 
			{
				LayoutInflater mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = mInflater.inflate(R.layout.spinner_row_for_constructor_ui, parent, false);
				mViewHolder.mImage = (ImageView) convertView.findViewById(R.id.sr_iv_image);
				mViewHolder.mName = (TextView) convertView.findViewById(R.id.sr_tv_name);
				mViewHolder.mInfo = (TextView) convertView.findViewById(R.id.sr_tv_info);
				convertView.setTag(mViewHolder);
			} 
			else 
			{
				mViewHolder = (ViewHolder) convertView.getTag();
			}
			mViewHolder.mImage.setImageBitmap(modules[position].image);
			mViewHolder.mName.setText(modules[position].name);
			mViewHolder.mInfo.setText("mass:"+modules[position].mass);

			return convertView;
		}
		
		@Override
		public View getDropDownView(int position, View convertView, ViewGroup parent) 
		{
			return getView(position, convertView, parent);
		}
	}
	
	static MainActivity MA;
	public static void init(MainActivity in_MA)
	{
		MA=in_MA;
		scaleY=MA.getGameManager().getScreenSize().y/1600f;
		if(scaleX>2)
			scaleX=2;
		if(scaleX<0.5f)
			scaleX=0.5f;
		scaleX=scaleY;
	}
	
	public static void update(final SpaceDock caller)
	{
		//MainActivity tma=ma
		LinearLayout MAL =MA.findViewById(R.id.ship_constructor_ui_MainLinearLayout);
		MAL.setBackgroundColor(Color.parseColor("#55ffffff"));
		Button produce=MA.findViewById(R.id.ship_constructor_ui_produce_button);
		produce.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				caller.setToProduce(selectedHull,selectedEngine,selectedGenerator,selectedShield,selectedTurrets,selectedWeapons);
			}
		}
		);
		//spinners
		Spinner hullSpinner = MA.findViewById(R.id.ship_constructor_ui_hull_Spinner);
		Spinner engineSpinner = MA.findViewById(R.id.ship_constructor_ui_engine_Spinner);
		Spinner generatorSpinner = MA.findViewById(R.id.ship_constructor_ui_generator_Spinner);
		Spinner shildsSpinner = MA.findViewById(R.id.ship_constructor_ui_shields_Spinner);
		
		//adapters
		CustomAdapter hullSpinnerAdp = new CustomAdapter(MA,ModulesDataSheet.getOfType(ModulesDataSheet.type.HULL));
		CustomAdapter engineSpinnerAdp = new CustomAdapter(MA,ModulesDataSheet.getOfType(ModulesDataSheet.type.ENGINE));
		CustomAdapter shieldsSpinnerAdp = new CustomAdapter(MA,ModulesDataSheet.getOfType(ModulesDataSheet.type.SHILED));
		CustomAdapter generatorSpinnerAdp = new CustomAdapter(MA,ModulesDataSheet.getOfType(ModulesDataSheet.type.GENERATOR));
		
		//adapters set
		hullSpinner.setAdapter(hullSpinnerAdp);
		engineSpinner.setAdapter(engineSpinnerAdp);
		shildsSpinner.setAdapter(shieldsSpinnerAdp);
		generatorSpinner.setAdapter(generatorSpinnerAdp);
		
		//backgroud set
		hullSpinner.setBackgroundColor(Color.parseColor("#55ffffff"));
		engineSpinner.setBackgroundColor(Color.parseColor("#55ffffff"));
		shildsSpinner.setBackgroundColor(Color.parseColor("#55ffffff"));
		generatorSpinner.setBackgroundColor(Color.parseColor("#55ffffff"));
		
		//listeners set
		hullSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() 
		{
			public void onItemSelected(AdapterView<?> parent,View itemSelected, int selectedItemPosition, long selectedId) 
			{
				selectedHull=(ModulesDataSheet.HullModule)ModulesDataSheet.getOfType(ModulesDataSheet.type.HULL)[selectedItemPosition];
				organaizeWeaponsSpinners();
				updatePreview(null,null);
			}
			
			public void onNothingSelected(AdapterView<?> parent){}
		});
		
		engineSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() 
		{
			public void onItemSelected(AdapterView<?> parent,View itemSelected, int selectedItemPosition, long selectedId) 
			{
				selectedEngine=(ModulesDataSheet.EngineModule)ModulesDataSheet.getOfType(ModulesDataSheet.type.ENGINE)[selectedItemPosition];
			}

		public void onNothingSelected(AdapterView<?> parent){}
		});
		
		generatorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() 
		{
			public void onItemSelected(AdapterView<?> parent,View itemSelected, int selectedItemPosition, long selectedId) 
			{
				selectedGenerator=(ModulesDataSheet.GeneratorModule)ModulesDataSheet.getOfType(ModulesDataSheet.type.GENERATOR)[selectedItemPosition];
			}

			public void onNothingSelected(AdapterView<?> parent){}
		});
		
		shildsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() 
		{
			public void onItemSelected(AdapterView<?> parent,View itemSelected, int selectedItemPosition, long selectedId) 
			{
				selectedShield=ModulesDataSheet.getOfType(ModulesDataSheet.type.SHILED)[selectedItemPosition];
			}

			public void onNothingSelected(AdapterView<?> parent){}
		});
		
		
		
	}
	
	private static void updatePreview(PointF f,PointF gf)
	{
		Bitmap hull=selectedHull.image;
		Bitmap bmOverlay = Bitmap.createBitmap(hull);
		Paint p=new Paint();
		p.setColor(Color.RED);
		p.setStyle(Paint.Style.STROKE);
		
		Canvas canvas = new Canvas(bmOverlay);

		canvas.drawBitmap(hull, 0,0, null);
		//Bitmap hulBM=selectedHull.image;
		if(f!=null)
		canvas.drawCircle(f.x+hull.getWidth()/2,f.y+hull.getHeight()/2,3,p);
		
		if(gf!=null)
		{
			p.setStrokeWidth(2);
			canvas.drawLine(f.x+hull.getWidth()/2,f.y+hull.getHeight()/2,f.x+hull.getWidth()/2,f.y+hull.getHeight()/2-5,p);
		}
		//((ImageView)MA.findViewById(R.id.ship_constructor_ui_image)).setBackgroundDrawable(new BitmapDrawable(Bitmap.createScaledBitmap(selectedHull.image,(int)(640*scaleX),(int)(640*scaleY),false)))
		((ImageView)MA.findViewById(R.id.ship_constructor_ui_image)).setImageBitmap(Bitmap.createScaledBitmap(bmOverlay,(int)(320*scaleX),(int)(320*scaleY),false));
		
	}
	
	private static void organaizeWeaponsSpinners()
	{
		
		selectedWeapons=new ModulesDataSheet.WeaponModule[((ModulesDataSheet.HullModule)selectedHull).gunMounts.length];
		selectedTurrets=new ModulesDataSheet.TurretModule[((ModulesDataSheet.HullModule)selectedHull).gunMounts.length];
		
		((LinearLayout)MA.findViewById(R.id.ship_constructor_ui_MainRightLinearLayout)).removeAllViews();
		int turretNumber=0;
		for(ModulesDataSheet.HullModule.GunMount g:((ModulesDataSheet.HullModule)selectedHull).gunMounts)
		{
			
			final ModulesDataSheet.HullModule.GunMount fg=g;
			final int currentTurret=turretNumber;
			
			
			CustomSpinner turretsSpinner=new CustomSpinner(MA,currentTurret);
			CustomAdapter turretsSpinnerAdp = new CustomAdapter(MA,ModulesDataSheet.getOfType(ModulesDataSheet.type.TURRET));
			turretsSpinner.setAdapter(turretsSpinnerAdp);
			turretsSpinner.setBackgroundColor(Color.parseColor("#55ffffff"));
			turretsSpinner.setOnSpinnerEventsListener(new CustomSpinner.OnSpinnerEventsListener()
			{
				public void onSpinnerOpened(Spinner s)
				{
					updatePreview(fg.mountPoints,null);
				}
				
				public void onSpinnerClosed(Spinner s)
				{
					updatePreview(null,null);
				}
			});
			turretsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() 
				{
					
					public void onItemSelected(AdapterView<?> parent,View itemSelected, int selectedItemPosition, long selectedId) 
					{
						selectedTurrets[currentTurret]=(ModulesDataSheet.TurretModule)ModulesDataSheet.getOfType(ModulesDataSheet.type.TURRET)[selectedItemPosition];
					}

					public void onNothingSelected(AdapterView<?> parent){}
				});
			((LinearLayout)MA.findViewById(R.id.ship_constructor_ui_MainRightLinearLayout)).addView(turretsSpinner);
			
			CustomSpinner weaponsSpinner=new CustomSpinner(MA,turretNumber);
			CustomAdapter weaponsSpinnerAdp = new CustomAdapter(MA,ModulesDataSheet.getOfType(ModulesDataSheet.type.WEAPON));
			weaponsSpinner.setAdapter(weaponsSpinnerAdp);
			weaponsSpinner.setBackgroundColor(Color.parseColor("#55ffffff"));
			weaponsSpinner.setOnSpinnerEventsListener(new CustomSpinner.OnSpinnerEventsListener()
				{
					public void onSpinnerOpened(Spinner s)
					{
						updatePreview(fg.mountPoints,fg.mountPoints);
					}

					public void onSpinnerClosed(Spinner s)
					{
						updatePreview(null,null);
					}
				});
			weaponsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() 
				{
					public void onItemSelected(AdapterView<?> parent,View itemSelected, int selectedItemPosition, long selectedId) 
					{
						selectedWeapons[currentTurret]=(ModulesDataSheet.WeaponModule)(ModulesDataSheet.getOfType(ModulesDataSheet.type.WEAPON)[selectedItemPosition]);
					}

					public void onNothingSelected(AdapterView<?> parent){}
				});
			((LinearLayout)MA.findViewById(R.id.ship_constructor_ui_MainRightLinearLayout)).addView(weaponsSpinner);
			
			turretNumber++;
		}
		
	}
	
	public static class CustomSpinner extends Spinner {

		public int number;
		public CustomSpinner(Context c,int number)
		{
			super(c);
			this.number=number;
		}
		/**
		 * An interface which a client of this Spinner could use to receive
		 * open/closed events for this Spinner. 
		 */
		 
		 
		public interface OnSpinnerEventsListener {

			/**
			 * Callback triggered when the spinner was opened.
			 */
			void onSpinnerOpened(Spinner spinner);

			/**
			 * Callback triggered when the spinner was closed.
			 */
			void onSpinnerClosed(Spinner spinner);

		}

		private OnSpinnerEventsListener mListener;
		private boolean mOpenInitiated = false;

		// implement the Spinner constructors that you need

		@Override
		public boolean performClick() {
			// register that the Spinner was opened so we have a status
			// indicator for when the container holding this Spinner may lose focus
			mOpenInitiated = true;
			if (mListener != null) {
				mListener.onSpinnerOpened(this);
			}
			return super.performClick();
		}

		@Override
		public void onWindowFocusChanged (boolean hasFocus) {
			if (hasBeenOpened() && hasFocus) {
				performClosedEvent();
			}
		}

		/**
		 * Register the listener which will listen for events.
		 */
		public void setOnSpinnerEventsListener(
            OnSpinnerEventsListener onSpinnerEventsListener) {
			mListener = onSpinnerEventsListener;
		}

		/**
		 * Propagate the closed Spinner event to the listener from outside if needed.
		 */
		public void performClosedEvent() {
			mOpenInitiated = false;
			if (mListener != null) {
				mListener.onSpinnerClosed(this);
			}
		}

		/**
		 * A boolean flag indicating that the Spinner triggered an open event.
		 * 
		 * @return true for opened Spinner 
		 */
		public boolean hasBeenOpened() {
			return mOpenInitiated;
		}

	}
	
}
