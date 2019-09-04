package com.lambda.voicechatroom.models

import com.google.gson.annotations.SerializedName

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
    @SerializedName("id")
    val groupId: Int,
    @SerializedName("name")
    val groupName: String,
    @SerializedName("callStatus")
    val callStatus: Boolean,
    @SerializedName("phoneNumber")
    val phoneNumber: String? = null,
    var accepted: Boolean = true
)


data class GroupDetails(
    val group: Group?,
    val members: List<GroupMember>,
    val owner: GroupMember
)

data class GroupMember(
    val displayName: String,
    val email: String,
    val groupId: Int
)