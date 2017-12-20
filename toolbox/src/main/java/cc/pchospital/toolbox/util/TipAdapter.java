package cc.pchospital.toolbox.util;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import cc.pchospital.toolbox.R;
import cc.pchospital.toolbox.WebActivity;
import cc.pchospital.toolbox.gson.Tip;

public class TipAdapter extends RecyclerView.Adapter<TipAdapter.ViewHolder> {

    private Context context;
    private List<Tip> tips;

    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView title;

        ViewHolder (View view) {
            super(view);
            title = view.findViewById(R.id.page_title);
            cardView = view.findViewById(R.id.page_card);
        }
    }

    public TipAdapter(List<Tip> tipList) {
        tips = tipList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (context == null) {
            context = parent.getContext();
        }
        View view = LayoutInflater.from(context)
                .inflate(R.layout.tips_card_item, parent, false);

        final ViewHolder holder = new ViewHolder(view);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                WebActivity.actionStart(context, tips.get(position));
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Tip tip = tips.get(position);
        holder.title.setText(tip.getTitle());
    }

    @Override
    public int getItemCount() {
        return tips.size();
    }
}
