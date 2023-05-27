package com.mao.model;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.mao.activity.R;
import com.mao.event.HttpEvent;
import com.mao.http.BaseHttp;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.BufferedSource;
import org.greenrobot.eventbus.EventBus;
import org.jetbrains.annotations.NotNull;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class WordItem {


    private int id;
    private String word;     // 单词
    private ArrayList<String> meaning;  // 词义
    private ArrayList<String> pos; //词性
    private ArrayList<String> example; //例句
    private int count;      // 出现次数
    private int heat;     //热度

    public WordItem() {
    }

    public WordItem(String word)  {
        this.word = word;
    }

    public WordItem(int id, String word, ArrayList<String> meaning, ArrayList<String> pos, ArrayList<String> example, int count, int heat) {
        this.id = id;
        this.word = word;
        this.meaning = meaning;
        this.pos = pos;
        this.example = example;
        this.count = count;
        this.heat = heat;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public void increaseCount() {
        this.count++;
    }

    public int getHeat() {
        return heat;
    }

    public void setHeat(int heat) {
        this.heat = heat;
    }

    public ArrayList<String> getMeaning() {
        return meaning;
    }

    public void setMeaning(ArrayList<String> meaning) {
        this.meaning = meaning;
    }

    public ArrayList<String> getPos() {
        return pos;
    }

    public void setPos(ArrayList<String> pos) {
        this.pos = pos;
    }

    public ArrayList<String> getExample() {
        return example;
    }

    public void setExample(ArrayList<String> example) {
        this.example = example;
    }

    public ArrayList<String[]> getPosAndMeaning() {
        ArrayList<String[]> result = new ArrayList<>();
        if(pos!=null && meaning!=null) {
            for (int i = 0; i < pos.size(); i++) {
                result.add(new String[]{pos.get(i), meaning.get(i)});
            }
        }
        return result;
    }



    public void reloadWordInfo() {
        String baseUrl = "https://www.bing.com/dict/search";
        Map<String, String> params = new HashMap<>();
        params.put("q", word);
        WordItem wordItem=this;
        Observer<Response> observer = new Observer<Response>() {
            @Override
            public void onSubscribe(@NotNull Disposable d) {
            }

            @Override
            public void onNext(@NotNull Response res) {
                // 解析响应、提取数据
                try {
                    if (res.body() != null) {
                        String html = res.body().source().readUtf8();
                        Document doc = Jsoup.parse(html);
                        Elements pos = doc.select("span.pos");
                        Elements meaning = doc.select(".def.b_regtxt");
                        Elements example = doc.select(".se_li1");
                        wordItem.pos = new ArrayList<>();
                        wordItem.meaning = new ArrayList<>();
                        wordItem.example = new ArrayList<>();
                        for (Element e : pos) {
                            wordItem.pos.add(e.text());
                        }
                        for (Element e : meaning) {
                            wordItem.meaning.add(e.text());
                        }
                        for (Element e : example) {
                            String s=e.child(0).text()+'\n'+e.child(1).text()+'\n'+
                                        e.child(2).text();
                            wordItem.example.add(s);
                        }
                    }
                } catch (IOException e) {
                    onError(e);
                }
            }
            @Override
            public void onError(@NotNull Throwable e) {
                Log.d("d","eventbus send error");
                HttpEvent msg = new HttpEvent();
                msg.what=1;
                msg.obj = null;
                msg.message="error";
                EventBus.getDefault().post(msg);
            }

            @Override
            public void onComplete(){
                Log.d("d","eventbus send success");
                HttpEvent msg = new HttpEvent();
                msg.what=1;
                msg.obj = wordItem;
                msg.message="success";
                EventBus.getDefault().post(msg);
            }
        };
        BaseHttp.httpGet(baseUrl, params).subscribe(observer);
    }

}
