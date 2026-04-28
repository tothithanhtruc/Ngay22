package com.example.hitcapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.switchmaterial.SwitchMaterial;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        ImageView btnBack = findViewById(R.id.btnBack);
        LinearLayout btnEditProfile = findViewById(R.id.btnEditProfile);
        LinearLayout btnChangePassword = findViewById(R.id.btnChangePassword);
        SwitchMaterial swNotifications = findViewById(R.id.swNotifications);
        SwitchMaterial swTwoFactor = findViewById(R.id.swTwoFactor);
        Button btnDeleteAccount = findViewById(R.id.btnDeleteAccount);

        // Quay lại
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        // Chỉnh sửa hồ sơ
        if (btnEditProfile != null) {
            btnEditProfile.setOnClickListener(v -> 
                Toast.makeText(this, "Tính năng Chỉnh sửa hồ sơ sẽ sớm ra mắt!", Toast.LENGTH_SHORT).show()
            );
        }

        // Đổi mật khẩu
        if (btnChangePassword != null) {
            btnChangePassword.setOnClickListener(v -> 
                Toast.makeText(this, "Tính năng Đổi mật khẩu sẽ sớm ra mắt!", Toast.LENGTH_SHORT).show()
            );
        }

        // Thông báo
        if (swNotifications != null) {
            swNotifications.setOnCheckedChangeListener((buttonView, isChecked) -> {
                String status = isChecked ? "Bật" : "Tắt";
                Toast.makeText(this, status + " thông báo thành công", Toast.LENGTH_SHORT).show();
            });
        }

        // Xóa tài khoản
        if (btnDeleteAccount != null) {
            btnDeleteAccount.setOnClickListener(v -> {
                new AlertDialog.Builder(this)
                    .setTitle("Xác nhận xóa")
                    .setMessage("Bạn có chắc chắn muốn xóa tài khoản này? Hành động này không thể hoàn tác.")
                    .setPositiveButton("Xóa", (dialog, which) -> {
                        Toast.makeText(this, "Yêu cầu xóa tài khoản đã được gửi!", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("Hủy", null)
                    .show();
            });
        }
    }
}
