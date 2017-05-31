package com.lance.common.loadingview.factory;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lance.common.demo.R;

/**
 * Material默认loading样式
 */
public class MaterialFactory implements LoadingFactory{

    @Override
    public View onCreateView(ViewGroup parent) {
        return LayoutInflater.from(parent.getContext()).inflate(R.layout.loading_progressbar_vertical_material, parent,false);
    }
}
