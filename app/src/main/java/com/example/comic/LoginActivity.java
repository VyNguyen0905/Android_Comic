package com.example.comic;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.comic.api.ApiLayUser;
import com.example.comic.interfaces.ApiService;
import com.example.comic.object.Login;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity implements ApiService {
    private EditText edtEmail, edtPassword;
    private Button btnLogin, btnOpenRegister;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnOpenRegister = findViewById(R.id.btnOpenRegister);
        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setMessage("Đang Đăng Nhập Tài Khoản!!");
        progressDialog.setCancelable(false);
        btnOpenRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRegisterActivity();
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edtEmail.getText().toString().trim();
                String password = edtPassword.getText().toString().trim();
                // Kiểm tra thông tin đăng nhập và thực hiện đăng nhập
                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                    Toast.makeText(LoginActivity.this, "Vui lòng nhập đầy đủ thông tin đăng nhập.", Toast.LENGTH_SHORT).show();
                } else {
                    // Gọi API để kiểm tra thông tin đăng nhập
                    progressDialog.show();
                    new ApiLayUser(LoginActivity.this).execute();
                }
            }
        });
    }
    private void openRegisterActivity() {
        Intent intent1 = new Intent(this, RegisterActivity.class);
        startActivity(intent1);
    }

    private boolean isValidLogin(String email, String password, JSONArray accountArray) {
        try {
            // Kiểm tra thông tin đăng nhập

            for (int i = 0; i < accountArray.length(); i++) {
                JSONObject accountObject = accountArray.getJSONObject(i);

                // Trích xuất thông tin
                String storedEmail = accountObject.getString("Email");
                String storedPassword = accountObject.getString("PassWord");

                // Xác thực thông tin đăng nhập
                if (TextUtils.equals(email, storedEmail) && TextUtils.equals(password, storedPassword)) {
                    return true; // Nếu tìm thấy tài khoản khớp, trả về true
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void ketThuc(String data) {
        try {
            JSONArray array = new JSONArray(data);
            // Kiểm tra thông tin đăng nhập
            if (isValidLogin(edtEmail.getText().toString().trim(), edtPassword.getText().toString().trim(), array)) {
                loginSuccess();
            } else {
                Toast.makeText(LoginActivity.this, "Đăng nhập thất bại. Kiểm tra lại email và mật khẩu.", Toast.LENGTH_SHORT).show();
            }
            progressDialog.dismiss();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void loginSuccess() {
        // Lưu thông tin đăng nhập (ví dụ: tên người đăng nhập) vào SharedPreferences hoặc cơ sở dữ liệu
        SharedPreferences.Editor editor = getSharedPreferences("myPrefs", MODE_PRIVATE).edit();
        editor.putBoolean("isLoggedIn", true);
        editor.putString("Email", edtEmail.getText().toString().trim());
        editor.apply();

        // Đặt kết quả là RESULT_OK để thông báo cho MainActivity biết là đăng nhập thành công
        Intent resultIntent = new Intent();
        resultIntent.putExtra("userEmail", edtEmail.getText().toString().trim());
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    @Override
    public void batDau() {
        // Có thể thêm xử lý khi bắt đầu gọi API
    }

    @Override
    public void biLoi() {
        // Có thể thêm xử lý khi gặp lỗi trong quá trình gọi API
    }
}
