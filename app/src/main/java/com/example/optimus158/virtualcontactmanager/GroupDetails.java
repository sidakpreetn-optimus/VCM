package com.example.optimus158.virtualcontactmanager;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by optimus158 on 11-Aug-15.
 * <p/>
 * Activity Class for setting different group fragments to activity as per user interaction
 */
public class GroupDetails extends AppCompatActivity {

    private FragmentManager fragmentManager = getFragmentManager();
    private GroupsModel groupModel;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_contact_details);
        // Gets the GroupsModel object for using it in fragments
        Intent intent = getIntent();
        groupModel = (GroupsModel) intent
                .getSerializableExtra("selectedGroup");
        String option = intent.getStringExtra("option");

        FragmentTransaction transaction;
        if (option.equalsIgnoreCase("add")) {
            transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.frameLayout, new GroupAddFragment());
            transaction.commit();
        }
        if (option.equalsIgnoreCase("browse")) {
            transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.frameLayout, new GroupBrowseFragment());
            transaction.commit();
        }
    }

    /**
     * Method for facilitating the onActivityResult in fragment
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * Method for accessing the GroupsModel object within fragment (activity-fragment communication)
     *
     * @return
     */
    public GroupsModel getGroupModel() {
        return groupModel;
    }
}
