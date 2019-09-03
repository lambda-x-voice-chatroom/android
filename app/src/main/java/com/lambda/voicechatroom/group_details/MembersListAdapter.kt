package com.lambda.voicechatroom.group_details

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.lambda.voicechatroom.models.GroupMember
import com.lambda.voicechatroom.R

class MembersListAdapter(val activity: Activity, val data: MutableList<GroupMember>, val ownerFlag: Boolean) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class GroupItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameView: TextView = view.findViewById(R.id.text_member_name)
        val emailView: TextView = view.findViewById(R.id.text_member_email)
        val deleteButton: ImageButton = view.findViewById(R.id.button_member_delete)
        val item: View = view.findViewById(R.id.recycler_details_members)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return GroupItemViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.member_item_layout,
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
        groupHolder.nameView.text = element.displayName
        groupHolder.emailView.text = element.email
        groupHolder.deleteButton.isEnabled = ownerFlag //Disable delete button if not owner.
        groupHolder.deleteButton.setOnClickListener {
            Toast.makeText(activity, "Deleting members not yet implemented", Toast.LENGTH_LONG).show()
        }
    }
}