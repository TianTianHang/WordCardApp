package com.mao.fragment;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.EditTextPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreference;
import com.mao.activity.R;
import com.mao.dao.WordItemDao;
import com.mao.db.DBHelper;
import com.mao.model.WordMP3;
import org.jetbrains.annotations.NotNull;

public class SettingsFragment extends PreferenceFragmentCompat {
    public SettingsFragment() {
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);
        SwitchPreference notificationPref = findPreference("switch theme");
        AppCompatDelegate delegate = AppCompatDelegate.create(requireActivity(), null);
        if (notificationPref != null) {
            notificationPref.setOnPreferenceChangeListener(new SwitchPreference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(@NonNull @NotNull Preference preference, Object newValue) {
                    boolean isChecked = (boolean) newValue;
                    Log.d("d", "change");

                    if (isChecked) {
                        // SwitchPreference已开启
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES); // Night mode
                    } else {
                        // SwitchPreference已关闭
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO); // Day mode
                    }
                    delegate.applyDayNight();
                    return true;
                }


            });
        }
        EditTextPreference etp1=findPreference("edp_drop_word");

        EditTextPreference etp2=findPreference("edp_drop_word_pron");

        if (etp1 != null) {
            etp1.setText("");
            etp1.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(@NonNull @NotNull Preference preference, Object newValue) {
                    if("yes".equals(newValue)){
                        new WordItemDao(requireContext()).getDbHelper().dropDatabase();
                        EditTextPreference etp1= (EditTextPreference) preference;
                        etp1.setText("");
                        return true;
                    }
                    return false;
                }
            });
        }
        if (etp2 != null) {
            etp2.setText("");
            etp2.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(@NonNull @NotNull Preference preference, Object newValue) {
                    if("yes".equals(newValue)){
                        //TODO
                        String meg=new WordMP3(requireContext()).delete();
                        Toast.makeText(requireContext(),meg,Toast.LENGTH_SHORT).show();
                        EditTextPreference etp2= (EditTextPreference) preference;
                        etp2.setText("");
                        return true;
                    }
                    return false;
                }
            });
        }
    }

}