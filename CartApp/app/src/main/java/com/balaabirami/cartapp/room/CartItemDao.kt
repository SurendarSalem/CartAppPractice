package com.balaabirami.cartapp.room

import androidx.room.*
import com.balaabirami.cartapp.model.CartItem
import kotlinx.coroutines.flow.Flow

@Dao
interface CartItemDao {
    @Query("SELECT * FROM cart_item_table")
    fun getAllCartItem(): Flow<List<CartItem>>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(cartItem: CartItem)

    @Update
    suspend fun update(cartItem: CartItem)
}