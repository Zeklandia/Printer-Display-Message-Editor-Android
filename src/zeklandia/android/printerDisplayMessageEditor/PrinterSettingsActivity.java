package zeklandia.android.printerDisplayMessageEditor;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;

public class PrinterSettingsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (PrinterSettingsStorage.useLightTheme(this)) {
            super.setTheme(R.style.HoloLight);
        }

        if (savedInstanceState == null) {
            PrinterSettingsFragment fragment = new PrinterSettingsFragment();
            fragment.setArguments(getIntent().getExtras());
            getFragmentManager().beginTransaction().add(android.R.id.content, fragment).commit();
        }

        ActionBar mActionBar = getActionBar();
        if (mActionBar != null) {
            mActionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent keyEvent) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, keyEvent);
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
    }
}