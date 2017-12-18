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
import cc.pchospital.toolbox.gson.User;
import cc.pchospital.toolbox.util.HttpUtil;
import okhttp3.Response;

import static android.app.Activity.RESULT_OK;

public class LoginTask extends AsyncTask<String, String, Boolean> {

    private WeakReference<LoginActivity> activity;

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
    protected Boolean doInBackground(String... strings) {
        try {
            Response response = HttpUtil.sendGetOkHttpRequest(strings[0]);
            String responseData = response.body().string();
            Gson gson = new Gson();
            List<User> users = gson.fromJson(responseData,
                    new TypeToken<List<User>>(){}.getType());
            activity.get().user = users.get(0);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        LoginActivity loginActivity = activity.get();
        if (aBoolean) {
            User user = loginActivity.user;
            SharedPreferences.Editor editor =
                    PreferenceManager.getDefaultSharedPreferences(loginActivity).edit();
            editor.putString(loginActivity.getString(R.string.app_db_user_uid), user.getUid() + "");
            editor.putString(loginActivity.getString(R.string.app_db_user_uname), user.getUname());
            editor.putString(loginActivity.getString(R.string.app_db_user_uphone), user.getUphone());
            editor.apply();
            loginActivity.setResult(RESULT_OK);
            loginActivity.finish();
        } else {
            Toast.makeText(loginActivity,
                    loginActivity.getString(R.string.toast_login_failed), Toast.LENGTH_SHORT).show();
        }
        Button login = loginActivity.findViewById(R.id.login);
        login.setText(loginActivity.getString((R.string.button_login_login_or_register)));
        login.setEnabled(true);
    }
}