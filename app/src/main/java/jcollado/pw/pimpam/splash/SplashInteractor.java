package jcollado.pw.pimpam.splash;

import android.content.Context;

/**
 * Created by jcolladosp on 10/06/2017.
 */

public interface SplashInteractor {

    interface OnSplashFinishedListener {
        void onNoInternetConnectionFound();

        void onUserLogged();

        void onUserNotLogged();
    }

    void checkInternetConnection(Context context, OnSplashFinishedListener listener);

    void isUsserLogged(Context context, OnSplashFinishedListener listener);

}
