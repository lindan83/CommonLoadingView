package com.lance.common.loadingview;

import android.app.Dialog;

public interface ILoadingDialog extends ILoading {
    Dialog create();

    ILoadingDialog setCancelable(boolean flag);

    ILoadingDialog setMessage(CharSequence message);
}
