package fr.cashmanager.services

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface CallWSExempleService {
    /**
     * @GET declares an HTTP GET request
     * @Path("product") annotation on the productId parameter marks it as a
     * replacement for the {product} placeholder in the @GET path
     */
    /*
    @GET("/products/{product}")
    fun getProduct(@Path("product") productId: String): Call<Product>
     */
}