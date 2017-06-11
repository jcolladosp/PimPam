package jcollado.pw.pimpam.login;


import android.text.TextUtils;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;


/**
 * Created by jcolladosp on 10/06/2017.
 */

public class LoginPresenterImpl implements LoginPresenter, LoginInteractor.OnLoginFinishedListener{
    private LoginView loginView;
    private LoginInteractor loginInteractor;

    public LoginPresenterImpl(LoginView loginView) {
        this.loginView = loginView;
        this.loginInteractor = new LoginInteractorImpl();
        loginInteractor.authListener(this);
    }


    @Override
    public void createAccountWithMail(String email, String password) {
        if(TextUtils.isEmpty(password) || TextUtils.isEmpty(email)){
            loginView.showDialogNotAllFieldsCompleted();
        }
        else{
            loginView.onPreStartConnection();
            loginInteractor.sendEmailRegisterRequest(email,password,this);
        }
    }

    @Override
    public void loginWithMail(String email, String password) {
        if(TextUtils.isEmpty(password) || TextUtils.isEmpty(email)){
            loginView.showDialogNotAllFieldsCompleted();
        }
        else{
            loginView.onPreStartConnection();
            loginInteractor.sendEmailLoginRequest(email,password,this);
        }

    }

    @Override
    public void restorePassword(String password) {
        if(TextUtils.isEmpty(password)){
            loginView.showDialogNotAllFieldsCompleted();
        }
        else{
            loginView.onPreStartConnection();
            loginInteractor.restorePasswordRequest(password,this);
        }

    }

    @Override
    public void loginWithGoogle() {
        loginInteractor.loginWithGoogleRequest(this);

    }



    @Override
    public void onDestroy() {
        loginView = null;
    }

    @Override
    public void addAuthStateListener() {
        loginInteractor.addAuthStateListener();
    }

    @Override
    public void removeAuthStateListener() {
        loginInteractor.removeAuthStateListener();
    }

    @Override
    public void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        loginInteractor.firebaseAuthWithGoogle(acct,this);
    }

    @Override
    public void onLoginMailSuccess() {
        loginView.stopRefreshing();
        loginView.showToastLoginSuccesfull();
        loginView.startMainActivity();

    }

    @Override
    public void onLoginMailFail(String error) {
        loginView.stopRefreshing();
        loginView.showToastLoginFailed(error);

    }

    @Override
    public void onRegisterMailSuccess() {
        loginView.stopRefreshing();
        loginView.showToastRegisterSuccesfull();
        loginView.startAccountDetailsActivity();

    }

    @Override
    public void onRegisterMailFail(String error) {
        loginView.stopRefreshing();
        loginView.showToastRegisterFailed(error);

    }

    @Override
    public void onLoginGoogleSuccess() {
        loginView.startMainActivity();

    }

    @Override
    public void onLoginGoogleFail() {
        loginView.showToastErrorRegisterGoogle();

    }

    @Override
    public void onRestorePasswordSuccess() {
        loginView.stopRefreshing();
        loginView.showDialogRestorePassword();

    }

    @Override
    public void onRestorePasswordFail() {
        loginView.stopRefreshing();
        loginView.showDialogGeneralError();

    }
}
