package cc.pchospital.toolbox.db;

import android.os.AsyncTask;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.ref.WeakReference;
import java.util.List;

import cc.pchospital.toolbox.R;
import cc.pchospital.toolbox.TicketDetailActivity;
import cc.pchospital.toolbox.util.HttpUtil;
import cc.pchospital.toolbox.util.Ticket;
import okhttp3.Response;

public class PullTicketTask extends AsyncTask<String, String, Boolean> {

    private WeakReference<TicketDetailActivity> context;
    private List<Ticket> tickets;

    public PullTicketTask(TicketDetailActivity ticketDetailActivity) {
        context = new WeakReference<>(ticketDetailActivity);
    }

    @Override
    protected Boolean doInBackground(String... strings) {
        try {
            Response response = HttpUtil.sendGetOkHttpRequest(strings[0]);
            String data = response.body().string();
            Gson gson = new Gson();
            tickets = gson.fromJson(data,
                    new TypeToken<List<Ticket>>(){}.getType());
            if (tickets.size() == 0) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }

        return true;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        TicketDetailActivity activity = context.get();
        if (aBoolean) {
            activity.ticket = tickets.get(0);
            activity.syncUIAfterRefresh();
        } else {
            Toast.makeText(
                    activity,
                    activity.getString(R.string.toast_ticket_detail_refresh_failed),
                    Toast.LENGTH_SHORT)
                    .show();
        }
        activity.swipeRefreshLayout.setRefreshing(false);
    }
}
