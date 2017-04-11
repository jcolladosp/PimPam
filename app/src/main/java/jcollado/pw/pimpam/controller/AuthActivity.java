package jcollado.pw.pimpam.controller;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jcollado.pw.pimpam.R;
import jcollado.pw.pimpam.utils.BaseActivity;
import mehdi.sakout.fancybuttons.FancyButton;

public class AuthActivity extends BaseActivity {
    @BindView(R.id.btn_create_account)
    FancyButton createAccountButton;
    @BindView(R.id.emailED)
    EditText emailED;
    @BindView(R.id.passwordED)
    EditText passwordED;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        ButterKnife.bind(this);
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d("auth", "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d("auth", "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };


    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @OnClick(R.id.btn_create_account)
    void onCreateAccount() {
        onPreStartConnection(getString(R.string.loading));

        mAuth.createUserWithEmailAndPassword(emailED.getText().toString(), passwordED.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (!task.isSuccessful()) {
                                Toast.makeText(AuthActivity.this, getString(R.string.auth_failed) + ": " + task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();

                        } else {

                            Toast.makeText(AuthActivity.this, R.string.register_succesful, Toast.LENGTH_SHORT).show();
                        }
                        stopRefreshing();

                    }
                });
    }
    @OnClick(R.id.btn_login)
    void onLoginAccount() {
        mAuth.signInWithEmailAndPassword(emailED.getText().toString(),  passwordED.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w("login", "signInWithEmail:failed", task.getException());
                            Toast.makeText(AuthActivity.this, getString(R.string.login_failed) + ": " + task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();

                        }

                         else {

                        Toast.makeText(AuthActivity.this, R.string.login_succesful, Toast.LENGTH_SHORT).show();
                    }
                    }
                });
    }
    @Override
    public void onBackPressed() {
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);

    }
    private void stopRefreshing() {

        onConnectionFinished();
    }
}


