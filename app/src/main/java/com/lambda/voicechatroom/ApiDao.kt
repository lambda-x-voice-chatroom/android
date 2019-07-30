package com.lambda.voicechatroom

import androidx.annotation.WorkerThread
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GetTokenResult
import kotlinx.coroutines.tasks.await
import kotlinx.serialization.*
import kotlinx.serialization.json.Json
import retrofit2.http.GET
import retrofit2.http.Query

const val baseUrl = "https://lambda-voice-chat.herokuapp.com"

@WorkerThread
object ApiDao {

    private suspend fun getToken(): String? {
        val await: GetTokenResult = FirebaseAuth.getInstance().getAccessToken(false).await()
        return await.token
    }

    suspend fun getCurrentUser(): String {
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
        return if (success) {
            result
        } else {
            "Unknown"
        }
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
            val userJson: JsonResponse = Json.parse(JsonResponse.serializer(), result)
            user = userJson.data.user
        }
        return user
    }
}

//interface userDao {
//    @GET("$baseUrl/api/auth")
//    suspend fun getUser(@Query("User") user: User) {
//
//    }
//}