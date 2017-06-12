package jcollado.pw.pimpam.accountDetails;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.widget.EditText;

import com.bumptech.glide.Glide;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import jcollado.pw.pimpam.R;
import jcollado.pw.pimpam.controller.MainActivity;
import jcollado.pw.pimpam.utils.BaseActivity;
import jcollado.pw.pimpam.utils.CameraUtils;
import jcollado.pw.pimpam.utils.Functions;
import jcollado.pw.pimpam.utils.UserInfo;


public class AccountDetailsActivity extends BaseActivity implements AccountDetailsView {


    @BindView(R.id.nameED)
    EditText nameED;
    @BindView(R.id.profile_image)
    CircleImageView profile_image;


    private AccountDetailsPresenter accountDetailsPresenter;

    private String imageurl = "https://animotionlatinoamerica.files.wordpress.com/2016/05/decoration-stickers-spiderman.jpg";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_details);
        ButterKnife.bind(this);
        accountDetailsPresenter = new AccountDetailsPresenterImpl(this);
        setUserInfoOnView();

    }

    private void setUserInfoOnView(){

        Glide.with(this).load(imageurl).placeholder(R.drawable.placeholder).dontAnimate().into(profile_image);

    }

    @OnClick(R.id.btn_continuar)
    void onContinuar() {
        accountDetailsPresenter.updateUserInfo(nameED.getText().toString(),imageurl);

    }

    @OnClick(R.id.profile_image)
    void onProfileImage() {
        CameraUtils.changeImage(this, getApplicationContext(), null);
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CameraUtils.CAMERA_PICK) {
            File f = new File(CameraUtils.getmCurrentPhotoPath());

            if (f.length() != 0) {
                profileIVfromURI(CameraUtils.getmCurrentPhotoPath());
            }
        }

        if (data != null && requestCode == CameraUtils.GALLERY_PICK) {
            profileIVfromURI(data.getData().toString());

        }
    }

    @Override public void stopRefreshing() {
        onConnectionFinished();
    }

    @Override public void onPreStartConnection(){
        super.onPreStartConnection();
    }

    @Override public void onDestroy() {
        accountDetailsPresenter.onDestroy();
        super.onDestroy();
    }

    @Override
    public void profileIVfromURI(String url) {
        Uri uri = Uri.parse(url);
        profile_image.setImageURI(uri);
        profile_image.buildDrawingCache();
        Bitmap bitmap = profile_image.getDrawingCache();
        imageurl = Functions.saveToInternalStorage(bitmap,"profileimage",this);
        accountDetailsPresenter.setImageChangedTrue();
    }

    @Override
    public String getNameED() {
        return nameED.getText().toString();
    }

    @Override
    public void showDialogGeneralError() {
        AlertDialog.Builder errorRestoreAlert = Functions.getModalError(this);
        errorRestoreAlert.show();
    }

    @Override
    public void startMainActivity() {
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);

    }

    @Override
    public void showDialogNameRequired() {
        AlertDialog.Builder allFieldsBuilder = Functions.getModal(getString(R.string.allFieldsRequired),getString(R.string.ok),this);
        allFieldsBuilder.show();
    }

}
