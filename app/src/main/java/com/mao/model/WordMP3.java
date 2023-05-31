package com.mao.model;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;
import com.mao.http.BaseHttp;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class WordMP3 {
    private final Context context;

    public WordMP3(Context context) {
        this.context = context;
    }

    public void download(String url, String fileName) {
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
            }

            @Override
            public void onComplete() {
                Log.d("d", "download complete");
            }
        };
        BaseHttp.httpGet(url, null).subscribe(observer);
    }

    public void play(String fileName) {
        MediaPlayer mediaPlayer = MediaPlayer.create(context, Uri.fromFile(context.getFileStreamPath(fileName)));
        mediaPlayer.start();
    }
}
