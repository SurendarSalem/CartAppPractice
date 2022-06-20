// Retrofit interface
package com.balaabirami.cartapp.room

import com.balaabirami.cartapp.model.Resource
import com.balaabirami.cartapp.model.Employee
import com.balaabirami.cartapp.model.Service
import retrofit2.http.GET

interface ServiceApi {
    @GET("challenge-services")
    suspend fun getServices(): Resource<List<Service>>

    @GET("challenge-employees")
    suspend fun getEmployees(): Resource<List<Employee>>
}
