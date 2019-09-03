package com.lambda.voicechatroom.groups

import android.content.Context
import android.content.Intent
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
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.auth.AuthUI
import com.lambda.voicechatroom.models.Group
import com.lambda.voicechatroom.R
import com.lambda.voicechatroom.login.MainActivity
import kotlinx.android.synthetic.main.content_view_groups.*
import kotlinx.coroutines.*
import java.util.ArrayList

class ViewGroupsActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var context: Context
    private lateinit var viewAdapter: GroupListAdapter
    private lateinit var viewModel: GroupViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_groups)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        context = this

        val fab: FloatingActionButton = this.findViewById(R.id.fab_viewgroups)
        fab.setOnClickListener {
            //                view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show()
//            val fragment = CreateGroupDialogFragment()
////            fragment.show(supportFragmentManager, "groupName")
            showCreateGroupDialog()
        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navView.setNavigationItemSelectedListener(this)


        viewModel = ViewModelProviders.of(this).get(GroupViewModel::class.java)

        val viewManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        recycler_groups.apply {
            setHasFixedSize(false)
            layoutManager = viewManager
        }
    }

    override fun onResume() {
        super.onResume()
        val observer = Observer<ArrayList<Group>> { groups ->
            if (recycler_groups.adapter == null) {
                viewAdapter = GroupListAdapter(this, groups)
                recycler_groups.adapter = viewAdapter
            }
            viewAdapter.notifyDataSetChanged()
        }
        viewModel.initList().observe(this, observer)
        CoroutineScope(Dispatchers.IO + Job()).launch {
            viewModel.refreshGroupsList()
            withContext(Dispatchers.Main) {
                viewAdapter.notifyDataSetChanged()
            }
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
            R.id.nav_logout -> {
                AuthUI.getInstance().signOut(this).addOnCompleteListener {
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
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

    private fun showCreateGroupDialog() {
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
                    viewModel.addGroup(newGroup.toString())
                    withContext(Dispatchers.Main) {
                        viewAdapter.notifyDataSetChanged()
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