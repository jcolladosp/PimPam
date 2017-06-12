package jcollado.pw.pimpam.accountDetails;


/**
 * Created by jcolladosp on 12/06/2017.
 */

public interface AccountDetailsPresenter {
    void onDestroy();
    void uploadProfileImage(String url,String name);
    void setImageChangedTrue();
    void updateUserInfo(String name,String url);

}
