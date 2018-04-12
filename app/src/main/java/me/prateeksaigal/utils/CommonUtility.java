package me.prateeksaigal.utils;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

/**
 * Created by rajnikant on 09/04/18.
 */

public class CommonUtility {
    //Check object is null
    public static boolean isNull(Object obj) {
        return obj == null;
    }
    public static boolean isNotNull(Object object) {
        return object != null;
    }
    /**
     * Show toast messageChat for long time
     *
     * @param context
     * @param msg
     */
    public static void showToastLong(Context context, String msg) {
        if (!TextUtils.isEmpty(msg)&&isNotNull(context)) {
            Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
        }
    }
}
