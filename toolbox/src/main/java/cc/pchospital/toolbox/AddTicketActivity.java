package cc.pchospital.toolbox;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;

import java.util.ArrayList;
import java.util.List;

import cc.pchospital.toolbox.db.PushTicketTask;
import cc.pchospital.toolbox.util.HttpUtil;
import cc.pchospital.toolbox.util.Ticket;

public class AddTicketActivity extends AppCompatActivity {

    public AMapLocationClient client;
    public AMapLocationListener listener;
    private Ticket newTicket;
    public  Button submit;
    private Button getLocation;
    private View.OnClickListener getLocationListener;
    private static final String TAG = "AddTicketActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_ticket);

        newTicket = new Ticket();

        // UI-Elements
        Toolbar toolbar = findViewById(R.id.toolBar);
        final TextInputEditText name = findViewById(R.id.name);
        final TextInputEditText phone = findViewById(R.id.phone);
        final TextInputEditText location = findViewById(R.id.location);
        final TextInputEditText notes = findViewById(R.id.note);
        getLocation = findViewById(R.id.get_location);
        submit = findViewById(R.id.send);


        // UI-ToolBar
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(getString(R.string.title_add_ticket));
        }

        // UI-Auto-Fill
        SharedPreferences preferences =
                PreferenceManager.getDefaultSharedPreferences(this);
        name.setText(preferences.getString(getString(R.string.app_db_user_uname), ""));
        phone.setText(preferences.getString(getString(R.string.app_db_user_uphone), ""));

        newTicket.setUserId(preferences.getString(getString(R.string.app_db_user_uid), ""));
        newTicket.setTicketDate(System.currentTimeMillis());

        // UI-LocationButton
        getLocationListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!requestPermissions()) {
                    return;
                }
                getLocation.setText(getString(R.string.button_add_ticket_finding_location));
                getLocation.setEnabled(false);
                client = new AMapLocationClient(getApplicationContext());
                listener = new AMapLocationListener() {
                    @Override
                    public void onLocationChanged(AMapLocation aMapLocation) {
                        if (aMapLocation != null){
                            if (aMapLocation.getErrorCode() == 0){
                                newTicket.setTicketLocationLa(aMapLocation.getLatitude());
                                newTicket.setTicketLocationLo(aMapLocation.getLongitude());
                                String address = aMapLocation.getDistrict() + aMapLocation.getStreet();
                                getLocation.setText(address);
                                getLocation.setEnabled(true);
                            } else {
                                Toast.makeText(AddTicketActivity.this,
                                        getString(R.string.toast_add_ticket_no_gps),
                                        Toast.LENGTH_SHORT)
                                        .show();
                                Log.e(TAG, "onLocationChanged: " + aMapLocation.getErrorInfo());
                                getLocation.setEnabled(true);
                            }
                        }
                    }
                };
                client.setLocationListener(listener);
                AMapLocationClientOption option = new AMapLocationClientOption();
                option.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
                option.setOnceLocation(true);
                option.setOnceLocationLatest(true);
                option.setNeedAddress(true);

                client.setLocationOption(option);
                client.startLocation();
            }
        };
        getLocation.setOnClickListener(getLocationListener);

        // UI-Submit Button
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (name.getText().length() == 0) {
                    name.setError(getString(R.string.error_add_ticket_no_name));
                    return;
                }
                if (phone.getText().length() != 11) {
                    phone.setError(getString(R.string.error_add_ticket_invalid_phone_number));
                    return;
                }
                if (location.getText().length() > 80) {
                    location.setError(getString(R.string.error_add_ticket_location_too_long));
                    return;
                }
                if (location.getText().length() == 0) {
                    location.setError(getString(R.string.error_add_ticket_no_location));
                    return;
                }

                newTicket.setTicketName(name.getText().toString());
                newTicket.setTicketPhone(phone.getText().toString());
                newTicket.setTicketLocation(location.getText().toString());
                newTicket.setTicketNote(notes.getText().toString());
                newTicket.setTicketStates(getString(R.string.app_ticket_states_unread));

                String url =
                        HttpUtil.buildURL(
                                getString(R.string.app_network_server_ip),
                                getString(R.string.app_network_push_ticket_page)
                        );
                new PushTicketTask(newTicket, AddTicketActivity.this).execute(url);
            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            default:
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0) {
                    for (int result : grantResults) {
                        if (result != PackageManager.PERMISSION_GRANTED) {
                            return;
                        }
                    }
                    getLocationListener.onClick(getLocation);
                }
                break;
            default:
        }
    }

    private boolean requestPermissions() {
        List<String> permissionList = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(AddTicketActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(AddTicketActivity.this,
                Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (ContextCompat.checkSelfPermission(AddTicketActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        if (!permissionList.isEmpty()) {
            String[] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(AddTicketActivity.this, permissions, 1);
            return false;
        }
        return true;
    }
}
