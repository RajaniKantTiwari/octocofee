package me.prateeksaigal.injector.component;



import javax.inject.Singleton;

import dagger.Component;
import me.prateeksaigal.injector.module.ApplicationModule;
import me.prateeksaigal.injector.module.NetworkModule;
import me.prateeksaigal.injector.scope.PerApplication;
import me.prateeksaigal.network.Repository;


@Singleton
@PerApplication
@Component(modules = {ApplicationModule.class, NetworkModule.class})
public interface ApplicationComponent {
    Repository repository();
}
