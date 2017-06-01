package jcollado.pw.pimpam.controller;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.InflateException;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import jcollado.pw.pimpam.R;
import jcollado.pw.pimpam.model.Database;
import jcollado.pw.pimpam.utils.BaseFragment;
import jcollado.pw.pimpam.utils.FirebaseModule;
import jcollado.pw.pimpam.utils.Functions;


public class SettingsFragment  extends BaseFragment {


    @BindView(R.id.profile_edit)
    CircleImageView profile_edit;
    @BindView(R.id.toolbar) Toolbar toolbar;
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
    private static final int GALLERY_PICK = 1;
    private static final int CAMERA_PICK = 2;

    public boolean imageChanged = false;
    private static View view;
    private FirebaseUser user;
    private String imageurl;
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
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_settings, container, false);
        ButterKnife.bind(this, view);
        Glide.with(this).load(FirebaseModule.getInstance().getCurrentUser().getPhotoUrl()).placeholder(R.drawable.placeholder).dontAnimate().into(profile_edit);
        prepareToolbar();
        setHasOptionsMenu(true);
         checkLanguageButton();
        user = FirebaseModule.getInstance().getCurrentUser();
        imageurl = user.getPhotoUrl().toString();
        nameED.setText(user.getDisplayName());


        return view;
    }

    @OnClick(R.id.profile_edit)
    void onProfileImage(){
        Functions.changeImage(getActivity(),getContext(),this);
    }

    @OnClick(R.id.eraseComicsBT)
    void onEraseComics(){
        AlertDialog.Builder builder = Functions.getModal(R.string.atention,R.string.confirmdelete, getActivity());
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Database.getInstance().deleteAllData();
                Functions.getModal(R.string.comicsDeletedOk,getContext()).show();
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
    public void onImageUploaded(String filename){

        imageurl = filename;
        addUserInfo();

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
                if(imageChanged){
                    changeProfileWithImage();
                }
                else{
                    addUserInfo();
                }
              //  getActivity().getSupportFragmentManager().popBackStack();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void checkLanguageButton(){
        String locale = getResources().getConfiguration().locale.toString();


            if(locale.contains("en")) {
                englishRB.setChecked(true);
            }

            else if(locale.contains("ca")) {
                valenciaRB.setChecked(true);
            }
            else {
                spanishRB.setChecked(true);
            }

        }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            imageChanged = true;
            if (requestCode == CAMERA_PICK) {
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                profile_edit.setImageBitmap(imageBitmap);
            }  if (requestCode == GALLERY_PICK) {
                profile_edit.setImageURI(data.getData());

            }
        }
    }

    private void changeProfileWithImage(){

            profile_edit.setDrawingCacheEnabled(true);
            profile_edit.buildDrawingCache();
            Bitmap bitmap = profile_edit.getDrawingCache();
            FirebaseModule.getInstance().uploadBitmap(bitmap,java.util.UUID.randomUUID().toString(),this,null);

        }
    private void addUserInfo(){
        onPreStartConnection(getString(R.string.loading));
        Uri picUri;

            picUri = Uri.parse(imageurl);

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

                                getLocaleSelected();
                                onConnectionFinished();
                        }
                    }
                });
    }

    private void getLocaleSelected(){
        int radioButtonID = buttonsLanguage.getCheckedRadioButtonId();
        View radioButton = buttonsLanguage.findViewById(radioButtonID);
        int idx = buttonsLanguage.indexOfChild(radioButton);
        switch (idx) {
            case 0:
                setLocale("es");
                break;
            case 1:
                setLocale("en");
                break;
            case 2:
                setLocale("ca");
                break;
        }
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




}



