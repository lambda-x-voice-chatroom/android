package com.lambda.voicechatroom.network

import androidx.annotation.WorkerThread
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GetTokenResult
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.lambda.voicechatroom.models.*
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
            groupList.invited.forEach {
                it.accepted = false
            }
            groups.addAll(groupList.invited)
            groups.addAll(groupList.owned)
            groups.addAll(groupList.belonged)
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
            val dataType = object : TypeToken<Group>() {}.type
            group = Gson().fromJson(result, dataType)
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
            //remove item below and use result once invite is fixed.
            val fixResult = result.replace("\"group\":-1", "\"group\":null")
            val dataType = object : TypeToken<JsonResponse<GroupDetails>>() {}.type
            val json: JsonResponse<GroupDetails> = Gson().fromJson(fixResult, dataType)
            groupDetails = json.data
        }
        return groupDetails
    }

    suspend fun postInvitation(id: Int, emailAddress: String): Boolean {
        val tokenString = getToken()
        val json = "{\"email\":\"$emailAddress\"}"
        val (success, result) = NetworkAdapter.httpRequest(
            stringUrl = "$baseUrl/groups/$id/groupInvitees",
            requestType = NetworkAdapter.POST,
            jsonBody = json,
            headerProperties = mapOf(
                "Authorization" to "$tokenString",
                "Content-Type" to "application/json",
                "Accept" to "application/json"
            )
        )
        return success
    }

    suspend fun acceptInvitation(groupId: Int): Boolean {
        val tokenString = getToken()
        val json = "{\"id\":\"$groupId\"}"
        val (success, result) = NetworkAdapter.httpRequest(
            stringUrl = "$baseUrl/groups/$groupId/invite",
            requestType = NetworkAdapter.POST,
            jsonBody = json,
            headerProperties = mapOf(
                "Authorization" to "$tokenString",
                "Content-Type" to "application/json",
                "Accept" to "application/json"
            )
        )
        return success
    }
}




