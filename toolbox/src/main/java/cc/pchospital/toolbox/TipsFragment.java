package cc.pchospital.toolbox;

import android.os.Bundle;
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

import cc.pchospital.toolbox.db.PullTipsTask;
import cc.pchospital.toolbox.gson.Tip;
import cc.pchospital.toolbox.util.HttpUtil;
import cc.pchospital.toolbox.util.TipAdapter;


public class TipsFragment extends Fragment {

    public SwipeRefreshLayout refreshLayout;
    SwipeRefreshLayout.OnRefreshListener refreshListener;
    public RecyclerView tipList;
    public List<Tip> tips;
    public TipAdapter adapter;

    public TipsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tips = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tips, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tipList = view.findViewById(R.id.tip_list);
        adapter = new TipAdapter(tips);
        tipList.setAdapter(adapter);

        GridLayoutManager manager = new GridLayoutManager(getContext(), 1);
        tipList.setLayoutManager(manager);

        refreshLayout = view.findViewById(R.id.tip_refresh);
        refreshLayout.setColorSchemeResources(R.color.colorPrimary);
        refreshListener = new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                String url = HttpUtil.buildURL(
                        getString(R.string.app_network_server_ip),
                        getString(R.string.app_network_query_all_tips_page)
                );
                new PullTipsTask(TipsFragment.this).execute(url);
            }
        };

        refreshLayout.setOnRefreshListener(refreshListener);
        refreshTips();
    }


    @Override
    public void onResume() {
        super.onResume();
        refreshTips();
    }

    private void refreshTips() {
        refreshLayout.setRefreshing(true);
        refreshListener.onRefresh();
    }
}
