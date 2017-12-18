package cc.pchospital.toolbox.db;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.ref.WeakReference;
import java.util.List;

import cc.pchospital.toolbox.R;
import cc.pchospital.toolbox.SettingsFragment;
import cc.pchospital.toolbox.gson.Staff;
import cc.pchospital.toolbox.util.HttpUtil;
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
            List<Staff> staffList = gson.fromJson(responseData,
                    new TypeToken<List<Staff>>(){}.getType());
            if (staffList.size() == 0) {
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
            settings.get().reloadStaffInfo();
        } else {
            result = settings.get().getString(R.string.toast_settings_user_info_update_failed);
            // 回滚操作
            SharedPreferences.Editor editor =
                    settings.get().getSharedPreferences().edit();
            editor.putString(settings.get().getString(R.string.app_db_staff_sname), oldName);
            editor.apply();
        }
        Toast.makeText(settings.get().getActivity(),
                result,
                Toast.LENGTH_SHORT)
                .show();
    }
}
