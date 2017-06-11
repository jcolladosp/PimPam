package jcollado.pw.pimpam.login;

/**
 * Created by jcolladosp on 10/06/2017.
 */

public interface LoginView {
    void showDialogNotAllFieldsCompleted();

    void showToastLoginFailed(String error);
    void showToastLoginSuccesfull();

    void showToastRegisterFailed(String error);
    void showToastRegisterSuccesfull();

    void showDialogRestorePassword();
    void showDialogGeneralError();

    void showToastErrorRegisterGoogle();

    void startAccountDetailsActivity();
    void startMainActivity();

    void stopRefreshing();
    void onDestroy();
    void onPreStartConnection();


}
