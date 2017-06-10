package jcollado.pw.pimpam.splash;

import android.content.Context;

import jcollado.pw.pimpam.utils.Functions;
import jcollado.pw.pimpam.utils.UserInfo;

/**
 * Created by jcolladosp on 10/06/2017.
 */

public class SplashInteractorImpl implements SplashInteractor {

    @Override
    public void checkInternetConnection(Context context, OnSplashFinishedListener listener) {
        if(Functions.isNetworkAvailable(context)){
            isUsserLogged(context,listener);
        }
        else{
            listener.onNoInternetConnectionFound();
        }
    }

    @Override
    public void isUsserLogged(Context context, OnSplashFinishedListener listener) {
        if(UserInfo.isLogged()){
            listener.onUserLogged();
        }
        else{
            listener.onUserNotLogged();
        }
    }
}
