package com.mao.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import androidx.fragment.app.Fragment;
import com.mao.activity.R;
import com.mao.model.WordItem;


public class MainFragment extends Fragment {


    public MainFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        ImageButton btSearch = view.findViewById(R.id.ib_search);
        btSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText filter = view.findViewById(R.id.et_search);
                Editable s = filter.getText();
                WordItem word = new WordItem(s.toString());
                word.reloadWordInfo();
            }
        });
        // Inflate the layout for this fragment
        return view;
    }
}