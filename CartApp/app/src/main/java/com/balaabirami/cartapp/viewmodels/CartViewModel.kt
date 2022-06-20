package com.balaabirami.cartapp.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.balaabirami.cartapp.model.CartItem
import com.balaabirami.cartapp.repository.CartRepository
import com.balaabirami.cartapp.room.CartItemDao

class CartViewModel(application: Application) : AndroidViewModel(application) {

    private var cartRepository = CartRepository.getInstance()

    fun loadServices(wordDao: CartItemDao) {
        cartRepository.getAllService(wordDao)
    }

    fun getCartListData(): LiveData<List<CartItem>> {
        return cartRepository.cartListLiveData
    }
}