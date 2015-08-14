package com.example.optimus158.virtualcontactmanager;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by optimus158 on 11-Aug-15.
 * <p/>
 * Fragment class for displaying group details
 */
public class GroupBrowseFragment extends Fragment {

    private ImageView vImageView;
    private GroupsModel model;
    private DBHelper helper;
    private RecyclerView vRecyclerView;
    private CollapsingToolbarLayout collapsingToolbarLayout;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_browse_group_fragment, container, false);

        collapsingToolbarLayout = (CollapsingToolbarLayout) view.findViewById(R.id.collapsing_toolbar_layout_group);

        helper = new DBHelper(getActivity());

        GroupDetails activity = (GroupDetails) getActivity();
        model = activity.getGroupModel();

        Toolbar toolBar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolBar);

        vRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerViewBrowseGroup);
        vRecyclerView.setHasFixedSize(true);
        vRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        vImageView = (ImageView) view.findViewById(R.id.imageViewBrowseGroup);

        setUpGroupDetails();

        return view;
    }

    /**
     * Sets the Group details to the UI Views
     */
    private void setUpGroupDetails() {

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
        collapsingToolbarLayout.setTitle(model.getGroupName());

        // get Groups members in list
        List<ContactsModel> list = new ArrayList<>();
        String[] members = model.getGroupMembers().split(",");
        // Extract each ContactsModel object from their Phone No and adds to List of contact's of this specific group
        for (String member : members) {
            String phoneNo = helper.getPhoneNo(Integer.parseInt(member));
            list.add(helper.getContactByPhoneNo(phoneNo));
        }

        RecyclerViewContactsAdapter recyclerViewContactsAdapter = new RecyclerViewContactsAdapter(getActivity(), list);
        vRecyclerView.setAdapter(recyclerViewContactsAdapter);
    }
}
