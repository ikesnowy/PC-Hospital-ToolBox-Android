package cc.pchospital.toolbox.db;

import android.os.AsyncTask;
import android.widget.Toast;

import java.lang.ref.WeakReference;

import cc.pchospital.toolbox.R;
import cc.pchospital.toolbox.TicketDetailActivity;
import cc.pchospital.toolbox.util.HttpUtil;
import okhttp3.Response;

public class AcceptTicketTask extends AsyncTask<String, String, Boolean> {

    private WeakReference<TicketDetailActivity> context;

    public AcceptTicketTask(TicketDetailActivity activity) {
        context = new WeakReference<>(activity);
    }

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected Boolean doInBackground(String... strings) {
        try {
            Response response = HttpUtil.sendGetOkHttpRequest(strings[0]);
            String data = response.body().string();
            if (data.trim().length() != 0) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        if (aBoolean) {
               context.get().refreshTicket();
        } else {
            Toast.makeText(context.get(),
                    context.get().getText(R.string.toast_ticket_activity_accept_ticket_failed),
                    Toast.LENGTH_SHORT)
                    .show();
            context.get().acceptTicket.setEnabled(true);
        }
    }
}
