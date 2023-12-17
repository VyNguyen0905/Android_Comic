package com.example.comic.api;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.comic.LoginActivity;
import com.example.comic.interfaces.ApiService;
import com.example.comic.interfaces.ApiServiceRegister;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class ApiRegisterAsyncTask extends AsyncTask<String, Void, String> {
    ApiServiceRegister apiServiceRegister; // Biến apiService

    public ApiRegisterAsyncTask(ApiServiceRegister apiServiceRegister) {
        this.apiServiceRegister = apiServiceRegister;
        this.apiServiceRegister.batDau();
    }
    @Override
    protected String  doInBackground(String... params) {
        String url = params[0];
        String jsonData = params[1];
        Log.d("ApiRegisterAsyncTask", "doInBackground started");
        try {
            URL apiUrl = new URL(url);
            HttpURLConnection urlConnection = (HttpURLConnection) apiUrl.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setDoOutput(true);

            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(urlConnection.getOutputStream());
            outputStreamWriter.write(jsonData);
            outputStreamWriter.flush();

            int responseCode = urlConnection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Xử lý phản hồi từ máy chủ (nếu có)
                BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                return response.toString();

            } else {
                // Xử lý lỗi từ máy chủ
                return "Error: " + responseCode;
            }
        } catch (IOException e) {
            Log.e("ApiRegisterAsyncTask", "Error in doInBackground: " + e.getMessage());
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }

    @Override
    protected void onPostExecute(String result) {
        Log.d("ApiRegisterAsyncTask", "Result from server: " + result);
        if (result == null) {
            this.apiServiceRegister.biLoiDangKy();
        } else {
            this.apiServiceRegister.ketThucDangKy(result);
        }
    }
}
