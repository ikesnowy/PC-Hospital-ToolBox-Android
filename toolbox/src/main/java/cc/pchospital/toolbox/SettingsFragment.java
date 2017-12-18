package cc.pchospital.toolbox;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceGroup;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import cc.pchospital.toolbox.db.ChangeNameTask;
import cc.pchospital.toolbox.db.ChangePhoneTask;
import cc.pchospital.toolbox.util.ChangeLocale;
import cc.pchospital.toolbox.util.HttpUtil;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends PreferenceFragment
        implements SharedPreferences.OnSharedPreferenceChangeListener {

    SharedPreferences sharedPreferences;
    private String staffNameBackup;
    private String staffPhoneBackup;

    public SettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preference);

        // 初始化类成员
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
        sharedPreferences = getPreferenceScreen().getSharedPreferences();
        initSummery(getPreferenceScreen());
        staffNameBackup =
                sharedPreferences.getString(getString(R.string.app_db_staff_sphone), null);
        staffPhoneBackup =
                sharedPreferences.getString(getString(R.string.app_db_staff_sphone), null);

        // 切换用户按钮监听
        Preference preference = findPreference(getString(R.string.settings_items_change_user));
        preference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent it = new Intent(getActivity(), LoginActivity.class);
                it.putExtra(getString(R.string.app_intent_extra_settings),
                        getString(R.string.app_intent_extra_settings_no_change_language_button));
                startActivityForResult(it, 1);
                return true;
            }
        });

        // 切换语言按钮监听
        Preference switchLanguage = findPreference(getString(R.string.settings_items_language));
        switchLanguage.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                ChangeLocale.Change(getActivity());
                restart();
                return true;
            }
        });
        switchLanguage.setSummary(getString(R.string.app_locale));

        // 用户名修改校验
        Preference userNameEditText = findPreference(getString(R.string.app_db_staff_sname));
        userNameEditText.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                String newName = (String) newValue;
                if (newName.length() == 0)
                    return false;
                if (newName.length() >= 40) {
                    Toast.makeText(getActivity(),
                            getString(R.string.error_login_name_too_long),
                            Toast.LENGTH_SHORT).show();
                    return false;
                }
                return true;
            }
        });

        // 手机号修改校验
        Preference userPhoneEditText = findPreference(getString(R.string.app_db_staff_sphone));
        userPhoneEditText.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                String newPhone = (String) newValue;
                if (newPhone.length() == 0) {
                    return false;
                }
                if (newPhone.length() != 11) {
                    Toast.makeText(getActivity(),
                            getString(R.string.error_login_invalid_phone_number),
                            Toast.LENGTH_SHORT).show();
                    return false;
                }
                return true;
            }
        });
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.app_db_push_enabled))) {
            return;
        }
        if (key.equals(getString(R.string.app_db_staff_sname))) {

            String sid = sharedPreferences.getString(getString(R.string.app_db_staff_sid),
                    null);
            String sname = sharedPreferences.getString(key, null);
            String url = HttpUtil.buildURL(
                    getString(R.string.app_network_server_ip),
                    getString(R.string.app_network_change_name_page),
                    getString(R.string.app_db_staff_sid),
                    sid,
                    getString(R.string.app_db_staff_sname),
                    sname);
            new ChangeNameTask(this, staffNameBackup)
                    .execute(url);
        } else if (key.equals(getString(R.string.app_db_staff_sphone))) {
            String uid = sharedPreferences.getString(getString(R.string.app_db_staff_sid),
                    null);
            String uphone = sharedPreferences.getString(key, null);
            String url = HttpUtil.buildURL(
                    getString(R.string.app_network_server_ip),
                    getString(R.string.app_network_change_phone_page),
                    getString(R.string.app_db_staff_sid),
                    uid,
                    getString(R.string.app_db_staff_sphone),
                    uphone);
            new ChangePhoneTask(this, staffPhoneBackup)
                    .execute(url);
        }
        Preference preference = findPreference(key);
        preference.setSummary(sharedPreferences.getString(key, "default"));
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        initSummery(getPreferenceScreen());
    }

    private void initSummery(Preference p) {
        if (p instanceof PreferenceGroup) {
            PreferenceGroup group = (PreferenceGroup) p;
            for (int i = 0; i < group.getPreferenceCount(); i++) {
                initSummery(group.getPreference(i));
            }
        } else {
            String key = p.getKey();
            if (key != null) {
                if (!key.equals(getString(R.string.app_db_push_enabled))) {
                    String summery = sharedPreferences.getString(key, null);
                    if (summery != null)
                        p.setSummary(summery);
                }

            }
        }
    }

    private void restart() {
        getActivity().finish();
        Intent it = new Intent(getActivity(), TicketActivity.class);
        it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(it);
    }


    public void reloadStaffInfo() {
        staffNameBackup =
                sharedPreferences.getString(getString(R.string.app_db_staff_sname), null);
        staffPhoneBackup =
                sharedPreferences.getString(getString(R.string.app_db_staff_sphone), null);
    }

    public SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }
}
