package com.example.optimus158.virtualcontactmanager;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.github.clans.fab.*;

import java.util.List;

/**
 * Main Screen Class which shows all the UI Views and sets the data to them
 */
public class Contacts extends AppCompatActivity implements View.OnClickListener {

    private DBHelper helper;
    private RecyclerView vRecyclerView, vRecyclerViewDrawer;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_contacts);

        vRecyclerViewDrawer = (RecyclerView) findViewById(R.id.recyclerViewDrawer);
        vRecyclerViewDrawer.setHasFixedSize(true);
        vRecyclerViewDrawer.setLayoutManager(new LinearLayoutManager(this));

        vRecyclerView = (RecyclerView) findViewById(R.id.recyclerViewContacts);
        vRecyclerView.setHasFixedSize(true);
        vRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        FloatingActionButton fabContact = (FloatingActionButton) findViewById(R.id.fabContact);
        FloatingActionButton fabGroup = (FloatingActionButton) findViewById(R.id.fabGroup);

        fabContact.setOnClickListener(this);
        fabContact.setColorNormal(R.color.colorAccent);
        fabGroup.setOnClickListener(this);
        fabGroup.setColorNormal(R.color.colorAccent);

        helper = new DBHelper(this);
        setupContactsList();
        setupGroupsList();

        Toolbar toolBar = (Toolbar) findViewById(R.id.toolbar);
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolBar, R.string.drawer_open, R.string.drawer_close) {
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu();
                syncState();
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
                syncState();
            }
        };
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        setSupportActionBar(toolBar);
        getSupportActionBar().setHomeButtonEnabled(true);
        actionBarDrawerToggle.syncState();
    }

    /**
     * Displays the contacts listContacts
     */
    private void setupContactsList() {
        List<ContactsModel> listContacts = helper.getAllContacts();
        RecyclerViewContactsAdapter recyclerViewContactsAdapter = new RecyclerViewContactsAdapter(this, listContacts);
        vRecyclerView.setAdapter(recyclerViewContactsAdapter);
    }

    /**
     * Displays the Groups list in Drawer
     */
    private void setupGroupsList() {
        List<GroupsModel> listGroups = helper.getAllGroups();
        RecyclerViewGroupsAdapter recyclerViewGroupsAdapter = new RecyclerViewGroupsAdapter(this, listGroups);
        vRecyclerViewDrawer.setAdapter(recyclerViewGroupsAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_contacts, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Handles the FloatingActionButtons click functionality for adding contact or group
     *
     * @param v Button that is clicked
     */
    public void onClick(View v) {
        if (v.getId() == R.id.fabContact) {
            Intent intent = new Intent(this, ContactDetails.class);
            intent.putExtra(Utils.OPTION, Utils.OPTION_ADD);
            startActivity(intent);
        }
        if (v.getId() == R.id.fabGroup) {
            Intent intent = new Intent(this, GroupDetails.class);
            intent.putExtra(Utils.OPTION, Utils.OPTION_ADD);
            startActivity(intent);
        }
    }

    /**
     * Takes the user to launcher when pressed back from Main Screen
     */
    @Override
    public void onBackPressed() {
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
    }

}
