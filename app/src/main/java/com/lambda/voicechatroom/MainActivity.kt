package com.lambda.voicechatroom

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.auth.AuthUI
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GetTokenResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    companion object {
        const val AUTH_REQUEST_CODE = 4
    }

    private lateinit var context: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        context = this
        if (FirebaseAuth.getInstance().currentUser != null) {
            AuthUI.getInstance().signOut(this)
        }
        startActivityForResult(Intent(context, FirebaseOauthActivity::class.java), AUTH_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == AUTH_REQUEST_CODE) {
                Toast.makeText(context, "Success!", Toast.LENGTH_SHORT).show()
                    val await: Task<GetTokenResult> = FirebaseAuth.getInstance().getAccessToken(false)
                    println("${await.result?.token}")
            }
        }
    }
}
