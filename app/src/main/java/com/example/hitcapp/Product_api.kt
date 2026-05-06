package com.example.hitcapp

// Class này để hứng dữ liệu hoa từ API về nè
data class Product_api(
    val id: Int,
    val title: String,      // Tên loại hoa
    val price: Double,      // Giá tiền
    val description: String, // Ý nghĩa của hoa
    val image: String       // Link hình ảnh hoa
)