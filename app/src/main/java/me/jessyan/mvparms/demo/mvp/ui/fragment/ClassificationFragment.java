package me.jessyan.mvparms.demo.mvp.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jess.arms.base.BaseFragment;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;

import butterknife.BindView;
import butterknife.OnClick;
import me.jessyan.mvparms.demo.di.component.DaggerClassificationComponent;
import me.jessyan.mvparms.demo.di.module.ClassificationModule;
import me.jessyan.mvparms.demo.mvp.contract.ClassificationContract;
import me.jessyan.mvparms.demo.mvp.presenter.ClassificationPresenter;

import me.jessyan.mvparms.demo.R;
import me.jessyan.mvparms.demo.mvp.ui.activity.MainActivity;
import me.jessyan.mvparms.demo.mvp.ui.activity.UserActivity;

import static com.jess.arms.utils.Preconditions.checkNotNull;


public class ClassificationFragment extends BaseFragment<ClassificationPresenter> implements ClassificationContract.View {

    @BindView(R.id.content_layout)
    TextView contentLayout;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof MainActivity){
            ((MainActivity)context).setFunctionsForFragment(getTag());
        }
    }

    @Override
    public void setupFragmentComponent(@NonNull AppComponent appComponent) {
        DaggerClassificationComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .classificationModule(new ClassificationModule(this))
                .build()
                .inject(this);
    }

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return LayoutInflater.from(getActivity()).inflate(R.layout.fragment_classification, container, false);
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {

    }

    /**
     * 此方法是让外部调用使fragment做一些操作的,比如说外部的activity想让fragment对象执行一些方法,
     * 建议在有多个需要让外界调用的方法时,统一传Message,通过what字段,来区分不同的方法,在setData
     * 方法中就可以switch做不同的操作,这样就可以用统一的入口方法做不同的事
     * <p>
     * 使用此方法时请注意调用时fragment的生命周期,如果调用此setData方法时onCreate还没执行
     * setData里却调用了presenter的方法时,是会报空的,因为dagger注入是在onCreated方法中执行的,然后才创建的presenter
     * 如果要做一些初始化操作,可以不必让外部调setData,在initData中初始化就可以了
     *
     * @param data
     */

    @Override
    public void setData(@NonNull Object data) {

    }


    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void endLoadMore() {

    }

    @Override
    public void showMessage(@NonNull String message) {
        checkNotNull(message);
        ArmsUtils.makeText(getContext(),message);
    }

    @Override
    public void launchActivity(@NonNull Intent intent) {
        checkNotNull(intent);
        ArmsUtils.startActivity(intent);
    }

    @Override
    public void killMyself() {

    }

    @OnClick(R.id.content_layout)
    public void onClickContentLayout(){
        startActivity(new Intent(getActivity(), UserActivity.class).putExtra("show_title_type", 1));
    }

}
