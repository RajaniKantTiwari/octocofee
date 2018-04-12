package me.prateeksaigal.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;

import me.prateeksaigal.ocotocoffee.R;


/**
 * Created by arvind on 06/11/17.
 */

public class ExplicitIntent {
    private static ExplicitIntent sInstance;

    public static ExplicitIntent getsInstance(){
        if(sInstance == null)
            sInstance = new ExplicitIntent();
        return sInstance;
    }

    private ExplicitIntent(){

    }
    public void navigateTo(Activity activity, Class<?> aClass){
        Intent intent = new Intent(activity,aClass);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    public void navigateFromLeftToRight(Activity activity, Class<?> aClass){
        Intent intent = new Intent(activity,aClass);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.animation_enter,
                R.anim.animation_leave);
    }

    public void clearPreviousNavigateTo(Activity activity, Class<?> aClass){
        Intent intent = new Intent(activity,aClass);
        // set the new task and clear flags
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    public void navigateToZoom(Activity activity, Class<?> aClass, Bundle bundle){
        Intent intent = new Intent(activity,aClass);
        intent.putExtras(bundle);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.zoomin, R.anim.fadein);
    }



    public void navigateTo(Activity activity, Class<?> aClass, Bundle bundle){
        Intent  intent = new Intent(activity,aClass);
        intent.putExtras(bundle);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
    }
    public void navigateTo(Activity activity, Class<?> aClass, int flag){
        Intent  intent = new Intent(activity,aClass);
        intent.setFlags(flag);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
    }

    public void navigateTo(Activity activity, Class<?> aClass, Bundle bundle, int flag){
        Intent  intent = new Intent(activity,aClass);
        intent.setFlags(flag);
        intent.putExtras(bundle);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
    }
    public void navigateForResult(Activity activity, Class<?> aClass, int requestCode){
        Intent intent = new Intent(activity, aClass);
        activity.startActivityForResult(intent,requestCode);
        activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    public void navigateForResult(Activity activity, Class<?> aClass, int requestCode , int flag){
        Intent intent = new Intent(activity, aClass);
        intent.setFlags(flag);
        activity.startActivityForResult(intent,requestCode);
        activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    public void navigateForResult(Activity activity, Class<?> aClass, int requestCode, Bundle bundle){
        Intent intent = new Intent(activity, aClass);
        intent.putExtras(bundle);
        activity.startActivityForResult(intent,requestCode);
        activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    public void navigateForResult(Activity activity, Fragment fragment, Class<?> aClass, int requestCode) {
        Intent intent = new Intent(activity, aClass);
        fragment.startActivityForResult(intent, requestCode);
        activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    public void navigateForResult(Activity activity, Fragment fragment, Class<?> aClass, int requestCode, Bundle bundle) {
        Intent intent = new Intent(activity, aClass);
        if (bundle != null)
            intent.putExtras(bundle);
        fragment.startActivityForResult(intent, requestCode);
        activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    public void startService(Activity activity,Class<?> aClass, Bundle bundle){
        Intent intent = new Intent(activity,aClass);
        intent.putExtras(bundle);
        activity.startService(intent);
    }
    public void launchSetting(Context context){
        Intent i = new Intent();
        i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        i.addCategory(Intent.CATEGORY_DEFAULT);
        i.setData(Uri.parse("package:" + context.getPackageName()));
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        context.startActivity(i);
    }
}
