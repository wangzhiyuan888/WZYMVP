package me.jessyan.mvparms.demo.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

import me.jessyan.mvparms.demo.mvp.contract.ClassificationContract;
import me.jessyan.mvparms.demo.mvp.model.ClassificationModel;


@Module
public class ClassificationModule {
    private ClassificationContract.View view;

    /**
     * 构建ClassificationModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public ClassificationModule(ClassificationContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    ClassificationContract.View provideClassificationView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    ClassificationContract.Model provideClassificationModel(ClassificationModel model) {
        return model;
    }
}