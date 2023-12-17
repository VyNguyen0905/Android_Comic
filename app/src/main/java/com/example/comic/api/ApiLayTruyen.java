package com.example.comic.api;

import android.os.AsyncTask;

import com.example.comic.interfaces.Laytruyenve;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;

import java.io.IOException;

public class ApiLayTruyen extends AsyncTask<Void, Void, Void> {
    String data;
    Laytruyenve laytruyenve;

    public ApiLayTruyen(Laytruyenve laytruyenve) {
        this.laytruyenve = laytruyenve;
        this.laytruyenve.batDau();
    }

    @Override
    protected Void doInBackground(Void... voids) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                //.url("https://www.myjsons.com/v/2a342142")
                .url("http://vytien.000webhostapp.com/layTruyen.php")
                .build();
        data = null;
        try {
            Response response = client.newCall(request).execute();
            ResponseBody body = response.body();
            data = body.string();
        }catch (IOException e){
            data = null;
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        if (data == null){
            this.laytruyenve.biLoi();
        }else {
            this.laytruyenve.ketThuc(data);
        }
    }

}
