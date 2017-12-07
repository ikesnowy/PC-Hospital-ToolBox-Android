package cc.pchospital.app.db;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.ref.WeakReference;
import java.util.List;

import cc.pchospital.app.R;
import cc.pchospital.app.SettingsFragment;
import cc.pchospital.app.gson.User;
import cc.pchospital.app.util.HttpUtil;
import okhttp3.Response;


public class ChangeNameTask extends AsyncTask<String, String, Boolean> {
    private WeakReference<SettingsFragment> settings;
    private String oldName;

    public ChangeNameTask(SettingsFragment fragment, String old) {
        settings = new WeakReference<>(fragment);
        this.oldName = old;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Boolean doInBackground(String... strings) {
        try {
            Response response = HttpUtil.sendGetOkHttpRequest(strings[0]);
            String responseData = response.body().string();
            Gson gson = new Gson();
            List<User> users = gson.fromJson(responseData,
                    new TypeToken<List<User>>(){}.getType());
            if (users.size() == 0) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        String result;
        if (aBoolean) {
            result = settings.get().getString(R.string.toast_settings_user_info_update_successfully);
            // 操作确认
            settings.get().reloadUserInfo();
        } else {
            result = settings.get().getString(R.string.toast_settings_user_info_update_failed);
            // 回滚操作
            SharedPreferences.Editor editor =
                    settings.get().getSharedPreferences().edit();
            editor.putString(settings.get().getString(R.string.app_db_user_uname), oldName);
            editor.apply();
        }
        Toast.makeText(settings.get().getActivity(),
                result,
                Toast.LENGTH_SHORT)
                .show();
    }
}
