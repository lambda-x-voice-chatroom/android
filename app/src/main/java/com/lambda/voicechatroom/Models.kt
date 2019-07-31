package com.lambda.voicechatroom

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class JsonResponse(
    val message: String,
    @SerializedName("data")
    val user: User
)

@Serializable
data class Data(
    @SerializedName("User")
    val user: User
)

@Serializable
data class User(
    val accountBalance: String,
    val avatar: String,
    val billingSubscription: String,
    val callStatus: Boolean,
    val createdAt: String,
    val displayName: String,
    val email: String,
    val firstName: String?,
    val id: String,
    val last4: String?,
    val lastName: String?,
    val phoneNumber: String?,
    val stripeId: String?
)