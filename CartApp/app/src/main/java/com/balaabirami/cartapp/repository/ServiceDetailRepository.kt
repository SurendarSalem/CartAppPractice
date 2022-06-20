package com.balaabirami.cartapp.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.balaabirami.cartapp.retrofit.RetrofitHelper
import com.balaabirami.cartapp.room.ServiceApi
import com.balaabirami.cartapp.model.Resource
import com.balaabirami.cartapp.model.CartItem
import com.balaabirami.cartapp.model.Employee
import com.balaabirami.cartapp.room.CartItemDao
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ServiceDetailRepository {

    private val _employeesListData = MutableLiveData<Resource<List<Employee>>>(null)
    val employeesListData: LiveData<Resource<List<Employee>>>
        get() = _employeesListData

    companion object {
        private var instance: ServiceDetailRepository? = null

        @Synchronized
        fun getInstance(): ServiceDetailRepository {
            if (instance == null)
                instance = ServiceDetailRepository()
            return instance!!
        }
    }


    fun getAllEmployees() {
        _employeesListData.postValue(Resource.loading(emptyList()))
        val serviceApi = RetrofitHelper.getInstance().create(ServiceApi::class.java)
        // launching a new coroutine
        GlobalScope.launch {
            val result = serviceApi.getEmployees()
            if (result.responseCode == 200) {
                Resource.success(result.data).let { _employeesListData.postValue(it) }
            } else {
                _employeesListData.postValue(Resource.error("Error", emptyList()))
            }

        }
    }

    suspend fun insertCartItem(cartItemDao: com.balaabirami.cartapp.room.CartItemDao, cartItem: CartItem) {
        cartItemDao.insert(cartItem)
    }

    suspend fun updateCartItem(cartItemDao: CartItemDao, cartItem: CartItem) {
        cartItemDao.update(cartItem)
    }
}