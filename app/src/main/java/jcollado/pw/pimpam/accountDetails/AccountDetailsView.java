package jcollado.pw.pimpam.accountDetails;

/**
 * Created by jcolladosp on 12/06/2017.
 */

public interface AccountDetailsView {
    void onDestroy();
    void onPreStartConnection();
    void stopRefreshing();
    String getNameED();
    void profileIVfromURI(String url);
    void showDialogGeneralError();
    void startMainActivity();
    void showDialogNameRequired();

}
