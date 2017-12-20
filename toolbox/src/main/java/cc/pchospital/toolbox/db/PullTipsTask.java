package cc.pchospital.toolbox.db;

import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.ref.WeakReference;
import java.util.List;

import cc.pchospital.toolbox.TipsFragment;
import cc.pchospital.toolbox.gson.Tip;
import cc.pchospital.toolbox.util.HttpUtil;
import okhttp3.Response;


public class PullTipsTask extends AsyncTask<String, String, Boolean> {
    WeakReference<TipsFragment> context;
    private List<Tip> tips;

    public PullTipsTask(TipsFragment tipsFragment) {
        context = new WeakReference<>(tipsFragment);
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
            tips = gson.fromJson(responseData, new TypeToken<List<Tip>>(){}.getType());
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        SwipeRefreshLayout swipe = context.get().refreshLayout;
        if (aBoolean) {
            context.get().tips.clear();
            context.get().tips.addAll(tips);
            context.get().adapter.notifyDataSetChanged();
            context.get().tipList.setAdapter(context.get().adapter);
        }
        swipe.setRefreshing(false);
    }
}
