package com.sb9.foloke.sectorb9.game.UI;
import com.sb9.foloke.sectorb9.*;
import android.widget.*;
import android.content.*;
import android.view.*;
import android.widget.AdapterView.*;
import android.graphics.drawable.*;
import android.graphics.*;

public class assemblerUI
{
	Integer[] itemsIDs;
	MainActivity MA;
	
	public void init(final MainActivity MA)
	{
		
		this.MA=MA;
		// Получаем экземпляр элемента Spinner
		final Spinner spinner = MA.findViewById(R.id.assembler_uiSpinner);

		// Подключаем свой шаблон с разными значками
		itemsIDs=new Integer[MA.getGame().itemsData.getLenght()];
		for(int i=0;i<MA.getGame().itemsData.getLenght();i++)
			itemsIDs[i]=MA.getGame().itemsData.findById(i).ID;
		MyCustomAdapter adapter = new MyCustomAdapter(MA,
													  R.layout.assembler_spinner_row, itemsIDs);

		// Вызываем адапетр
		spinner.setAdapter(adapter);
		//spinner.setPromptId(R.string.chooseday);
		spinner.setSelection(2, true);
		spinner.setBackgroundColor(Color.BLACK);
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> parent, View view,
										   int pos, long id) {

				}

				@Override
				public void onNothingSelected(AdapterView<?> arg0) {

				}
			});
	}
	public class MyCustomAdapter extends ArrayAdapter<Integer> {

		public MyCustomAdapter(Context context, int textViewResourceId,
							   Integer[] objects) {
			super(context, textViewResourceId, objects);

		}

		@Override
		public View getDropDownView(int position, View convertView,
									ViewGroup parent) {

			return getCustomView(position, convertView, parent);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			return getCustomView(position, convertView, parent);
		}

		public View getCustomView(int position, View convertView,
								  ViewGroup parent) {

			LayoutInflater inflater = MA.getLayoutInflater();
			View row = inflater.inflate(R.layout.assembler_spinner_row, parent, false);
			TextView label = (TextView) row.findViewById(R.id.weekofday);
			label.setText(MA.getGame().itemsData.findById(itemsIDs[position]).name+"");

			ImageView icon = (ImageView) row.findViewById(R.id.icon);
			BitmapDrawable bdrawable;
			bdrawable = new BitmapDrawable(MA.getResources(),MA.getGame().itemsData.findById(itemsIDs[position]).image);
			icon.setImageDrawable(bdrawable);
			
			//icon.setD(MA.getGame().itemsData.findById(itemsIDs[position]).image);
	
			return row;
			}
			}
}
