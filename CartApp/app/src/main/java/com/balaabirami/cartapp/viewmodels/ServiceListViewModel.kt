package com.balaabirami.cartapp.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.balaabirami.cartapp.model.Resource
import com.balaabirami.cartapp.model.Service
import com.balaabirami.cartapp.repository.ServiceRepository

class ServiceListViewModel(application: Application) : AndroidViewModel(application) {

    private var serviceRepository = ServiceRepository.getInstance()

    fun loadServices(){
        serviceRepository.getAllService()
    }

    fun getServiceListData(): LiveData<Resource<List<Service>>> {
        return serviceRepository.servicesListData
    }
}