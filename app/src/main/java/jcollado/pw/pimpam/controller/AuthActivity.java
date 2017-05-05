package jcollado.pw.pimpam.controller;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
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
import com.google.firebase.auth.UserProfileChangeRequest;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jcollado.pw.pimpam.R;
import jcollado.pw.pimpam.utils.BaseActivity;
import jcollado.pw.pimpam.utils.Functions;
import jcollado.pw.pimpam.utils.Singleton;
import mehdi.sakout.fancybuttons.FancyButton;

public class AuthActivity extends BaseActivity implements GoogleApiClient.OnConnectionFailedListener {
    @BindView(R.id.btn_create_account)
    FancyButton createAccountButton;
    @BindView(R.id.emailED)
    EditText emailED;
    @BindView(R.id.passwordED)
    EditText passwordED;
    @BindView(R.id.nameED)
    EditText nameED;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser user;
    private GoogleApiClient mGoogleApiClient;
    private static final int RC_SIGN_IN = 9001;

    private boolean notGoogle = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        ButterKnife.bind(this);
        mAuth = Singleton.getInstance().getFirebaseModule().getmAuth();

        authListener();
        configGoogle();


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
private void configGoogle(){
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

private void authListener(){
    mAuthListener = new FirebaseAuth.AuthStateListener() {
        @Override
        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
            user = firebaseAuth.getCurrentUser();
            if (user != null) {
                // User is signed in
                Singleton.getInstance().getFirebaseModule().setConnectionDatabase();
                if(notGoogle){
                    addUserInfo(user);

                }
                else{
                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(i);
                }


            } else {
                // User is signed out
                // Log.d(TAG, "onAuthStateChanged:signed_out");
            }
            // ...
        }
    };
}
    private void addUserInfo( FirebaseUser user ){
        Uri picUri = Uri.parse("https://s-media-cache-ak0.pinimg.com/originals/d3/cf/69/d3cf690f988f41fd1894526e78c1e1f8.png");
        String name = nameED.getText().toString();


        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .setPhotoUri(picUri)
                .build();

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Intent i = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(i);
                        }
                    }
                });


    }


    @OnClick(R.id.btn_create_account)
    void onCreateAccount() {
        if(checkAllFieldsCompleted()) {
            onPreStartConnection(getString(R.string.loading));
            notGoogle = true;
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
    }
    @OnClick(R.id.btn_login)
    void onLoginAccount() {
        if(checkAllFieldsCompleted()) {
            onPreStartConnection(getString(R.string.loading));
            mAuth.signInWithEmailAndPassword(emailED.getText().toString(), passwordED.getText().toString())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                Log.w("login", "signInWithEmail:failed", task.getException());
                                Toast.makeText(AuthActivity.this, getString(R.string.login_failed) + ": " + task.getException().getMessage(),
                                        Toast.LENGTH_SHORT).show();

                            } else {

                                Toast.makeText(AuthActivity.this, R.string.login_succesful, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
            stopRefreshing();
        }

    }

    @OnClick(R.id.btn_restore_password)
    void onRestorePassword(){
        if(emailED.getText().length() !=0 ) {
            onPreStartConnection(getString(R.string.loading));

            mAuth.sendPasswordResetEmail(emailED.getText().toString())
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                AlertDialog.Builder successRestoreAlert = Functions.getModal(getString(R.string.succesful_restored_pass), getString(R.string.ok), AuthActivity.this);
                                successRestoreAlert.show();

                            } else {
                                AlertDialog.Builder errorRestoreAlert = Functions.getModalError(AuthActivity.this);
                                errorRestoreAlert.show();
                            }
                            stopRefreshing();

                        }
                    });
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                Toast.makeText(AuthActivity.this, R.string.register_succesful, Toast.LENGTH_SHORT).show();
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                // Google Sign In failed, update UI appropriately
                // ...
            }
        }
    }
    @OnClick(R.id.btn_google)
    void signInGoogle() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onBackPressed() {
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);

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
                            Toast.makeText(AuthActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        // ...
                    }
                });
    }
    private void stopRefreshing() {

        onConnectionFinished();
    }
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d("google", "onConnectionFailed:" + connectionResult);
    }

    private boolean checkAllFieldsCompleted(){
        if (nameED.getText().length() != 0 && passwordED.getText().length() != 0 && emailED.getText().length() != 0)
                return true;
        else{
            AlertDialog.Builder allFieldsBuilder = Functions.getModal(getString(R.string.allFieldsRequired),getString(R.string.ok),this);
            allFieldsBuilder.show();
            return false;


        }
    }

}


