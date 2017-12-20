package cc.pchospital.toolbox;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

import cc.pchospital.toolbox.util.ChangeLocale;

public class TicketActivity extends AppCompatActivity {

    public static final int TYPE_LOGIN = 1;
    public static final int TYPE_SETTINGS = 2;
    private static final int TYPE_ADD_TICKET = 3;

    private DrawerLayout mDrawerLayout;
    private TextView sName;
    private TextView sTitle;
    SharedPreferences userProfile;
    MainFragment mainFragment;

    private static final String TAG = "TicketActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket);

        // Preference
        PreferenceManager.setDefaultValues(this, R.xml.preference, false);

        // UI-Tool Bar
        Toolbar toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.icon_menu);
            actionBar.setTitle(R.string.title_ticket_all_ticket);
        }

        // UI-NavigationView
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                mDrawerLayout.closeDrawers();
                return true;
            }
        });
        navigationView.setCheckedItem(R.id.nav_main);
        // UI-Name TextView and Phone TextView
        sName = navigationView.getHeaderView(0).findViewById(R.id.nav_staff_name);
        sTitle = navigationView.getHeaderView(0).findViewById(R.id.nav_staff_title);
        // UI-Setting Button
        ImageButton settings = navigationView.getHeaderView(0).findViewById(R.id.nav_settings);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.closeDrawers();
                Intent intent = new Intent(TicketActivity.this, SettingsActivity.class);
                startActivityForResult(intent, TYPE_SETTINGS);
            }
        });
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.nav_main:
                        mDrawerLayout.closeDrawers();
                        if (actionBar != null) {
                            actionBar.setTitle(R.string.title_ticket_all_ticket);
                        }
                        replaceFragment(new MainFragment());
                        break;
                    case R.id.nav_about:
                        mDrawerLayout.closeDrawers();
                        if (actionBar != null) {
                            actionBar.setTitle(R.string.title_about);
                        }
                        replaceFragment(new AboutFragment());
                        break;
                    case R.id.nav_help:
                        mDrawerLayout.closeDrawers();
                        if (actionBar != null) {
                            actionBar.setTitle(R.string.title_help_center);
                        }
                        replaceFragment(new TipsFragment());
                        break;
                    default:
                        break;
                }
                return true;
            }
        });

        // UI-ChangeLanguage
        Switch language = findViewById(R.id.nav_language);
        Resources resources = getResources();
        Configuration config = resources.getConfiguration();
        if (config.locale.equals(Locale.SIMPLIFIED_CHINESE)) {
            language.setChecked(true);
        } else {
            language.setChecked(false);
        }
        language.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ChangeLocale.Change(TicketActivity.this);
                restart();
            }
        });

        // 自动登录，如果没有自动登录信息则跳转到登录界面
        userProfile =
                PreferenceManager.getDefaultSharedPreferences(this);
        String userid = userProfile.getString(getString(R.string.app_db_staff_sid), null);
        if (userid == null) {
            Intent intent = new Intent(TicketActivity.this, LoginActivity.class);
            Toast.makeText(this, getString(R.string.toast_ticket_need_login),
                    Toast.LENGTH_SHORT).show();
            startActivityForResult(intent, TYPE_LOGIN);
        } else {
            // UI-NavigationHead
            updateUserInfo();
            // 启动主 Fragment
            mainFragment = new MainFragment();
            replaceFragment(mainFragment);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            default:
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TYPE_LOGIN:
                if (resultCode == RESULT_OK) {
                    SharedPreferences staffProfile =
                            PreferenceManager.getDefaultSharedPreferences(this);
                    String staffID = staffProfile.getString(getString(R.string.app_db_staff_sid),
                            null);
                    if (staffID == null) {
                        Log.e(TAG, "onActivityResult: Unexpected Error!");
                        finish();
                    }
                    String staffName = staffProfile.getString(getString(R.string.app_db_staff_sname),
                            null);
                    String staffTitle = staffProfile.getString(getString(R.string.app_db_staff_stitle),
                            null);
                    // UI-NavigationHead
                    sName.setText(staffName);
                    sTitle.setText(staffTitle);
                    // UI-StartFragment
                    mainFragment = new MainFragment();
                    replaceFragment(mainFragment);
                } else if (resultCode == RESULT_CANCELED) {
                    String resultdata = data.getStringExtra
                            (getString(R.string.app_intent_extra_login));
                    if (resultdata == null) {
                        finish();
                    } else {
                        if (resultdata.equals
                                (getString(R.string.app_intent_extra_login_language))) {
                            ChangeLocale.Change(TicketActivity.this);
                            restart();
                        } else {
                            finish();
                        }
                    }
                }
                break;
            case TYPE_SETTINGS:
                updateUserInfo();
                break;
            case TYPE_ADD_TICKET:
                mainFragment.refreshCards();
                break;
            default:
        }
    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.dynamic_content, fragment);
        transaction.commit();
    }

    private void restart() {
        finish();
        Intent it = new Intent(TicketActivity.this, TicketActivity.class);
        it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(it);
    }

    private void updateUserInfo() {
        sName.setText(userProfile.getString(getString(R.string.app_db_staff_sname),
                null));
        sTitle.setText(userProfile.getString(getString(R.string.app_db_staff_stitle),
                null));
    }
}
