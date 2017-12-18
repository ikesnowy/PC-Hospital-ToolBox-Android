package cc.pchospital.toolbox.util;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HttpUtil {

    public static Response sendGetOkHttpRequest(String address) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .get()
                .url(address)
                .build();
        return client.newCall(request).execute();
    }

    public static Response sendPostOkHttpRequest(String address, RequestBody body) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .post(body)
                .url(address)
                .build();
        return client.newCall(request).execute();
    }

    public static String buildURL(String serverURL, String targetPage, String... params) {
        StringBuilder url = new StringBuilder();
        url.append("http://");
        url.append(serverURL);
        url.append("/");
        url.append(targetPage);
        if (params.length != 0) {
            url.append("?");
            for (int i = 0; i < params.length; i += 2) {
                url.append(params[i]);
                url.append("=");
                url.append(params[i + 1]);
                url.append("&");
            }
            url.deleteCharAt(url.length() - 1);
        }
        return url.toString();
    }
}
