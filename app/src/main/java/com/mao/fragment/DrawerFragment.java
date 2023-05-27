package com.mao.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Editable;
import android.util.Log;
import android.widget.*;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.mao.activity.R;
import com.mao.adapter.WordAdapter;
import com.mao.event.HttpEvent;
import com.mao.model.WordItem;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Objects;


public class DrawerFragment extends Fragment {
    private ArrayList<WordItem> wordItems;

    public DrawerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();;
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
        WordItem wordItem1=new WordItem();
        wordItem1.setWord("hello");
        wordItem1.setHeat(100);
        wordItem1.setCount(1);

        WordItem wordItem2=new WordItem();
        wordItem2.setWord("good");
        wordItem2.setHeat(0);
        wordItem2.setCount(10);
        wordItems=new ArrayList<>();
        wordItems.add(wordItem1);
        wordItems.add(wordItem2);
        // 获取view
        View view=inflater.inflate(R.layout.fl_drawer, container, false);
        ListView listView = view.findViewById(R.id.words_listview);
        // 创建适配器，上下文，listview_item,数据
        WordAdapter adapter = new WordAdapter(requireActivity(),
                R.layout.f1_drawer_lvi, wordItems);
        // 设置适配器
        listView.setAdapter(adapter);

        ImageButton btSearch=view.findViewById(R.id.bt_search);
        btSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText search=view.findViewById(R.id.search_word);
                Editable s= search.getText();
                WordItem word=new WordItem(s.toString());
                word.reloadWordInfo();
            }
        });

        return view;
    }
}