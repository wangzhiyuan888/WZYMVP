package me.jessyan.mvparms.demo.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

import me.jessyan.mvparms.demo.mvp.contract.ShoppingCartContract;
import me.jessyan.mvparms.demo.mvp.model.ShoppingCartModel;


@Module
public class ShoppingCartModule {
    private ShoppingCartContract.View view;

    /**
     * 构建ShoppingCartModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public ShoppingCartModule(ShoppingCartContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    ShoppingCartContract.View provideShoppingCartView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    ShoppingCartContract.Model provideShoppingCartModel(ShoppingCartModel model) {
        return model;
    }
}