package com.example.curso

import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import okhttp3.Response
import java.io.IOException


object PhotoService {

    private val BASE_URL="https://embrapa-indices.rj.r.appspot.com"
    var client= OkHttpClient()


    fun postForm(params: File){
        var url = "$BASE_URL/send_new_picture"

        val MEDIA_TYPE_JPG = "imagem/jpg".toMediaType()

        var requestBody=MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("token", "1180")
            .addFormDataPart("file",params.name,
                File(params.path).asRequestBody(MEDIA_TYPE_JPG)
            )
            .build()
        val request=Request.Builder()
            .url(url)
            .post(requestBody)
            .build()

        client.newCall(request).execute().use { response ->
            if(!response.isSuccessful) throw IOException("Não foi possível fazer o upload $response")

            println(response.body!!.string())

        }
    }
}


