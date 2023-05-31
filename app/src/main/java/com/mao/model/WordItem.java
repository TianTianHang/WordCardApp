package com.mao.model;

import android.content.Context;
import android.util.Log;
import com.mao.event.HttpEvent;
import com.mao.http.BaseHttp;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import okhttp3.Response;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WordItem {


    private int id;
    private String word;     // 单词
    private ArrayList<String> meaning;  // 词义
    private ArrayList<String> pos; //词性
    private ArrayList<String> example; //例句
    private String[] pron;
    private int count;      // 出现次数
    private Boolean isLoad = false;

    public WordItem() {
    }

    public WordItem(String word) {
        this.word = word;
    }

    public WordItem(int id, String word, ArrayList<String> meaning, ArrayList<String> pos, ArrayList<String> example, int count, String[] pron) {
        this.id = id;
        this.word = word;
        this.meaning = meaning;
        this.pos = pos;
        this.example = example;
        this.count = count;
        this.pron = pron;
    }

    public Boolean getLoad() {

        return isLoad;
    }

    public void setLoad(Boolean load) {
        isLoad = load;
    }

    public String[] getPron() {
        return pron;
    }

    public void setPron(String[] pron) {
        this.pron = pron;
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
        if (pos != null && meaning != null) {
            for (int i = 0; i < pos.size(); i++) {
                result.add(new String[]{pos.get(i), meaning.get(i)});
            }
        }
        return result;
    }


    public void reloadWordInfo(Context context) {
        String baseUrl = "https://www.bing.com/dict/search";
        Map<String, String> params = new HashMap<>();
        params.put("q", word);
        WordItem wordItem = this;
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
                        Elements pron = doc.select(".hd_p1_1").get(0).getAllElements().select("div");
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
                            String s = e.child(0).text() + '\n' + e.child(1).text() + '\n' +
                                    e.child(2).text();
                            wordItem.example.add(s);
                        }
                        wordItem.pron = new String[]{pron.get(1).text(), pron.get(3).text()};
                        Elements audioElements = pron.select("a");
                        Pattern pattern = Pattern.compile("https(.*)\\.mp3");
                        WordMP3 wordMP3 = new WordMP3(context);
                        for (int i = 0; i < 2; i++) {
                            Matcher matcher = pattern.matcher(audioElements.get(i).attributes().get("onclick"));
                            matcher.find();
                            String url = matcher.group();
                            wordMP3.download(url, word + "-" + (i + 1) + ".mp3");
                        }
                    }
                } catch (IOException e) {
                    onError(e);
                }
            }

            @Override
            public void onError(@NotNull Throwable e) {
                Log.d("d", "eventbus send error");
                HttpEvent msg = new HttpEvent();
                msg.what = 2;
                msg.obj = e;
                msg.message = "error";
                EventBus.getDefault().post(msg);
            }

            @Override
            public void onComplete() {
                Log.d("d", "eventbus send success");
                HttpEvent msg = new HttpEvent();
                msg.what = 1;
                msg.obj = wordItem;
                msg.message = "success";
                EventBus.getDefault().post(msg);
                isLoad = true;
            }
        };
        BaseHttp.httpGet(baseUrl, params).subscribe(observer);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WordItem wordItem = (WordItem) o;

        return getId() == wordItem.getId();
    }

    @Override
    public int hashCode() {
        int result = getId();
        result = 31 * result + getWord().hashCode();
        return result;
    }
}
