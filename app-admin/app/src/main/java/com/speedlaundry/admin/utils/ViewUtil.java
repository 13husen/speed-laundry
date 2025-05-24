package com.speedlaundry.admin.utils;

import android.net.Uri;
import android.text.InputType;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class ViewUtil {
    public static final int PROFILE_IMAGE_REQ_CODE = 101;
    public static final int GALLERY_IMAGE_REQ_CODE = 102;
    public static final int CAMERA_IMAGE_REQ_CODE = 103;

    public static void disableInput(EditText editText){
        editText.setInputType(InputType.TYPE_NULL);
        editText.setTextIsSelectable(false);
        editText.setOnKeyListener((v, keyCode, event) -> {
            return true;  // Blocks input from hardware keyboards.
        });
    }


    public static void setLocalImage(Uri uri, ImageView img) {
            Glide.with(img).load(uri).into(img);
    }

}
