package com.example.comic;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.comic.adapter.TruyenTranhAdapter;
import com.example.comic.api.ApiLayTruyen;
import com.example.comic.interfaces.Laytruyenve;
import com.example.comic.object.TruyenTranh;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MainActivity extends AppCompatActivity implements Laytruyenve {

    private static final int REQUEST_LOGIN = 1;
    private static final int REQUEST_LOGIN_FOR_TRUYEN_VIEW = 2;

    GridView gdvDSTruyen;
    TruyenTranhAdapter adapter;
    ArrayList<TruyenTranh> truyenTranhArrayList;
    GridView gdvTopDSTruyen;
    private static ArrayList<TruyenTranh> ToptruyenTranhArrayList = new ArrayList<>();
    private static TruyenTranhAdapter Topadapter;
    EditText edttimkiem;
    private ProgressDialog progressDialog;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView tvUserName;
    private Button btnOpenLogin;
    ImageButton btnLogout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        anhXa();
        setUp();
        setClick();
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Đang Lấy Dữ Liệu Mới Nhất!!!"); // Thông điệp bạn muốn hiển thị
        progressDialog.setCancelable(false);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                progressDialog.show();
                new ApiLayTruyen(MainActivity.this).execute();
            }
        });

        new ApiLayTruyen(this).execute();
    }
    private void init(){
        truyenTranhArrayList = new ArrayList<>();
        ToptruyenTranhArrayList = new ArrayList<>();

    }
    private void anhXa(){
        gdvDSTruyen = findViewById(R.id.gdvDSTruyen);
        gdvTopDSTruyen = findViewById(R.id.gdvTopDSTruyen);
        edttimkiem = findViewById(R.id.edttimkiem);
        tvUserName = findViewById(R.id.tvUserName);
        btnOpenLogin = findViewById(R.id.btnOpenLogin);
        btnLogout = findViewById(R.id.btnLogout);

    }
    private void setUp(){
    }
    private void setClick(){
        if (isLoggedIn()) {
            String userEmail = getUserEmail();
            showLoggedInUI(userEmail);
        } else {
            btnOpenLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openLoginActivity();
                }
            });
        }
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performLogout();
            }
        });
        edttimkiem.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String b = edttimkiem.getText().toString();
                if (adapter != null) {
                    adapter.timkiem(b);
                }
            }
        });
        gdvDSTruyen.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (isLoggedIn()) {
                    TruyenTranh truyenTranh = truyenTranhArrayList.get(i);
                    if (!truyenTranh.isDaHienThiTrongTop()) {
                        truyenTranh.setDaHienThiTrongTop(true);
                        ToptruyenTranhArrayList.add(0, truyenTranh);
                        Collections.sort(ToptruyenTranhArrayList, new Comparator<TruyenTranh>() {
                            @Override
                            public int compare(TruyenTranh t1, TruyenTranh t2) {
                                return Integer.compare(t2.getSoLanClick(), t1.getSoLanClick());
                            }
                        });
                        if (ToptruyenTranhArrayList.size() > 5) {
                            ToptruyenTranhArrayList.remove(5);
                        }
                        Topadapter = new TruyenTranhAdapter(MainActivity.this, 0, ToptruyenTranhArrayList);
                        gdvTopDSTruyen.setAdapter(Topadapter);
                        Topadapter.notifyDataSetChanged();
                    }
                    openChapActivity(truyenTranh, i);
                } else {
                    openLoginActivityForTruyenView(i);
                }

            }
        });
        gdvTopDSTruyen.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TruyenTranh truyenTranh1 = ToptruyenTranhArrayList.get(i);
                Bundle b = new Bundle();
                b.putSerializable("truyen", truyenTranh1);
                Intent intent = new Intent(MainActivity.this, ChapActivity.class);
                intent.putExtra("data", b);
                startActivity(intent);
            }
        });
    }
    private void openLoginActivityForTruyenView(int truyenIndex) {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivityForResult(intent, REQUEST_LOGIN_FOR_TRUYEN_VIEW);
    }
    private void openChapActivity(TruyenTranh truyenTranh, int truyenIndex) {
        if (isLoggedIn()) {
            Bundle b = new Bundle();
            b.putSerializable("truyen", truyenTranh);
            b.putString("userEmail", getUserEmail());
            Intent intent = new Intent(MainActivity.this, ChapActivity.class);
            intent.putExtra("data", b);
            startActivity(intent);
        } else {
            openLoginActivityForTruyenView(truyenIndex);
        }
    }
    private void performLogout() {
        SharedPreferences.Editor editor = getSharedPreferences("myPrefs", MODE_PRIVATE).edit();
        editor.putBoolean("isLoggedIn", false);
        editor.apply();

        tvUserName.setVisibility(View.GONE);
        btnOpenLogin.setVisibility(View.VISIBLE);
        btnLogout.setVisibility(View.GONE);
    }

    private void openLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivityForResult(intent, REQUEST_LOGIN);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_LOGIN && resultCode == RESULT_OK) {
            String userEmail = data.getStringExtra("userEmail");
            showLoggedInUI(userEmail);
        } else if (requestCode == REQUEST_LOGIN_FOR_TRUYEN_VIEW) {
            if (resultCode == RESULT_OK) {
                String userEmail = data.getStringExtra("userEmail");
                showLoggedInUI(userEmail);
                int truyenIndex = data.getIntExtra("truyenIndex", -1);
                if (truyenIndex != -1) {
                    TruyenTranh truyenTranh = truyenTranhArrayList.get(truyenIndex);
                    openChapActivity(truyenTranh, truyenIndex);
                }
            } else {
                // Đăng nhập không thành công, bạn có thể xử lý ở đây nếu cần
            }
        }
    }
    private void showLoggedInUI(String userEmail) {
        tvUserName.setText(userEmail);
        tvUserName.setVisibility(View.VISIBLE);
        btnOpenLogin.setVisibility(View.GONE);
        btnLogout.setVisibility(View.VISIBLE);
    }

    private boolean isLoggedIn() {
        SharedPreferences preferences = getSharedPreferences("myPrefs", MODE_PRIVATE);
        return preferences.getBoolean("isLoggedIn", false);
    }

    private String getUserEmail() {
        SharedPreferences preferences = getSharedPreferences("myPrefs", MODE_PRIVATE);
        return preferences.getString("Email","Lai");
    }

    @Override
    public void batDau() {
        progressDialog.show();
    }

    @Override
    public void ketThuc(String data) {
        try {
            truyenTranhArrayList.clear();
            ToptruyenTranhArrayList.clear();
            JSONArray arr = new JSONArray(data);
            for (int i = 0; i < arr.length(); i++) {
                JSONObject o = arr.getJSONObject(i);
                TruyenTranh truyenTranh = new TruyenTranh(o);
                truyenTranhArrayList.add(truyenTranh);
                adapter = new TruyenTranhAdapter(this, 0, truyenTranhArrayList);
                gdvDSTruyen.setAdapter(adapter);
            }
            Topadapter = new TruyenTranhAdapter(MainActivity.this, 0, ToptruyenTranhArrayList);
            gdvTopDSTruyen.setAdapter(Topadapter);
            Topadapter.notifyDataSetChanged();
            adapter.notifyDataSetChanged();
            progressDialog.dismiss();
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            // Ẩn hiệu ứng làm mới
            swipeRefreshLayout.setRefreshing(false);
        }
    }


    @Override
    public void biLoi() {
        Toast.makeText(this,"Loi Ket Noi",Toast.LENGTH_SHORT).show();
    }

    public void update(View view) {
        new ApiLayTruyen(this).execute();
    }

}