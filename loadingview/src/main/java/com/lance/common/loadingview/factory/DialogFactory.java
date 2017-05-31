package com.lance.common.loadingview.factory;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.StyleRes;

/**
 * 对话框工厂
 * @param <D> Dialog
 */
public interface DialogFactory<D extends Dialog> {
    /**
     * 创建dialog
     */
    D onCreateDialog(Context context);

    /**
     * 设置提示消息
     */
    void setMessage(D dialog, CharSequence message);

    /**
     * 进入退出的动画id
     */
    @StyleRes int getAnimateStyleId();
}
