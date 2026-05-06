package com.example.hitcapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.google.android.material.switchmaterial.SwitchMaterial;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PaymentActivity extends AppCompatActivity {

    private TextView tvRecipientInfo, tvAddressDetail;
    private SwitchMaterial swDefaultAddress;
    private Address selectedAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        // Ánh xạ View
        ImageView btnBack = findViewById(R.id.btnBack);
        Button btnOrder = findViewById(R.id.btnOrder);
        LinearLayout lnItemsContainer = findViewById(R.id.lnItemsContainer);
        TextView tvSubtotal = findViewById(R.id.tvSubtotal);
        TextView tvTotalFinal = findViewById(R.id.tvTotalFinal);
        TextView tvBottomTotal = findViewById(R.id.tvBottomTotal);
        
        // View địa chỉ
        tvRecipientInfo = findViewById(R.id.tvRecipientInfo);
        tvAddressDetail = findViewById(R.id.tvAddressDetail);
        TextView btnChangeAddress = findViewById(R.id.btnChangeAddress);
        swDefaultAddress = findViewById(R.id.swDefaultAddress);

        // Hiển thị địa chỉ mặc định ban đầu
        selectedAddress = AddressManager.getDefaultAddress();
        updateAddressUI();

        // 🏠 Xử lý thay đổi địa chỉ
        if (btnChangeAddress != null) {
            btnChangeAddress.setOnClickListener(v -> showAddressSelectionDialog());
        }

        // 🔘 Xử lý nút gạt Mặc định
        if (swDefaultAddress != null) {
            swDefaultAddress.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (selectedAddress != null) {
                    selectedAddress.setDefault(isChecked);
                    if (isChecked) {
                        Toast.makeText(this, "Đã đặt làm địa chỉ mặc định", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        // Lấy danh sách sản phẩm từ CartManager (chỉ lấy món được chọn)
        List<CartItem> cartItems = CartManager.getCartItems();
        List<CartItem> selectedItems = new ArrayList<>();
        double subtotal = 0;

        if (lnItemsContainer != null) {
            for (CartItem item : cartItems) {
                if (item.isSelected()) {
                    selectedItems.add(item);
                    View itemView = LayoutInflater.from(this).inflate(R.layout.item_payment, lnItemsContainer, false);
                    
                    ImageView imgProduct = itemView.findViewById(R.id.imgProductPayment);
                    TextView tvName = itemView.findViewById(R.id.tvProductNamePayment);
                    TextView tvQuantity = itemView.findViewById(R.id.tvProductQuantityPayment);
                    TextView tvPrice = itemView.findViewById(R.id.tvProductPricePayment);

                    // Sửa lỗi: Dùng Glide tải ảnh từ URL
                    Glide.with(this)
                            .load(item.getImageUrl())
                            .placeholder(R.drawable.hinh)
                            .into(imgProduct);

                    tvName.setText(item.getName());
                    tvQuantity.setText("Số lượng: " + item.getQuantity());
                    tvPrice.setText(item.getPrice());

                    // Tính toán tiền (Xử lý chuỗi giá)
                    try {
                        String cleanPrice = item.getPrice().replaceAll("[^0-9.]", "");
                        double priceValue = Double.parseDouble(cleanPrice);
                        subtotal += priceValue * item.getQuantity();
                    } catch (Exception e) { 
                        e.printStackTrace(); 
                    }

                    lnItemsContainer.addView(itemView);
                }
            }
        }

        // Hiển thị tổng tiền
        double shippingFee = subtotal > 0 ? (subtotal > 1000 ? 30000 : 10) : 0;
        double total = subtotal + shippingFee;

        if (tvSubtotal != null) tvSubtotal.setText(formatDisplayPrice(subtotal));
        if (tvTotalFinal != null) tvTotalFinal.setText(formatDisplayPrice(total));
        if (tvBottomTotal != null) tvBottomTotal.setText(formatDisplayPrice(total));

        btnBack.setOnClickListener(v -> finish());
        
        final double finalTotal = total;
        btnOrder.setOnClickListener(v -> {
            if (selectedItems.isEmpty()) {
                Toast.makeText(this, "Không có sản phẩm nào để đặt!", Toast.LENGTH_SHORT).show();
                return;
            }
            
            // Lưu đơn hàng vào OrderManager
            String orderId = "ORD" + System.currentTimeMillis();
            String date = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(new Date());
            Order newOrder = new Order(orderId, new ArrayList<>(selectedItems), (long)finalTotal, "WaitConfirm", date);
            OrderManager.addOrder(newOrder);

            // Xóa những sp đã đặt khỏi giỏ hàng
            cartItems.removeAll(selectedItems);

            Intent intent = new Intent(PaymentActivity.this, OrderSuccessActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void updateAddressUI() {
        if (selectedAddress != null) {
            tvRecipientInfo.setText(selectedAddress.getName() + " | " + selectedAddress.getPhone());
            tvAddressDetail.setText(selectedAddress.getDetail());
            swDefaultAddress.setChecked(selectedAddress.isDefault());
        }
    }

    private void showAddressSelectionDialog() {
        List<Address> addresses = AddressManager.getAddressList();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_address_list, null);
        builder.setView(dialogView);

        LinearLayout lnAddressList = dialogView.findViewById(R.id.lnAddressList);
        Button btnAddNew = dialogView.findViewById(R.id.btnAddNewAddress);
        
        AlertDialog dialog = builder.create();

        // Hiển thị danh sách địa chỉ
        for (Address addr : addresses) {
            View itemView = LayoutInflater.from(this).inflate(R.layout.item_address, lnAddressList, false);
            TextView tvInfo = itemView.findViewById(R.id.tvNamePhone);
            TextView tvDetail = itemView.findViewById(R.id.tvAddress);
            RadioButton rb = itemView.findViewById(R.id.rbSelect);

            tvInfo.setText(addr.getName() + " | " + addr.getPhone());
            tvDetail.setText(addr.getDetail());
            rb.setChecked(addr == selectedAddress);

            itemView.setOnClickListener(v -> {
                selectedAddress = addr;
                updateAddressUI();
                dialog.dismiss();
            });
            
            lnAddressList.addView(itemView);
        }

        btnAddNew.setOnClickListener(v -> {
            dialog.dismiss();
            showAddAddressDialog();
        });

        dialog.show();
    }

    private void showAddAddressDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_add_address, null);
        builder.setView(view);

        EditText edtName = view.findViewById(R.id.edtName);
        EditText edtPhone = view.findViewById(R.id.edtPhone);
        EditText edtDetail = view.findViewById(R.id.edtDetail);
        CheckBox cbDefault = view.findViewById(R.id.cbDefault);

        builder.setPositiveButton("Thêm", (dialog, which) -> {
            String name = edtName.getText().toString().trim();
            String phone = edtPhone.getText().toString().trim();
            String detail = edtDetail.getText().toString().trim();
            boolean isDefault = cbDefault.isChecked();

            if (!name.isEmpty() && !phone.isEmpty() && !detail.isEmpty()) {
                Address newAddr = new Address(name, phone, detail, isDefault);
                AddressManager.addAddress(newAddr);
                selectedAddress = newAddr;
                updateAddressUI();
                Toast.makeText(this, "Đã thêm địa chỉ mới!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Hủy", null);
        builder.show();
    }

    private String formatDisplayPrice(double price) {
        if (price > 1000) {
            return String.format(Locale.getDefault(), "%,.0f đ", price).replace(',', '.');
        } else {
            return String.format(Locale.US, "%.2f $", price);
        }
    }
}
