package com.nakardo.atableview.demo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nakardo.atableview.foundation.NSIndexPath;
import com.nakardo.atableview.internal.ATableViewCellAccessoryView.ATableViewCellAccessoryType;
import com.nakardo.atableview.protocol.ATableViewDataSourceExt;
import com.nakardo.atableview.protocol.ATableViewDelegate;
import com.nakardo.atableview.view.ATableView;
import com.nakardo.atableview.view.ATableView.ATableViewStyle;
import com.nakardo.atableview.view.ATableViewCell;
import com.nakardo.atableview.view.ATableViewCell.ATableViewCellSelectionStyle;
import com.nakardo.atableview.view.ATableViewCell.ATableViewCellStyle;

public class MainActivity extends Activity {
	private List<List<String>> mCapitals;
	private List<List<String>> mProvinces;
	private String[] mRegions = {
		"Northwest", "Gran Chaco", "Mesopotamia", "Pampas", "Cuyo", "Patagonia", "Single"
	};
	
	private static List<List<String>> createProvincesList() {
		List<List<String>> provinces = new ArrayList<List<String>>();
		
		provinces.add(Arrays.asList(new String[] { "Jujuy", "Salta", "Tucum�n", "Catamarca" }));
		provinces.add(Arrays.asList(new String[] { "Formosa", "Chaco", "Santiago del Estero" }));
		provinces.add(Arrays.asList(new String[] { "Misiones", "Entre R�os", "Corrientes" }));
		provinces.add(Arrays.asList(new String[] { "C�rdoba", "Santa Fe", "La Pampa", "Buenos Aires" }));
		provinces.add(Arrays.asList(new String[] { "San Juan", "La Rioja", "Mendoza", "San Luis" }));
		provinces.add(Arrays.asList(new String[] { "Rio Negro", "Neuqu�n", "Chubut", "Santa Cruz", "Tierra del Fuego" }));
		provinces.add(Arrays.asList(new String[] { "Single Province" }));
		
		return provinces;
	}
	
	private static List<List<String>> createCapitalsList() {
		List<List<String>> capitals = new ArrayList<List<String>>();
		
		capitals.add(Arrays.asList(new String[] { "San Salvador de Jujuy", "Salta", "San Miguel de Tucuman", "S.F.V. de Catamarca" }));
		capitals.add(Arrays.asList(new String[] { "Formosa", "Resistencia", "Santiago del Estero" }));
		capitals.add(Arrays.asList(new String[] { "Posadas", "Parana", "Corrientes" }));
		capitals.add(Arrays.asList(new String[] { "Cordoba", "Santa Fe", "Santa Rosa", "Capital Federal" }));
		capitals.add(Arrays.asList(new String[] { "San Juan", "La Rioja", "Mendoza", "San Luis" }));
		capitals.add(Arrays.asList(new String[] { "Viedma", "Neuqu�n", "Rawson", "Rio Gallegos", "Ushuaia" }));
		capitals.add(Arrays.asList(new String[] { "Single Capital" }));
		
		return capitals;
	}
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        mCapitals = createCapitalsList();
        mProvinces = createProvincesList();
        
        ATableView tableView = new ATableView(ATableViewStyle.Grouped, this);
        tableView.setDataSource(new SampleATableViewDataSource());
        tableView.setDelegate(new SampleATableViewDelegate());
        
        FrameLayout container = (FrameLayout)findViewById(android.R.id.content);
        container.addView(tableView);
    }

    private Drawable getDrawableForRow(int row) {
    	Drawable drawable = null;
		switch (row) {
			case 0:
				drawable = getResources().getDrawable(R.drawable.san_juan);
				break;
			case 1:
				drawable = getResources().getDrawable(R.drawable.la_rioja);
				break;
			case 2:
				drawable = getResources().getDrawable(R.drawable.mendoza);
				break;
			default:
				drawable = getResources().getDrawable(R.drawable.san_luis);
				break;
		}
		
		return drawable;
    }
    
	private class SampleATableViewDataSource extends ATableViewDataSourceExt {
		
		@Override
		public ATableViewCell cellForRowAtIndexPath(ATableView tableView, NSIndexPath indexPath) {
			String cellIdentifier = "CellIdentifier0";
			ATableViewCellStyle cellStyle = ATableViewCellStyle.Default;
			ATableViewCellAccessoryType accessoryType = ATableViewCellAccessoryType.None;
			
			// set proper style and identifier for cells on each section.
			int section = indexPath.getSection();
			if (section == 0) {
				cellIdentifier = "CellIdentifier1";
				cellStyle = ATableViewCellStyle.Subtitle;
				accessoryType = ATableViewCellAccessoryType.DisclosureIndicator;
			} else if (section == 1) {
				cellIdentifier = "CellIdentifier2";
				cellStyle = ATableViewCellStyle.Value1;
				accessoryType = ATableViewCellAccessoryType.DisclosureButton;
			} else if (section == 2) {
				cellIdentifier = "CellIdentifier3";
				cellStyle = ATableViewCellStyle.Value2;
				accessoryType = ATableViewCellAccessoryType.Checkmark;
			}  else if (section == 5) {
				cellIdentifier = "CustomCellIdentifier";
			}
			
			// get row data.
			int row = indexPath.getRow();
			String province = mProvinces.get(section).get(row);
			
			ATableViewCell cell = null;
			if (section != 5) {
				cell = dequeueReusableCellWithIdentifier(cellIdentifier);
				if (cell == null) {
					cell = new ATableViewCell(cellStyle, cellIdentifier, MainActivity.this);
					cell.setSelectionStyle(ATableViewCellSelectionStyle.Blue);
					cell.setAccessoryType(accessoryType);
				}
				
				// imageView
				ImageView imageView = cell.getImageView();
				if (indexPath.getSection() == 4) {
					int paddingLeft = (int) (8 * getResources().getDisplayMetrics().density);
					imageView.setPadding(paddingLeft, 0, 0, 0);
					imageView.setImageDrawable(getDrawableForRow(row));
				} else {
					imageView.setPadding(0, 0, 0, 0);
					imageView.setImageDrawable(null);
				}
				
				// textLabel
				cell.getTextLabel().setText(province);
				
				// detailTextLabel
				TextView detailTextLabel = cell.getDetailTextLabel();
				if (detailTextLabel != null) {
					String capital = mCapitals.get(section).get(row);
					detailTextLabel.setText(capital);
				}
			} else {
				MyCustomCell customCell = (MyCustomCell)dequeueReusableCellWithIdentifier(cellIdentifier);
				if (cell == null) {
					customCell = new MyCustomCell(ATableViewCellStyle.Default, cellIdentifier, MainActivity.this);
					customCell.setSelectionStyle(ATableViewCellSelectionStyle.Gray);
				}
				
				// customLabel
				customCell.getCustomLabel().setText(province);
				
				cell = customCell;
			}
			
			return cell;
		}

		@Override
		public int numberOfRowsInSection(ATableView tableView, int section) {
			return mCapitals.get(section).size();
		}
		
		@Override
		public int numberOfSectionsInTableView(ATableView tableView) {
			return mRegions.length;
		}
		
		@Override
		public String titleForHeaderInSection(ATableView tableView, int section) {
			if (section < mRegions.length) {
				return mRegions[section];
			}
			
			return null;
		}

		@Override
		public int numberOfRowStyles() {
			return 5;
		}

		@Override
		public int styleForRowAtIndexPath(NSIndexPath indexPath) {
			int section = indexPath.getSection();
			if (section < 4) {
				return section;
			} else if (section == 4 || section == 6) {
				return 3;
			}
			
			return 4;
		}
	}
	
	private class SampleATableViewDelegate extends ATableViewDelegate {
		
		@Override
		public int heightForRowAtIndexPath(ATableView tableView, NSIndexPath indexPath) {
			return 44;
		}
		
		@Override
		public void didSelectRowAtIndexPath(ATableView tableView, NSIndexPath indexPath) {
			CharSequence text = String.format("Selected IndexPath [%d, %d]",
					indexPath.getSection(), indexPath.getRow());
			Toast toast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT);
			toast.show();
		}
		
		@Override
		public void accessoryButtonTappedForRowWithIndexPath(ATableView tableView, NSIndexPath indexPath) {
			CharSequence text = String.format("Tapped DisclosureButton at indexPath [%d, %d]",
					indexPath.getSection(), indexPath.getRow());
			Toast toast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT);
			toast.show();
		}
	}
}
