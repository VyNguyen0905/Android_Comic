package com.example.comic.api;

import android.os.AsyncTask;

import com.example.comic.interfaces.ApiService;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;

import java.io.IOException;

public class ApiLayUser extends AsyncTask<Void, Void, Void> {
    String data;
    ApiService apiService;

    // Sửa tên hàm khởi tạo thành tên của class (được gọi khi khởi tạo đối tượng)
    public ApiLayUser(ApiService apiService) {
        this.apiService = apiService;
        this.apiService.batDau();
    }

    @Override
    protected Void doInBackground(Void... voids) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://vytien.000webhostapp.com/layUser.php")
                .build();
        data = null;
        try {
            Response response = client.newCall(request).execute();
            ResponseBody body = response.body();
            if (body != null) {
                data = body.string();
            }
        } catch (IOException e) {
            e.printStackTrace();
            data = null;
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        if (data == null) {
            this.apiService.biLoi();
        } else {
            this.apiService.ketThuc(data);
        }
    }
}
