package com.mao.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import androidx.annotation.NonNull;
import com.mao.activity.R;
import com.mao.dao.WordItemDao;
import com.mao.event.HttpEvent;
import com.mao.model.WordItem;
import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

public class WordAdapter extends ArrayAdapter<WordItem> implements Filterable {
    private final ArrayList<WordItem> mOriginalValues;
    private List<WordItem> mValues;

    public WordAdapter(@NonNull Context context, int resource, @NonNull List<WordItem> objects) {
        super(context, resource, objects);
        mOriginalValues = new ArrayList<>(objects);
        mValues = objects;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.f1_drawer_lvi, parent, false);
        }
        WordItem word = getItem(position);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!word.getLoad()) {
                    word.reloadWordInfo(getContext());

                } else {
                    HttpEvent msg = new HttpEvent();
                    msg.what = 1;
                    msg.obj = word;
                    msg.message = "already load";
                    EventBus.getDefault().post(msg);
                    word.increaseCount();
                }
                new WordItemDao(getContext()).updateWord(word);
            }
        });
        view.setOnTouchListener(new View.OnTouchListener() {
            private long downTime;

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        downTime = System.currentTimeMillis();
                        break;
                    case MotionEvent.ACTION_UP:
                        long upTime = System.currentTimeMillis();
                        if (upTime - downTime > 500) {
                            // Long press detected
                            // Handle long press event
                            WordItemDao wordItemDao = new WordItemDao(getContext());
                            String id = ((TextView) v.findViewById(R.id.tv_word_id)).getText().toString();
                            wordItemDao.deleteWord(Integer.parseInt(id));
                            return true;
                        }
                        break;
                }
                return false;
            }
        });

        TextView wordText = view.findViewById(R.id.word_text);
        wordText.setText(word.getWord());


        TextView countText = view.findViewById(R.id.count_text);
        countText.setBackgroundResource(getHeatBg(word.getCount()));
        countText.setText(String.valueOf(word.getCount()));
        TextView idText = view.findViewById(R.id.tv_word_id);
        idText.setText(String.valueOf(word.getId()));
        return view;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                if (constraint != null) {
                    // 过滤数据源,得到过滤后的list
                    ArrayList<WordItem> filterList = new ArrayList<>();
                    for (WordItem item : mOriginalValues) {
                        if (item.getWord().contains(constraint.toString())) {
                            filterList.add(item);
                        }
                    }
                    results.values = filterList;
                    results.count = filterList.size();
                }
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                // 更新adapter的数据源
                mValues.clear();
                mValues.addAll((ArrayList<WordItem>) results.values);
                notifyDataSetChanged();
            }
        };
    }


    // 根据热度值获取对应背景
    private int getHeatBg(int heat) {
        if (heat >= 80) {
            return R.drawable.heat_bg_hot;
        } else if (heat >= 40) {
            return R.drawable.heat_bg_warm;
        } else {
            return R.drawable.heat_bg_normal;
        }
    }
}