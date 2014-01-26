package zeklandia.android.printerDisplayMessageEditor;

/*
 * TODO:
 * 
 * Insert this code so that it works:
 * 				
 *				final Button button = (Button) findViewById(R.id.buttonSend);
 *			       button.setOnClickListener(new View.OnClickListener() {
 *			           public void onClick(View v) {
 *			           	if (((RadioGroup)findViewById(R.id.radioGroupMessage)).getCheckedRadioButtonId() == 0 ) {
 *			           	 new sendRDYMSGTask().execute(editTextIP.getText().toString(), editTextMessage.getText().toString());
 *			           	} else if (((RadioGroup)findViewById(R.id.radioGroupMessage)).getCheckedRadioButtonId() == 1 ) {
 *			           	 new sendERRMSGTask().execute(editTextIP.getText().toString(), editTextMessage.getText().toString());
 *			           	} else if (((RadioGroup)findViewById(R.id.radioGroupMessage)).getCheckedRadioButtonId() == 2 ) {
 *			           	 new sendOPMSGTask().execute(editTextIP.getText().toString(), editTextMessage.getText().toString());
 *			           	}
 *			           	
 *			           }
 *			       });
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerTitleStrip;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;

public class MainActivity extends FragmentActivity {

	SectionsPagerAdapter mSectionsPagerAdapter;

	static ViewPager mViewPager;
	
	Intent helpActivity;
	Intent aboutActivity;
	
	static ListAdapter mPrinterListAdapter;
	List<HashMap<String, String>> mPrinterListData = new ArrayList<HashMap<String, String>>();
	static String[] mPrinterListFrom = new String[]{"PRINTER_NAME", "PRINTER_IP"};
	static int[] mPrinterListTo = new int[]{R.id.title_text, R.id.subtitle_text};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
        if(PrinterSettingsStorage.useLightTheme(getContext())) {
            super.setTheme(R.style.HoloLight);
        }

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());

		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

        if(PrinterSettingsStorage.useLightTheme(getContext())) {
        PagerTitleStrip scrollTitle = (PagerTitleStrip) findViewById(R.id.pager_title_strip);
            scrollTitle.setTextColor(Color.parseColor("#323232"));
        }
    }
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }
	
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                startActivity(new Intent(this, PrinterSettingsActivity.class));
                finish();
                return true;
            default:
                return true;
        }
    }

    public Context getContext() {
        return MainActivity.this;
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			Fragment fragment = new SectionFragment();
			Bundle args = new Bundle();
			args.putInt(SectionFragment.ARG_SECTION_NUMBER, position);
			fragment.setArguments(args);
			
			return fragment;
		}

		@Override
		public int getCount() {
			return 3;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			switch (position) {
			case 0:
				return getString(R.string.main_title).toUpperCase();
			case 1:
				return getString(R.string.help_title).toUpperCase();
			case 2:
				return getString(R.string.about_title).toUpperCase();
			}
			return null;
		}
	}


	public static class SectionFragment extends Fragment {

		public static final String ARG_SECTION_NUMBER = "section_number";
		
		public SectionFragment() {
			
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			LayoutInflater inflater1 = (LayoutInflater) mViewPager.getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			Bundle args = new Bundle();
			int resId = (Integer) getArguments().get(ARG_SECTION_NUMBER);
			switch (resId) {
			case 0:
				resId = R.layout.main_activity;
				break;
			case 1:
				resId = R.layout.helplayout;
				break;
			case 2:
				resId = R.layout.aboutlayout;
			}

			View view = inflater1.inflate(resId, null);
			
			return view;
		}
	}

}