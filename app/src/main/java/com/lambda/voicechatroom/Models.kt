package com.lambda.voicechatroom

import kotlinx.serialization.Serializable

@Serializable
data class JsonResponse(
    val `data`: Data,
    val message: String
)

@Serializable
data class Data(
    val user: User
)

@Serializable
data class User(
    val accountBalance: String,
    val avatar: String,
    val billingSubcription: String,
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