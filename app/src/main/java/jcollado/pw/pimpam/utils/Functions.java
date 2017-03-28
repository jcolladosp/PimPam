package jcollado.pw.pimpam.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.telephony.TelephonyManager;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
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

    public static boolean validateEmail(String email) {
        return email.matches("[a-zA-Z0-9._-]+@[a-zA-Z0-9._-]+\\.+[a-z]+");
    }
    public static AlertDialog.Builder getModalError(Context context) {
        return Functions.getModal(R.string.error_general, context);
    }
    public static boolean validateDate(String fecha) {
        try {
            SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            formatoFecha.setLenient(false);
            formatoFecha.parse(fecha);
        } catch (ParseException e) {
            return false;
        }
        return true;
    }

    public static boolean validatePasswordSize(String password) {
        return password.length() >= 6;
    }


    public static boolean validateNifNie(String nifNie) {
        Pattern pattern = Pattern.compile("(([X-Z]{1})([-]?)(\\d{7})([-]?)([A-Z]{1}))|((\\d{8})([-]?)([A-Z]{1}))");
        Matcher matcher = pattern.matcher(nifNie.toUpperCase());
        return matcher.matches();
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
     public static String formatURLforQuery(String url){
         String returnurl = "";
         try {
             returnurl =  URLEncoder.encode(url, "UTF-8");
         } catch (UnsupportedEncodingException e) {
             e.printStackTrace();
         }
         return returnurl;
     }

    public static AlertDialog.Builder checkConnectionAndAlert(Context context){
         AlertDialog.Builder builder = null;
        if(!Functions.isNetworkAvailable(context)) {
             builder = getModal(context.getString(R.string.error_title),context.getString(R.string.body_connection_error),context.getString(R.string.ok),context);

        }
        return builder;
    }



    public static String capitalizeString(String string) {
        return string.substring(0, 1).toUpperCase() + string.substring(1).toLowerCase();
    }

    public static Boolean isYearLeapYear(int year) {
        return ((year % 100 != 0) && (year % 4 == 0)) || year % 400 == 0;
    }

    public static int getImageFromString(String string, Context context) {
        return context.getResources().getIdentifier("drawable/" + string.toLowerCase(), null, context.getPackageName());
    }

    public static String transformImageBase64(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
        byte[] b = baos.toByteArray();
        return Base64.encodeToString(b, Base64.DEFAULT);
    }

    public static SharedPreferences getPrefs(Context context) {
        return context.getSharedPreferences(PrefKeys.NAME.toString(), Context.MODE_PRIVATE);
    }


    public static String getID(Context context) {
        return getPrefs(context).getString(PrefKeys.ID.toString(), "");
    }
    public static String getCompanyID(Context context) {
        return getPrefs(context).getString(PrefKeys.IDEMPRESA.toString(), "");
    }


    public static Intent sendEmail(String to,String subject) {

        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");

        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{to});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);

        return emailIntent;
    }
    public static Intent openWeb(String url){
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        return i;
    }
    public static String getPhoneIMEI(Context context){
        TelephonyManager mngr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return mngr.getDeviceId();
    }

    public static String getDateFormated(){
        Date date = Calendar.getInstance().getTime();
        Calendar now = Calendar.getInstance();

        // Display a date in day, month, year format
        DateFormat formatDate = new SimpleDateFormat("yyyy/MM/dd");
        DateFormat formatHour = new SimpleDateFormat("HH:mm:ss");

        String today = formatDate.format(date) + " " + formatHour.format(date);
        return today;
    }

}