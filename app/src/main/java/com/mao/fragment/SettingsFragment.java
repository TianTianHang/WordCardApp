package com.mao.fragment;

import android.os.Bundle;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreference;
import com.mao.activity.R;
import org.jetbrains.annotations.NotNull;

public class SettingsFragment extends PreferenceFragmentCompat {
    public SettingsFragment() {
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);
        SwitchPreference notificationPref = findPreference("switch theme");
        if (notificationPref != null) {
            notificationPref.setOnPreferenceChangeListener(new SwitchPreference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(@NonNull @NotNull Preference preference, Object newValue) {
                    boolean isChecked = (boolean) newValue;
                    Log.d("d", "change");
                    AppCompatDelegate delegate = AppCompatDelegate.create(requireActivity(), null);
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
    }

}