package com.mao.dao;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import com.mao.db.DBHelper;
import com.mao.model.WordItem;

import java.util.ArrayList;
import java.util.Arrays;

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
                "  `heat` int NOT NULL)" , 1);
    }

    // 创建单词
    public void createWord(WordItem item) {
        String sql = "INSERT INTO word_item VALUES(null,?,?,?,?,?,?)";
        Object[] params = {item.getWord(), item.getMeaning(),
                item.getPos(), item.getExample(), item.getCount(),
                item.getHeat()};
        dbHelper.execSQL(sql, params);
    }

    // 更新单词
    public void updateWord(WordItem item) {
        String sql = "UPDATE word_item SET meaning = ?, pos = ?, example = ?, count = ?, heat = ?  WHERE id = ?";
        Object[] params = {item.getMeaning(), item.getPos(), item.getExample(),
                item.getCount(), item.getHeat(), item.getId()};
        dbHelper.execSQL(sql, params);
    }

    // 删除单词
    public void deleteWord(int id) {
        String sql = "DELETE FROM word_item WHERE id = ?";
        dbHelper.execSQL(sql, new Object[]{id});
    }

    // 查询单词
    public WordItem queryWord(String word) {
        String sql = "SELECT * FROM word_item WHERE word = ?";
        Cursor cursor = dbHelper.query(sql, new String[]{word});
        if (cursor.moveToNext()) {
            @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex("id"));
            @SuppressLint("Range") String meaning = cursor.getString(cursor.getColumnIndex("meaning"));
            ArrayList<String> meanings = new ArrayList<>(Arrays.asList(meaning.split(",")));
            @SuppressLint("Range") String pos = cursor.getString(cursor.getColumnIndex("pos"));
            ArrayList<String> poss = new ArrayList<>(Arrays.asList(pos.split(",")));
            @SuppressLint("Range") String example = cursor.getString(cursor.getColumnIndex("example"));
            ArrayList<String> examples = new ArrayList<>(Arrays.asList(example.split(",")));
            @SuppressLint("Range") int count = cursor.getInt(cursor.getColumnIndex("count"));
            @SuppressLint("Range") int heat  = cursor.getInt(cursor.getColumnIndex("heat"));
            return new WordItem(id, word, meanings, poss, examples, count, heat);
        }
        return null;
    }
}