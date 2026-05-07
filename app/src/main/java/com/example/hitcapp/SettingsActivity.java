package com.example.hitcapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

        // Ánh xạ các View từ layout
        ImageView btnBack = findViewById(R.id.btnBack);
        LinearLayout btnEditProfile = findViewById(R.id.btnEditProfile);
        LinearLayout btnChangePassword = findViewById(R.id.btnChangePassword);
        SwitchMaterial swNotifications = findViewById(R.id.swNotifications);
        SwitchMaterial swTwoFactor = findViewById(R.id.swTwoFactor);
        Button btnDeleteAccount = findViewById(R.id.btnDeleteAccount);

        // 🔙 Thiết lập sự kiện cho nút Quay lại
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> {
                finish(); // Đóng Activity hiện tại để quay về trang Profile
            });
        }

        // Chỉnh sửa hồ sơ
        if (btnEditProfile != null) {
            btnEditProfile.setOnClickListener(v -> {
                startActivity(new Intent(SettingsActivity.this, EditProfileActivity.class));
            });
        }

        // Đổi mật khẩu
        if (btnChangePassword != null) {
            btnChangePassword.setOnClickListener(v -> showChangePasswordDialog());
        }

        // Cài đặt thông báo
        if (swNotifications != null) {
            swNotifications.setChecked(UserManager.isNotificationsEnabled(this));
            swNotifications.setOnCheckedChangeListener((buttonView, isChecked) -> {
                UserManager.setNotificationsEnabled(this, isChecked);
                Toast.makeText(this, (isChecked ? "Bật" : "Tắt") + " thông báo thành công", Toast.LENGTH_SHORT).show();
            });
        }

        // Xóa tài khoản
        if (btnDeleteAccount != null) {
            btnDeleteAccount.setOnClickListener(v -> {
                new AlertDialog.Builder(this)
                    .setTitle("Xác nhận xóa")
                    .setMessage("Bạn có chắc chắn muốn xóa tài khoản? Dữ liệu không thể khôi phục.")
                    .setPositiveButton("Xóa", (dialog, which) -> {
                        Toast.makeText(this, "Yêu cầu đã gửi!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    })
                    .setNegativeButton("Hủy", null)
                    .show();
            });
        }
    }

    private void showChangePasswordDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Thay đổi mật khẩu");
        
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_add_address, null);
        EditText edtOld = view.findViewById(R.id.edtName);
        EditText edtNew = view.findViewById(R.id.edtPhone);
        EditText edtConfirm = view.findViewById(R.id.edtDetail);
        
        if (edtOld != null) {
            edtOld.setHint("Mật khẩu hiện tại");
            edtOld.setInputType(android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD);
        }
        if (edtNew != null) {
            edtNew.setHint("Mật khẩu mới");
            edtNew.setInputType(android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD);
        }
        if (edtConfirm != null) {
            edtConfirm.setHint("Xác nhận mật khẩu mới");
            edtConfirm.setInputType(android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD);
        }
        
        builder.setView(view);
        builder.setPositiveButton("Cập nhật", (dialog, which) -> {
            String newPass = edtNew != null ? edtNew.getText().toString() : "";
            String confirmPass = edtConfirm != null ? edtConfirm.getText().toString() : "";
            
            if (newPass.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập mật khẩu mới", Toast.LENGTH_SHORT).show();
            } else if (!newPass.equals(confirmPass)) {
                Toast.makeText(this, "Mật khẩu xác nhận không khớp", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Đổi mật khẩu thành công!", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Hủy", null);
        builder.show();
    }
}
