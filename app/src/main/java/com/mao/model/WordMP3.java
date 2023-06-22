package com.mao.model;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;
import com.mao.dao.WordItemDao;
import com.mao.event.HttpEvent;
import com.mao.http.BaseHttp;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import okhttp3.Response;
import org.greenrobot.eventbus.EventBus;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WordMP3 {
    private final Context context;

    public WordMP3(Context context) {
        this.context = context;
    }

    public void download(String url, String fileName) {
        File file = context.getFileStreamPath(fileName);
        HttpEvent event = new HttpEvent();
        if (!file.exists()) {
            // 文件不存在
            Observer<Response> observer = new Observer<Response>() {
                @Override
                public void onSubscribe(@NotNull Disposable d) {

                }

                @Override
                public void onNext(@NotNull Response res) {
                    try {
                        FileOutputStream fileOutputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE);
                        InputStream inputStream = null;
                        if (res.body() != null) {
                            inputStream = res.body().byteStream();
                        }

                        byte[] buffer = new byte[4096];
                        int bufferLength;
                        if (inputStream != null) {
                            while ((bufferLength = inputStream.read(buffer)) > 0) {
                                fileOutputStream.write(buffer, 0, bufferLength);
                            }
                        }

                        fileOutputStream.close();
                    } catch (IOException e) {
                        onError(e);
                    }
                }

                @Override
                public void onError(@NotNull Throwable e) {
                    Log.d("d", "download error");
                    event.what = 3;
                    event.message = "音频下载错误";
                    EventBus.getDefault().post(event);
                }

                @Override
                public void onComplete() {
                    Log.d("d", "download complete");
                    event.what = 3;
                    event.message = "音频下载完成";
                    EventBus.getDefault().post(event);

                }
            };
            BaseHttp.httpGet(url, null).subscribe(observer);
        }else{
            Log.d("d", "file exit");
        }
    }

    public void play(String fileName,String url) {
        File file = context.getFileStreamPath(fileName);
        if (!file.exists()) {
            // MP3文件不存在,显示提示下载对话框
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("文件不存在");
            builder.setMessage("MP3文件 " + fileName + " 不存在,是否下载?");

            builder.setPositiveButton("下载", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // 下载文件逻辑
                    download(url,fileName);
                }
            });
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // 用户取消了下载
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();
        } else {
            MediaPlayer mediaPlayer = MediaPlayer.create(context, Uri.fromFile(context.getFileStreamPath(fileName)));
            mediaPlayer.start();
        }
    }
        public String delete () {
            File filesDir = context.getFilesDir();
            File[] files = filesDir.listFiles();
            int count=0;
            if (files != null) {
                for (File f : files) {
                    if (f.getName().endsWith(".mp3")) {
                        if(f.delete()){
                            count++;
                        }
                    }
                }
            }
            return "共有"+files.length+"个文件，删除了"+count+"个";
        }

    }
