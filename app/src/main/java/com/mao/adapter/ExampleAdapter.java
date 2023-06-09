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

public class ExampleAdapter extends ArrayAdapter<String> {
    public ExampleAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }

    public ExampleAdapter(@NonNull Context context, int resource, int textViewResourceId) {
        super(context, resource, textViewResourceId);
    }

    public ExampleAdapter(@NonNull Context context, int resource, @NonNull String[] objects) {
        super(context, resource, objects);
    }

    public ExampleAdapter(@NonNull Context context, int resource, int textViewResourceId, @NonNull String[] objects) {
        super(context, resource, textViewResourceId, objects);
    }

    public ExampleAdapter(@NonNull Context context, int resource, @NonNull List<String> objects) {
        super(context, resource, objects);
    }

    public ExampleAdapter(@NonNull Context context, int resource, int textViewResourceId, @NonNull List<String> objects) {
        super(context, resource, textViewResourceId, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.example_lvi, parent, false);
        }

        String[] example = getItem(position).split("\n");
        TextView exampleChText = view.findViewById(R.id.tv_example_ch);
        TextView exampleEhText = view.findViewById(R.id.tv_example_en);
        TextView exampleSrcText = view.findViewById(R.id.tv_src);
        exampleEhText.setText(example[0]);
        exampleChText.setText(example[1]);
        exampleSrcText.setText(example[2]);
        return view;
    }
}
