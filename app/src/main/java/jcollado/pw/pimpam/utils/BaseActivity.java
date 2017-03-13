package jcollado.pw.pimpam.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.multidex.MultiDex;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;



public abstract class BaseActivity extends AppCompatActivity {


    protected ProgressDialog progressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MultiDex.install(this);
        progressBar = new ProgressDialog(this);
        progressBar.setIndeterminate(true);


    }



    public void onPreStartConnection(String message) {
        progressBar.setMessage(message);
        progressBar.setCancelable(false);
        progressBar.show();
    }

    public void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public void onConnectionFinished() {
        progressBar.hide();
    }
}
