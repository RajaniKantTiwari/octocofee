package me.prateeksaigal.injector.module;


import dagger.Module;
import dagger.Provides;
import me.prateeksaigal.OctoCofeeApplication;
import me.prateeksaigal.injector.scope.PerApplication;


@Module
public class ApplicationModule {
    private OctoCofeeApplication application;

    public ApplicationModule(OctoCofeeApplication application) {
        this.application = application;
    }
    @Provides
    @PerApplication
    public OctoCofeeApplication provideApplication() {
        return application;
    }
}
