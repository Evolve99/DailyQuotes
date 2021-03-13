package com.rutershok.daily;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import com.rutershok.daily.database.Storage;
import com.rutershok.daily.utils.Constant;
import com.rutershok.daily.utils.DailyNotification;
import com.rutershok.daily.utils.Dialog;
import com.rutershok.daily.utils.Setting;

public class SettingsFragment extends PreferenceFragment implements Preference.OnPreferenceClickListener, Preference.OnPreferenceChangeListener {

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initSettings();
    }

    private void initSettings() {
        for (String key : Constant.PREFERENCES) {
            findPreference(key).setOnPreferenceClickListener(this);
            findPreference(key).setOnPreferenceChangeListener(this);
        }
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        switch (preference.getKey()) {
            case Constant.PREF_NOTIFICATION_TIME:
                Dialog.showNotificationTimePicker(getActivity());
                break;
                case Constant.PREF_NOTIFICATION_TEST:
                DailyNotification.show(getActivity(), getString(R.string.lorem_ipsium));
                break;
            case Constant.PREF_RATE:
                Setting.openAppPage(getActivity(), getActivity().getPackageName());
                break;
            case Constant.PREF_SHARE:
                Setting.shareApp(getActivity());
                break;
            case Constant.PREF_GOOGLE_PLAY:
                Setting.openPublisherPage(getActivity());
                break;

            case Constant.PREF_INSTAGRAM:
                Setting.openInstagram(getActivity());
                break;
            case Constant.PREF_FACEBOOK:
                Setting.openFacebook(getActivity());
                break;
            case Constant.PREF_TWITTER:
                Setting.openTwitter(getActivity());
                break;
            case Constant.PREF_CONTACT_US:
                Setting.contactUs(getActivity());
                break;
            /*case Constant.PREF_PERMISSIONS:
                break; //to.do*/
            case Constant.PREF_GDPR:
                Dialog.showGdprConsent(getActivity());
                break;
            case Constant.PREF_PRIVACY:
                Setting.openPrivacyPolicy(getActivity());
                break;
        }
        return true;
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        switch (preference.getKey()) {
            case Constant.PREF_ANIMATIONS:
                Storage.setAnimations(getActivity(), (boolean) newValue);
                break;
            case Constant.PREF_NOTIFICATIONS:
                Storage.setNotificationsEnabled(getActivity(), (boolean) newValue);
                break;
            case Constant.PREF_NOTIFICATION_TIME:
                Dialog.showNotificationTimePicker(getActivity());
                break;
            case Constant.PREF_DATA_SAVING:
                Log.e("NewValue", newValue.toString());
                Storage.setSaveData(getActivity(), (String) newValue);
                break;
            case Constant.PREF_BACKGROUND_COLOR:
                Storage.setBackgroundColor(getActivity(), (int) newValue);
                break;
            case Constant.PREF_TEXT_COLOR:
                Storage.setTextColor(getActivity(), (int) newValue);
                break;
            default:
                break;
        }
        return true;
    }
}
