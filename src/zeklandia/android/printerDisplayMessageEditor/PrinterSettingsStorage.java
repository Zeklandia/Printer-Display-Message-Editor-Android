package zeklandia.android.printerDisplayMessageEditor;

import android.content.Context;
import android.preference.PreferenceManager;

public class PrinterSettingsStorage {
    public static boolean useLightTheme(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean("THEME", false);
    }
}
