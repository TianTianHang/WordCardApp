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
import com.mao.dao.WordItemDao;
import com.mao.event.HttpEvent;
import com.mao.model.WordItem;
import org.greenrobot.eventbus.EventBus;


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
        EditText search = view.findViewById(R.id.et_search);
        btSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Editable s = search.getText();
                WordItemDao wordItemDao=new WordItemDao(requireContext());
                WordItem wordItem=wordItemDao.queryWord(s.toString());
                if(wordItem!=null){
                    HttpEvent msg = new HttpEvent();
                    msg.what = 1;
                    msg.obj = wordItem;
                    msg.message = "already load";
                    EventBus.getDefault().post(msg);
                }else {
                    WordItem word = new WordItem(s.toString());
                    word.reloadWordInfo(requireContext());
                }
            }
        });
        // Inflate the layout for this fragment
        return view;
    }
}