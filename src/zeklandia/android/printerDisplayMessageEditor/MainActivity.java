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

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.RadioGroup;
import android.widget.Toast;

public class MainActivity extends FragmentActivity {

	SectionsPagerAdapter mSectionsPagerAdapter;

	static ViewPager mViewPager;
	
	private static final int printerPort = 9100;
    private final static String formatRDYMSG = "\u001B%-12345X@PJL JOB\n" +
    "@PJL RDYMSG DISPLAY=\"{0}\"\n@PJL EOJ\n\u001B%-12345X\n";
    private final static String formatOPMSG = "\u001B%-12345X@PJL JOB\n" +
    "@PJL OPMSG DISPLAY=\"{0}\"\n@PJL EOJ\n\u001B%-12345X\n";
    private final static String formatERRMSG = "\u001B%-12345X@PJL JOB\n" +
    "@PJL ERRMSG DISPLAY=\"{0}\"\n@PJL EOJ\n\u001B%-12345X\n";
    
	static EditText editTextIP;
	static EditText editTextMessage;
	
	Intent helpActivity;
	Intent aboutActivity;
	
	static ListAdapter mPrinterListAdapter;
	List<HashMap<String, String>> mPrinterListData = new ArrayList<HashMap<String, String>>();
	static String[] mPrinterListFrom = new String[]{"PRINTER_NAME", "PRINTER_IP"};
	static int[] mPrinterListTo = new int[]{R.id.title_text, R.id.subtitle_text};
	static final boolean debug = false;
	
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
				resId = R.layout.main;
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

private class sendRDYMSGTask extends AsyncTask<String, Void, Integer> {
        public static final int TOO_LONG_ERROR = 0;
        public static final int CONNECTION_ERROR = 1;
        public static final int SUCESS = 2;
        
        protected Integer doInBackground(String... params) {
                return sendRDYMSG(params[0], params[1]);
        }
        
        protected void onPostExecute(Integer result) {
                switch(result) {
                case SUCESS:
                        Toast.makeText(MainActivity.this, "Sent", Toast.LENGTH_SHORT).show();
                        break;
                case CONNECTION_ERROR:
                        Toast.makeText(MainActivity.this, "An error occured", Toast.LENGTH_SHORT).show();
                        break;
                default:
                        break;
                }
        }
        
        int sendRDYMSG(String ip, String message) {
                try {
                        Socket socket = new Socket(ip, printerPort);
                        OutputStream out = socket.getOutputStream();
                        out.write(MessageFormat.format(formatRDYMSG, message).getBytes());
                        out.close();
                        socket.close();
                } catch (UnknownHostException e) {
                        e.printStackTrace();
                        return CONNECTION_ERROR;
                } catch (IOException e) {
                        e.printStackTrace();
                        return CONNECTION_ERROR;
                }
                return SUCESS;
        }
}

private class sendERRMSGTask extends AsyncTask<String, Void, Integer> {
    public static final int TOO_LONG_ERROR = 0;
    public static final int CONNECTION_ERROR = 1;
    public static final int SUCESS = 2;
    
    protected Integer doInBackground(String... params) {
            return sendERRMSG(params[0], params[1]);
    }
    
    protected void onPostExecute(Integer result) {
            switch(result) {
            case SUCESS:
                    Toast.makeText(MainActivity.this, "Sent", Toast.LENGTH_SHORT).show();
                    break;
            case CONNECTION_ERROR:
                    Toast.makeText(MainActivity.this, "An error occured", Toast.LENGTH_SHORT).show();
                    break;
            default:
                    break;
            }
    }
    
    int sendERRMSG(String ip, String message) {
            try {
                    Socket socket = new Socket(ip, printerPort);
                    OutputStream out = socket.getOutputStream();
                    out.write(MessageFormat.format(formatERRMSG, message).getBytes());
                    out.close();
                    socket.close();
            } catch (UnknownHostException e) {
                    e.printStackTrace();
                    return CONNECTION_ERROR;
            } catch (IOException e) {
                    e.printStackTrace();
                    return CONNECTION_ERROR;
            }
            return SUCESS;
    }
}

private class sendOPMSGTask extends AsyncTask<String, Void, Integer> {
        public static final int TOO_LONG_ERROR = 0;
        public static final int CONNECTION_ERROR = 1;
        public static final int SUCESS = 2;
        
        protected Integer doInBackground(String... params) {
                return sendOPMSG(params[0], params[1]);
        }
        
        protected void onPostExecute(Integer result) {
                switch(result) {
                case SUCESS:
                        Toast.makeText(MainActivity.this, "Sent", Toast.LENGTH_SHORT).show();
                        break;
                case CONNECTION_ERROR:
                        Toast.makeText(MainActivity.this, "An error occurred", Toast.LENGTH_SHORT).show();
                        break;
                default:
                        break;
                }
        }
        
        int sendOPMSG(String ip, String message) {
        try {
                Socket socket = new Socket(ip, printerPort);
                OutputStream out = socket.getOutputStream();
                out.write(MessageFormat.format(formatOPMSG, message).getBytes());
                out.close();
                socket.close();
        } catch (UnknownHostException e) {
                e.printStackTrace();
                return CONNECTION_ERROR;
        } catch (IOException e) {
                e.printStackTrace();
                return CONNECTION_ERROR;
        }
        return SUCESS;
}
}

}