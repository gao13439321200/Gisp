package com.giiisp.giiisp.view.fragment;

import com.giiisp.giiisp.R;
import com.giiisp.giiisp.base.BaseActivity;
import com.giiisp.giiisp.utils.ChartUtil;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 论文详情-统计
 */
public class StatisticsFragment extends BaseActivity {

    @BindView(R.id.lineChart)
    LineChart mLineChart;

    @Override
    public int getLayoutId() {
        return R.layout.layout_fragment_statistics;
    }

    @Override
    public void initView() {
        List<String> xDataList = new ArrayList<>();// x轴数据源
        List<Entry> yDataList = new ArrayList<>();// y轴数据数据源
//给上面的X、Y轴数据源做假数据测试
        for (int i = 0; i < 24; i++) {
            // x轴显示的数据
            xDataList.add(i + ":00");
            //y轴生成float类型的随机数
            float value = (float) (Math.random()) + 3;
            yDataList.add(new Entry(i, value));
        }
//显示图表,参数（ 上下文，图表对象， X轴数据，Y轴数据，图表标题，曲线图例名称，坐标点击弹出提示框中数字单位）
        ChartUtil.showChart(this, mLineChart, xDataList, yDataList, "供热趋势图", "供热量/时间", "kw/h");
    }
}
