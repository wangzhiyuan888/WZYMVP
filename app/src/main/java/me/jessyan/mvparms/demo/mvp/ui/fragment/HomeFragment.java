package me.jessyan.mvparms.demo.mvp.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.github.jdsjlzx.recyclerview.ProgressStyle;
import com.jess.arms.base.DefaultAdapter;
import com.jess.arms.base.struct.FunctionException;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.tbruyelle.rxpermissions2.RxPermissions;

import javax.inject.Inject;

import butterknife.BindView;
import com.jess.arms.base.BaseFragment;
import me.jessyan.mvparms.demo.di.component.DaggerHomeComponent;
import me.jessyan.mvparms.demo.di.module.HomeModule;
import me.jessyan.mvparms.demo.mvp.contract.HomeContract;
import me.jessyan.mvparms.demo.mvp.presenter.HomePresenter;

import me.jessyan.mvparms.demo.R;
import me.jessyan.mvparms.demo.mvp.ui.activity.MainActivity;
import me.jessyan.mvparms.demo.mvp.ui.adapter.UserAdapter;
import timber.log.Timber;


public class HomeFragment extends BaseFragment<HomePresenter> implements HomeContract.View {

    @BindView(R.id.recyclerView)
    LRecyclerView mRecyclerView;
    @Inject
    RxPermissions mRxPermissions;
    @Inject
    RecyclerView.LayoutManager mLayoutManager;
    @Inject
    RecyclerView.Adapter mAdapter;

    private LRecyclerViewAdapter mLRecyclerViewAdapter = null;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof MainActivity){
            ((MainActivity)context).setFunctionsForFragment(getTag());
        }
    }


    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public void setupFragmentComponent(AppComponent appComponent) {
        DaggerHomeComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .homeModule(new HomeModule(this))
                .build()
                .inject(this);
    }

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        initRecyclerView();
        mLRecyclerViewAdapter = new LRecyclerViewAdapter(mAdapter);
        mRecyclerView.setAdapter(mLRecyclerViewAdapter);
        //是否禁用自动加载更多功能,false为禁用, 默认开启自动加载更多功能
        mRecyclerView.setLoadMoreEnabled(true);
        mPresenter.requestUsers(true);//打开 App 时自动加载列表

        //点击事件监听
        ((UserAdapter)mAdapter).setOnItemClickListener(new DefaultAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int viewType, Object data, int position) {
                String str = null;
                try {
                    str = mFunctionsManager.invokeFunc(INTERFACE, "I Konw!", String.class);
                    Toast.makeText(getContext(), "str: "+str, Toast.LENGTH_LONG).show();
                } catch (FunctionException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    @Override
    public void setData(Object data) {

    }

    /**
     * 初始化RecyclerView
     */
    private void initRecyclerView() {

        mRecyclerView.setRefreshProgressStyle(ProgressStyle.SysProgress);
        mRecyclerView.setArrowImageView(R.drawable.ic_pulltorefresh_arrow);
        mRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.SysProgress);

        super.setNetWorkErrorViewClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showContent();
                mRecyclerView.refresh();

            }
        });

        super.setEmptyViewClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showContent();
                mRecyclerView.refresh();

            }
        });

        mRecyclerView.setLScrollListener(new LRecyclerView.LScrollListener() {

            @Override
            public void onScrollUp() {
            }

            @Override
            public void onScrollDown() {
            }


            @Override
            public void onScrolled(int distanceX, int distanceY) {
            }

            @Override
            public void onScrollStateChanged(int state) {

            }

        });


        mRecyclerView.setOnRefreshListener(onRefreshListener);
        mRecyclerView.setOnLoadMoreListener(onLoadmoreListener);
        mRecyclerView.refresh();

        ArmsUtils.configRecyclerView(mRecyclerView, mLayoutManager);
    }


    @Override
    public void showLoading() {
        Timber.tag(TAG).w("showLoading");
    }

    @Override
    public void hideLoading(int lastId) {
        Timber.tag(TAG).w("hideLoading");
        mRecyclerView.refreshComplete(lastId);
        /*mRefreshLayout.finishRefresh();//传入false表示刷新失败*/
    }

    @Override
    public void showMessage(String message) {
        ArmsUtils.makeText(getContext(),message);
    }

    @Override
    public void launchActivity(Intent intent) {
        ArmsUtils.startActivity(intent);
    }

    @Override
    public void killMyself() {
    }

    /**
     * 开始加载更多
     */
    @Override
    public void startLoadMore() {
    }

    /**
     * 结束加载更多
     */
    @Override
    public void endLoadMore(int lastId) {
        mRecyclerView.refreshComplete(lastId);
        /*mRefreshLayout.finishLoadmore();//传入false表示加载失败*/
    }

    @Override
    public Activity getFragmentActivity() {
        return getActivity();
    }

    @Override
    public RxPermissions getRxPermissions() {
        return mRxPermissions;
    }

    @Override
    public void onDestroy() {
        DefaultAdapter.releaseAllHolder(mRecyclerView);//super.onDestroy()之后会unbind,所有view被置为null,所以必须在之前调用
        super.onDestroy();
        this.mRxPermissions = null;
        
    }

    public com.github.jdsjlzx.interfaces.OnRefreshListener onRefreshListener = new com.github.jdsjlzx.interfaces.OnRefreshListener() {
        @Override
        public void onRefresh() {
            mPresenter.requestUsers(true);

        }
    };

    public OnLoadMoreListener onLoadmoreListener = new OnLoadMoreListener() {
        @Override
        public void onLoadMore() {
            mPresenter.requestUsers(false);

        }
    };

}
