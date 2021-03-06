package cc.pchospital.toolbox.db;

import android.os.AsyncTask;
import android.widget.Toast;

import java.lang.ref.WeakReference;

import cc.pchospital.toolbox.R;
import cc.pchospital.toolbox.TicketDetailActivity;
import cc.pchospital.toolbox.util.HttpUtil;
import okhttp3.Response;

public class ChangeTicketStateTask extends AsyncTask<String, String, Boolean> {
    private WeakReference<TicketDetailActivity> context;

    public ChangeTicketStateTask(TicketDetailActivity activity){
        context = new WeakReference<>(activity);
    }

    @Override
    protected Boolean doInBackground(String... strings) {

        try {
            Response response = HttpUtil.sendGetOkHttpRequest(strings[0]);
            String receivedData = response.body().string();
            if (receivedData.trim().length() != 0){
                return false;
            }
        } catch (Exception e) {
            return false;
        }

        return true;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        if (!aBoolean) {
            Toast.makeText(context.get(),
                    context.get().getString(R.string.toast_ticket_detail_update_state_failed),
                    Toast.LENGTH_SHORT).show();
        }
        context.get().refreshTicket();
    }
}
