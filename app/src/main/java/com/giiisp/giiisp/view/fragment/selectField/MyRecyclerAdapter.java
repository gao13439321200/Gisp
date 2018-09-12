package com.giiisp.giiisp.view.fragment.selectField;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.blankj.utilcode.util.SPUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.giiisp.giiisp.R;
import com.giiisp.giiisp.api.UrlConstants;
import com.giiisp.giiisp.dto.MajorVO;
import com.giiisp.giiisp.dto.PeopleVO;
import com.giiisp.giiisp.dto.SubjectVO;
import com.giiisp.giiisp.dto.WordVO;
import com.giiisp.giiisp.model.GlideApp;
import com.giiisp.giiisp.view.adapter.ClickEntity;

import java.util.ArrayList;
import java.util.List;

import static com.giiisp.giiisp.view.activity.PaperDetailsActivity.CN;

public class MyRecyclerAdapter extends BaseQuickAdapter<ClickEntity, BaseViewHolder> {
    private int type;
    private List<String> selectIds = new ArrayList<>();
    private OnMyItemClick mOnMyItemClick;

    public MyRecyclerAdapter(int layoutResId, @Nullable List data, int type, OnMyItemClick onMyItemClick) {
        super(layoutResId, data);
        this.type = type;
        this.mOnMyItemClick = onMyItemClick;
    }

    @Override
    protected void convert(BaseViewHolder helper, ClickEntity item) {
        switch (type) {
            case 1://学科
                SubjectVO vo = item.getSubjectVO();
                helper.setText(R.id.text, isChinese() ? vo.getName() : vo.getEnarea());
                helper.setChecked(R.id.text, selectIds.contains(vo.getId()));
                helper.getView(R.id.text).setOnClickListener(v -> {
                    setSelectIdsData(vo.getId());
                    notifyDataSetChanged();
                    mOnMyItemClick.myItemClick(type, vo.getId());
                });
                break;
            case 2://专业
                MajorVO majorVO = item.getMajorVO();
                helper.setText(R.id.text, isChinese() ? majorVO.getName() : majorVO.getEnarea());
                helper.setChecked(R.id.text, selectIds.contains(majorVO.getId()));
                helper.getView(R.id.text).setOnClickListener(v -> {
                    setSelectIdsData(majorVO.getId());
                    notifyDataSetChanged();
                    mOnMyItemClick.myItemClick(type, majorVO.getId());
                });
                break;
            case 3://关键字
            case 4://关键字
                WordVO wordVO = item.getWordVO();
                helper.setText(R.id.text, wordVO.getAntistop());
                helper.setChecked(R.id.text, selectIds.contains(wordVO.getId()));
                helper.getView(R.id.text).setOnClickListener(v -> {
                    setSelectIdsData(wordVO.getId());
                    notifyDataSetChanged();
                    mOnMyItemClick.myItemClick(type, wordVO.getId());
                });
                break;
            case 5://学者页专业
                MajorVO majorVO1 = item.getMajorVO();
                helper.setText(R.id.text, isChinese() ? majorVO1.getName() : majorVO1.getEnarea());
                helper.setChecked(R.id.text, selectIds.contains(majorVO1.getId()));
                helper.getView(R.id.text).setOnClickListener(v -> {
                    setSelectIdsData(majorVO1.getId());
                    notifyDataSetChanged();
                    mOnMyItemClick.myItemClick(type, majorVO1.getId());
                });
                break;
            case 6://推荐学者
            case 7://关注学者
                PeopleVO peopleVO = item.getPeopleVO();
                helper.setText(R.id.tv_name, peopleVO.getName());
                GlideApp.with(mContext)
                        .load(peopleVO.getAvatar())
                        .placeholder(R.mipmap.ic_launcher)
                        .into((ImageView) helper.getView(R.id.img_head));
                helper.setBackgroundRes(R.id.ll_all, selectIds.contains(peopleVO.getId())
                        ? R.drawable.tag_select_people : R.color.bg_white);

                helper.getView(R.id.ll_all).setOnClickListener(v -> {
                    setSelectIdsData(peopleVO.getId());
                    notifyDataSetChanged();
                    mOnMyItemClick.myItemClick(type, peopleVO.getId());
                });
                break;
            default:
                break;
        }
    }

    private boolean isChinese() {
        return CN.equals(SPUtils.getInstance().getString(UrlConstants.LANGUAGE, CN));
    }

    public void setSelectIdsData(String id) {
        switch (type) {
            case 1://学科（单选）
            case 2://专业（单选）
            case 5://学者页专业（单选）
                if (selectIds.contains(id)) {
                    selectIds.remove(id);
                } else {
                    selectIds.clear();
                    selectIds.add(id);
                }
                break;
            case 3://推荐关键字（多选）
            case 4://关注关键字（多选）
            case 6://推荐学者（多选）
            case 7://关注学者（多选）
                if (selectIds.contains(id)) {
                    selectIds.remove(id);
                } else {
                    selectIds.add(id);
                }
                break;
            default:
                break;
        }
    }

    public interface OnMyItemClick {
        void myItemClick(int type, String id);
    }

    public void clearSelectIds() {
        selectIds.clear();
    }


}


