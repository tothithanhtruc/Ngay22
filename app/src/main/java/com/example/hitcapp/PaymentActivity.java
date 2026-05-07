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
import android.widget.RadioGroup;
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
    private RadioGroup rgPaymentMethod;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        // Ánh xạ View chính
        ImageView btnBack = findViewById(R.id.btnBack);
        Button btnOrder = findViewById(R.id.btnOrder);
        LinearLayout lnItemsContainer = findViewById(R.id.lnItemsContainer);
        TextView tvSubtotal = findViewById(R.id.tvSubtotal);
        TextView tvTotalFinal = findViewById(R.id.tvTotalFinal);
        TextView tvBottomTotal = findViewById(R.id.tvBottomTotal);
        rgPaymentMethod = findViewById(R.id.rgPaymentMethod);
        
        tvRecipientInfo = findViewById(R.id.tvRecipientInfo);
        tvAddressDetail = findViewById(R.id.tvAddressDetail);
        TextView btnChangeAddress = findViewById(R.id.btnChangeAddress);
        swDefaultAddress = findViewById(R.id.swDefaultAddress);

        selectedAddress = AddressManager.getDefaultAddress();
        updateAddressUI();

        if (btnChangeAddress != null) {
            btnChangeAddress.setOnClickListener(v -> showAddressSelectionDialog());
        }

        List<CartItem> cartItems = CartManager.getCartItems();
        List<CartItem> selectedItems = new ArrayList<>();
        double subtotalValue = 0;

        if (lnItemsContainer != null) {
            lnItemsContainer.removeAllViews();
            for (CartItem item : cartItems) {
                if (item.isSelected()) {
                    selectedItems.add(item);
                    View itemView = LayoutInflater.from(this).inflate(R.layout.item_payment, lnItemsContainer, false);
                    
                    ImageView imgProduct = itemView.findViewById(R.id.imgProductPayment);
                    TextView tvName = itemView.findViewById(R.id.tvProductNamePayment);
                    TextView tvUnitPrice = itemView.findViewById(R.id.tvUnitPricePayment);
                    TextView tvQuantity = itemView.findViewById(R.id.tvProductQuantityPayment);
                    TextView tvTotalPrice = itemView.findViewById(R.id.tvProductPricePayment);

                    Glide.with(this)
                            .load(item.getImageUrl())
                            .placeholder(R.drawable.hinh)
                            .into(imgProduct);

                    tvName.setText(item.getName());
                    tvQuantity.setText("Số lượng: " + item.getQuantity());
                    tvUnitPrice.setText("Đơn giá: " + item.getPrice());

                    try {
                        double unitPriceValue = parsePrice(item.getPrice());
                        double lineTotal = unitPriceValue * item.getQuantity();
                        subtotalValue += lineTotal;
                        tvTotalPrice.setText(formatDisplayPrice(lineTotal));
                    } catch (Exception e) {
                        tvTotalPrice.setText(item.getPrice());
                    }

                    lnItemsContainer.addView(itemView);
                }
            }
        }

        double shippingFee = subtotalValue > 0 ? (subtotalValue > 1000 ? 30000 : 5) : 0;
        double finalTotal = subtotalValue + shippingFee;

        if (tvSubtotal != null) tvSubtotal.setText(formatDisplayPrice(subtotalValue));
        if (tvTotalFinal != null) tvTotalFinal.setText(formatDisplayPrice(finalTotal));
        if (tvBottomTotal != null) tvBottomTotal.setText(formatDisplayPrice(finalTotal));

        btnBack.setOnClickListener(v -> finish());
        
        btnOrder.setOnClickListener(v -> {
            if (selectedItems.isEmpty()) {
                Toast.makeText(this, "Giỏ hàng trống!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Lấy phương thức thanh toán
            String method = "COD";
            if (rgPaymentMethod != null && rgPaymentMethod.getCheckedRadioButtonId() == R.id.rbEWallet) {
                method = "E-Wallet";
            }
            
            Order newOrder = new Order(
                "ORD" + System.currentTimeMillis(),
                new ArrayList<>(selectedItems),
                (long)finalTotal,
                "WaitConfirm",
                new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(new Date())
            );
            // Lưu thông tin (có thể mở rộng class Order để lưu 'method' nếu cần)
            OrderManager.addOrder(newOrder);
            CartManager.clearSelectedItems();

            startActivity(new Intent(PaymentActivity.this, OrderSuccessActivity.class));
            finish();
        });
    }

    private double parsePrice(String priceStr) {
        try {
            if (priceStr.contains("đ") || priceStr.contains("VNĐ")) {
                return Double.parseDouble(priceStr.replace(".", "").replaceAll("[^0-9]", ""));
            } else {
                return Double.parseDouble(priceStr.replaceAll("[^0-9.]", ""));
            }
        } catch (Exception e) {
            return 0;
        }
    }

    private void updateAddressUI() {
        if (selectedAddress != null) {
            tvRecipientInfo.setText(selectedAddress.getName() + " | " + selectedAddress.getPhone());
            tvAddressDetail.setText(selectedAddress.getDetail());
            if (swDefaultAddress != null) swDefaultAddress.setChecked(selectedAddress.isDefault());
        }
    }

    private String formatDisplayPrice(double price) {
        if (price >= 1000) {
            return String.format(Locale.getDefault(), "%,.0f đ", price).replace(',', '.');
        } else {
            return String.format(Locale.US, "%.2f $", price);
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
        builder.setPositiveButton("Thêm", (dialog, which) -> {
            String name = edtName.getText().toString().trim();
            String phone = edtPhone.getText().toString().trim();
            String detail = edtDetail.getText().toString().trim();
            if (!name.isEmpty() && !phone.isEmpty() && !detail.isEmpty()) {
                Address newAddr = new Address(name, phone, detail, false);
                AddressManager.addAddress(newAddr);
                selectedAddress = newAddr;
                updateAddressUI();
            }
        });
        builder.setNegativeButton("Hủy", null);
        builder.show();
    }
}
