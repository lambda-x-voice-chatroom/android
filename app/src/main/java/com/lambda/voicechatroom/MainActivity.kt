package com.lambda.voicechatroom

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import kotlinx.serialization.json.Json

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

        test_auth.setOnClickListener {
            if (FirebaseAuth.getInstance().currentUser != null) {
                AuthUI.getInstance().signOut(this)
            }
            startActivityForResult(Intent(context, FirebaseOauthActivity::class.java), AUTH_REQUEST_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == AUTH_REQUEST_CODE) {
                CoroutineScope(Dispatchers.IO + Job()).launch {
                    val user = ApiDao.authUser()
                    withContext(Dispatchers.Main) {
                        val editIntent = Intent(context, EditProfileActivity::class.java)
                        editIntent.putExtra(USER_KEY, Json.stringify(User.serializer(), user))
                        startActivity(Intent(context, EditProfileActivity::class.java))
                        finish()
                    }
                }
            }
        }
    }
}
