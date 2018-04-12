package me.prateeksaigal;

import android.support.multidex.MultiDexApplication;

import com.orhanobut.hawk.Hawk;

import me.prateeksaigal.injector.component.ApplicationComponent;
import me.prateeksaigal.injector.component.DaggerApplicationComponent;
import me.prateeksaigal.injector.module.ApplicationModule;


public class OctoCofeeApplication extends MultiDexApplication {

    private ApplicationComponent applicationComponent;
    public static boolean isDebug=false;
    @Override
    public void onCreate() {
        super.onCreate();
        Hawk.init(this).build();
        setUpInjector();
    }

    private void setUpInjector() {
        applicationComponent = DaggerApplicationComponent.builder().
                applicationModule(new ApplicationModule(this)).build();
    }


    public ApplicationComponent getApplicationComponent() {
        return applicationComponent;
    }
}
