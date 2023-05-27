package com.mao.http;

import io.reactivex.Observable;
import okhttp3.*;

import java.io.IOException;
import java.util.Map;

public class BaseHttp {
    public static Observable<Response> httpGet(String baseUrl,Map<String, String> params) {
        HttpUrl httpUrl = HttpUrl.parse(baseUrl);
        HttpUrl.Builder builder = null;
        if (httpUrl != null) {
            builder = httpUrl.newBuilder();
        }
        if (builder != null) {
            for (String key : params.keySet()) {
                builder.addQueryParameter(key, params.get(key));
            }
        }

        String url;
        if (builder!= null) {
            url = builder.toString();
        } else {
            url = null;
        }
        // 使用RxJava发送请求
        Observable<Response> responseObservable = Observable.create(emitter -> {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(url)
                    .get()
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    emitter.onError(e);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    emitter.onNext(response);
                    emitter.onComplete();
                }
            });
        });
        return responseObservable;
    }
}
