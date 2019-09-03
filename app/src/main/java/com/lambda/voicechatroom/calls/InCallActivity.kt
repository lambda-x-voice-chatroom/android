package com.lambda.voicechatroom.calls

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.lambda.voicechatroom.R
import com.lambda.voicechatroom.models.Group
import kotlinx.android.synthetic.main.activity_in_call.*

class InCallActivity : AppCompatActivity() {

    companion object {
        const val GROUP_KEY = "GroupKey"
    }
    private lateinit var context:Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_in_call)
        context = this

        val dataType = object : TypeToken<Group>() {}.type
        val group: Group = Gson().fromJson(intent.getStringExtra(GROUP_KEY), dataType)
        text_incall_groupname.text = group.groupName
        chron_incall_timer.start()

        //Call api and whatever else necessary to make Twilio Voice call.
        Toast.makeText(context, "Calling not yet implemented", Toast.LENGTH_SHORT).show()

        button_incall_hangup.setOnClickListener {
            //Call Twilio API to hang up.
            finish()
        }
    }
}
