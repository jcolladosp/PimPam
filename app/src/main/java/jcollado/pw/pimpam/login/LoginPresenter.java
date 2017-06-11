package jcollado.pw.pimpam.login;


import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

/**
 * Created by jcolladosp on 10/06/2017.
 */

public interface LoginPresenter  {

    void createAccountWithMail(String email,String password);
    void loginWithMail(String email,String password);
    void restorePassword(String password);
    void loginWithGoogle();
    void onDestroy();
    void addAuthStateListener();
    void removeAuthStateListener();
    void firebaseAuthWithGoogle(GoogleSignInAccount acct);
}
