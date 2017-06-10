package jcollado.pw.pimpam.splash;

import jcollado.pw.pimpam.utils.UserInfo;

/**
 * Created by jcolladosp on 10/06/2017.
 */

public class SplashInteractorImpl implements SplashInteractor {

    @Override
    public void checkInternetConnection(boolean isInternetActive, OnSplashFinishedListener listener) {
        if(isInternetActive){
            isUsserLogged(listener);
        }
        else{
            listener.onNoInternetConnectionFound();
        }
    }

    @Override
    public void isUsserLogged(OnSplashFinishedListener listener) {
        if(UserInfo.isLogged()){
            listener.onUserLogged();
        }
        else{
            listener.onUserNotLogged();
        }
    }
}
