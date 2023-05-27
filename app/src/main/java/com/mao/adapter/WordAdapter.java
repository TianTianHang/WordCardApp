package com.mao.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.mao.activity.R;
import com.mao.event.HttpEvent;
import com.mao.fragment.WordCardFragment;
import com.mao.model.WordItem;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

public class WordAdapter extends ArrayAdapter<WordItem> {
    public WordAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }

    public WordAdapter(@NonNull Context context, int resource, int textViewResourceId) {
        super(context, resource, textViewResourceId);
    }

    public WordAdapter(@NonNull Context context, int resource, @NonNull WordItem[] objects) {
        super(context, resource, objects);
    }

    public WordAdapter(@NonNull Context context, int resource, int textViewResourceId, @NonNull WordItem[] objects) {
        super(context, resource, textViewResourceId, objects);
    }

    public WordAdapter(@NonNull Context context, int resource, @NonNull List<WordItem> objects) {
        super(context, resource, objects);

    }

    public WordAdapter(@NonNull Context context, int resource, int textViewResourceId, @NonNull List<WordItem> objects) {
        super(context, resource, textViewResourceId, objects);
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
                word.reloadWordInfo();
            }
        });


        TextView wordText = view.findViewById(R.id.word_text);
        wordText.setText(word.getWord());


        TextView heatText = view.findViewById(R.id.heat_text);
        heatText.setBackgroundResource(getHeatBg(word.getHeat()));
        heatText.setText(String.valueOf(word.getHeat()));

        return view;
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