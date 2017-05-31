package com.lance.common.loadingview.factory;

import android.view.View;
import android.view.ViewGroup;

/**
 * LoadingView工厂
 */
public interface LoadingFactory {
    View onCreateView(ViewGroup parent);
}
