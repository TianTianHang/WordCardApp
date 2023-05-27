package com.mao.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import com.mao.activity.R;


import java.util.List;

public class PosMeaningAdapter extends ArrayAdapter<String[]> {

    public PosMeaningAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }

    public PosMeaningAdapter(@NonNull Context context, int resource, int textViewResourceId) {
        super(context, resource, textViewResourceId);
    }

    public PosMeaningAdapter(@NonNull Context context, int resource, @NonNull String[][] objects) {
        super(context, resource, objects);
    }

    public PosMeaningAdapter(@NonNull Context context, int resource, int textViewResourceId, @NonNull String[][] objects) {
        super(context, resource, textViewResourceId, objects);
    }

    public PosMeaningAdapter(@NonNull Context context, int resource, @NonNull List<String[]> objects) {
        super(context, resource, objects);
    }

    public PosMeaningAdapter(@NonNull Context context, int resource, int textViewResourceId, @NonNull List<String[]> objects) {
        super(context, resource, textViewResourceId, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.pos_meaning_lvi, parent, false);
        }

        String[] pos_meaning = getItem(position);

        TextView posText = view.findViewById(R.id.tv_pos);
        posText.setText(pos_meaning[0]);


        TextView meaningText = view.findViewById(R.id.tv_meaning);
        meaningText.setText(pos_meaning[1]);
        return view;
    }
}
