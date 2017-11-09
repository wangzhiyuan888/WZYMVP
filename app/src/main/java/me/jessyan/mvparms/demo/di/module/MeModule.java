package me.jessyan.mvparms.demo.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

import me.jessyan.mvparms.demo.mvp.contract.MeContract;
import me.jessyan.mvparms.demo.mvp.model.MeModel;


@Module
public class MeModule {
    private MeContract.View view;

    /**
     * 构建MeModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public MeModule(MeContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    MeContract.View provideMeView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    MeContract.Model provideMeModel(MeModel model) {
        return model;
    }
}