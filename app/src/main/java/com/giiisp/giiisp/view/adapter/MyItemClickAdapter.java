package com.giiisp.giiisp.view.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.giiisp.giiisp.dto.HotImgVO;

import java.util.List;

public class MyItemClickAdapter extends BaseQuickAdapter<HotImgVO,BaseViewHolder> {


    public MyItemClickAdapter(int layoutResId, @Nullable List<HotImgVO> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, HotImgVO item) {

    }
}
