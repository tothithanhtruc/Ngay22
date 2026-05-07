package com.example.hitcapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        android.view.View mainView = findViewById(R.id.main);
        if (mainView != null) {
            ViewCompat.setOnApplyWindowInsetsListener(mainView, (insetsView, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                insetsView.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });
        }

        // Ánh xạ View
        EditText etEmail = findViewById(R.id.etEmail);
        EditText etUsername = findViewById(R.id.etUsername);
        EditText etPassword = findViewById(R.id.etPassword);
        EditText etConfirmPassword = findViewById(R.id.etConfirmPassword);
        Button btnSignUp = findViewById(R.id.btnSignUp);
        TextView btnLogin = findViewById(R.id.btnLogin);

        // Xử lý khi nhấn Đăng ký
        if (btnSignUp != null) {
            btnSignUp.setOnClickListener(v -> {
                String email = etEmail.getText().toString().trim();
                String user = etUsername.getText().toString().trim();
                String pass = etPassword.getText().toString().trim();
                String confirm = etConfirmPassword.getText().toString().trim();

                // 🟢 Kiểm tra xem đã nhập đủ thông tin chưa
                if (email.isEmpty() || user.isEmpty() || pass.isEmpty() || confirm.isEmpty()) {
                    Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin để đăng ký!", Toast.LENGTH_SHORT).show();
                } else if (!pass.equals(confirm)) {
                    Toast.makeText(this, "Mật khẩu xác nhận không khớp!", Toast.LENGTH_SHORT).show();
                } else {
                    // Lưu thông tin người dùng vào UserManager để hiển thị ở Profile
                    UserManager.saveUserName(this, user);
                    UserManager.saveUserEmail(this, email);
                    
                    Toast.makeText(this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
                    
                    // Chuyển sang màn hình chính
                    Intent intent = new Intent(RegisterActivity.this, HomeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }
            });
        }

        if (btnLogin != null) {
            btnLogin.setOnClickListener(v -> finish());
        }
    }
}
