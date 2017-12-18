package cc.pchospital.toolbox.db;

import android.os.AsyncTask;
import android.widget.Toast;

import java.io.IOException;
import java.lang.ref.WeakReference;

import cc.pchospital.toolbox.AddTicketActivity;
import cc.pchospital.toolbox.R;
import cc.pchospital.toolbox.util.HttpUtil;
import cc.pchospital.toolbox.util.Ticket;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PushTicketTask extends AsyncTask<String, String, Boolean> {

    private Ticket newTicket;
    private WeakReference<AddTicketActivity> context;

    public PushTicketTask(Ticket ticket, AddTicketActivity addTicketActivity) {
        newTicket = ticket;
        context = new WeakReference<>(addTicketActivity);
    }

    @Override
    protected void onPreExecute() {
        context.get().submit.setEnabled(false);
        context.get().submit.setText(context.get().getString(R.string.button_add_ticket_pushing_ticket));
    }

    @Override
    protected Boolean doInBackground(String... strings) {
        AddTicketActivity activity = context.get();
        RequestBody body = new FormBody.Builder()
                .add(activity.getString(R.string.app_db_ticket_uid), newTicket.getUserId())
                .add(activity.getString(R.string.app_db_ticket_name), newTicket.getTicketName())
                .add(activity.getString(R.string.app_db_ticket_phone), newTicket.getTicketPhone())
                .add(activity.getString(R.string.app_db_ticket_location), newTicket.getTicketLocation())
                .add(activity.getString(R.string.app_db_ticket_location_la), newTicket.getTicketLocationLa().toString())
                .add(activity.getString(R.string.app_db_ticket_location_lo), newTicket.getTicketLocationLo().toString())
                .add(activity.getString(R.string.app_db_ticket_note), newTicket.getTicketNote())
                .add(activity.getString(R.string.app_db_ticket_date), newTicket.getTicketDate().toString())
                .add(context.get().getString(R.string.app_db_ticket_state), newTicket.getTicketStates())
                .build();

        try {
            Response response = HttpUtil.sendPostOkHttpRequest(strings[0], body);
            String responseData = response.body().string();
            if (responseData == null){
                return false;
            }
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        if (aBoolean) {
            Toast.makeText(context.get(),
                    context.get().getString(R.string.toast_add_ticket_success),
                    Toast.LENGTH_SHORT)
                    .show();
            context.get().finish();
        } else {
            Toast.makeText(context.get(),
                    context.get().getString(R.string.toast_add_ticket_failed),
                    Toast.LENGTH_SHORT)
                    .show();
            context.get().submit.setText(context.get().getString(R.string.button_add_ticket_send));
            context.get().submit.setEnabled(true);
        }


    }
}
