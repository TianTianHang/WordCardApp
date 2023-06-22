package com.mao.fragment;

import android.os.Bundle;
import android.view.*;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.mao.activity.R;
import com.mao.adapter.ExampleAdapter;
import com.mao.adapter.PosMeaningAdapter;
import com.mao.dao.WordItemDao;
import com.mao.event.HttpEvent;
import com.mao.model.WordItem;
import com.mao.model.WordMP3;
import org.greenrobot.eventbus.EventBus;
import org.jetbrains.annotations.NotNull;


public class WordCardFragment extends Fragment {

    private WordItem wordItem;

    public WordItem getWordItem() {
        return wordItem;
    }

    public void setWordItem(WordItem wordItem) {
        this.wordItem = wordItem;
    }

    public WordCardFragment() {
        // Required empty public constructor
    }

    public WordCardFragment(WordItem wordItem) {
        this.wordItem = wordItem;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(@NotNull Menu menu, MenuInflater inflater) {
        menu.setGroupVisible(R.id.group1, true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_menu_item:
                // TODO 添加当前fragment worditem到数据库，并更新drawer UI
                WordItemDao wordItemDao = new WordItemDao(requireActivity());
                if (wordItemDao.queryWord(wordItem.getWord()) == null) {
                    wordItemDao.createWord(wordItem);
                    Toast.makeText(requireActivity(), "添加成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(requireActivity(), "单词已存在，请勿重复添加", Toast.LENGTH_SHORT).show();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_word_card, container, false);
        TextView tv_word = view.findViewById(R.id.tv_word);
        TextView tv_pron1 = view.findViewById(R.id.tv_pron1);
        TextView tv_pron2 = view.findViewById(R.id.tv_pron2);
        ImageView iv_sound1 = view.findViewById(R.id.iv_sound1);
        ImageView iv_sound2 = view.findViewById(R.id.iv_sound2);
        ListView lv_example = view.findViewById(R.id.lv_example);
        ListView lv_pos_meaning = view.findViewById(R.id.lv_pos_meaning);
        ImageView iv_home = view.findViewById(R.id.iv_home);
        ImageView iv_back = view.findViewById(R.id.iv_back);
        PosMeaningAdapter posMeaningAdapter = new PosMeaningAdapter(requireActivity(),
                R.layout.pos_meaning_lvi, wordItem.getPosAndMeaning());
        ExampleAdapter exampleAdapter = new ExampleAdapter(requireActivity(), R.layout.example_lvi, wordItem.getExample());
        tv_word.setText(wordItem.getWord());
        tv_pron1.setText(wordItem.getPron()[0]);
        tv_pron2.setText(wordItem.getPron()[1]);
        iv_sound1.setColorFilter(R.color.dayNight_black);
        iv_sound2.setColorFilter(R.color.dayNight_black);
        lv_pos_meaning.setAdapter(posMeaningAdapter);
        lv_example.setAdapter(exampleAdapter);
        iv_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HttpEvent event=new HttpEvent();
                event.what=4;
                event.message="home";
                EventBus.getDefault().post(event);
            }
        });
        WordMP3 wordMP3 = new WordMP3(requireContext());
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                fragmentManager.popBackStack();
            }
        });
        iv_sound1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wordMP3.play(wordItem.getWord() + "-1.mp3",wordItem.getUrls()[0]);
            }
        });
        iv_sound2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wordMP3.play(wordItem.getWord() + "-2.mp3",wordItem.getUrls()[1]);
            }
        });
        return view;
    }
}