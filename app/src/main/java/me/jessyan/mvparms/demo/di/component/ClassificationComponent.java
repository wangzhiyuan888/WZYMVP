package me.jessyan.mvparms.demo.di.component;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import me.jessyan.mvparms.demo.di.module.ClassificationModule;

import me.jessyan.mvparms.demo.mvp.ui.fragment.ClassificationFragment;

@ActivityScope
@Component(modules = ClassificationModule.class, dependencies = AppComponent.class)
public interface ClassificationComponent {
    void inject(ClassificationFragment fragment);
}