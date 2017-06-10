package jcollado.pw.pimpam.splash;

import android.content.Context;

/**
 * Created by jcolladosp on 10/06/2017.
 */

public class SplashPresenterImpl implements SplashPresenter,SplashInteractor.OnSplashFinishedListener{
    private SplashView splashView;
    private SplashInteractor splashInteractor;
    private Context context;

    public SplashPresenterImpl(SplashView splashView, Context context) {
        this.splashView = splashView;
        this.splashInteractor = new SplashInteractorImpl();
        this.context = context;
    }



    @Override
    public void startSplash() {
        splashInteractor.checkInternetConnection(context,this);

    }

    @Override
    public void onNoInternetConnectionFound() {
        splashView.showErrorNotInternetConnection();

    }

    @Override
    public void onUserLogged() {
        splashView.startMainActivity();
    }

    @Override
    public void onUserNotLogged() {
        splashView.startLoginActivity();

    }
    @Override public void onDestroy() {
        splashView = null;
    }

}
