package com.example.optimus158.virtualcontactmanager;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by optimus158 on 08-Aug-15.
 * <p/>
 * Fragment class for viewing contact details
 */
public class ContactBrowseFragment extends Fragment implements View.OnClickListener {

    private ImageView vImageView;
    private TextView vFirstName, vLastName, vPhoneNo;
    private ContactsModel model;
    private DBHelper helper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_browse_contact_fragment, container, false);

        helper = new DBHelper(getActivity());

        ContactDetails activity = (ContactDetails) getActivity();
        model = activity.getContactModel();

        Toolbar toolBar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolBar);

        vImageView = (ImageView) view.findViewById(R.id.imageViewBrowseContact);
        vFirstName = (TextView) view.findViewById(R.id.textViewFirstNameBrowseContact);
        vLastName = (TextView) view.findViewById(R.id.textViewLastNameBrowseContact);
        vPhoneNo = (TextView) view.findViewById(R.id.textViewPhoneNoBrowseContact);
        Button vButtonEdit = (Button) view.findViewById(R.id.buttonEditBrowseContact);
        Button vButtonDelete = (Button) view.findViewById(R.id.buttonDeleteBrowseContact);

        vButtonEdit.setOnClickListener(this);
        vButtonDelete.setOnClickListener(this);

        setUpContactDetails();
        return view;

    }

    /**
     * Sets the contact information to views for displaying
     */
    private void setUpContactDetails() {
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
     * Handles the button click for 2 options Edit and Delete
     *
     * @param v
     */
    public void onClick(View v) {
        // if Edit button clicked then start new screen with selected contact
        if (v.getId() == R.id.buttonEditBrowseContact) {
            Intent intent = new Intent(getActivity(), ContactDetails.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable(Utils.SELECTED_CONTACT, model);
            intent.putExtras(bundle);
            intent.putExtra(Utils.OPTION, Utils.OPTION_EDIT);
            startActivity(intent);
        }
        // if Delete button then remove the selected contact from database and return to main screen
        if (v.getId() == R.id.buttonDeleteBrowseContact) {
            helper.deleteContact(model);
            startActivity(new Intent(getActivity(), Contacts.class));
        }
    }
}
