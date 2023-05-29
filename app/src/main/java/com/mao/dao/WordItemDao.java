package com.mao.dao;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import com.mao.db.DBHelper;
import com.mao.event.BaseEvent;
import com.mao.event.DBEvent;
import com.mao.model.WordItem;
import org.greenrobot.eventbus.EventBus;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WordItemDao {
    private final DBHelper dbHelper;

    public WordItemDao(Context context) {
        dbHelper = new DBHelper(context, "word.db", "`word_item` (\n" +
                "  `id` INTEGER PRIMARY KEY,\n" +
                "  `word` varchar(50) NOT NULL,\n" +
                "  `meaning` text NOT NULL,\n" +
                "  `pos` text NOT NULL,\n" +
                "  `example` text NOT NULL,\n" +
                "  `count` int NOT NULL,\n" +
                "  `heat` int NOT NULL)", 1);
    }

    public static String listToString(List<?> list, char sep) {
        StringBuilder result = new StringBuilder();
        for (Object o : list) {
            result.append(o).append(sep);
        }
        return result.toString();
    }

    public void postEvent(BaseEvent event) {
        EventBus.getDefault().post(event);
    }

    // 创建单词
    public void createWord(WordItem item) {
        String sql = "INSERT INTO word_item VALUES(null,?,?,?,?,?,?)";
        Object[] params = {item.getWord(), listToString(item.getMeaning(), '|'), listToString(item.getPos(), '|'),
                listToString(item.getExample(), '|'), item.getCount(),
                item.getHeat()};
        dbHelper.execSQL(sql, params);
        DBEvent event = new DBEvent();
        event.type = "insert";
        event.obj = queryWord(item.getWord());
        postEvent(event);
    }

    // 更新单词
    public void updateWord(WordItem item) {
        String sql = "UPDATE word_item SET meaning = ?, pos = ?, example = ?, count = ?, heat = ?  WHERE id = ?";
        Object[] params = {listToString(item.getMeaning(), '|'), listToString(item.getPos(), '|'),
                listToString(item.getExample(), '|'),
                item.getCount(), item.getHeat(), item.getId()};
        dbHelper.execSQL(sql, params);
        DBEvent event = new DBEvent();
        event.type = "update";
        postEvent(event);
    }

    // 删除单词
    public void deleteWord(int id) {
        String sql = "DELETE FROM word_item WHERE id = ?";
        dbHelper.execSQL(sql, new Object[]{id});
        DBEvent event = new DBEvent();
        event.type = "delete";
        event.obj = id;
        postEvent(event);
    }

    private WordItem getWordItem(@NotNull Cursor cursor) {
        @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex("id"));
        @SuppressLint("Range") String word = cursor.getString(cursor.getColumnIndex("word"));
        @SuppressLint("Range") String meaning = cursor.getString(cursor.getColumnIndex("meaning"));
        ArrayList<String> meanings = new ArrayList<>(Arrays.asList(meaning.split("\\|")));
        @SuppressLint("Range") String pos = cursor.getString(cursor.getColumnIndex("pos"));
        ArrayList<String> poss = new ArrayList<>(Arrays.asList(pos.split("\\|")));
        @SuppressLint("Range") String example = cursor.getString(cursor.getColumnIndex("example"));
        ArrayList<String> examples = new ArrayList<>(Arrays.asList(example.split("\\|")));
        @SuppressLint("Range") int count = cursor.getInt(cursor.getColumnIndex("count"));
        @SuppressLint("Range") int heat = cursor.getInt(cursor.getColumnIndex("heat"));
        return new WordItem(id, word, meanings, poss, examples, count, heat);
    }

    // 查询单词
    public WordItem queryWord(String word) {
        String sql = "SELECT * FROM word_item WHERE word = ?";
        Cursor cursor = dbHelper.query(sql, new String[]{word});
        WordItem wordItem = null;
        DBEvent event = new DBEvent();
        event.type = " query one";
        if (cursor.moveToNext()) {
            wordItem = getWordItem(cursor);
        }
        event.obj = wordItem;
        postEvent(event);
        return wordItem;
    }

    public List<WordItem> findAllWord() {
        List<WordItem> list = new ArrayList<>();
        String sql = "SELECT * FROM word_item";
        Cursor cursor = dbHelper.query(sql, null);
        DBEvent event = new DBEvent();
        event.type = " query all";
        while (cursor.moveToNext()) {
            WordItem wordItem = getWordItem(cursor);
            list.add(wordItem);
        }
        event.obj = list;
        postEvent(event);
        return list;

    }

}