package com.lambda.voicechatroom

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.common.api.Api

class GroupListAdapter(val activity: Activity) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class GroupItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val groupNameView: TextView = view.findViewById(R.id.text_groupitem_name)
        val callButton: ImageButton = view.findViewById(R.id.button_groupitem_call)
        val item: View = view.findViewById(R.id.recycler_view_item)
    }

    val data = mutableListOf<Group>()

    init {
        getItems()
    }

    private fun getItems() {
        val groups = Api.getGroups()
        data.addAll(groups)
        activity.runOnUiThread {
//            activity.progress.visibility = View.INVISIBLE
            notifyDataSetChanged()
        }
/*        DataDao.getGroups(object : DataDao.DataCallback {
            override fun callback(success: Boolean, groups: ArrayList<Group>) {
                if (success) {
                    data.addAll(groups)
                    activity.runOnUiThread {
                        activity.progress.visibility = View.INVISIBLE
                        notifyDataSetChanged()
                    }
                } else {
                    activity.runOnUiThread {
                        activity.progress.visibility = View.INVISIBLE
                        activity.text_error.visibility = View.VISIBLE
                        activity.text_error.text = activity.getString(R.string.networkFailure)
                    }
                }
            }
        })*/
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
        groupHolder.groupNameView.text = element.name
        groupHolder.callButton.setOnClickListener {
            Toast.makeText(activity,"Calling not yet implemented", Toast.LENGTH_LONG).show()
        }
        groupHolder.item.setOnClickListener {
            Toast.makeText(activity,"View group details not yet implemented", Toast.LENGTH_LONG).show()
//            val groupIntent = Intent(activity, GroupDetailsActivity::class.java)
//            groupIntent.putExtra(GroupDetailsActivity.GROUP_DETAILS_KEY, element)
//            activity.startActivity(groupIntent)
        }
    }
}