package jcollado.pw.pimpam.accountDetails;


import jcollado.pw.pimpam.utils.UploadImageListener;

/**
 * Created by jcolladosp on 12/06/2017.
 */

public interface AccountDetailsInteractor {
    interface OnAccountDetailsListener extends UploadImageListener {
        void onUserInfoUpdateSuccess();



    }
    void uploadImageRequest(String url, String name,AccountDetailsInteractor.OnAccountDetailsListener listener);
    void AddUserInfoRequest(String name,String url,AccountDetailsInteractor.OnAccountDetailsListener listener);


}
