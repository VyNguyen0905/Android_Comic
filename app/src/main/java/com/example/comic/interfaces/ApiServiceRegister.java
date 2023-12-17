package com.example.comic.interfaces;

public interface ApiServiceRegister extends ApiService{
    void ketThucDangKy(String message); // Phương thức để xử lý kết quả đăng ký

    void biLoiDangKy(); // Phương thức để xử lý lỗi khi đăng ký
}
