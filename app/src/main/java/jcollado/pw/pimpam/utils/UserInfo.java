package jcollado.pw.pimpam.utils;

import android.net.Uri;

import com.google.firebase.auth.FirebaseUser;

/**
 * Created by colla on 02/05/2017.
 */

public  class UserInfo {


    private UserInfo() {

    }

    public static String getUniqueID() {
        FirebaseUser user = FirebaseModule.getInstance().getCurrentUser();
        if (user != null) {
            return user.getUid();
        }
        return null;
    }
    public static String getUserName() {
        FirebaseUser user = FirebaseModule.getInstance().getCurrentUser();
        if (user != null) {
            return user.getDisplayName();
        }
        return null;
    }
    public static String getDisplayName() {
        FirebaseUser user = FirebaseModule.getInstance().getCurrentUser();
        if (user != null) {
            return user.getDisplayName();
        }
        return null;
    }
    public static Uri getProfilePictureURL() {
        FirebaseUser user = FirebaseModule.getInstance().getCurrentUser();
        if (user != null) {
            return user.getPhotoUrl();
        }
        return null;
    }
    public static String getUserEmail() {
        FirebaseUser user = FirebaseModule.getInstance().getCurrentUser();
        if (user != null) {
            return user.getEmail();
        }
        return null;
    }
    public static boolean isLogged() {
        FirebaseUser user = FirebaseModule.getInstance().getCurrentUser();
        if (user != null) {
            return true;
        }
        return false;
    }
}
