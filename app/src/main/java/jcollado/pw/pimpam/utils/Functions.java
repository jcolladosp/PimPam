package jcollado.pw.pimpam.utils;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.TypedValue;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jcollado.pw.pimpam.R;
import jcollado.pw.pimpam.controller.AddComicFragment;


public class Functions {



    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (NetworkInfo anInfo : info)
                    if (anInfo.getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
        }
        return false;
    }
    public static AlertDialog.Builder getModal(int title, int body, Context context) {
        final  AlertDialog.Builder builder = new  AlertDialog.Builder (context);
        builder.setTitle(title);
        builder.setMessage(body);

        return builder;
    }
    public static  AlertDialog.Builder getModal(int title, Context context) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(title);
        builder.setNegativeButton(context.getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        return builder;
    }


    public static AlertDialog.Builder getModalError(Context context) {
        return Functions.getModal(R.string.error_general, context);
    }


    public static boolean validatePasswordSize(String password) {
        return password.length() >= 6;
    }




    public static AlertDialog.Builder getModal(String title, String body, String cancelText, final Context context) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(body);
        builder.setNegativeButton(cancelText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();

            }
        });
        return builder;
    }
    public static AlertDialog.Builder getModal( String body, String cancelText, final Context context) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setMessage(body);
        builder.setNegativeButton(cancelText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();

            }
        });
        return builder;
    }
    public static AlertDialog.Builder getModalLogOut( final Context context) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setMessage(context.getString(R.string.logout_confirm));
        builder.setNegativeButton(context.getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();

            }
        });

        return builder;
    }


    public static AlertDialog.Builder getModalNoInternetConnection(final Context context){
        AlertDialog.Builder builder  = getModal(context.getString(R.string.error_title),context.getString(R.string.body_connection_error),context.getString(R.string.ok),context);
            builder.setNegativeButton(context.getString(R.string.ok), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                    closeApp(context);
                }
            });


        return builder;
    }



    /**
     * Converting dp to pixel
     */
    public static int dpToPx(int dp,Context context) {
        Resources r = context.getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    public static Intent sendEmail(String to,String subject) {

        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");

        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{to});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);

        return emailIntent;
    }

public static void closeApp(Context context){
    Intent a = new Intent(Intent.ACTION_MAIN);
    a.addCategory(Intent.CATEGORY_HOME);
    a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    context.startActivity(a);
}



    public static String saveToInternalStorage(Bitmap bitmapImage,String name,Context context){
        ContextWrapper cw = new ContextWrapper(context);
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        File mypath=new File(directory,name);

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return mypath.getAbsolutePath();
    }
}
