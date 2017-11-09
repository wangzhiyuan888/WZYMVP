package me.jessyan.mvparms.demo.di.component;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import me.jessyan.mvparms.demo.di.module.ShoppingCartModule;

import me.jessyan.mvparms.demo.mvp.ui.fragment.ShoppingCartFragment;

@ActivityScope
@Component(modules = ShoppingCartModule.class, dependencies = AppComponent.class)
public interface ShoppingCartComponent {
    void inject(ShoppingCartFragment fragment);
}