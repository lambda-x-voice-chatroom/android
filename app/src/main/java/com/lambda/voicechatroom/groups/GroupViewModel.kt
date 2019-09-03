package com.lambda.voicechatroom.groups

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lambda.voicechatroom.models.Group
import java.util.*

class GroupViewModel: ViewModel() {
    private val repo: GroupRepo =
        GroupRepo()

    fun initList(): MutableLiveData<ArrayList<Group>> {
        return repo.initList()
    }

    suspend fun refreshGroupsList() {
        repo.getGroups()
    }

    suspend fun addGroup(name: String) {
        repo.addGroup(name)
    }
}
