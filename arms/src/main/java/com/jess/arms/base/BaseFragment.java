/**
  * Copyright 2017 JessYan
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  *      http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
package com.jess.arms.base;

import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.jess.arms.R;
import com.jess.arms.base.delegate.IFragment;
import com.jess.arms.base.struct.FunctionsManager;
import com.jess.arms.integration.cache.Cache;
import com.jess.arms.integration.cache.CacheType;
import com.jess.arms.integration.lifecycle.FragmentLifecycleable;
import com.jess.arms.mvp.IPresenter;
import com.jess.arms.utils.ArmsUtils;
import com.trello.rxlifecycle2.android.FragmentEvent;

import java.util.Map;

import javax.inject.Inject;

import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.Subject;
import timber.log.Timber;

/**
 * ================================================
 * 因为 Java 只能单继承,所以如果要用到需要继承特定 @{@link Fragment} 的三方库,那你就需要自己自定义 @{@link Fragment}
 * 继承于这个特定的 @{@link Fragment},然后再按照 {@link BaseFragment} 的格式,将代码复制过去,记住一定要实现{@link IFragment}
 * <p>
 * Created by JessYan on 22/03/2016
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * ================================================
 */
public abstract class BaseFragment<P extends IPresenter> extends Fragment implements IFragment, FragmentLifecycleable {
    protected final String TAG = this.getClass().getSimpleName();
    private final BehaviorSubject<FragmentEvent> mLifecycleSubject = BehaviorSubject.create();
    private Cache<String, Object> mCache;
    @Inject
    protected P mPresenter;

    protected FunctionsManager mFunctionsManager;
    public static final String INTERFACE = BaseFragment.class.getName();

    public void setFunctionsManager(FunctionsManager functionsManager) {
        this.mFunctionsManager = functionsManager;
    }

    @NonNull
    @Override
    public synchronized Cache<String, Object> provideCache() {
        if (mCache == null) {
            mCache = ArmsUtils.obtainAppComponentFromContext(getActivity()).cacheFactory().build(CacheType.FRAGMENT_CACHE);
        }
        return mCache;
    }


    /***************************************  StubView 相关配置 ***********************************/
    private View mView;
    private FrameLayout tempLayout;

    /**
     * 视图类型,内容,加载中,没有数据,网络异常
     */
    enum ViewType {
        CONTENT, PROGRESS, EMPTY, ERROR
    }

    // 当前视图类型
    private ViewType mCurrentViewType = ViewType.CONTENT;
    private String mEmptyMessage = "没有数据";
    private int mEmptyMessageIcon = R.drawable.ic_order_empty;

    private View mProgressContainer;//进度区域
    private View mContentContainer;//内容区域
    private View mContentView;//内容视图
    private View mEmptyView;//空区域
    private View mNetWorkErrorView;//网络异常视图

    private View mTempView;//临时保存创建的内容,在onViewCreated之后设置进去

    private ViewStub mProgressStub;
    private ViewStub mEmptyStub;
    private ViewStub mNetWorkErrorStub;

    private View.OnClickListener mEmptyViewClickListener;
    private View.OnClickListener mNetWorkErrorViewClickListener;
    /** 视图是否已经创建完成 */
    protected boolean mIsViewCreated = false;


    @NonNull
    @Override
    public final Subject<FragmentEvent> provideLifecycleSubject() {
        return mLifecycleSubject;
    }

    /**
     * 默认布局
     * @return
     */
    protected View initDefaultView(){
        return LayoutInflater.from(getActivity()).inflate(R.layout.activity_commont_layout_type, null, false);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = initDefaultView();
        tempLayout = (FrameLayout) mView.findViewById(R.id.temp_layout);
        tempLayout.removeAllViews();
        tempLayout.addView(LayoutInflater.from(getActivity()).inflate(getFragmentLayoutResourceId(), null, false));
        ensureContent();
        if (mCurrentViewType != ViewType.CONTENT) {
            switchView(getCurrentView(), mContentView, false);
        }
        mIsViewCreated = true;
        mTempView = initView(inflater, container, savedInstanceState);
        getCurrentView();
        return mView;
    }

    /**
     * {@link Fragment} 布局ID
     * 如果需要更改Fragment布局文件需要重写此方法,布局文件结构必须和R.layout.fragment_progress一致
     *
     * @return @LayoutRes(eg R.layout.fragment_progress)
     */
    public @LayoutRes
    int getFragmentLayoutResourceId() {
        return R.layout.fragment_progress;
    }

    /**
     * Attach to mTempView once the mTempView hierarchy has been created.
     */
    @Override public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        /*ensureContent();
        if (mCurrentViewType != ViewType.CONTENT) {
            switchView(getCurrentView(), mContentView, false);
        }
        mIsViewCreated = true;
        getCurrentView();*/
    }

    /**
     * Detach from mTempView.
     */
    @Override public void onDestroyView() {
        mProgressContainer = mContentContainer = mContentView = mEmptyView = mNetWorkErrorView = null;
        mProgressStub = mEmptyStub = mNetWorkErrorStub = null;
        super.onDestroyView();
    }

    /**
     * Return content mTempView or null if the content mTempView has not been initialized.
     *
     * @return content mTempView or null
     * @see #setContentView(View)
     */
    public View getContentView() {
        if (mContentContainer instanceof ViewGroup) {
            ViewGroup contentContainer = (ViewGroup) mContentContainer;
            if (mContentView == null) {
                contentContainer.addView(mTempView);
            } else {
                int index = contentContainer.indexOfChild(mContentView);
                // replace content mTempView
                contentContainer.removeView(mContentView);
                contentContainer.addView(mTempView, index);
            }
            mContentView = mTempView;
        }
        return mContentView;
    }

    /**
     * Set the content content from a layout resource.
     *
     * @param layoutResId Resource ID to be inflated.
     * @see #setContentView(View)
     * @see #getContentView()
     */
    public void setContentView(@LayoutRes int layoutResId) {
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        View contentView = layoutInflater.inflate(layoutResId, null);
        setContentView(contentView);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) mPresenter.onDestroy();//释放资源
        this.mPresenter = null;
    }


    /**
     * 是否使用eventBus,默认为使用(true)，
     *
     * @return
     */
    @Override
    public boolean useEventBus() {
        return true;
    }

    /**
     * Set the content mTempView to an explicit mTempView. If the content mTempView was installed
     * earlier,
     * the content will be replaced with a new mTempView.
     *
     * @param view The desired content to display. Value can't be null.
     */
    public void setContentView(View view) {
        ensureContent();
        if (view == null) {
            throw new IllegalArgumentException("Content mTempView can't be null");
        }
        if (mContentContainer instanceof ViewGroup) {
            ViewGroup contentContainer = (ViewGroup) mContentContainer;
            if (mContentView == null) {
                contentContainer.addView(view);
            } else {
                int index = contentContainer.indexOfChild(mContentView);
                // replace content mTempView
                contentContainer.removeView(mContentView);
                contentContainer.addView(view, index);
            }
            mContentView = view;
        } else {
            throw new IllegalStateException("Can't be used with a custom content mTempView");
        }
    }

    /**
     * 显示进度
     */
    public void showProgress() {
        if (mCurrentViewType == ViewType.PROGRESS) return;
        if (mIsViewCreated) {
            View hideView = getCurrentView();
            View showView = getProgressContainer();
            switchView(showView, hideView, false);
        }
        mCurrentViewType = ViewType.PROGRESS;
    }

    /**
     * 显示内容
     */
    public void showContent() {
        if (mCurrentViewType == ViewType.CONTENT) return;
        if (mIsViewCreated) {
            View hideView = getCurrentView();
            View showView = mContentView;
            switchView(showView, hideView, false);
        }
        mCurrentViewType = ViewType.CONTENT;
    }

    /**
     * 显示空视图
     */
    public void showEmpty() {
        if (mCurrentViewType == ViewType.EMPTY) return;
        if (mIsViewCreated) {
            View hideView = getCurrentView();
            View showView = getEmptyView();
            switchView(showView, hideView, false);
        }
        mCurrentViewType = ViewType.EMPTY;
    }

    /**
     * 显示网络错误
     */
    public void showNetWorkError() {
        if (mCurrentViewType == ViewType.ERROR) return;
        if (mIsViewCreated) {
            View hideView = getCurrentView();
            View showView = getNetWorkErrorView();
            switchView(showView, hideView, false);
        }
        mCurrentViewType = ViewType.ERROR;
    }

    /**
     * 设置空视图点击事件
     *
     * @param emptyViewClickListener
     */
    public void setEmptyViewClickListener(View.OnClickListener emptyViewClickListener) {
        if (mEmptyView != null) {
            mEmptyView.setOnClickListener(emptyViewClickListener);
        }
        mEmptyViewClickListener = emptyViewClickListener;
    }

    /**
     * 设置网络异常点击事件
     *
     * @param netWorkErrorViewClickListener
     */
    public void setNetWorkErrorViewClickListener(View.OnClickListener netWorkErrorViewClickListener) {
        if (mNetWorkErrorView != null) {
            mNetWorkErrorView.setOnClickListener(netWorkErrorViewClickListener);
        }
        mNetWorkErrorViewClickListener = netWorkErrorViewClickListener;
    }

    public View getEmptyView() {
        if (mEmptyView == null) {
            mEmptyView = mEmptyStub.inflate();
            mEmptyView.setOnClickListener(mEmptyViewClickListener);
            this.setEmptyMessage(mEmptyMessage, mEmptyMessageIcon);
        }
        return mEmptyView;
    }

    /**
     * 设置空数据视图
     *
     * @param message 消息名称
     * @param icon 图标
     */
    public void setEmptyMessage(String message, @DrawableRes int icon) {
        this.mEmptyMessage = message;
        this.mEmptyMessageIcon = icon;
        if (!mIsViewCreated || mEmptyView == null) return;
        TextView textView = (TextView) mEmptyView.findViewById(R.id.data_empty_text);
        if (textView == null) {
            Timber.e(new RuntimeException("空数据视图必须包含id为R.id.data_empty_text的TextView"));
            return;
        }
        textView.setText(message);
        textView.setCompoundDrawablesWithIntrinsicBounds(0, icon, 0, 0);
    }

    private View getProgressContainer() {
        if (mProgressContainer == null) mProgressContainer = mProgressStub.inflate();
        return mProgressContainer;
    }

    private View getNetWorkErrorView() {
        if (mNetWorkErrorView == null) {
            mNetWorkErrorView = mNetWorkErrorStub.inflate();
            mNetWorkErrorView.setOnClickListener(mNetWorkErrorViewClickListener);
        }
        return mNetWorkErrorView;
    }

    /**
     * Initialization views.
     */
    private void ensureContent() {
        if (mContentContainer != null) {// 已经初始化
            return;
        }

        // 内容
        mContentContainer = mView.findViewById(R.id.content_container);
        if (mContentContainer == null) {
            throw new RuntimeException(
                    "Your content must have a ViewGroup whose id attribute is 'R.id.content_container'");
        }

        // 加载进度
        mProgressStub = (ViewStub) mView.findViewById(R.id.progress_stub);
        if (mProgressStub == null) {
            throw new RuntimeException(
                    "Your content must have a ViewStub whose id attribute is 'R.id.progress_stub'");
        }

        // 空视图
        mEmptyStub = (ViewStub) mView.findViewById(R.id.empty_stub);
        if (mEmptyStub == null) {
            throw new RuntimeException(
                    "Your content must have a ViewStub whose id attribute is 'R.id.empty_stub'");
        }

        // 网络异常
        mNetWorkErrorStub = (ViewStub) mView.findViewById(R.id.network_error_stub);
        if (mNetWorkErrorStub == null) {
            throw new RuntimeException(
                    "Your content must have a ViewStub whose id attribute is 'R.id.network_error_stub'");
        }
    }

    /**
     * 获取当前显示的View
     *
     * @return 当前显示的View
     */
    private View getCurrentView() {
        View view = null;
        switch (mCurrentViewType) {
            case PROGRESS: {
                view = getProgressContainer();
                break;
            }
            case CONTENT: {
                if (mContentView == null) {
                    view = getContentView();
                } else {
                    view = mContentView;

                }
                break;
            }
            case EMPTY: {
                view = getEmptyView();
                break;
            }
            case ERROR: {
                view = getNetWorkErrorView();
                break;
            }
        }
        return view;
    }

    /**
     * 切换当前显示的视图
     *
     * @param shownView 需要显示的View
     * @param hiddenView 需要隐藏的View
     * @param animate 动画?
     */
    private void switchView(View shownView, View hiddenView, boolean animate) {
        if (animate) {
            shownView.startAnimation(
                    AnimationUtils.loadAnimation(this.getActivity(), android.R.anim.fade_in));
            hiddenView.startAnimation(
                    AnimationUtils.loadAnimation(this.getActivity(), android.R.anim.fade_out));
        } else {
            shownView.clearAnimation();
            hiddenView.clearAnimation();
        }

        shownView.setVisibility(View.VISIBLE);
        hiddenView.setVisibility(View.GONE);
    }

    public void showTypeView(Object object){
        Map<String,Object> map = (Map<String, Object>) object;
        if(map != null && "1".equals(map.get("type"))){
            //网络不可用
            showNetWorkError();

        }if(map != null && "2".equals(map.get("type"))){
            //请求网络超时
            showNetWorkError();

        }if(map != null && "3".equals(map.get("type"))){
            //数据解析错误
            setEmptyMessage("数据解析错误", R.drawable.ic_order_empty);
            showEmpty();

        }if(map != null && "4".equals(map.get("type"))){
            //服务器发生错误
            setEmptyMessage("服务器发生错误", R.drawable.ic_order_empty);
            showEmpty();

        }if(map != null && "5".equals(map.get("type"))){
            //请求地址不存在
            setEmptyMessage("请求地址不存在", R.drawable.ic_order_empty);
            showEmpty();

        }if(map != null && "6".equals(map.get("type"))){
            //请求被服务器拒绝
            setEmptyMessage("请求被服务器拒绝", R.drawable.ic_order_empty);
            showEmpty();

        }if(map != null && "7".equals(map.get("type"))){
            //请求被重定向到其他页面
            setEmptyMessage("请求被重定向到其他页面", R.drawable.ic_order_empty);
            showEmpty();

        }if(map != null && "8".equals(map.get("type"))){
            //未知错误
            setEmptyMessage("未知错误", R.drawable.ic_order_empty);
            showEmpty();

        }

    }

}
