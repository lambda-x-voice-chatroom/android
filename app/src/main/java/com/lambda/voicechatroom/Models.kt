package com.lambda.voicechatroom

data class JsonResponse<T>(
    val message: String,
    val data: T
)

data class User(
    val accountBalance: String,
    val avatar: String,
    val billingSubscription: String,
    val callStatus: Boolean,
    val createdAt: String,
    var displayName: String,
    var email: String,
    var firstName: String?,
    val id: String,
    val last4: String?,
    var lastName: String?,
    var phoneNumber: String?,
    val stripeId: String?
)

data class GroupList(
    val belonged: List<Group>,
    val invited: List<Group>,
    val owned: List<Group>
)

data class Group(
    val callStatus: Boolean,
    val groupId: Int,
    val groupName: String
)