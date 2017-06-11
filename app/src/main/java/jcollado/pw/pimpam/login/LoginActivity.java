package jcollado.pw.pimpam.login;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jcollado.pw.pimpam.R;
import jcollado.pw.pimpam.controller.AccountDetailsActivity;
import jcollado.pw.pimpam.controller.MainActivity;
import jcollado.pw.pimpam.utils.BaseActivity;
import jcollado.pw.pimpam.utils.FirebaseModule;
import jcollado.pw.pimpam.utils.Functions;
import mehdi.sakout.fancybuttons.FancyButton;

public class LoginActivity extends BaseActivity implements LoginView, GoogleApiClient.OnConnectionFailedListener {
    @BindView(R.id.btn_create_account)
    FancyButton createAccountButton;
    @BindView(R.id.emailED)
    EditText emailED;
    @BindView(R.id.passwordED)
    EditText passwordED;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private GoogleApiClient mGoogleApiClient;
    private static final int RC_SIGN_IN = 9001;

    private LoginPresenter loginPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        mAuth = FirebaseModule.getInstance().getmAuth();
        loginPresenter = new LoginPresenterImpl(this);

        configGoogle();


    }

    @Override
    public void onStart() {
        super.onStart();
        loginPresenter.addAuthStateListener();
    }

    @Override
    public void onStop() {
        super.onStop();
        loginPresenter.removeAuthStateListener();
    }

    private void configGoogle() {
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();


    }




    @OnClick(R.id.btn_create_account)
    void onCreateAccount() {
        loginPresenter.createAccountWithMail(emailED.getText().toString(),passwordED.getText().toString());
    }

    @OnClick(R.id.btn_login)
    void onLoginAccount() {
           loginPresenter.loginWithMail(emailED.getText().toString(),passwordED.getText().toString());
    }

    @OnClick(R.id.btn_restore_password)
    void onRestorePassword(){
          loginPresenter.restorePassword(emailED.getText().toString());

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
                loginPresenter.loginWithGoogle();
            } else {
                showToastErrorRegisterGoogle();
            }
        }
    }

    @OnClick(R.id.btn_google)
    void signInGoogle() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);

    }


    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d("google", "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("google", "signInWithCredential:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w("google", "signInWithCredential", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }


    @Override
    public void stopRefreshing() {
        onConnectionFinished();
    }
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d("google", "onConnectionFailed:" + connectionResult);
    }
    @Override
    public void showDialogNotAllFieldsCompleted(){
        AlertDialog.Builder allFieldsBuilder = Functions.getModal(getString(R.string.allFieldsRequired),getString(R.string.ok),this);
        allFieldsBuilder.show();
    }

    @Override
    public void showToastLoginFailed(String error){
        Toast.makeText(LoginActivity.this, getString(R.string.login_failed) + ": " + error,
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showToastLoginSuccesfull() {
        Toast.makeText(LoginActivity.this, R.string.login_succesful, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void showToastRegisterFailed(String error) {
        Toast.makeText(LoginActivity.this, getString(R.string.auth_failed) + ": " + error,
                Toast.LENGTH_SHORT).show();

    }

    @Override
    public void showToastRegisterSuccesfull() {
        Toast.makeText(LoginActivity.this, R.string.register_succesful, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void showDialogRestorePassword() {
        AlertDialog.Builder successRestoreAlert = Functions.getModal(getString(R.string.succesful_restored_pass), getString(R.string.ok), LoginActivity.this);
        successRestoreAlert.show();

    }

    @Override
    public void showDialogGeneralError() {
        AlertDialog.Builder errorRestoreAlert = Functions.getModalError(LoginActivity.this);
        errorRestoreAlert.show();
    }

    @Override
    public void showToastErrorRegisterGoogle() {
        Toast.makeText(LoginActivity.this, "Authentication failed.",Toast.LENGTH_SHORT).show();

    }

    @Override
    public void startAccountDetailsActivity() {
        Intent i = new Intent(getApplicationContext(), AccountDetailsActivity.class);
        startActivity(i);

    }

    @Override
    public void startMainActivity() {
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);

    }

    @Override
    public void onBackPressed() {
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);

    }

    @Override public void onDestroy() {
        loginPresenter.onDestroy();
        super.onDestroy();
    }
    @Override public void onPreStartConnection(){
        super.onPreStartConnection();
    }


}


