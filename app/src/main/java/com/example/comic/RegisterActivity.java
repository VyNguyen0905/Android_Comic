package com.example.comic;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.comic.api.ApiRegisterAsyncTask;
import com.example.comic.interfaces.ApiServiceRegister;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity implements ApiServiceRegister {
    private EditText edtRegEmail, edtRegPassword, edtRegConfirmPassword;
    private Button btnRegister;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        edtRegEmail = findViewById(R.id.edtRegEmail);
        edtRegPassword = findViewById(R.id.edtRegPassword);
        edtRegConfirmPassword = findViewById(R.id.edtRegConfirmPassword);
        btnRegister = findViewById(R.id.btnRegister);
        progressDialog = new ProgressDialog(RegisterActivity.this);
        progressDialog.setMessage("Đang Đăng Kí Thành Viên");
        progressDialog.setCancelable(false);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edtRegEmail.getText().toString().trim();
                String password = edtRegPassword.getText().toString().trim();
                String confirmPassword = edtRegConfirmPassword.getText().toString().trim();

                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword)) {
                    Toast.makeText(RegisterActivity.this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                } else if (!password.equals(confirmPassword)) {
                    Toast.makeText(RegisterActivity.this, "Mật khẩu và xác nhận mật khẩu không khớp", Toast.LENGTH_SHORT).show();
                } else {
                    // TODO: Gọi hàm đăng ký (có thể là API hoặc phương thức xử lý đăng ký)
                    performRegistration(email, password);
                    progressDialog.show();
                }
            }
        });
    }

    private void performRegistration(String email, String password) {
        String registerUrl = "https://vytien.000webhostapp.com/Dangki.php";
        String jsonData = buildJsonData(email, password);
        // Gọi AsyncTask và truyền ApiServiceRegister
        ApiRegisterAsyncTask registerAsyncTask = new ApiRegisterAsyncTask(this);
        registerAsyncTask.execute(registerUrl, jsonData);
    }
    private String buildJsonData(String email, String password) {
        return "{\"Email\": \"" + email + "\", \"PassWord\": \"" + password + "\"}";
    }
    @Override
    public void ketThucDangKy(String result) {
        // Xử lý kết quả đăng ký thành công
        try {
            JSONObject jsonObject = new JSONObject(result);
            String message1 = jsonObject.getString("message");

            // Kiểm tra kết quả đăng ký
            if (message1.equals("TC")) {
                // Thực hiện các bước tiếp theo và thông báo cho người dùng
                // Ví dụ: Chuyển người dùng đến màn hình đăng nhập
                Intent loginIntent = new Intent(this, MainActivity.class);
                startActivity(loginIntent);
            } else {
                // Đăng ký không thành công, thông báo lỗi cho người dùng
                Toast.makeText(this, "Đăng ký không thành công. " + message1, Toast.LENGTH_SHORT).show();
            }
            progressDialog.dismiss();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void biLoiDangKy() {
        Toast.makeText(this, "Có lỗi khi đăng ký", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void batDau() {
    }

    @Override
    public void ketThuc(String result) {
    }

    @Override
    public void biLoi() {
    }
}
