package com.mao.fragment;

import android.os.Bundle;
import android.view.*;
import android.widget.ListView;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import com.mao.activity.R;
import com.mao.adapter.ExampleAdapter;
import com.mao.adapter.PosMeaningAdapter;
import com.mao.dao.WordItemDao;
import com.mao.model.WordItem;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;


public class WordCardFragment extends Fragment {

    private WordItem wordItem;

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
        inflater.inflate(R.menu.word_card_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_menu_item:
                // TODO 添加当前fragment worditem到数据库，并更新drawer UI
                WordItemDao wordItemDao=new WordItemDao(requireActivity());
                wordItemDao.createWord(wordItem);
                return true;
            case R.id.exit_menu_item:
                requireActivity().finish();
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
        ListView lv_example = view.findViewById(R.id.lv_example);
        ListView lv_pos_meaning = view.findViewById(R.id.lv_pos_meaning);
        PosMeaningAdapter posMeaningAdapter = new PosMeaningAdapter(requireActivity(),
                R.layout.pos_meaning_lvi, wordItem.getPosAndMeaning());
        ExampleAdapter exampleAdapter= new ExampleAdapter(requireActivity(),R.layout.example_lvi,wordItem.getExample());
        tv_word.setText(wordItem.getWord());
        lv_pos_meaning.setAdapter(posMeaningAdapter);
        lv_example.setAdapter(exampleAdapter);
        return view;
    }
}