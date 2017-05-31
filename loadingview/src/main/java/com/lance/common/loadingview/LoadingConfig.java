package com.lance.common.loadingview;

import com.lance.common.loadingview.factory.DialogFactory;
import com.lance.common.loadingview.factory.LoadingFactory;
import com.lance.common.loadingview.factory.MaterialDialogFactory;
import com.lance.common.loadingview.factory.MaterialFactory;

public class LoadingConfig {
    private final static LoadingFactory DEFAULT_LOADING_FACTORY = new MaterialFactory();
    private final static DialogFactory DEFAULT_DIALOG_FACTORY = new MaterialDialogFactory();

    private static LoadingFactory loadingFactory = DEFAULT_LOADING_FACTORY;
    private static DialogFactory dialogFactory = DEFAULT_DIALOG_FACTORY;

    /**
     * 全局配置
     * 在程序入口调用
     */
    public static void setFactory(LoadingFactory loadingFactory, DialogFactory dialogFactory) {
        if (loadingFactory != null) {
            LoadingConfig.loadingFactory = loadingFactory;
        }
        if (dialogFactory != null) {
            LoadingConfig.dialogFactory = dialogFactory;
        }
    }


    public static void defaultFactory() {
        setFactory(new MaterialFactory(), new MaterialDialogFactory());
    }

    public static LoadingFactory getLoadingFactory() {
        return loadingFactory;
    }

    public static DialogFactory getDialogFactory() {
        return dialogFactory;
    }
}
