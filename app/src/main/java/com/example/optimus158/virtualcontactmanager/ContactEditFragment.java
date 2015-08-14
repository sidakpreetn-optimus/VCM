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

/**
 * Created by optimus158 on 08-Aug-15.
 * <p/>
 * Fragment class for editing contact's details
 */
public class ContactEditFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private static final int STATUS_CAMERA = 1;
    private static final int STATUS_GALLERY = 2;
    private String picturePath;
    private DBHelper helper;
    private ImageView vImageView;
    private Spinner vSpinner;
    private EditText vFirstName, vLastName, vPhoneNo;
    private String[] addImage = {"-SELECT-", "Camera", "Gallery"};
    private ContactsModel model;

    /**
     * Sets the views for UI and initializes variables
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_edit_contact_fragment, container, false);

        Toolbar toolBar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolBar);

        ContactDetails activity = (ContactDetails) getActivity();
        model = activity.getContactModel();

        helper = new DBHelper(getActivity());
        vImageView = (ImageView) view.findViewById(R.id.imageViewEditContact);
        vSpinner = (Spinner) view.findViewById(R.id.spinnerEditContact);
        vFirstName = (EditText) view.findViewById(R.id.editTextFirstNameEditContact);
        vLastName = (EditText) view.findViewById(R.id.editTextLastNameEditContact);
        vPhoneNo = (EditText) view.findViewById(R.id.editTextPhoneNoEditContact);
        Button vButtonSave = (Button) view.findViewById(R.id.buttonSaveEditContact);
        Button vButtonCancel = (Button) view.findViewById(R.id.buttonCancelEditContact);
        vButtonSave.setOnClickListener(this);
        vButtonCancel.setOnClickListener(this);

        ArrayAdapter<String> spinneradapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_list_item_1, addImage);
        vSpinner.setAdapter(spinneradapter);
        vSpinner.setOnItemSelectedListener(this);

        setupContactDetails();

        return view;
    }

    /**
     * Method for setting contact details to UI views
     */
    private void setupContactDetails() {
        if (model.getPicturePath().equals("")) {
            Bitmap bitmap = ThumbnailUtils.extractThumbnail(BitmapFactory
                    .decodeResource(getActivity().getResources(),
                            R.drawable.default_image), 300, 300);
            vImageView.setImageBitmap(bitmap);
        } else {
            Bitmap bitmap = ThumbnailUtils.extractThumbnail(
                    BitmapFactory.decodeFile(model.getPicturePath()),
                    300, 300);
            vImageView.setImageBitmap(bitmap);
        }
        vFirstName.setText(model.getFirstName());
        vLastName.setText(model.getLastName());
        vPhoneNo.setText(model.getPhoneNo());
    }

    /**
     * Handles the button click functionality for Save and Cancel
     *
     * @param v
     */
    public void onClick(View v) {
        // Saves the changed values to Database
        if (v.getId() == R.id.buttonSaveEditContact) {
            if (vFirstName.getText().toString().equals("")
                    || vLastName.getText().toString().equals("")
                    || vPhoneNo.getText().toString().equals("")) {
                Toast.makeText(getActivity(), "Field(s) cannot be Blank",
                        Toast.LENGTH_SHORT).show();
                return;
            }
            model.setFirstName(vFirstName.getText().toString());
            model.setLastName(vLastName.getText().toString());
            model.setPhoneNo(vPhoneNo.getText().toString());

            if (model.getPicturePath().equals("") && picturePath == null) {
                model.setPicturePath("");
            } else {
                model.setPicturePath(model.getPicturePath());
            }

            int status = helper.updateContact(model);
            if (status != -1) {
                startActivity(new Intent(getActivity(), Contacts.class));
                Toast.makeText(getActivity(),
                        "Data Updated Successfully !!!", Toast.LENGTH_SHORT)
                        .show();
            }
            if (status == -1) {
                Toast.makeText(getActivity(), "ERROR", Toast.LENGTH_SHORT).show();
            }
        }// Presing cancel just returns to the main screen
        if (v.getId() == R.id.buttonCancelEditContact) {
            startActivity(new Intent(getActivity(), Contacts.class));
        }
    }

    /**
     * Method for performing action for spinner selection : Gallery or Camera
     *
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        int selection = vSpinner.getSelectedItemPosition();
        if (vPhoneNo.getText().toString().equals("")) {
            Toast.makeText(
                    getActivity(),
                    "Contact Nunber to be entered before Selecting Picutre !!!",
                    Toast.LENGTH_LONG).show();
            return;
        }
        switch (selection) {
            case 1:
                Intent cameraIntent = new Intent(
                        android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                picturePath = Utils.getCameraFile(cameraIntent, getActivity(), vPhoneNo.getText().toString()).getAbsolutePath();
                model.setPicturePath(picturePath);
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
    @Override
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
                model.setPicturePath(picturePath);
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
