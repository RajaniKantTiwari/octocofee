package me.prateeksaigal.injector.component;


import dagger.Component;
import me.prateeksaigal.injector.module.CommonModule;
import me.prateeksaigal.injector.scope.PerActivity;
import me.prateeksaigal.ocotocoffee.ProfileActivity;


@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = CommonModule.class)
public interface CommonComponent {
    void inject(ProfileActivity activity);

}
