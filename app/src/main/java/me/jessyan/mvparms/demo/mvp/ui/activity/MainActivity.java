package me.jessyan.mvparms.demo.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.util.Log;

import com.hjm.bottomtabbar.BottomTabBar;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.base.BaseFragment;
import com.jess.arms.base.struct.FunctionWithParamAndResult;
import com.jess.arms.base.struct.FunctionsManager;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;

import butterknife.BindView;
import me.jessyan.mvparms.demo.di.component.DaggerMainComponent;
import me.jessyan.mvparms.demo.di.module.MainModule;
import me.jessyan.mvparms.demo.mvp.contract.MainContract;
import me.jessyan.mvparms.demo.mvp.presenter.MainPresenter;

import me.jessyan.mvparms.demo.R;
import me.jessyan.mvparms.demo.mvp.ui.fragment.ClassificationFragment;
import me.jessyan.mvparms.demo.mvp.ui.fragment.FindFragment;
import me.jessyan.mvparms.demo.mvp.ui.fragment.HomeFragment;
import me.jessyan.mvparms.demo.mvp.ui.fragment.MeFragment;
import me.jessyan.mvparms.demo.mvp.ui.fragment.ShoppingCartFragment;
import timber.log.Timber;


import static com.jess.arms.utils.Preconditions.checkNotNull;


public class MainActivity extends BaseActivity<MainPresenter> implements MainContract.View {

    @Nullable
    @BindView(R.id.bottom_bar)
    BottomTabBar mBottomBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setHasFragment(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setupActivityComponent(AppComponent appComponent) {
        DaggerMainComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .mainModule(new MainModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(Bundle savedInstanceState) {
        return R.layout.activity_main; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        mBottomBar.init(getSupportFragmentManager())
                .setImgSize(23, 23)
                .setFontSize(12)
                .setTabPadding(10, 3, 10)
                .setChangeColor(getResources().getColor(R.color.tab_checked), getResources().getColor(R.color.tab_unchecked))
                .addTabItem("首页", R.mipmap.home_selected, R.mipmap.home_unselected, HomeFragment.class)
                .addTabItem("分类", R.mipmap.classification_selected, R.mipmap.classification_unselected, ClassificationFragment.class)
                .addTabItem("发现", R.mipmap.find_selected, R.mipmap.find_unselected, FindFragment.class)
                .addTabItem("购物车", R.mipmap.shoppingcart_selected, R.mipmap.shoppingcart_unselected, ShoppingCartFragment.class)
                .addTabItem("我的", R.mipmap.my_selected, R.mipmap.my_unselected, MeFragment.class)
                //.setTabBarBackgroundResource(R.mipmap.ic_launcher)
                .isShowDivider(true)
                .setOnTabChangeListener(new BottomTabBar.OnTabChangeListener() {
                    @Override
                    public void onTabChange(int position, String name) {
                        Timber.tag(TAG).d("位置：" + position + "      选项卡：" + name);
                    }
                });

    }


    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading(int lastId) {

    }

    @Override
    public void endLoadMore(int lastId) {

    }

    @Override
    public void showMessage(@NonNull String message) {
        checkNotNull(message);
        ArmsUtils.makeText(this,message);
    }

    @Override
    public void launchActivity(@NonNull Intent intent) {
        checkNotNull(intent);
        ArmsUtils.startActivity(intent);
    }

    @Override
    public void killMyself() {
        finish();
    }

    public void goToFragmentShowTypeView(Object object){
        ((BaseFragment)mBottomBar.getmTabHost().getWantFragment(mBottomBar.getmTabHost().getCurrentTab())).showTypeView(object);

    }

    /**
     * Fragment 与Activity间的互相通讯
     * @param tag
     */
    public void setFunctionsForFragment(String tag){
        FragmentManager fm = getSupportFragmentManager();
        BaseFragment fragment = (BaseFragment)fm.findFragmentByTag(tag);
        FunctionsManager fmager = FunctionsManager.getInstance();
        fragment.setFunctionsManager(fmager.addFunction(new FunctionWithParamAndResult<String, String>(HomeFragment.INTERFACE) {
            @Override
            public String function(String data) {
                return "WZY I Love You,"+data;

            }
        }));
    }


}
