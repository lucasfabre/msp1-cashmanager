package com.epitech.cashmanager.beans

import com.google.gson.annotations.SerializedName

/**
 * Product
 *
 * This class represent an product
 *
 * @property String define description of the product
 * @property Int define id of the product
 * @property String define name of the product
 * @property String define price of the product
 * @property Int define photos of the product
 */

data class Product(

    @SerializedName("description")
    var description: String? = null,

    @SerializedName("id")
    var id: Int? = null,

    @SerializedName("name")
    var name: String? = null,

    @SerializedName("price")
    var price: String? = null,

    @SerializedName("photos")
    var photos: Int? = null

)