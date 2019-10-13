package fr.cashmanager.services

import retrofit2.Retrofit

class Retrofit {
    var retrofit = Retrofit.Builder()
        .baseUrl("https://[API URL].com/")
        .build()

    var service = retrofit.create(CallWSExempleService::class.java)
}