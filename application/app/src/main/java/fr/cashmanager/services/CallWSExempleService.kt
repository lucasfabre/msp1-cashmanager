package fr.cashmanager.services

import Retrofit

interface CallWSExempleService {
    /**
     * @GET declares an HTTP GET request
     * @Path("product") annotation on the productId parameter marks it as a
     * replacement for the {product} placeholder in the @GET path
     */
    @GET("/products/{product}")
    fun getProduct(@Path("product") productId: String): Call<Product>
}