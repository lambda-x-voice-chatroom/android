package com.lambda.voicechatroom

import androidx.annotation.WorkerThread
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GetTokenResult
import com.google.gson.Gson
import kotlinx.coroutines.tasks.await
import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.Headers


const val baseUrl = "https://lambda-voice-chat-dev.herokuapp.com"

@WorkerThread
object ApiDao {
    suspend fun getToken(): String? {
        val await: GetTokenResult = FirebaseAuth.getInstance().getAccessToken(false).await()
        return await.token
    }

    suspend fun authUser(): User? {
        val tokenString = getToken()
        val (success, result) = NetworkAdapter.httpRequest(
            stringUrl = "$baseUrl/api/auth",
            requestType = NetworkAdapter.GET,
            jsonBody = null,
            headerProperties = mapOf(
                "Authorization" to "$tokenString",
                "Content-Type" to "application/json",
                "Accept" to "application/json"
            )
        )
        var user: User? = null
        if (success) {
            val gson = Gson()
            val userJson: JsonResponse = gson.fromJson(result, JsonResponse::class.java)
            user = userJson.user
        }
        return user
    }

    suspend fun updateUser(user: User): Boolean {
        val tokenString = getToken()
        val (success, result) = NetworkAdapter.httpRequest(
            stringUrl = "$baseUrl/api/users",
            requestType = NetworkAdapter.PUT,
            jsonBody = Gson().toJson(user),
            headerProperties = mapOf(
                "Authorization" to "$tokenString",
                "Content-Type" to "application/json",
                "Accept" to "application/json"
            )
        )
        return success
/*        return if (success) {
            val gson = Gson()
            val userJson: JsonResponse = gson.fromJson(result, JsonResponse::class.java)
            userJson.user
        } else {
            null
        }*/
    }

    suspend fun getGroups(): MutableList<Group> {
        val tokenString = getToken()
        val (success, result) = NetworkAdapter.httpRequest(
            stringUrl = "$baseUrl/api/auth",
            requestType = NetworkAdapter.GET,
            jsonBody = null,
            headerProperties = mapOf(
                "Authorization" to "$tokenString",
                "Content-Type" to "application/json",
                "Accept" to "application/json"
            )
        )
        val groups = mutableListOf<Group>()
        if (success) {
            val userJson = Gson().fromJson(result, GroupList::class.java)
            groups.addAll(userJson.owned)
            groups.addAll(userJson.belonged)
            //TODO Do something with the invited groups.
        }
        return groups
    }
//    @Headers("{Content-Type: application/json}, {Accept: application/json}")
//    @GET("$baseUrl/api/auth")
//    fun retrofitGet(@HeaderMap headers: Headers):User? {
//
//    }
}

//@WorkerThread
//object Apifactory{
//
//    //Creating Auth Interceptor to add api_key query in front of all the requests.
//    private val authInterceptor = Interceptor {chain->
//        val newUrl = chain.request().url
//            .newBuilder()
//            .build()
//
//        val newRequest = chain.request()
//            .newBuilder()
//            .addHeader("Authorization", "${FirebaseAuth.getInstance().getAccessToken(false).await().token}")
//            .url(newUrl)
//            .build()
//
//        chain.proceed(newRequest)
//    }
//
//    //OkhttpClient for building http request url
//    private val tmdbClient = OkHttpClient().newBuilder()
//        .addInterceptor(authInterceptor)
//        .build()
//
//
//
//    fun retrofit() : Retrofit = Retrofit.Builder()
//        .client(tmdbClient)
//        .baseUrl("$baseUrl/api/auth")
//        .addConverterFactory(MoshiConverterFactory.create())
//        .addCallAdapterFactory(CoroutineCallAdapterFactory())
//        .build()
//
//
//    val tmdbApi : TmdbApi = retrofit().create(TmdbApi::class.java)
//
//}



