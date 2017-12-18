package cc.pchospital.toolbox.util;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import java.util.Locale;

/**
 * Created by ikesn on 2017/12/7.
 */

public class ChangeLocale {
    public static void Change(Context context, Locale locale) {
        Resources resources = context.getResources();
        Configuration config = resources.getConfiguration();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        config.locale = locale;
        resources.updateConfiguration(config, metrics);
    }

    public static void Change(Context context) {
        Resources resources = context.getResources();
        Configuration config = resources.getConfiguration();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        if (config.locale.equals(Locale.SIMPLIFIED_CHINESE))
            config.locale = Locale.ENGLISH;
        else
            config.locale = Locale.SIMPLIFIED_CHINESE;
        resources.updateConfiguration(config, metrics);
    }
}
