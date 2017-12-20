package cc.pchospital.toolbox.db;

import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.ref.WeakReference;
import java.util.List;

import cc.pchospital.toolbox.MainFragment;
import cc.pchospital.toolbox.R;
import cc.pchospital.toolbox.util.Card;
import cc.pchospital.toolbox.util.HttpUtil;
import okhttp3.Response;

public class PullCardsTask extends AsyncTask<String, String, Boolean> {
    private WeakReference<MainFragment> context;
    private List<Card> receivedCards;

    public PullCardsTask(MainFragment main){
        context = new WeakReference<>(main);
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
            receivedCards = gson.fromJson(responseData,
                    new TypeToken<List<Card>>(){}.getType());
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        SwipeRefreshLayout swipe = context.get().getActivity().findViewById(R.id.swipe_refresh);
        if (aBoolean){
            context.get().cards.clear();
            RelativeLayout noCards = context.get().getActivity().findViewById(R.id.no_cards);
            if (receivedCards == null) {
                noCards.setVisibility(View.VISIBLE);
            } else {
                if (receivedCards.size() == 0){
                    noCards.setVisibility(View.VISIBLE);
                } else {
                    context.get().cards.addAll(receivedCards);
                    noCards.setVisibility(View.GONE);
                }
                context.get().cardAdapter.notifyDataSetChanged();
                context.get().recyclerView.setAdapter(context.get().cardAdapter);
            }
        }
        swipe.setRefreshing(false);
    }
}
