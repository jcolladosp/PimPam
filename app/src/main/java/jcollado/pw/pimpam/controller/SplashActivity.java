package jcollado.pw.pimpam.controller;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.Timer;
import java.util.TimerTask;

import jcollado.pw.pimpam.R;
import jcollado.pw.pimpam.utils.BaseActivity;
import jcollado.pw.pimpam.utils.Functions;
import jcollado.pw.pimpam.utils.UserInfo;

public class SplashActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        startSplash();
    }
    private void startSplash() {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                if(!UserInfo.isLogged()) {
                    Intent i = new Intent(getApplicationContext(), AuthActivity.class);
                    startActivity(i);
                }
                else{
                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(i);
                }
            }

        }, 1500);
    }

}
