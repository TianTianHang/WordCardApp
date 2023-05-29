package com.mao.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import androidx.fragment.app.Fragment;
import com.mao.activity.R;
import com.mao.adapter.WordAdapter;
import com.mao.dao.WordItemDao;
import com.mao.model.WordItem;

import java.util.List;


public class DrawerFragment extends Fragment {

    public DrawerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();
        ;
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        WordItemDao wordItemDao = new WordItemDao(requireActivity());
        List<WordItem> wordItems = wordItemDao.findAllWord();
        // 获取view
        View view = inflater.inflate(R.layout.fl_drawer, container, false);
        ListView listView = view.findViewById(R.id.words_listview);
        // 创建适配器，上下文，listview_item,数据
        WordAdapter adapter = new WordAdapter(requireActivity(),
                R.layout.f1_drawer_lvi, wordItems);
        // 设置适配器
        listView.setAdapter(adapter);

        EditText filter = view.findViewById(R.id.et_filter);
        filter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(filter.getText());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        return view;
    }
}