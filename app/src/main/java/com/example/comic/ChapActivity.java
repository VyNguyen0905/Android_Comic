package com.example.comic;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.comic.adapter.ChapTruyenAdapter;
import com.example.comic.api.ApiLayChapTruyen;
import com.example.comic.api.ApiLayTruyen;
import com.example.comic.interfaces.LayChapVe;
import com.example.comic.object.ChapTruyen;
import com.example.comic.object.TruyenTranh;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class ChapActivity extends AppCompatActivity implements LayChapVe {

    TextView txvTenTruyen;
    ImageView imgAnhTruyens;
    TruyenTranh truyenTranh;
    ListView lsvDanhSachChap;
    ArrayList<ChapTruyen> arrChap;
    ChapTruyenAdapter chapTruyenAdapter;
    private SwipeRefreshLayout swipeRefreshLayoutChap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chap);
        init();
        anhXa();
        seClick();
        setUp();

        swipeRefreshLayoutChap = findViewById(R.id.swipeRefreshLayoutChap);
        swipeRefreshLayoutChap.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Gọi phương thức update khi người dùng thực hiện cử chỉ pull-to-refresh
                swipeRefreshLayoutChap.setRefreshing(false);
            }
        });
        new ApiLayChapTruyen(this, truyenTranh.getId()).execute();

    }
    private void init(){
        Bundle b = getIntent().getBundleExtra("data");
        truyenTranh = (TruyenTranh) b.getSerializable("truyen");
        arrChap = new ArrayList<>();
    }
    private void anhXa(){
        imgAnhTruyens = findViewById(R.id.imgAnhTruyens);
        txvTenTruyen = findViewById(R.id.txvTenTruyen);
        lsvDanhSachChap = findViewById(R.id.lsvDanhSachChap);
    }
    private void setUp(){
        txvTenTruyen.setText(truyenTranh.getTenTruyen());
        Glide.with(this).load(truyenTranh.getLinkAnh()).into(imgAnhTruyens);

    }
    private void seClick(){
        lsvDanhSachChap.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Bundle b = new Bundle();
                b.putString("idChap", arrChap.get(i).getId());
                Intent intent = new Intent(ChapActivity.this, DocTruyenActivity.class);
                intent.putExtra("data", b);
                startActivity(intent);
            }
        });
    }

    @Override
    public void batDau() {
        Toast.makeText(this,"Dang Chap Lay Ve",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void ketThuc(String data) {
        try {
            JSONArray array = new JSONArray(data);
            for(int i = 0; i < array.length(); i++){
                ChapTruyen chapTruyen = new ChapTruyen(array.getJSONObject(i));
                arrChap.add(chapTruyen);
            }
            chapTruyenAdapter = new ChapTruyenAdapter(this, 0, arrChap);
            lsvDanhSachChap.setAdapter(chapTruyenAdapter);
            chapTruyenAdapter.notifyDataSetChanged();
        }catch (JSONException e){
            e.printStackTrace();
        }finally {
            // Ẩn hiệu ứng làm mới
            swipeRefreshLayoutChap.setRefreshing(false);
        }
    }

    @Override
    public void biLoi() {

    }
}