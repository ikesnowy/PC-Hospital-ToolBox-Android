package cc.pchospital.toolbox.util;

import android.content.Context;

import java.util.HashMap;

import cc.pchospital.toolbox.R;

public class DictionaryUtil {

    public static HashMap<String, String> initLabelHashMaps(Context context) {

        HashMap<String, String> labelDictionary = new HashMap<>();

        // label
        labelDictionary.put(
                context.getString(R.string.app_ticket_states_uploading),
                context.getString(R.string.label_ticket_uploading)
        );
        labelDictionary.put(
                context.getString(R.string.app_ticket_states_uploaded),
                context.getString(R.string.label_ticket_uploaded)
        );
        labelDictionary.put(
                context.getString(R.string.app_ticket_states_unread),
                context.getString(R.string.label_ticket_unread)
        );
        labelDictionary.put(
                context.getString(R.string.app_ticket_states_noticed),
                context.getString(R.string.label_ticket_noticed)
        );
        labelDictionary.put(
                context.getString(R.string.app_ticket_states_accepted),
                context.getString(R.string.label_ticket_accepted)
        );
        labelDictionary.put(
                context.getString(R.string.app_ticket_states_complete),
                context.getString(R.string.label_ticket_complete)
        );
        labelDictionary.put(
                context.getString(R.string.app_ticket_states_canceled),
                context.getString(R.string.label_ticket_canceled)
        );
        labelDictionary.put(
                context.getString(R.string.app_ticket_states_failed),
                context.getString(R.string.label_ticket_upload_failed)
        );

        return labelDictionary;


    }

    public static HashMap<String, Integer> initColorHashMap(Context context) {
        HashMap<String, Integer> colorDictionary = new HashMap<>();
        // color
        colorDictionary.put(
                context.getString(R.string.app_ticket_states_uploading),
                R.color.textDefault
        );
        colorDictionary.put(
                context.getString(R.string.app_ticket_states_uploaded),
                R.color.textDefault
        );
        colorDictionary.put(
                context.getString(R.string.app_ticket_states_unread),
                R.color.States_unread
        );
        colorDictionary.put(
                context.getString(R.string.app_ticket_states_noticed),
                R.color.States_noticed
        );
        colorDictionary.put(
                context.getString(R.string.app_ticket_states_accepted),
                R.color.States_accepted
        );
        colorDictionary.put(
                context.getString(R.string.app_ticket_states_complete),
                R.color.States_complete
        );
        colorDictionary.put(
                context.getString(R.string.app_ticket_states_canceled),
                R.color.States_canceled
        );
        colorDictionary.put(
                context.getString(R.string.app_ticket_states_failed),
                R.color.States_failed
        );
        return colorDictionary;
    }
}
