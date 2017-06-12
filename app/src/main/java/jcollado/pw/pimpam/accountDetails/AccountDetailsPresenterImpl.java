package jcollado.pw.pimpam.accountDetails;

import android.text.TextUtils;

/**
 * Created by jcolladosp on 12/06/2017.
 */

public class AccountDetailsPresenterImpl implements AccountDetailsPresenter,AccountDetailsInteractor.OnAccountDetailsListener {
    private AccountDetailsView accountDetailsView;
    private AccountDetailsInteractor accountDetailsInteractor;

    private boolean imageChanged = false;


    public AccountDetailsPresenterImpl(AccountDetailsView accountDetailsView) {
        this.accountDetailsView = accountDetailsView;
        this.accountDetailsInteractor = new AccountDetailsInteractorImpl();

    }

    @Override
    public void onUploadImageSuccess(String url) {
        accountDetailsInteractor.AddUserInfoRequest(accountDetailsView.getNameED(), url, this);

    }

    @Override
    public void onUploadImageError() {
        accountDetailsView.stopRefreshing();
        accountDetailsView.showDialogGeneralError();
    }

    @Override
    public void onDestroy() {
        accountDetailsView = null;
    }

    @Override
    public void uploadProfileImage(String url, String name) {
        accountDetailsView.onPreStartConnection();
        accountDetailsInteractor.uploadImageRequest(url,name,this);

    }

    @Override
    public void setImageChangedTrue() {
    imageChanged = true;
    }

    @Override
    public void updateUserInfo(String name, String url) {
        if(name.length() == 0){
            accountDetailsView.showDialogNameRequired();
        }
        else {
            if (imageChanged) {
                uploadProfileImage(url, java.util.UUID.randomUUID().toString());
            } else {
                accountDetailsView.onPreStartConnection();
                accountDetailsInteractor.AddUserInfoRequest(name, url, this);
            }
        }
    }

    @Override
    public void onUserInfoUpdateSuccess() {
        accountDetailsView.stopRefreshing();
        accountDetailsView.startMainActivity();
    }
}


