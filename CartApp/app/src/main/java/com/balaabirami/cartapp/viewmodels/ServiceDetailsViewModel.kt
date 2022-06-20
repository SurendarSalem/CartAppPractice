package com.balaabirami.cartapp.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.balaabirami.cartapp.model.Resource
import com.balaabirami.cartapp.model.CartItem
import com.balaabirami.cartapp.model.Employee
import com.balaabirami.cartapp.repository.ServiceDetailRepository
import com.balaabirami.cartapp.room.CartItemDao
import kotlinx.coroutines.launch

class ServiceDetailsViewModel(application: Application) : AndroidViewModel(application) {

    private var serviceDetailRepository = ServiceDetailRepository.getInstance()

    fun getAllEmployees() {
        serviceDetailRepository.getAllEmployees()
    }

    fun getEmployeesListData(): LiveData<Resource<List<Employee>>> {
        return serviceDetailRepository.employeesListData
    }

     fun insertCartItem(cartItemDao: CartItemDao, cartItem: CartItem) {
         viewModelScope.launch {
             serviceDetailRepository.insertCartItem(cartItemDao, cartItem)
         }
    }

    fun updateCartItem(cartItemDao: com.balaabirami.cartapp.room.CartItemDao, cartItem: CartItem) {
        viewModelScope.launch {
            serviceDetailRepository.updateCartItem(cartItemDao, cartItem)
        }
    }
}