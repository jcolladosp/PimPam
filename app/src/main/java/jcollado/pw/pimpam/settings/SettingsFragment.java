package jcollado.pw.pimpam.settings;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import jcollado.pw.pimpam.R;
import jcollado.pw.pimpam.controller.MainActivity;
import jcollado.pw.pimpam.utils.BaseFragment;
import jcollado.pw.pimpam.utils.CameraUtils;
import jcollado.pw.pimpam.utils.Functions;
import jcollado.pw.pimpam.utils.UserInfo;


public class SettingsFragment extends BaseFragment implements SettingsView {


    @BindView(R.id.profile_edit)
    CircleImageView profile_edit;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.nameED)
    EditText nameED;
    @BindView(R.id.radio_english)
    RadioButton englishRB;
    @BindView(R.id.radio_spanish)
    RadioButton spanishRB;
    @BindView(R.id.radio_valencia)
    RadioButton valenciaRB;
    @BindView(R.id.language)
    RadioGroup buttonsLanguage;


    private static View view;

    private String imageurl;
    private SettingsPresenter settingsPresenter;
    private static SettingsFragment fragment;

    public SettingsFragment() {
        // Required empty public constructor
    }

    public static SettingsFragment newInstance() {
        fragment = new SettingsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_settings, container, false);
        ButterKnife.bind(this, view);
        settingsPresenter =  new SettingsPresenterImpl(this);

        prepareToolbar();
        setHasOptionsMenu(true);
        checkLanguageButton();
        setUserInfo();

        return view;
    }

    @OnClick(R.id.profile_edit)
    void onProfileImage(){
        final BaseFragment fragment = this;
        CameraUtils.changeImage(getActivity(),getContext(),fragment);


    }

    private void setUserInfo(){
        imageurl = UserInfo.getProfilePictureURL().toString();
        nameED.setText(UserInfo.getDisplayName());
        Glide.with(this).load(imageurl).placeholder(R.drawable.placeholder).dontAnimate().into(profile_edit);
    }

    @OnClick(R.id.eraseComicsBT)
    void onEraseComics(){
        showDialogDeleteAllComics();
    }

    private void prepareToolbar(){
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(false);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.hamburger));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.openDrawer();
            }
        });
        toolbar.setTitle(getString(R.string.settings));
    }



    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        getActivity().getMenuInflater().inflate( R.menu.settings_menu, menu);
        super.onCreateOptionsMenu(menu,inflater);


    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_done_settings:
                settingsPresenter.updateUserInfo(nameED.getText().toString(),imageurl);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void checkLanguageButton(){
        String locale = getResources().getConfiguration().locale.toString();
        settingsPresenter.checkLocaleRadioButton(locale);

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


    public int getLocaleSelected(){
        int radioButtonID = buttonsLanguage.getCheckedRadioButtonId();
        View radioButton = buttonsLanguage.findViewById(radioButtonID);
        return buttonsLanguage.indexOfChild(radioButton);

    }

    @Override
    public void setCheckedEnglishRB() {
        englishRB.setChecked(true);

    }

    @Override
    public void setCheckedSpanishRB() {
        spanishRB.setChecked(true);
    }

    @Override
    public void setCheckedValencianRB() {

        valenciaRB.setChecked(true);

    }

    @Override
    public void profileIVfromURI(String url) {
        Uri uri = Uri.parse(url);
        profile_edit.setImageURI(uri);
        profile_edit.buildDrawingCache();
        Bitmap bitmap = profile_edit.getDrawingCache();
        imageurl = Functions.saveToInternalStorage(bitmap,"profileimage",getContext());
        settingsPresenter.setImageChangedTrue();
    }

    @Override
    public String getNameED() {
        return nameED.getText().toString();
    }

    @Override
    public void showDialogInfoUpdatedCorrectly() {
         AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(R.string.profileUpdatedCorrectly);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            settingsPresenter.setLocaleSelection(getLocaleSelected());
            }
        });
        builder.show();
    }


    public  void setLocale(String lang) {
        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
        Intent refresh = new Intent(getContext(), MainActivity.class);
        startActivity(refresh);
        getActivity().finish();
    }

    @Override public void onDestroy() {
        settingsPresenter.onDestroy();
        super.onDestroy();
    }

    @Override
    public void showDialogDeleteAllComics() {
        AlertDialog.Builder builder = Functions.getModal(R.string.atention,R.string.confirmdelete, getActivity());
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                settingsPresenter.deleteAllComics();

            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();

            }
        });

        builder.show();
    }

    @Override
    public void showDialogsComicsDeletedSuccess() {
        Functions.getModal(R.string.comicsDeletedOk,getContext()).show();
    }

    @Override
    public void stopRefreshing() {
        onConnectionFinished();
    }

    @Override public void onPreStartConnection(){
        super.onPreStartConnection();
    }

    @Override
    public void showDialogGeneralError() {
        AlertDialog.Builder errorRestoreAlert = Functions.getModalError(getActivity());
        errorRestoreAlert.show();
    }
}



