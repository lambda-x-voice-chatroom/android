package com.lambda.voicechatroom.group_details

import android.app.Activity
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
import android.provider.ContactsContract
import android.content.Intent
import android.database.Cursor
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.lambda.voicechatroom.models.GroupDetails
import com.lambda.voicechatroom.models.GroupMember
import com.lambda.voicechatroom.R
import com.lambda.voicechatroom.network.ApiDao


class GroupDetailsActivity : AppCompatActivity() {

    companion object {
        const val GROUP_DETAILS_KEY = "GroupID"
        const val RESULT_PICK_CONTACT = 47
    }

    private lateinit var context: Context
    private lateinit var dialogView: View
    private var groupDetails: GroupDetails? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group_details)
        setSupportActionBar(toolbar)

        context = this
        var ownerFlag = false
        val members = mutableListOf<GroupMember>()

        val fab: ImageButton = findViewById(R.id.fab_add_user)

        val groupId = intent.getIntExtra(GROUP_DETAILS_KEY, -1)
        val viewAdapter =
            MembersListAdapter(this, members, ownerFlag)
        val viewManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        recycler_details_members.apply {
            setHasFixedSize(false)
            layoutManager = viewManager
            adapter = viewAdapter
        }

        // Get details from API
        CoroutineScope(Dispatchers.IO + Job()).launch {
            groupDetails = ApiDao.getGroupById(groupId)
            if (groupDetails != null) {
                val details: GroupDetails = groupDetails as GroupDetails
                // Spin off another request to get user.
                CoroutineScope(Dispatchers.IO + Job()).launch {
                    val user = ApiDao.authUser()
                    if (user != null) {
                        //TODO see if this can be changed to use id, not email.
                        if (user.email == details.owner.email) {
                            withContext(Dispatchers.Main) {
                                fab.visibility = View.VISIBLE
                                ownerFlag = true
                                viewAdapter.notifyDataSetChanged()
                            }
                        }
                    }
                }
                withContext(Dispatchers.Main) {
                    if (details.group != null) {
                        text_details_name.text = details.group.groupName
                    } else {
                        button_details_accept.visibility = View.VISIBLE
                    }
                    text_details_owner.text = details.owner.displayName
                    text_details_email.text = details.owner.email
                    members.clear()
                    members.addAll(details.members)
                    viewAdapter.notifyDataSetChanged()
                }
            }
        }


        fab.setOnClickListener {
            dialogView = layoutInflater.inflate(R.layout.invite_dialog, null)
            showInviteDialog(dialogView)
        }

        button_details_accept.setOnClickListener {
            CoroutineScope(Dispatchers.IO + Job()).launch {
                if (ApiDao.acceptInvitation(groupId)) {
                    groupDetails = ApiDao.getGroupById(groupId)
                    withContext(Dispatchers.Main) {
                        button_details_accept.visibility = View.GONE
                        text_details_name.text = groupDetails?.group?.groupName
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, "Failed to join group.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == RESULT_PICK_CONTACT) {
                val uri = data?.data
                if (uri != null) {
                    val cursor: Cursor? = contentResolver.query(uri, null, null, null, null)
                    if (cursor != null) {
                        if (cursor.moveToFirst()) {
                            val emailIndex =
                                cursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS)
                            val emailAddress = cursor.getString(emailIndex)
                            val emailEditText: EditText =
                                dialogView.findViewById(R.id.edit_invite_email)
                            emailEditText.setText(emailAddress)
                        }
                        cursor.close()
                    }
                }
            }
        } else {
            Toast.makeText(context, "No contact was selected.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showInviteDialog(view: View) {
        val builder = AlertDialog.Builder(context)

        val emailEditText: EditText = view.findViewById(R.id.edit_invite_email)

        val contactButton: Button = view.findViewById(R.id.button_invite_contact)

        contactButton.setOnClickListener {
            val contactPickerIntent =
                Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Email.CONTENT_URI)
            startActivityForResult(contactPickerIntent,
                RESULT_PICK_CONTACT
            )
        }

        builder.setView(view)

        // set up the ok button
        builder.setPositiveButton(android.R.string.ok) { dialog, p1 ->
            val newGroup = emailEditText.text
            var isValid = true
            if (newGroup.isBlank()) {
                emailEditText.error = "Enter a valid email address"
                isValid = false
            }

            if (isValid) {

                CoroutineScope(Dispatchers.IO + Job()).launch {
                    //Send invitation to backend via ApiDao
                    val success = ApiDao.postInvitation(
                        groupDetails?.group?.groupId!!,
                        emailEditText.text.toString()
                    )
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, "Added", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            if (isValid) {
                dialog.dismiss()
            }
        }

        builder.setNegativeButton(android.R.string.cancel) { dialog, p1 ->
            dialog.cancel()
        }

        builder.show()
    }

}

