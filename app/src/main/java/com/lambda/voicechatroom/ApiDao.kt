package com.lambda.voicechatroom

import androidx.annotation.WorkerThread
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GetTokenResult
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.tasks.await


const val baseUrl = "https://lambda-voice-chat-dev.herokuapp.com/api"

@WorkerThread
object ApiDao {
    suspend fun getToken(): String? {
        val await: GetTokenResult = FirebaseAuth.getInstance().getAccessToken(false).await()
        return await.token
    }

    suspend fun authUser(): User? {
        val tokenString = getToken()
        val (success, result) = NetworkAdapter.httpRequest(
            stringUrl = "$baseUrl/auth",
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
            val dataType = object : TypeToken<JsonResponse<User>>() {}.type
            val userJson: JsonResponse<User> = Gson().fromJson(result, dataType)
            user = userJson.data
        }
        return user
    }

    suspend fun getUser(): User? {
        val tokenString = getToken()
        val (success, result) = NetworkAdapter.httpRequest(
            stringUrl = "$baseUrl/user",
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
            val dataType = object : TypeToken<JsonResponse<User>>() {}.type
            val userJson: JsonResponse<User> = Gson().fromJson(result, dataType)
            user = userJson.data
        }
        return user
    }


    suspend fun updateUser(user: User): Boolean {
        val tokenString = getToken()
        val (success, result) = NetworkAdapter.httpRequest(
            stringUrl = "$baseUrl/users",
            requestType = NetworkAdapter.PUT,
            jsonBody = Gson().toJson(user),
            headerProperties = mapOf(
                "Authorization" to "$tokenString",
                "Content-Type" to "application/json",
                "Accept" to "application/json"
            )
        )
        return success
    }

    suspend fun getGroups(): MutableList<Group> {
        val tokenString = getToken()
        val (success, result) = NetworkAdapter.httpRequest(
            stringUrl = "$baseUrl/groups",
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
            val dataType = object : TypeToken<JsonResponse<GroupList>>() {}.type
            val userJson: JsonResponse<GroupList> = Gson().fromJson(result, dataType)
            val groupList = userJson.data
            groups.addAll(groupList.owned)
            groups.addAll(groupList.belonged)
            //TODO Do something with the invited groups.
        }
        return groups
    }

    suspend fun createGroup(name: String): Group? {
        val tokenString = getToken()
        val json = "{\"groupName\":\"$name\"}"
        val (success, result) = NetworkAdapter.httpRequest(
            stringUrl = "$baseUrl/groups",
            requestType = NetworkAdapter.POST,
            jsonBody = json,
            headerProperties = mapOf(
                "Authorization" to "$tokenString",
                "Content-Type" to "application/json",
                "Accept" to "application/json"
            )
        )
        var group: Group? = null
        if (success) {
            val dataType = object : TypeToken<JsonResponse<Group>>() {}.type
            val jsonResponse: JsonResponse<Group> = Gson().fromJson(result, dataType)
            group = jsonResponse.data
        }
        return group
    }

    suspend fun getGroupById(id: Int): GroupDetails? {
        val tokenString = getToken()
        val (success, result) = NetworkAdapter.httpRequest(
            stringUrl = "$baseUrl/groups/$id",
            requestType = NetworkAdapter.GET,
            jsonBody = null,
            headerProperties = mapOf(
                "Authorization" to "$tokenString",
                "Content-Type" to "application/json",
                "Accept" to "application/json"
            )
        )
        var groupDetails: GroupDetails? = null
        if (success) {
            val dataType = object : TypeToken<JsonResponse<GroupDetails>>() {}.type
            val json: JsonResponse<GroupDetails> = Gson().fromJson(result, dataType)
            groupDetails = json.data
        }
        return groupDetails
    }
}




