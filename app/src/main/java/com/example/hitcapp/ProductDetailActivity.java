package com.example.hitcapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductDetailActivity extends AppCompatActivity {

    private String name, price, imageUrl, description;
    private int productId = -1;

    private ImageView imgProductDetail;
    private TextView tvProductNameDetail, tvProductPriceDetail, tvDescription;
    private Button btnAddToCart, btnBuyNow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        initViews();

        Intent intent = getIntent();
        productId = intent.getIntExtra("productId", -1);

        if (productId != -1) {
            fetchProductDetailFromApi(productId);
        } else {
            name = intent.getStringExtra("productName");
            price = intent.getStringExtra("productPrice");
            imageUrl = intent.getStringExtra("productImage");
            description = intent.getStringExtra("productDescription");
            displayProductData();
        }

        // Luôn load danh sách gợi ý khi mở trang chi tiết
        setupRecommendations(findViewById(R.id.rcvRecommendedDetail));
    }

    private void initViews() {
        imgProductDetail = findViewById(R.id.imgProductDetail);
        tvProductNameDetail = findViewById(R.id.tvProductNameDetail);
        tvProductPriceDetail = findViewById(R.id.tvProductPriceDetail);
        tvDescription = findViewById(R.id.tvProductDescription);
        btnAddToCart = findViewById(R.id.btnAddToCart);
        btnBuyNow = findViewById(R.id.btnBuyNow);
        
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
        
        // Thêm vào giỏ hàng
        btnAddToCart.setOnClickListener(v -> {
            if (name != null) {
                CartItem newItem = new CartItem(name, price, imageUrl, 1);
                CartManager.addItem(newItem);
                Toast.makeText(this, "Đã thêm vào giỏ hàng!", Toast.LENGTH_SHORT).show();
            }
        });

        // Mua ngay: Thêm vào giỏ và đi tới trang giỏ hàng ngay lập tức
        btnBuyNow.setOnClickListener(v -> {
            if (name != null) {
                CartItem newItem = new CartItem(name, price, imageUrl, 1);
                newItem.setSelected(true); // Đánh dấu là đã chọn để thanh toán
                CartManager.addItem(newItem);
                startActivity(new Intent(this, CartActivity.class));
            }
        });
    }

    private void fetchProductDetailFromApi(int id) {
        RetrofitClient.INSTANCE.getInstance().getProductDetail(id).enqueue(new Callback<Product>() {
            @Override
            public void onResponse(Call<Product> call, Response<Product> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Product p = response.body();
                    name = p.getTitle();
                    price = String.valueOf(p.getPrice()) + " $";
                    imageUrl = p.getImage();
                    description = p.getDescription();
                    displayProductData();
                }
            }

            @Override
            public void onFailure(Call<Product> call, Throwable t) {
                Toast.makeText(ProductDetailActivity.this, "Không thể lấy thông tin sản phẩm", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayProductData() {
        tvProductNameDetail.setText(name);
        tvProductPriceDetail.setText(price);
        tvDescription.setText(description);
        Glide.with(this).load(imageUrl).placeholder(R.drawable.hinh).into(imgProductDetail);
    }

    private void setupRecommendations(RecyclerView rcv) {
        if (rcv == null) return;
        RetrofitClient.INSTANCE.getInstance().getProducts().enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Product> products = response.body();
                    // Hiển thị 8 sản phẩm gợi ý
                    ProductAdapter adapter = new ProductAdapter(ProductDetailActivity.this, products.subList(0, Math.min(products.size(), 8)));
                    rcv.setLayoutManager(new LinearLayoutManager(ProductDetailActivity.this, LinearLayoutManager.HORIZONTAL, false));
                    rcv.setAdapter(adapter);
                }
            }
            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Log.e("API_ERROR", "Gợi ý: " + t.getMessage());
            }
        });
    }
}