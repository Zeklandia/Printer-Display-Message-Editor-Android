package zeklandia.android.printerDisplayMessageEditor;

import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;
import android.widget.ListView;


public class PrinterSettingsFragment extends PreferenceFragment {

    private static final String EXTRA_LIST_POSITION = "list_position";
    private static final String EXTRA_LIST_VIEW_OFFSET = "list_view_top";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_general);

        SwitchPreference holoTheme = (SwitchPreference) findPreference("THEME");
        if (holoTheme != null) {
            holoTheme.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    if ((Boolean) newValue != PrinterSettingsStorage.useLightTheme(getActivity())) {
                        Intent intent = new Intent(getActivity(), PrinterSettingsActivity.class);

                        startActivity(intent);
                        getActivity().finish();


                        getActivity().overridePendingTransition(android.R.anim.fade_in,
                                android.R.anim.fade_out);
                    }
                    return true;
                }
            });
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        final Bundle args = getArguments();
        if (args != null) {
            getListView().setSelectionFromTop(
                    args.getInt(EXTRA_LIST_POSITION, 0),
                    args.getInt(EXTRA_LIST_VIEW_OFFSET, 0)
            );
        }
    }

    public ListView getListView() {
        return (ListView) getView().findViewById(android.R.id.list);
    }

}