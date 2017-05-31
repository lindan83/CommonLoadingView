package com.lance.common.loadingview.impl;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.ContextWrapper;

import com.lance.common.loadingview.LoadingConfig;
import com.lance.common.loadingview.factory.DialogFactory;
import com.lance.common.loadingview.ILoadingDialog;

public class LoadingDialog implements ILoadingDialog {
    private static LoadingDialog LOADING_DIALOG;

    private Dialog dialog;
    private DialogFactory<Dialog> factory;

    public LoadingDialog(Context context, DialogFactory<Dialog> factory) {
        this.dialog = factory.onCreateDialog(context);
        this.factory = factory;
        int animateStyleId = this.factory.getAnimateStyleId();
        if (animateStyleId > 0) {
            this.dialog.getWindow().setWindowAnimations(animateStyleId);
        }
    }

    @Override
    public void show() {
        if (isValid() && !dialog.isShowing()) {
            dialog.show();
        }
    }

    public void cancelDialog() {
        if (isValid() && dialog.isShowing()) {
            dialog.cancel();
        }
    }

    @Override
    public Dialog create() {
        return dialog;
    }

    @Override
    public ILoadingDialog setCancelable(boolean flag) {
        dialog.setCancelable(flag);
        return this;
    }

    @Override
    public ILoadingDialog setMessage(CharSequence message) {
        factory.setMessage(dialog, message);
        return this;
    }

    private boolean isValid() {
        if (dialog != null) {
            Context context = dialog.getContext();
            if (context instanceof ContextWrapper) {
                context = ((ContextWrapper) context).getBaseContext();
            }
            if (context instanceof Activity) {
                if (!((Activity) context).isFinishing()) {
                    return true;
                }
            }
        }
        return false;
    }

    public static LoadingDialog make(Context context) {
        return make(context, LoadingConfig.getDialogFactory());
    }

    public static LoadingDialog make(Context context, DialogFactory<Dialog> factory) {
        cancel();
        LOADING_DIALOG = new LoadingDialog(context, factory);
        return LOADING_DIALOG;
    }

    public static void cancel() {
        if (LOADING_DIALOG != null) {
            LOADING_DIALOG.cancelDialog();
            LOADING_DIALOG = null;
        }
    }
}