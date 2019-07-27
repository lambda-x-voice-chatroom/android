package com.lambda.voicechatroom

data class User(
    val `data`: Data,
    val message: String
)

data class Data(
    val user: UserX
)

data class UserX(
    val accountBalance: String,
    val avatar: String,
    val billingSubcription: String,
    val callStatus: Boolean,
    val createdAt: String,
    val displayName: String,
    val email: String,
    val firstName: Any,
    val id: String,
    val last4: Any,
    val lastName: Any,
    val phoneNumber: Any,
    val stripeId: Any
)