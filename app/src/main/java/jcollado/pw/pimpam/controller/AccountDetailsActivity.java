package jcollado.pw.pimpam.controller;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import butterknife.BindView;
import jcollado.pw.pimpam.R;
import jcollado.pw.pimpam.utils.BaseActivity;


public class AccountDetailsActivity extends BaseActivity {
    @BindView(R.id.nameED)
    EditText nameED;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_details);
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
}
