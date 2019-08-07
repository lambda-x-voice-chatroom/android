package com.lambda.voicechatroom

import android.content.Context
import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import androidx.core.view.GravityCompat
import androidx.appcompat.app.ActionBarDrawerToggle
import android.view.MenuItem
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.view.Menu
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.content_view_groups.*
import kotlinx.coroutines.*
import okhttp3.internal.notify

class ViewGroupsActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var context: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_groups)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        context = this

        val fab: FloatingActionButton = findViewById(R.id.fab_viewgroups)
        fab.setOnClickListener {
//                view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show()
//            val fragment = CreateGroupDialogFragment()
////            fragment.show(supportFragmentManager, "groupName")
            showCreateGroupDialog()
        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navView.setNavigationItemSelectedListener(this)

        val viewAdapter = GroupListAdapter(this)
        val viewManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        recycler_groups.apply {
            setHasFixedSize(false)
            layoutManager = viewManager
            adapter = viewAdapter
        }
    }


    override fun onBackPressed() {
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.view_groups, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent context in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_home -> {
                // Handle the camera action
            }
            R.id.nav_gallery -> {

            }
            R.id.nav_slideshow -> {

            }
            R.id.nav_tools -> {

            }
            R.id.nav_share -> {

            }
            R.id.nav_send -> {

            }
        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    fun showCreateGroupDialog() {
//        val context = this
        val builder = AlertDialog.Builder(context)
//        builder.setTitle("New Group")

        val view = layoutInflater.inflate(R.layout.group_name_dialog, null)

        val groupEditText = view.findViewById(R.id.edit_dialog_groupname) as EditText

        builder.setView(view)

        // set up the ok button
        builder.setPositiveButton(android.R.string.ok) { dialog, p1 ->
            val newGroup = groupEditText.text
            var isValid = true
            if (newGroup.isBlank()) {
                groupEditText.error = getString(R.string.validation_empty)
                isValid = false
            }

            if (isValid) {

                CoroutineScope(Dispatchers.IO + Job()).launch {
                    val group = ApiDao.createGroup(newGroup.toString())
                    withContext(Dispatchers.Main) {
                        recycler_groups.notify()
                    }
                }
                Toast.makeText(context, "$newGroup", Toast.LENGTH_LONG).show()
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

/*class CreateGroupDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            // Get the layout inflater
            val inflater = requireActivity().layoutInflater

            // Inflate and set the layout for the dialog
            // Pass null as the parent view because its going in the dialog layout
            builder.setView(inflater.inflate(R.layout.group_name_dialog, null))
                // Add action buttons
                .setPositiveButton(R.string.OK,
                    DialogInterface.OnClickListener { dialog, id ->
                        CoroutineScope(Dispatchers.IO + Job()).launch {
                            val edt = R.id.edit_dialog_groupname
                            val groupName = e.text.toString()
                         val group = ApiDao.createGroup(groupName)
                        }
                        })
                .setNegativeButton(R.string.cancel,
                    DialogInterface.OnClickListener { dialog, id ->
                        getDialog().cancel()
                    })
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}*/
