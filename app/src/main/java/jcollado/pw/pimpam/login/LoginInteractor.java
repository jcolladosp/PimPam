package jcollado.pw.pimpam.login;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

/**
 * Created by jcolladosp on 10/06/2017.
 */

public interface LoginInteractor {
    interface OnLoginFinishedListener {
        void onLoginMailSuccess();
        void onLoginMailFail(String error);
        void onRegisterMailSuccess();
        void onRegisterMailFail(String error);
        void onLoginGoogleSuccess();
        void onLoginGoogleFail();
        void onRestorePasswordSuccess();
        void onRestorePasswordFail();

    }

    void sendEmailLoginRequest(String email,String password,OnLoginFinishedListener listener);
    void sendEmailRegisterRequest(String email,String password,OnLoginFinishedListener listener);
    void restorePasswordRequest(String password,OnLoginFinishedListener listener);
    void loginWithGoogleRequest(OnLoginFinishedListener listener);
    void authListener(OnLoginFinishedListener listener);
    void addAuthStateListener();
    void removeAuthStateListener();
    void firebaseAuthWithGoogle(GoogleSignInAccount acct,OnLoginFinishedListener listener);
}
