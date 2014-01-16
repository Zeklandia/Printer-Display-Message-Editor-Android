package com.qrohlf.printerhack;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.admob.android.ads.AdManager;
import com.admob.android.ads.AdView;

public class PrinterHack extends Activity implements OnClickListener {
    private static final int PJL_PORT = 9100;
    private final static String ReadyMsgFormat = "\u001B%-12345X@PJL JOB\n" +
    "@PJL RDYMSG DISPLAY=\"{0}\"\n@PJL EOJ\n\u001B%-12345X\n";
    private final static String OpMsgFormat = "\u001B%-12345X@PJL JOB\n" +
    "@PJL OPMSG DISPLAY=\"{0}\"\n@PJL EOJ\n\u001B%-12345X\n";
	private EditText ipEditText;
	private EditText msgEditText;
	private Button rdymsgButton;
	private Button opmsgButton;
	private ImageButton rdymsgHelpButton;
	private ImageButton opmsgHelpButton;
	private ListView printerSearchListView;
	private AdView adView;
	Intent helpActivity;
	Intent aboutActivity;
	Intent donateActivity;
	private ListAdapter mPrinterListAdapter;
	List<HashMap<String, String>> mPrinterListData = new ArrayList<HashMap<String, String>>();
	private String[] mPrinterListFrom = new String[]{"PRINTER_NAME", "PRINTER_IP"};
	private int[] mPrinterListTo = new int[]{R.id.title_text, R.id.subtitle_text};
	private static final boolean test_mode = false; //TODO: change this for release!
	
	public static class Dialogs {
		public static final int RDYMSG_HELP_ID = 0;
		public static final int OPMSG_HELP_ID = 1;
		public static final int PRINTER_SEARCH_ID = 2;
		
	}

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        setupViews();
        helpActivity = new Intent(getBaseContext(),
                HelpActivity.class);
        aboutActivity = new Intent(getBaseContext(),
        		AboutActivity.class);
        donateActivity = new Intent(getBaseContext(),
        		DonateActivity.class);
        HashMap<String, String> testMap = new HashMap<String, String>();
        for (int i=0; i<8; i++) {
        	testMap.put("PRINTER_NAME", "Printer " + i);
            testMap.put("PRINTER_IP", "192.168.1." + i);
        }
        mPrinterListData.add(testMap);
 
        
    }
    
    private void setupViews() {
		ipEditText= (EditText) findViewById(R.id.printer_ip);
		ipEditText.setImeOptions(EditorInfo.IME_ACTION_DONE);
		msgEditText = (EditText) findViewById(R.id.printer_message);
		msgEditText.setImeOptions(EditorInfo.IME_ACTION_DONE);
		rdymsgButton = (Button) findViewById(R.id.rdymsg_button);
		rdymsgButton.setOnClickListener(this);
		opmsgButton = (Button) findViewById(R.id.opmsg_button);
		opmsgButton.setOnClickListener(this);
		rdymsgHelpButton = (ImageButton) findViewById(R.id.rdymsg_button_help);
		rdymsgHelpButton.setOnClickListener(this);
		opmsgHelpButton = (ImageButton) findViewById(R.id.opmsg_button_help);
		opmsgHelpButton.setOnClickListener(this);
		adView = (AdView) findViewById(R.id.ad);
		adView.requestFreshAd(); //TODO
		if (test_mode) {
			AdManager.setTestDevices( new String[] { "DB579D2E7CDA2FE32F71C8CB4B59EDCC" } );
		}
	}
    
    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.options_main, menu);
	    if (test_mode) {
	    	menu.findItem(R.id.printer_search_test).setVisible(true);
	    	menu.findItem(R.id.ad_test).setVisible(true);
	    }
	    return true;
	}
    
    @Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	    case R.id.main_help:
	    	startActivity(helpActivity);
	    	return true;
	    case R.id.about_app:
	    	startActivity(aboutActivity);
	    	return true;
	    case R.id.support:
	    	startActivity(donateActivity);
	    	return true;
	    case R.id.printer_search_test:
	    	showDialog(Dialogs.PRINTER_SEARCH_ID);
	    	return true;
	    case R.id.ad_test:
	    	adView.requestFreshAd();
	    	return true;
	    default:
	        return super.onOptionsItemSelected(item);
	    }
	}
    
    protected Dialog onCreateDialog(int id) {
        Dialog dialog = null;
        switch(id) {
        case Dialogs.OPMSG_HELP_ID:
            AlertDialog.Builder opmsgbuilder = new AlertDialog.Builder(this);
            opmsgbuilder.setTitle("OPMSG Help")
            .setMessage(R.string.opmsg_help_text)
            .setCancelable(true)
            .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					
				}
			});
            dialog = opmsgbuilder.create();
            break;
            
        case Dialogs.RDYMSG_HELP_ID:
        	AlertDialog.Builder rdymsgbuilder = new AlertDialog.Builder(this);
            rdymsgbuilder.setTitle("RDYMSG Help")
            .setMessage(R.string.rdymsg_help_text)
            .setCancelable(true)
            .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					
				}
			});
            dialog = rdymsgbuilder.create();
            break;
            
        /*
         * Create the search dialog. Use the dialog class because it makes it easier to 
         * load a custom layout file into the dialog. 
         * TODO: write the layout file, all the listeners that go with it, etc. etc.
         */
        case Dialogs.PRINTER_SEARCH_ID:
        	Dialog searchdialog = new Dialog(this);
        	searchdialog.setTitle("Pick a printer");
        	searchdialog.setContentView(R.layout.search_dialog_layout);
        	searchdialog.setCancelable(true);
        	dialog = searchdialog;
        	printerSearchListView = (ListView)findViewById(R.id.printer_search_listview);
        	break;
        default:
            dialog = null;
        }
        return dialog;
    }

    
    
	@Override
	protected void onPrepareDialog(int id, Dialog dialog) {
		switch(id) {
		case Dialogs.PRINTER_SEARCH_ID:
			//TODO: Zero the listview and start fetching printers
			mPrinterListAdapter = new SimpleAdapter(this, mPrinterListData, R.layout.list_item, mPrinterListFrom, mPrinterListTo);
			printerSearchListView.setAdapter(mPrinterListAdapter);
		default:
			super.onPrepareDialog(id, dialog);
		}
	}

	/**
	 * Listen for user input
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rdymsg_button:
			new RDYMSGTask().execute(ipEditText.getText().toString(), msgEditText.getText().toString());
			break;
		case R.id.opmsg_button:
			new OPMSGTask().execute(ipEditText.getText().toString(), msgEditText.getText().toString());
			break;
		case R.id.rdymsg_button_help:
			showDialog(Dialogs.RDYMSG_HELP_ID);
			break;
		case R.id.opmsg_button_help:
			showDialog(Dialogs.OPMSG_HELP_ID);
			break;
		default:
			break;
		}
		
		
	}
	
	private class PrinterSearchTask extends AsyncTask<String, Void, Integer> {

		@Override
		protected Integer doInBackground(String... params) {
			// TODO Auto-generated method stub
			return null;
		}
		
	}
	
	/**
	 * Asynchronous Task to send RDYMSG commands
	 * @author QRohlf
	 *
	 */
	private class RDYMSGTask extends AsyncTask<String, Void, Integer> {
		public static final int TOO_LONG_ERROR = 0;
		public static final int CONNECTION_ERROR = 1;
		public static final int SUCESS = 2;
		
		protected Integer doInBackground(String... params) {
			return sendRdyMsg(params[0], params[1]);
		}
		
		protected void onPostExecute(Integer result) {
			switch(result) {
			case TOO_LONG_ERROR:
				Toast.makeText(PrinterHack.this, "Message exceeds 16 character limit", Toast.LENGTH_SHORT).show();
				break;
			case SUCESS:
				Toast.makeText(PrinterHack.this, "message sent", Toast.LENGTH_SHORT).show();
				break;
			case CONNECTION_ERROR:
				Toast.makeText(PrinterHack.this, "message error", Toast.LENGTH_SHORT).show();
				break;
			default:
				break;
			}
		}
		
		int sendRdyMsg(String ip, String message) {
			if (message.length() > 16) {
			return TOO_LONG_ERROR;
			}
			try {
				Socket socket = new Socket(ip, PJL_PORT);
				OutputStream out = socket.getOutputStream();
				out.write(MessageFormat.format(ReadyMsgFormat, message).getBytes());
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
	
	private class OPMSGTask extends AsyncTask<String, Void, Integer> {
		public static final int TOO_LONG_ERROR = 0;
		public static final int CONNECTION_ERROR = 1;
		public static final int SUCESS = 2;
		
		protected Integer doInBackground(String... params) {
			return sendOpMsg(params[0], params[1]);
		}
		
		protected void onPostExecute(Integer result) {
			switch(result) {
			case TOO_LONG_ERROR:
				Toast.makeText(PrinterHack.this, "Message exceeds 16 character limit", Toast.LENGTH_SHORT).show();
				break;
			case SUCESS:
				Toast.makeText(PrinterHack.this, "message sent", Toast.LENGTH_SHORT).show();
				break;
			case CONNECTION_ERROR:
				Toast.makeText(PrinterHack.this, "message error", Toast.LENGTH_SHORT).show();
				break;
			default:
				break;
			}
		}
		
		int sendOpMsg(String ip, String message) {
			if (message.length() > 16) {
			return TOO_LONG_ERROR;
			}
		try {
			Socket socket = new Socket(ip, PJL_PORT);
			OutputStream out = socket.getOutputStream();
			out.write(MessageFormat.format(OpMsgFormat, message).getBytes());
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