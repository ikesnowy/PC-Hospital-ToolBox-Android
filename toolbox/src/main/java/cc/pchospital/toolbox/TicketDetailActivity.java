package cc.pchospital.toolbox;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;

import cc.pchospital.toolbox.db.PullTicketTask;
import cc.pchospital.toolbox.util.DictionaryUtil;
import cc.pchospital.toolbox.util.HttpUtil;
import cc.pchospital.toolbox.util.Ticket;

public class TicketDetailActivity extends AppCompatActivity {

    public Ticket ticket;
    public SwipeRefreshLayout swipeRefreshLayout;
    private SwipeRefreshLayout.OnRefreshListener listener;
    private TextView ticketState;
    private TextView ticketNotes;
    private TextView lastUpdate;
    private TextView ticketName;
    private TextView ticketPhone;
    private Button ticketLocation;

    public static void actionStart(Context context, String ticketId) {
        Intent intent = new Intent(context, TicketDetailActivity.class);
        intent.putExtra("ticketId", ticketId);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_detail);

        Intent intent = getIntent();
        final String ticketId = intent.getStringExtra("ticketId");

        Toolbar toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(getString(R.string.title_ticket_detail));
        }

        swipeRefreshLayout = findViewById(R.id.detail_swipe_refresh);
        listener = new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                String url = HttpUtil.buildURL(
                        getString(R.string.app_network_server_ip),
                        getString(R.string.app_network_pull_ticket_page),
                        getString(R.string.app_db_ticket_tid),
                        ticketId
                );
                new PullTicketTask(TicketDetailActivity.this).execute(url);
            }
        };
        swipeRefreshLayout.setOnRefreshListener(listener);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);

        ticketState = findViewById(R.id.detail_state);
        ticketNotes = findViewById(R.id.detail_notes);
        lastUpdate = findViewById(R.id.detail_latest_update);
        ticketName = findViewById(R.id.detail_name);
        ticketName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                copyToClipboard(ticketName.getText().toString());
            }
        });
        ticketPhone = findViewById(R.id.detail_phone_number);
        ticketPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                copyToClipboard(ticketPhone.getText().toString());
            }
        });
        ticketLocation = findViewById(R.id.detail_location);
    }

    @Override
    protected void onStart() {
        super.onStart();
        refreshTicket();
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshTicket();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return true;
    }

    private void refreshTicket() {
        swipeRefreshLayout.setRefreshing(true);
        listener.onRefresh();
    }

    private void copyToClipboard(String text) {
        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        ClipData data= ClipData.newPlainText("text", text);
        if (clipboardManager != null) {
            clipboardManager.setPrimaryClip(data);
            Toast.makeText(this,
                    getString(R.string.toast_ticket_detail_copy_to_clipboard_successfully),
                    Toast.LENGTH_SHORT)
                    .show();
        }
    }

    public void syncUIAfterRefresh() {
        HashMap<String, String> labelDictionary = DictionaryUtil.initLabelHashMaps(this);
        HashMap<String, Integer> colorDictionary = DictionaryUtil.initColorHashMap(this);

        // StatesCard
        ticketState.setText(labelDictionary.get(ticket.getTicketStates()));
        ticketState.setTextColor(colorDictionary.get(ticket.getTicketStates()));
        ticketNotes.setText(ticket.getTicketNote());
        DateFormat simpleDateFormat = SimpleDateFormat.getDateInstance();
        String time = simpleDateFormat.format(ticket.getTicketDate());
        lastUpdate.setText(time);

        // ContactsCard
        ticketName.setText(ticket.getTicketName());
        ticketPhone.setText(ticket.getTicketPhone());
        ticketLocation.setText(ticket.getTicketLocation());
        if (ticket.getTicketLocationLa() != 0.0) {
            ticketLocation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NavigationToActivity.actionStart(TicketDetailActivity.this,
                            ticket.getTicketLocationLa(), ticket.getTicketLocationLo());
                }
            });
        }

        // NoteCard
        ticketNotes.setText(ticket.getTicketNote());
    }
}
