package cc.pchospital.toolbox.db;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.ref.WeakReference;
import java.util.List;

import cc.pchospital.toolbox.LoginActivity;
import cc.pchospital.toolbox.R;
import cc.pchospital.toolbox.gson.Staff;
import cc.pchospital.toolbox.gson.User;
import cc.pchospital.toolbox.util.HttpUtil;
import okhttp3.Response;

import static android.app.Activity.RESULT_OK;

public class LoginTask extends AsyncTask<String, String, Integer> {

    private WeakReference<LoginActivity> activity;
    private static final int LOGIN_SUCCESSFUL = 0;
    private static final int NO_INTERNET = 1;
    private static final int NO_SUCH_USER = 2;

    public LoginTask(LoginActivity context) {
        activity = new WeakReference<>(context);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Button login = activity.get().findViewById(R.id.login);
        login.setText(activity.get().getString(R.string.states_login_signing_in));
    }

    @Override
    protected Integer doInBackground(String... strings) {
        try {
            Response response = HttpUtil.sendGetOkHttpRequest(strings[0]);
            String responseData = response.body().string();
            Gson gson = new Gson();
            List<Staff> staffList = gson.fromJson(responseData,
                    new TypeToken<List<Staff>>(){}.getType());
            if (staffList.size() == 0) {
                return NO_SUCH_USER;
            }
            activity.get().user = staffList.get(0);
        } catch (Exception e) {
            return NO_INTERNET;
        }
        return LOGIN_SUCCESSFUL;
    }

    @Override
    protected void onPostExecute(Integer resultcode) {
        LoginActivity loginActivity = activity.get();
        if (resultcode == LOGIN_SUCCESSFUL) {
            Staff staff = loginActivity.user;
            SharedPreferences.Editor editor =
                    PreferenceManager.getDefaultSharedPreferences(loginActivity).edit();
            editor.putString(loginActivity.getString(R.string.app_db_staff_sid), staff.getSid() + "");
            editor.putString(loginActivity.getString(R.string.app_db_staff_sname), staff.getSname());
            editor.putString(loginActivity.getString(R.string.app_db_staff_sphone), staff.getSphone());
            editor.putString(loginActivity.getString(R.string.app_db_staff_snumber), staff.getSnumber());
            editor.putString(loginActivity.getString(R.string.app_db_staff_stitle), staff.getStitle());
            editor.apply();
            loginActivity.setResult(RESULT_OK);
            loginActivity.finish();
        } else if (resultcode == NO_INTERNET) {
            Toast.makeText(loginActivity,
                    loginActivity.getString(R.string.toast_login_failed), Toast.LENGTH_SHORT).show();
        } else if (resultcode == NO_SUCH_USER) {
            Toast.makeText(loginActivity,
                    loginActivity.getString(R.string.toast_login_no_such_user), Toast.LENGTH_SHORT).show();
        }
        Button login = loginActivity.findViewById(R.id.login);
        login.setText(loginActivity.getString((R.string.button_login_login)));
        login.setEnabled(true);
    }
}