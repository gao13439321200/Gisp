package com.giiisp.giiisp.widget;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.giiisp.giiisp.R;

import java.util.List;

/**
 * 自定义PopupWindow
 * Created by 05 on 2016/9/29.
 */
public class SpinnerPopWindow<T> extends PopupWindow {
    private LayoutInflater inflater;
    private ListView mListView;
    private List<T> list;
    private PopUpAdapter popAdapter;

    public SpinnerPopWindow(Context context, List<T> list, AdapterView.OnItemClickListener clickListener) {
        super(context);
        inflater = LayoutInflater.from(context);
        this.list = list;
        init(clickListener);
    }

    private void init(AdapterView.OnItemClickListener clickListener){
        View view = inflater.inflate(R.layout.spiner_window_layout, null);
        setContentView(view);
        setWidth(LinearLayout.LayoutParams.WRAP_CONTENT);
        setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        setFocusable(true);
        ColorDrawable dw = new ColorDrawable(0x00);
        setBackgroundDrawable(dw);
        mListView = (ListView) view.findViewById(R.id.popup_listview);
        mListView.setAdapter(popAdapter = new PopUpAdapter());
        mListView.setOnItemClickListener(clickListener);
    }

    private class PopUpAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if(convertView == null){
                viewHolder = new ViewHolder();

                convertView = inflater.inflate(R.layout.pop_up_spinner_layout_item,null);
                viewHolder.textTv = (TextView) convertView.findViewById(R.id.pop_up_item_text_tv);

                convertView.setTag(viewHolder);
            }else{
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.textTv.setText(getItem(position).toString());
            return convertView;
        }
    }

    private class ViewHolder{
        private TextView textTv;
    }
}

