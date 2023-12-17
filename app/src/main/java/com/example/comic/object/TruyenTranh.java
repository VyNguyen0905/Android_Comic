package com.example.comic.object;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class TruyenTranh implements Serializable {
    private String tenTruyen, tenChap, LinkAnh, id;
    private int soLanClick;
    private boolean daHienThiTrongTop = false;

    public TruyenTranh(JSONObject o) throws JSONException {
        id = o.getString("id");
        tenTruyen = o.getString("tenTruyen");
        tenChap = o.getString("tenChap");
        LinkAnh = o.getString("LinkAnh");

    }
    public TruyenTranh(String tenTruyen, String tenChap, String linkAnh) {
        this.tenTruyen = tenTruyen;
        this.tenChap = tenChap;
        LinkAnh = linkAnh;

    }
    // Phương thức để tăng số lần click khi người dùng click vào truyện

    public boolean isDaHienThiTrongTop() {
        return daHienThiTrongTop;
    }

    public void setDaHienThiTrongTop(boolean daHienThiTrongTop) {
        this.daHienThiTrongTop = daHienThiTrongTop;
    }

    public int getSoLanClick() {
        return soLanClick;
    }
    public void setSoLanClick(int soLanClick) {
        this.soLanClick = soLanClick;
    }
    public void tangSoLanClick() {
        this.soLanClick++;
        Log.d("Số lần click", "Sau khi tăng: " + this.soLanClick);
    }
    // Phương thức để tăng số lần click khi người dùng click vào truyện
    public String getTenTruyen() {
        return tenTruyen;
    }

    public void setTenTruyen(String tenTruyen) {
        this.tenTruyen = tenTruyen;
    }

    public String getTenChap() {
        return tenChap;
    }

    public void setTenChap(String tenChap) {
        this.tenChap = tenChap;
    }

    public String getLinkAnh() {
        return LinkAnh;
    }

    public void setLinkAnh(String linkAnh) {
        LinkAnh = linkAnh;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


}
