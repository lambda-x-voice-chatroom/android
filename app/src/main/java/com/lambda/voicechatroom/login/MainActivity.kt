package com.lambda.voicechatroom.login

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson
import com.lambda.voicechatroom.network.ApiDao
import com.lambda.voicechatroom.edit_profile.EditProfileActivity
import com.lambda.voicechatroom.R
import com.lambda.voicechatroom.groups.ViewGroupsActivity
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {

    companion object {
        const val AUTH_REQUEST_CODE = 4
        const val USER_KEY = "user_key"
    }

    private lateinit var context: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        context = this
        
        if (FirebaseAuth.getInstance().currentUser != null) {
//            AuthUI.getInstance().signOut(this)
//            startActivityForResult(Intent(context, FirebaseOauthActivity::class.java), AUTH_REQUEST_CODE)
            requestUserData()
        } else {
            startActivityForResult(Intent(context, FirebaseOauthActivity::class.java),
                AUTH_REQUEST_CODE
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == AUTH_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                requestUserData()
            } else {
                Toast.makeText(
                    context,
                    "Failed to log in.  Please check internet connection and try again.",
                    Toast.LENGTH_LONG
                ).show()
                startActivityForResult(Intent(context, FirebaseOauthActivity::class.java),
                    AUTH_REQUEST_CODE
                )
            }
        }
    }

    private fun requestUserData() {
        CoroutineScope(Dispatchers.IO + Job()).launch {
            val user = ApiDao.authUser()
            withContext(Dispatchers.Main) {
                if (user == null) {
                    Toast.makeText(
                        context,
                        "Failed to connect to database.  Please check internet connection and try again.",
                        Toast.LENGTH_LONG
                    ).show()
//                    startActivity(Intent(context, ViewGroupsActivity::class.java))

                    startActivityForResult(
                        Intent(context, FirebaseOauthActivity::class.java),
                        AUTH_REQUEST_CODE
                    )
                } else if (user.firstName == null || user.lastName == null) {
                    val editIntent = Intent(context, EditProfileActivity::class.java)
                    editIntent.putExtra(USER_KEY,  Gson().toJson(user))
                    startActivity(editIntent)
                    finish()
                } else {
                    startActivity(Intent(context, ViewGroupsActivity::class.java))
                }
            }
        }
    }
}
