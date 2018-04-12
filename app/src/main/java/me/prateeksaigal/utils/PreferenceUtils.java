package me.prateeksaigal.utils;

import com.orhanobut.hawk.Hawk;

/**
 * Created by rajnikant on 12/04/18.
 */

public class PreferenceUtils {
    public PreferenceUtils() {
    }

    public static void setAuthToken(String token) {
        Hawk.put(Constans.TOKEN, token);
    }

    public static String getAuthToken() {
        return Hawk.get(Constans.TOKEN);

    }
}
