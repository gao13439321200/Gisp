package com.giiisp.giiisp.view.fragment;

import android.graphics.Color;
import android.os.Bundle;

import com.blankj.utilcode.util.ToastUtils;
import com.giiisp.giiisp.R;
import com.giiisp.giiisp.base.BaseMvpFragment;
import com.giiisp.giiisp.dto.BaseBean;
import com.giiisp.giiisp.dto.StatisticsBean;
import com.giiisp.giiisp.entity.BaseEntity;
import com.giiisp.giiisp.presenter.WholePresenter;
import com.giiisp.giiisp.view.impl.BaseImpl;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;

/**
 * 论文详情-统计
 */
public class StatisticsFragment extends BaseMvpFragment<BaseImpl, WholePresenter> {

    @BindView(R.id.lineChart_update)
    LineChart mLineChartUpdate;
    @BindView(R.id.lineChart_collection)
    LineChart mLineChartCollection;
    @BindView(R.id.lineChart_time)
    LineChart mLineChartTime;
    @BindView(R.id.barChart_update)
    BarChart barChartUpdate;
    @BindView(R.id.barChart_collection)
    BarChart barChartCollection;
    @BindView(R.id.barChart_time)
    BarChart barChartTime;

    public static StatisticsFragment newInstance(String pid) {

        Bundle args = new Bundle();
        args.putString("pid", pid);
        StatisticsFragment fragment = new StatisticsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.layout_fragment_statistics;
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {
        super.initData();
        setPid(getArguments().getString("pid"));
    }

    public void setPid(String pid) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("pid", pid);
        map.put("uid", getUserID());
        presenter.getDataAll("220", map);
    }

    @Override
    protected WholePresenter initPresenter() {
        return new WholePresenter(this);
    }

    @Override
    public void onSuccess(BaseEntity entity) {

    }

    @Override
    public void onSuccessNew(String url, BaseBean baseBean) {
        super.onSuccessNew(url, baseBean);
        switch (url) {
            case "220":
                StatisticsBean bean = (StatisticsBean) baseBean;
                if (bean.getX().size() != bean.getCollect().size()
                        || bean.getX().size() != bean.getDownload().size()
                        || bean.getX().size() != bean.getTime().size()) {
                    ToastUtils.showShort("统计数据信息异常");
                    break;
                }
//                List<String> xDataList = new ArrayList<>();//x轴数据源
//                List<Entry> downtimeDataList = new ArrayList<>();// y轴数据数据源
//                List<Entry> collectDataList = new ArrayList<>();// y轴数据数据源
//                List<Entry> timeDataList = new ArrayList<>();// y轴数据数据源
                List<BarEntry> downtimeDataList = new ArrayList<>();// y轴数据数据源
                List<BarEntry> collectDataList = new ArrayList<>();// y轴数据数据源
                List<BarEntry> timeDataList = new ArrayList<>();// y轴数据数据源
                boolean down = bean.getX().size() == bean.getDownload().size();
                boolean collect = bean.getX().size() == bean.getCollect().size();
                boolean time = bean.getX().size() == bean.getTime().size();
//                for (int i = 0; i < bean.getX().size(); i++) {
//                    xDataList.add(bean.getX().get(i) + ":00");
//                    if (down)
//                        downtimeDataList.add(new Entry(Float.parseFloat(bean.getX().get(i))
//                                , Float.parseFloat(bean.getDownload().get(i))));
//                    if (collect)
//                        collectDataList.add(new Entry(Float.parseFloat(bean.getX().get(i))
//                                , Float.parseFloat(bean.getCollect().get(i))));
//                    if (time)
//                        timeDataList.add(new Entry(Float.parseFloat(bean.getX().get(i))
//                                , Float.parseFloat(bean.getTime().get(i))));
//                }
                for (int i = 0; i < bean.getX().size(); i++) {
//                    xDataList.add(bean.getX().get(i) + ":00");
                    if (down)
                        downtimeDataList.add(new BarEntry(Float.parseFloat(bean.getX().get(i))
                                , Float.parseFloat(bean.getDownload().get(i))));
                    if (collect)
                        collectDataList.add(new BarEntry(Float.parseFloat(bean.getX().get(i))
                                , Float.parseFloat(bean.getCollect().get(i))));
                    if (time)
                        timeDataList.add(new BarEntry(Float.parseFloat(bean.getX().get(i))
                                , Float.parseFloat(bean.getTime().get(i))));
                }
//                //曲线图显示图表,参数（ 上下文，图表对象， X轴数据，Y轴数据，图表标题，曲线图例名称，坐标点击弹出提示框中数字单位）
//                if (downtimeDataList.size() != 0)
//                    ChartUtil.showChart(getActivity(), mLineChartUpdate, xDataList, downtimeDataList, "", "", "");
//                if (collectDataList.size() != 0)
//                    ChartUtil.showChart(getActivity(), mLineChartCollection, xDataList, collectDataList, "", "", "");
//                if (timeDataList.size() != 0)
//                    ChartUtil.showChart(getActivity(), mLineChartTime, xDataList, timeDataList, "", "", "");
                //柱状图
                if (downtimeDataList.size() != 0)
                    initBarChart(barChartUpdate, new BarDataSet(downtimeDataList, ""));
                if (collectDataList.size() != 0)
                    initBarChart(barChartCollection, new BarDataSet(collectDataList, ""));
                if (timeDataList.size() != 0)
                    initBarChart(barChartTime, new BarDataSet(timeDataList, ""));

                break;
            default:
                break;
        }
    }

    private void initBarChart(BarChart barChart, BarDataSet barDataSet) {
        //背景颜色
        barChart.setBackgroundColor(Color.WHITE);
        //不显示图表网格
        barChart.setDrawGridBackground(false);
        //背景阴影
        barChart.setDrawBarShadow(false);
        barChart.setHighlightFullBarEnabled(false);
        //显示边框
        barChart.setDrawBorders(false);
        //设置动画效果
        barChart.animateY(1000, Easing.EasingOption.Linear);
        barChart.animateX(1000, Easing.EasingOption.Linear);


        /***XY轴的设置***/
        //X轴设置显示位置在底部
        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
//        xAxis.setAxisMiimum(0f);
        xAxis.setGranularity(1f);

        YAxis leftAxis = barChart.getAxisLeft();
        YAxis rightAxis = barChart.getAxisRight();
        //保证Y轴从0开始，不然会上移一点
        leftAxis.setAxisMinimum(0f);
        rightAxis.setAxisMinimum(0f);

        //隐藏线条
        xAxis.setDrawAxisLine(false);
        leftAxis.setDrawAxisLine(false);
        //隐藏右侧y轴
        rightAxis.setEnabled(false);

        //不显示X轴网格线
        xAxis.setDrawGridLines(false);
        //右侧Y轴网格线设置为虚线
        rightAxis.enableGridDashedLine(0f, 0f, 0f);

        barDataSet.setColor(getResources().getColor(R.color.blue));
        barDataSet.setFormLineWidth(1f);
        barDataSet.setFormSize(15.f);
        //不显示柱状图顶部值
        barDataSet.setDrawValues(true);

        barChart.setData(new BarData(barDataSet));

        //隐藏右下角
        Description description = new Description();
        description.setEnabled(false);
        barChart.setDescription(description);

        //折线图例 标签 设置
        Legend legend = barChart.getLegend();
        //是否显示图例
        legend.setEnabled(false);
    }

}
