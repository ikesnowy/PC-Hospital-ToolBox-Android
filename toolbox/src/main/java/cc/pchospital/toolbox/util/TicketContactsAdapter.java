package cc.pchospital.toolbox.util;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cc.pchospital.toolbox.R;

public class TicketContactsAdapter extends RecyclerView.Adapter<TicketContactsAdapter.ViewHolder> {

    private Ticket ticket;
    private Context context;

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView itemLabel;
        TextView itemValue;

        public ViewHolder (View view) {
            super(view);
            itemLabel = view.findViewById(R.id.item_label);
            itemValue = view.findViewById(R.id.item_value);
        }
    }

    public TicketContactsAdapter(Context context, Ticket ticket) {
        this.ticket = ticket;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ticket_detail_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        switch (position) {
            case 0:
                holder.itemLabel.setText(context.getString(R.string.label_ticket_detail_name));
                holder.itemValue.setText(ticket.getTicketName());
                break;
            case 1:
                holder.itemLabel.setText(context.getString(R.string.label_ticket_detail_phone_number));
                holder.itemValue.setText(ticket.getTicketPhone());
                break;
            case 2:
                holder.itemLabel.setText(context.getString(R.string.label_ticket_detail_location));
                holder.itemValue.setText(ticket.getTicketLocation());
                break;
            default:
                break;
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
