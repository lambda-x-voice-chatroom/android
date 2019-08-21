package com.lambda.voicechatroom

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
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog


class GroupDetailsActivity : AppCompatActivity() {

    companion object {
        const val GROUP_DETAILS_KEY = "GroupID"
        const val RESULT_PICK_CONTACT = 47
    }

    private lateinit var context: Context
    private lateinit var dialogView:View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group_details)
        setSupportActionBar(toolbar)

        context = this
        var ownerFlag = false
        val members = mutableListOf<GroupMember>()

        val fab: ImageButton = findViewById(R.id.fab_add_user)

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


        fab.setOnClickListener {
            dialogView = layoutInflater.inflate(R.layout.invite_dialog, null)
            showInviteDialog(dialogView)
        }
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == RESULT_PICK_CONTACT) {
//                RESULT_PICK_CONTACT -> contactPicked(data)

                var emailAddress = ""
                val uri = data?.data
                    val cursor: Cursor? = contentResolver.query(uri, null, null, null, null)

                if (cursor != null) {
                    if (cursor.moveToFirst()) {
                        val emailIndex =
                            cursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS)
                        emailAddress = cursor.getString(emailIndex)
                        val emailEditText:EditText = dialogView.findViewById(R.id.edit_invite_email)
                        emailEditText.setText(emailAddress)
//                        Toast.makeText(context, emailAddress, Toast.LENGTH_SHORT).show()
                    }
                    cursor.close()
                }
            }
        } else {
            Log.e("ContactFragment", "Failed to pick contact")
        }
    }

    fun showInviteDialog(view:View) {
        val builder = AlertDialog.Builder(context)

        val emailEditText:EditText = view.findViewById(R.id.edit_invite_email)

        val contactButton:Button = view.findViewById(R.id.button_invite_contact)

        contactButton.setOnClickListener {
            val contactPickerIntent = Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Email.CONTENT_URI)
            startActivityForResult(contactPickerIntent, RESULT_PICK_CONTACT)
        }

        builder.setView(view)

        // set up the ok button
        builder.setPositiveButton(android.R.string.ok) { dialog, p1 ->
            val newGroup = emailEditText.text
            var isValid = true
            if (newGroup.isBlank()) {
                emailEditText.error = getString(R.string.validation_empty)
                isValid = false
            }

            if (isValid) {

                CoroutineScope(Dispatchers.IO + Job()).launch {
                    //Send invitation to backend via ApiDao
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, "Not yet implemented.", Toast.LENGTH_SHORT).show()
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

