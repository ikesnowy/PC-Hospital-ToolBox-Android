package cc.pchospital.toolbox;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import cc.pchospital.toolbox.db.PullCardsTask;
import cc.pchospital.toolbox.util.Card;
import cc.pchospital.toolbox.util.CardAdapter;
import cc.pchospital.toolbox.util.HttpUtil;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {

    public List<Card> cards;
    public CardAdapter cardAdapter;
    public RecyclerView recyclerView;
    private String userId;
    private SwipeRefreshLayout swipeRefreshLayout;
    private SwipeRefreshLayout.OnRefreshListener refreshListener;

    private static final int TYPE_ADD_TICKET = 3;

    public MainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        cards = new ArrayList<>();

        recyclerView = view.findViewById(R.id.mainCard);
        cardAdapter = new CardAdapter(cards);
        recyclerView.setAdapter(cardAdapter);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 1);
        recyclerView.setLayoutManager(gridLayoutManager);

        SharedPreferences userProfile = PreferenceManager.getDefaultSharedPreferences(getContext());
        userId = userProfile.getString(getString(R.string.app_db_staff_sid), "");
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        refreshListener =
                new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                String url =
                        HttpUtil.buildURL(
                                getString(R.string.app_network_server_ip),
                                getString(R.string.app_network_query_cards),
                                getString(R.string.app_db_staff_sid),
                                userId);
                new PullCardsTask(MainFragment.this).execute(url);
            }
        };
        swipeRefreshLayout.setOnRefreshListener(refreshListener);

        refreshCards();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshCards();
    }

    public void refreshCards() {
        swipeRefreshLayout.setRefreshing(true);
        refreshListener.onRefresh();
    }
}
