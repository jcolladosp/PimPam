package jcollado.pw.pimpam.splash;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import java.util.Timer;
import java.util.TimerTask;

import jcollado.pw.pimpam.R;
import jcollado.pw.pimpam.controller.MainActivity;
import jcollado.pw.pimpam.login.LoginActivity;
import jcollado.pw.pimpam.utils.BaseActivity;
import jcollado.pw.pimpam.utils.Functions;

public class SplashActivity extends BaseActivity implements SplashView{
    private SplashPresenter splashPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        splashPresenter = new SplashPresenterImpl(this);

        splashPresenter.startSplash();
    }

    @Override public void startLoginActivity() {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                    Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(i);
            }

        }, 1500);
    }


    @Override public void startMainActivity(){
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
            }

        }, 1500);

    }
    @Override public void showErrorNotInternetConnection(){
        AlertDialog.Builder noInternetError = Functions.getModalNoInternetConnection(this);
        noInternetError.show();

   }
    @Override protected void onDestroy() {
        splashPresenter.onDestroy();
        super.onDestroy();
    }
    @Override public  boolean isNetworkAvailable(){
        return Functions.isNetworkAvailable(this);
    }

}
