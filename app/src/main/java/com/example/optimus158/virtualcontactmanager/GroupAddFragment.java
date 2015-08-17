package com.example.optimus158.virtualcontactmanager;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.List;

/**
 * Created by optimus158 on 11-Aug-15.
 * <p/>
 * Fragment Class for Adding a new Group
 */
public class GroupAddFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private static final int STATUS_CAMERA = 1;
    private static final int STATUS_GALLERY = 2;
    private String[] addImage = {"-SELECT-", "Camera", "Gallery"};
    private String picturePath;
    private DBHelper helper;
    private ImageView vImageView;
    private Spinner vSpinner;
    private EditText vEditText;
    private RecyclerViewGroupsAddAdapter recyclerViewGroupsAddAdapter;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_add_group_fragment, container, false);

        Toolbar toolBar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolBar);

        helper = new DBHelper(getActivity());
        vImageView = (ImageView) view.findViewById(R.id.imageViewAddGroup);
        vSpinner = (Spinner) view.findViewById(R.id.spinnerGroup);
        vEditText = (EditText) view.findViewById(R.id.editTextNameGroup);
        Button vButton = (Button) view.findViewById(R.id.buttonAddGroup);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        RecyclerView vRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerViewGroups);
        vRecyclerView.setHasFixedSize(true);
        vRecyclerView.setLayoutManager(linearLayoutManager);

        List<ContactsModel> list = helper.getAllContacts();
        recyclerViewGroupsAddAdapter = new RecyclerViewGroupsAddAdapter(getActivity(), list);
        vRecyclerView.setAdapter(recyclerViewGroupsAddAdapter);

        ArrayAdapter<String> spinneradapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_list_item_1, addImage);
        vSpinner.setAdapter(spinneradapter);
        vSpinner.setOnItemSelectedListener(this);

        vButton.setOnClickListener(this);

        return view;
    }

    /**
     * Click functionality for adding group
     *
     * @param v
     */
    public void onClick(View v) {
        // saves the group members contact's rowid in CSV
        String groupMembersRowID = "";
        int groupCount = 0;
        GroupsModel group = new GroupsModel();
        if (vEditText.getText().toString().equals("")) {
            Toast.makeText(getActivity(), "Field(s) cannot be Blank",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        group.setGroupName(vEditText.getText().toString());
        if (picturePath == null) {
            group.setPicturePath("");
        } else {
            group.setPicturePath(picturePath);
        }

        // List of contacts currently selected by user for adding to the group
        List<ContactsModel> tempList = recyclerViewGroupsAddAdapter.getContactsList();
        for (int i = 0; i < tempList.size(); i++) {
            ContactsModel contact = tempList.get(i);
            // if contact if selected then its rowid if fetched and saved in CSV along with others contacts selected
            if (contact.isSelected()) {
                groupCount++;
                groupMembersRowID = groupMembersRowID + "," + helper.getRowId(contact.getPhoneNo());
            }
        }
        group.setGroupMembers(groupMembersRowID.substring(1));
        group.setGroupCount(groupCount);

        int status = helper.createGroup(group);
        if (status != -1 && groupCount != 0) {
            startActivity(new Intent(getActivity(), Contacts.class));
            Toast.makeText(getActivity(),
                    "Data Inserted Successfully !!", Toast.LENGTH_SHORT)
                    .show();
        }
        if (status == -1) {
            Toast.makeText(getActivity(), "ERROR", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Method for spinner selection Camera or Gallery
     *
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        int selection = vSpinner.getSelectedItemPosition();
        if (vEditText.getText().toString().equals("")) {
            Toast.makeText(
                    getActivity(),
                    "Group Name to be entered before Selecting Picutre !!!",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        switch (selection) {
            case 1:
                Intent cameraIntent = new Intent(
                        android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                picturePath = Utils.getCameraFile(cameraIntent, getActivity(), vEditText.getText().toString()).getAbsolutePath();
                startActivityForResult(cameraIntent, STATUS_CAMERA);
                break;
            case 2:
                startActivityForResult(Utils.getGalleryIntent(), STATUS_GALLERY);
                break;
        }
    }

    public void onNothingSelected(AdapterView<?> parent) {

    }

    /*
     * Method called after returning from Gallery or Camera
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {

            if (requestCode == STATUS_GALLERY && resultCode == Activity.RESULT_OK
                    && null != data) {

                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String imgDecodableString = cursor.getString(columnIndex);
                cursor.close();
                picturePath = imgDecodableString;

            }
        } catch (Exception e) {
            Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_LONG)
                    .show();
        }

        Bitmap bitmap = ThumbnailUtils.extractThumbnail(
                BitmapFactory.decodeFile(picturePath), 200, 200);
        vImageView.setImageBitmap(bitmap);
    }
}
