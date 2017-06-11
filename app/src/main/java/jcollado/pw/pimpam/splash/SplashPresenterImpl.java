package jcollado.pw.pimpam.splash;


/**
 * Created by jcolladosp on 10/06/2017.
 */

public class SplashPresenterImpl implements SplashPresenter,SplashInteractor.OnSplashFinishedListener{
    private SplashView splashView;
    private SplashInteractor splashInteractor;

    public SplashPresenterImpl(SplashView splashView) {
        this.splashView = splashView;
        this.splashInteractor = new SplashInteractorImpl();
    }



    @Override
    public void startSplash() {
        boolean isInternetActive = splashView.isNetworkAvailable();
        splashInteractor.checkInternetConnection(isInternetActive,this);

    }


    @Override
    public void onNoInternetConnectionFound() {
        if (splashView != null) {
            splashView.showErrorNotInternetConnection();
        }

    }

    @Override
    public void onUserLogged() {
        if (splashView != null) {
            splashView.startMainActivity();
        }
    }

    @Override
    public void onUserNotLogged() {
        if (splashView != null) {
            splashView.startLoginActivity();
        }

    }

    @Override public void onDestroy() {
        splashView = null;
    }

}
