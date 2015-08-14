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
import android.view.View.OnClickListener;
import android.widget.AdapterView.OnItemSelectedListener;

/**
 * Created by optimus158 on 08-Aug-15.
 * <p/>
 * Fragment Class for adding a Contact
 */
public class ContactAddFragment extends Fragment implements OnClickListener, OnItemSelectedListener {

    private static final int STATUS_CAMERA = 1;
    private static final int STATUS_GALLERY = 2;
    private String[] addImage = {"-SELECT-", "Camera", "Gallery"};
    private String picturePath;
    private DBHelper helper;
    private ImageView vImageView;
    private Spinner vSpinner;
    private EditText vFirstName, vLastName, vPhoneNo;

    /**
     * Method for setting the UI for layout
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_add_contact_fragment, container, false);

        Toolbar toolBar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolBar);

        helper = new DBHelper(getActivity());
        vImageView = (ImageView) view.findViewById(R.id.imageViewAddContact);
        vSpinner = (Spinner) view.findViewById(R.id.spinnerContact);
        vFirstName = (EditText) view.findViewById(R.id.editTextFirstName);
        vLastName = (EditText) view.findViewById(R.id.editTextLastName);
        vPhoneNo = (EditText) view.findViewById(R.id.editTextPhoneNo);
        Button vButton = (Button) view.findViewById(R.id.buttonAddContact);
        vButton.setOnClickListener(this);

        ArrayAdapter<String> spinneradapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_list_item_1, addImage);
        vSpinner.setAdapter(spinneradapter);
        vSpinner.setOnItemSelectedListener(this);

        return view;
    }

    /**
     * Method for handling the add functionality
     */
    public void onClick(View v) {
        // Check for fields not being blank
        if (vFirstName.getText().toString().equals("")
                || vLastName.getText().toString().equals("")
                || vPhoneNo.getText().toString().equals("")) {
            Toast.makeText(getActivity(), "Field(s) cannot be Blank",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        ContactsModel contact = new ContactsModel();
        contact.setFirstName(vFirstName.getText().toString());
        contact.setLastName(vLastName.getText().toString());
        contact.setPhoneNo(vPhoneNo.getText().toString());
        // checks if picture set if not then set default image
        if (picturePath == null) {
            contact.setPicturePath("");
        } else {
            contact.setPicturePath(picturePath);
        }

        int status = helper.createContact(contact);
        // checks if Contact is added to Database or not
        if (status != -1) {
            startActivity(new Intent(getActivity(), Contacts.class));
            Toast.makeText(getActivity(),
                    "Data Inserted Successfully !!!", Toast.LENGTH_SHORT)
                    .show();
        }
        if (status == -1) {
            Toast.makeText(getActivity(), "ERROR", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * When user selects spinner option camera or gallery
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
                    "Contact Number to be entered before Selecting Picutre !!!",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        switch (selection) {
            case 1:
                Intent cameraIntent = new Intent(
                        android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                picturePath = Utils.getCameraFile(cameraIntent, getActivity(), vPhoneNo.getText().toString()).getAbsolutePath();
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
            // if control returned from gallery then this code executes for saving the result image
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
        // sets the image to imageview
        Bitmap bitmap = ThumbnailUtils.extractThumbnail(
                BitmapFactory.decodeFile(picturePath), 200, 200);
        vImageView.setImageBitmap(bitmap);
    }
}
