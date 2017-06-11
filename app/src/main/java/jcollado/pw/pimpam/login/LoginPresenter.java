package jcollado.pw.pimpam.login;

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
}
