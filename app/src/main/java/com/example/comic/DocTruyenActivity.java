package com.example.comic;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.comic.adapter.ImageAdapter;
import com.example.comic.api.ApiLayAnh;
import com.example.comic.interfaces.LayAnhVe;
import com.example.comic.object.TruyenTranh;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;


public class DocTruyenActivity extends AppCompatActivity implements LayAnhVe {
    ImageView imgAnh;
    TextView txvSoTrang;
    ArrayList<String> arrUrlAnh;
    int soTrang, soTrangDangDoc;
    String idChap;
    private ImageAdapter imageAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doc_truyen);
        init();
        anhXa();
        setUp();
        setClick();
        new ApiLayAnh(this,idChap).execute();

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        imageAdapter = new ImageAdapter(arrUrlAnh);
        recyclerView.setAdapter(imageAdapter);

    }
    public void init(){
        Bundle b = getIntent().getBundleExtra("data");
        idChap = b.getString("idChap");
        /*
        arrUrlAnh = new ArrayList<>();
        arrUrlAnh.add("https://cdnntx.com/nettruyen/ke-san-anh-hung/147/1.jpg");
        arrUrlAnh.add("https://cdnntx.com/nettruyen/ke-san-anh-hung/147/2.jpg");
        arrUrlAnh.add("https://cdnntx.com/nettruyen/ke-san-anh-hung/147/3.jpg");
        arrUrlAnh.add("https://cdnntx.com/nettruyen/ke-san-anh-hung/147/4.jpg");
        soTrangDangDoc = 1;
        soTrang = arrUrlAnh.size();*/
    }
    public void anhXa(){
        imgAnh = findViewById(R.id.imgAnh);
    }
    public void setUp(){
    }
    public void setClick(){

    }

    @Override
    public void batDau() {

    }

    @Override
    public void ketThuc(String data) {
        try {
            arrUrlAnh = new ArrayList<>();
            JSONArray array = new JSONArray(data);
            for (int i = 0; i < array.length(); i++) {
                arrUrlAnh.add(array.getString(i));
            }
            // Hiển thị hình ảnh đầu tiên
            hienThiHinhAnh();
            Toast.makeText(this, "Đã Lấy Được Dữ Liệu Hình Ảnh", Toast.LENGTH_SHORT).show();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    // Phương thức hiển thị hình ảnh từ URL sử dụng Glide
    private void hienThiHinhAnh() {
        if (arrUrlAnh != null && arrUrlAnh.size() > 0) {
            ImageAdapter imageAdapter = new ImageAdapter(arrUrlAnh);
            RecyclerView recyclerView = findViewById(R.id.recyclerView);
            recyclerView.setAdapter(imageAdapter);
        }
    }
    @Override
    public void biLoi() {

    }
}