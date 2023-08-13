package com.example.helpinghand.Models

import android.widget.Toast

object ShoppingCart {
    private val cartItems: MutableList<CartItem> = mutableListOf()

    fun addItem(item: CartItem) {

        val existingItem = cartItems.find { it.name == item.name }

        if (existingItem == null) {
            cartItems.add(item)
        }
    }
    fun removeItem(item: CartItem) {
        cartItems.remove(item)
    }

    fun getCartItems(): List<CartItem> {
        return cartItems.toList()
    }
}
