package cc.pchospital.toolbox;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import cc.pchospital.toolbox.db.LoginTask;
import cc.pchospital.toolbox.gson.Staff;
import cc.pchospital.toolbox.gson.User;
import cc.pchospital.toolbox.util.HttpUtil;

public class LoginActivity extends AppCompatActivity {
    public Staff user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // ToolBar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.icon_arrow_back_black);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        user = new Staff();
        Button language = findViewById(R.id.language);
        language.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent();
                it.putExtra(getString(R.string.app_intent_extra_login),
                        getString(R.string.app_intent_extra_login_language));
                setResult(RESULT_CANCELED, it);
                finish();
            }
        });
        // 根据调用方决定是否显示语言按钮
        Intent intent = getIntent();
        String extra = intent.getStringExtra(getString(R.string.app_intent_extra_settings));
        if (extra != null) {
            language.setVisibility(View.GONE);
        }

            final Button login = findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 输入校验
                TextInputEditText inputname = findViewById(R.id.input_name);
                String sname = inputname.getText().toString();
                TextInputEditText inputphone = findViewById(R.id.input_phone);
                String sphone = inputphone.getText().toString();
                if (sname.length() > 40){
                    inputname.setError(getString(R.string.error_login_name_too_long));
                    return;
                } else if (sname.length() == 0) {
                    inputname.setError(getString(R.string.error_login_no_name));
                    return;
                } else if (sphone.length() != 11) {
                    inputphone.setError(getString(R.string.error_login_invalid_phone_number));
                    return;
                }
                // 登录或注册
                login.setEnabled(false);
                String url = HttpUtil.buildURL(
                        getString(R.string.app_network_server_ip),
                        getString(R.string.app_network_login_page),
                        getString(R.string.app_db_staff_sname),
                        sname,
                        getString(R.string.app_db_staff_sphone),
                        sphone);
                new LoginTask(LoginActivity.this).execute(url);
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
    public void onBackPressed() {
        Intent it = new Intent();
        it.putExtra(getString(R.string.app_intent_extra_login),
                getString(R.string.app_intent_extra_login_back));
        setResult(RESULT_CANCELED, it);
        finish();
    }
}
