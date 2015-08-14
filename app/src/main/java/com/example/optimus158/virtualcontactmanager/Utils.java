package com.example.optimus158.virtualcontactmanager;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.File;

/**
 * Created by optimus158 on 13-Aug-15.
 */
public class Utils {

    public static final String OPTION = "option";
    public static final String OPTION_ADD = "add";
    public static final String OPTION_BROWSE = "browse";
    public static final String OPTION_EDIT = "edit";
    public static final String SELECTED_CONTACT = "selectedContact";

    /**
     * This method returns gallery intent for fetching image
     */
    public static Intent getGalleryIntent() {

        return new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
    }

    public static File getCameraFile(Intent intent, Context context, String fileName) {
        File photoFile = null;
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            try {
                File storageDirectory = context.getExternalFilesDir("");
                photoFile = new File(storageDirectory, fileName
                        + ".jpg");
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (photoFile != null) {
                intent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photoFile));
            }
        }
        return photoFile;
    }
}
