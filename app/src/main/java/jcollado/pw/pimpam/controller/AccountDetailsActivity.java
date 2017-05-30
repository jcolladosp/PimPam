package jcollado.pw.pimpam.controller;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.EditText;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import jcollado.pw.pimpam.R;
import jcollado.pw.pimpam.utils.BaseActivity;
import jcollado.pw.pimpam.utils.FirebaseModule;
import jcollado.pw.pimpam.utils.Functions;


public class AccountDetailsActivity extends BaseActivity {

    private static final int GALLERY_PICK = 1;
    private static final int CAMERA_PICK = 2;

    @BindView(R.id.nameED)
    EditText nameED;
    @BindView(R.id.profile_image)
    CircleImageView profile_image;
    public boolean imageChanged = false;
    private Bitmap imageBitmap;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_details);
        ButterKnife.bind(this);
        Glide.with(this).load("http://i.annihil.us/u/prod/marvel/i/mg/2/00/53710b14a320b.png")
                .placeholder(R.drawable.placeholder).into(profile_image);


    }
    @OnClick(R.id.btn_continuar)
        void onContinuar(){
        addUserInfo(FirebaseModule.getInstance().getmAuth().getCurrentUser());
    }
    @OnClick(R.id.profile_image)
    void onProfileImage(){
        Functions.changeImage(this,getApplicationContext());

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
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            imageChanged = true;
            if (requestCode == CAMERA_PICK) {
                Bundle extras = data.getExtras();
                imageBitmap = (Bitmap) extras.get("data");
                profile_image.setImageBitmap(imageBitmap);
            } else {
                profile_image.setImageURI(data.getData());

            }
        }
    }
}
