package com.lance.common.loadingview.impl;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.lance.common.loadingview.LoadingConfig;
import com.lance.common.loadingview.listener.OnLoadingBarListener;
import com.lance.common.loadingview.factory.LoadingFactory;
import com.lance.common.loadingview.ILoadingBar;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 加载进度
 * 可用于FrameLayout、RelativeLayout、DrawerLayout、CoordinatorLayout、CardView
 */
public final class LoadingBar implements ILoadingBar {
    private static final Map<View, LoadingBar> LOADING_BARS = new HashMap<>();
    private static int LOADING_LIMIT = 15;//默认15,超过会检查资源释放

    private ViewGroup parent;
    private View view;
    private OnLoadingBarListener listener;

    private LoadingBar(ViewGroup parent, LoadingFactory factory) {
        this.parent = parent;
        view = factory.onCreateView(parent);
    }


    /**
     * 显示loading
     */
    @Override
    public void show() {
        if (view != null) {
            view.setVisibility(View.VISIBLE);
            if (view.getParent() != null) {
                parent.removeView(view);
            }
            parent.addView(view);
        }
    }

    /**
     * 取消loading
     */
    @Override
    public void cancel() {
        if (view != null) {
            view.setVisibility(View.GONE);
            parent.removeView(view);
            view = null;
            if (this.listener != null) {
                this.listener.onCancel(parent);
            }
        }
    }

    public LoadingBar setOnClickListener(View.OnClickListener listener) {
        if (view != null) {
            view.setOnClickListener(listener);
        }
        return this;
    }

    public LoadingBar setOnLoadingBarListener(OnLoadingBarListener mListener) {
        this.listener = mListener;
        return this;
    }

    public static LoadingBar make(View parent) {
        return make(parent, LoadingConfig.getLoadingFactory());
    }

    public static LoadingBar make(View parent, LoadingFactory factory) {
        //如果已经有Loading在显示了
        if (LOADING_BARS.containsKey(parent)) {
            LoadingBar loadingBar = LOADING_BARS.get(parent);
            loadingBar.parent.removeView(loadingBar.view);
        }
        LoadingBar newLoadingBar = new LoadingBar(findSuitableParent(parent), factory);
        LOADING_BARS.put(parent, newLoadingBar);
        return newLoadingBar;
    }


    public static LoadingBar make(View parent, final View loadingView) {
        return make(parent, new LoadingFactory() {
            @Override
            public View onCreateView(ViewGroup parent) {
                return loadingView;
            }
        });
    }

    /**
     * 根据父节点取消单个loading
     *
     * @param parent show传过来的父节点
     */
    public static void cancel(View parent) {
        LoadingBar loadingBar = LOADING_BARS.get(parent);
        if (loadingBar != null) {
            loadingBar.cancel();
        }
        LOADING_BARS.remove(parent);
    }

    /**
     * 取消所有loading
     */
    private static void cancelAll() {
        for (Map.Entry<View, LoadingBar> entry : LOADING_BARS.entrySet()) {
            cancel(entry.getKey());
        }
    }


    public static void release() {
        release(LOADING_LIMIT);
    }

    /**
     * 释放无用的资源
     * 可在BaseActivity onDestroy调用
     *
     * @param limit loading池的限制,超过数量才检查资源释放
     */
    public static void release(int limit) {
        if (limit <= 0) {
            limit = LOADING_LIMIT;
        }
        if (LOADING_BARS.size() < limit) {
            return;
        }
        Log.d("LoadingBar", "release before loading size - " + LOADING_BARS.size());
        Iterator<Map.Entry<View, LoadingBar>> it = LOADING_BARS.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<View, LoadingBar> entry = it.next();
            Context context = entry.getKey().getContext();
            if (context instanceof Activity && ((Activity) context).isFinishing()) {
                it.remove();
            }
        }
        Log.d("LoadingBar", "release after loading size - " + LOADING_BARS.size());
    }

    /**
     * 找到合适的父布局
     */
    private static ViewGroup findSuitableParent(View parent) {
        if (parent == null) {
            return null;
        }
        View suitableParent = parent;
        do {
            String parentClassName = suitableParent.getClass().getName();
            if (suitableParent instanceof FrameLayout || suitableParent instanceof RelativeLayout ||
                    "android.support.v4.widget.DrawerLayout".equals(parentClassName) ||
                    "android.support.design.widget.CoordinatorLayout".equals(parentClassName) ||
                    "android.support.v7.widget.CardView".equals(parentClassName)) {
                return (ViewGroup) suitableParent;
            } else {
                final ViewParent viewParent = suitableParent.getParent();
                suitableParent = viewParent instanceof View ? (View) viewParent : null;
                return (ViewGroup) suitableParent;
            }
        } while (suitableParent != null);
    }
}