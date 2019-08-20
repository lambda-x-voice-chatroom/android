package com.lambda.voicechatroom

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_group_details.*
import kotlinx.android.synthetic.main.content_group_details.*
import kotlinx.coroutines.*

class GroupDetailsActivity : AppCompatActivity() {

    companion object {
        const val GROUP_DETAILS_KEY = "GroupID"
    }

    private lateinit var context: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group_details)
        setSupportActionBar(toolbar)

        context = this
        var ownerFlag = false
        val members = mutableListOf<GroupMember>()

        val fab:ImageButton = findViewById(R.id.fab_add_user)

        val groupId = intent.getIntExtra(GROUP_DETAILS_KEY, -1)
        val viewAdapter = MembersListAdapter(this, members, ownerFlag)
        val viewManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        recycler_details_members.apply {
            setHasFixedSize(false)
            layoutManager = viewManager
            adapter = viewAdapter
        }

        // Get details from API
        CoroutineScope(Dispatchers.IO + Job()).launch {
            val groupDetails: GroupDetails? = ApiDao.getGroupById(groupId)
            if (groupDetails != null) {
                // Spin off another request to get user.
                CoroutineScope(Dispatchers.IO + Job()).launch {
                    val user = ApiDao.authUser()
                    if (user != null) {
                        //TODO see if this can be changed to use id, not email.
                        if (user.email == groupDetails.owner.email) {
                            withContext(Dispatchers.Main) {
                                fab.visibility = View.VISIBLE
                                ownerFlag = true
                                viewAdapter.notifyDataSetChanged()
                            }
                        }
                    }
                }
                withContext(Dispatchers.Main) {
                    members.clear()
                    members.addAll(groupDetails.members)
                    viewAdapter.notifyDataSetChanged()
                }
            }
        }


        fab.setOnClickListener { view ->
            //            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show()

        }
    }
}

