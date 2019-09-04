package com.lambda.voicechatroom.groups

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.lambda.voicechatroom.models.Group
import com.lambda.voicechatroom.group_details.GroupDetailsActivity
import com.lambda.voicechatroom.calls.InCallActivity
import com.lambda.voicechatroom.R

class GroupListAdapter(private val activity: Activity, private val data: MutableList<Group>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class GroupItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val groupNameView: TextView = view.findViewById(R.id.text_groupitem_name)
        val callButton: ImageButton = view.findViewById(R.id.button_groupitem_call)
        val item: View = view.findViewById(R.id.recycler_view_item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return GroupItemViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.group_item_layout,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, index: Int) {
        val element = data[index]
        val groupHolder = viewHolder as GroupItemViewHolder
        groupHolder.groupNameView.text = element.groupName
        groupHolder.callButton.setOnClickListener {
            val callIntent = Intent(activity, InCallActivity::class.java)
            val gsonString = Gson().toJson(element)
            callIntent.putExtra(InCallActivity.GROUP_KEY, gsonString)
            activity.startActivity(callIntent)
        }
        groupHolder.item.setOnClickListener {
            val groupIntent = Intent(activity, GroupDetailsActivity::class.java)
            groupIntent.putExtra(GroupDetailsActivity.GROUP_DETAILS_KEY, element.groupId)
            activity.startActivity(groupIntent)
        }
    }
}