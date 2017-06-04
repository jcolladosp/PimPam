package jcollado.pw.pimpam.utils;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import jcollado.pw.pimpam.R;

/**
 * Created by colla on 04/06/2017.
 */

public class CameraUtils {
    public static final int GALLERY_PICK = 1;
    public static final int CAMERA_PICK = 2;

    public static String getmCurrentPhotoPath() {
        return mCurrentPhotoPath;
    }

    static String mCurrentPhotoPath;

    public static void openCamera(final Activity activity, final Context context, final BaseFragment fragment){
        Dexter.withActivity(activity)
                .withPermission(Manifest.permission.CAMERA)
                .withListener(new PermissionListener() {
                    @Override public void onPermissionGranted(PermissionGrantedResponse response) {
                        dispatchTakePictureIntent(activity,context,fragment);

                    }
                    @Override public void onPermissionDenied(PermissionDeniedResponse response) {
                        Functions.getModal(context.getString(R.string.cameraPermissionsDenied),context.getString(R.string.ok),context).show();
                    }
                    @Override public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {/* ... */}
                }).check();
    }
    public static void changeImage(final Activity activity, final Context context,  final BaseFragment fragment) {

        AlertDialog.Builder builder = Functions.getModal(R.string.warning_modal_title,R.string.news_image_message, activity);
        builder.setPositiveButton(R.string.news_image_camera, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                openCamera(activity,context,fragment);

            }
        });
        builder.setNeutralButton(R.string.news_image_gallery, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                if(fragment!=null) fragment.startActivityForResult(pickPhoto, GALLERY_PICK);
                else{
                    activity.startActivityForResult(pickPhoto, GALLERY_PICK);
                }

            }
        });
        builder.show();

    }
    private static File createImageFile(Context context) throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }


    private static void dispatchTakePictureIntent(final Activity activity, final Context context, final BaseFragment fragment) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(context.getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile(context);
            } catch (IOException ex) {
                // Error occurred while creating the File

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(context,
                        "jcollado.pw.pimpam.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                if(fragment!=null) fragment.startActivityForResult(takePictureIntent, CAMERA_PICK);
                else{
                    activity.startActivityForResult(takePictureIntent, CAMERA_PICK);
                }
            }
        }
    }
}
