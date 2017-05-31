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
    String spiderman = "https://animotionlatinoamerica.files.wordpress.com/2016/05/decoration-stickers-spiderman.jpg";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_details);
        ButterKnife.bind(this);
        Glide.with(this).load(spiderman).placeholder(R.drawable.placeholder).dontAnimate().into(profile_image);


    }
    @OnClick(R.id.btn_continuar)
        void onContinuar(){
        onPreStartConnection(getString(R.string.loading));
        if(imageChanged){
            profile_image.setDrawingCacheEnabled(true);
            profile_image.buildDrawingCache();
            Bitmap bitmap = profile_image.getDrawingCache();
           String imageURL = FirebaseModule.getInstance().uploadBitmap(bitmap,java.util.UUID.randomUUID().toString(),null,this);

        }
        else{
            addUserInfo(FirebaseModule.getInstance().getmAuth().getCurrentUser(),null);
            onConnectionFinished();
        }


    }
    @OnClick(R.id.profile_image)
    void onProfileImage(){
        Functions.changeImage(this,getApplicationContext(),null);

    }
    @Override
    public void onImageUploaded(String filename){
        addUserInfo(FirebaseModule.getInstance().getmAuth().getCurrentUser(),filename);
        onConnectionFinished();
    }


    private void addUserInfo( FirebaseUser user, String filename ){
       Uri picUri;
        if(filename!=null){
            picUri = Uri.parse(filename);
        }
        else {
            picUri = Uri.parse(spiderman);
        }
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
                            FirebaseModule.getInstance().setConnectionDatabase();

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
