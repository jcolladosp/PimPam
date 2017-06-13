package jcollado.pw.pimpam.controller;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.storage.FirebaseStorage;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jcollado.pw.pimpam.R;
import jcollado.pw.pimpam.model.Comic;
import jcollado.pw.pimpam.model.Database;
import jcollado.pw.pimpam.model.FactoryComic;
import jcollado.pw.pimpam.model.FactorySerie;
import jcollado.pw.pimpam.model.Serie;
import jcollado.pw.pimpam.utils.BaseFragment;
import jcollado.pw.pimpam.utils.CameraUtils;
import jcollado.pw.pimpam.utils.FirebaseModule;
import jcollado.pw.pimpam.utils.Functions;
import jcollado.pw.pimpam.utils.UploadImageListener;
import jcollado.pw.pimpam.widgets.SquareImageView;


public class AddComicFragment extends BaseFragment implements UploadImageListener{

    public static final int PLACE_IN_DRAWER = 2;
    private static  AddComicFragment fragment;

    @BindView(R.id.nameED)
    EditText nameED;
    @BindView(R.id.anyoED)
    EditText anyoED;
    @BindView(R.id.editorialED)
    EditText editorialED;
    @BindView(R.id.numeroED)
    EditText numeroED;

    @BindView(R.id.comicIV)
    SquareImageView comicIV;
    @BindView(R.id.toolbar2)
    Toolbar toolbar;
    @BindView(R.id.serieAC)
    AutoCompleteTextView serieAC;

    FirebaseStorage storage;
    String imageLink;
    String imageurl;
    static Comic comic;
    static Serie serie;
    public boolean imageChanged = false;



    public AddComicFragment() {
        // Required empty public constructor
    }


    public static AddComicFragment newInstance() {
        fragment = new AddComicFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_add_comic, container, false);
        ButterKnife.bind(this, view);

        prepareSpinner();
        prepareToolbar();

        storage = FirebaseStorage.getInstance();
        return view ;

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
    public void profileIVfromURI(String url) {
        Uri uri = Uri.parse(url);
        comicIV.setImageURI(uri);
        comicIV.buildDrawingCache();
        Bitmap bitmap = comicIV.getDrawingCache();
        imageurl = Functions.saveToInternalStorage(bitmap,"profileimage",getContext());
        imageChanged = true;
    }

    private void onItemSerieACTVListener(){
        serieAC.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick (AdapterView<?> parent, View view, int position, long id) {
                Serie serie = (Database.getInstance().getSerieByName(serieAC.getText().toString()));
               anyoED.setText(serie.getYear());
                editorialED.setText(serie.getEditorial());
            }
        });
    }

    @OnClick(R.id.addFab) void submit() {
        hideKeyboard();
        if(isFieldsCompleted()){
            onPreStartConnection();
            serie = Database.getInstance().getSerieByName(serieAC.getText().toString());
            if(serie == null) {
                serie = FactorySerie.createSerie(serieAC.getText().toString(),anyoED.getText().toString() , editorialED.getText().toString());
            }
            comic = FactoryComic.createComic(nameED.getText().toString(),editorialED.getText().toString(),"",numeroED.getText().toString(),anyoED.getText().toString(),serie,false);


            if(imageChanged) {
                FirebaseModule.getInstance().uploadImageToFirebase(imageurl, java.util.UUID.randomUUID().toString(),this);
            }
            else{
                imageLink = "https://firebasestorage.googleapis.com/v0/b/pim-pam-comics-ff8d4.appspot.com/o/images?alt=media&token=e8ea32a5-a787-4a03-8bee-c4143b257844";
                onImageUploaded(imageLink);
            }


        }
    }

    private boolean isFieldsCompleted(){
        boolean completed = true;
        if (serieAC.getText().toString().equals("")) {
            serieAC.setError(getString(R.string.empty_name_comic));
            completed = false;
        }
        if (editorialED.getText().toString().equals("")) {
            editorialED.setError(getString(R.string.empty_editorial_comic));
            completed = false;
        }
        if (numeroED.getText().length() == 0) {
            numeroED.setError(getString(R.string.empty_numero_comic));
            completed = false;
        }
        return completed;
    }

    @Override
    public void onImageUploaded(String imageURL){
        Database.getInstance().serieToDatabase(serie);
        comic.setImageURL(imageURL);
        serie.addComicToSerie(comic);
        Database.getInstance().comicToDatabase(comic,this);
    }

    @Override
    public void comicUploaded(){
        onConnectionFinished();
        new Toast(getContext()).makeText(getContext(),getString(R.string.comic_addded_succesfull), Toast.LENGTH_SHORT).show();
        CollectionFragment collection = CollectionFragment.newInstance();
         openFragment(collection,collection.PLACE_IN_DRAWER,"collection");
    }


    @OnClick(R.id.comicIV)
    public void changeImage() {
        CameraUtils.changeImage(getActivity(),getContext(),fragment);

    }

    private void prepareSpinner(){

        ArrayAdapter adapter = new ArrayAdapter(getActivity(),android.R.layout.simple_list_item_1,Database.getInstance().getSeriesNameArray());

        serieAC.setAdapter(adapter);
        serieAC.setThreshold(1);
        onItemSerieACTVListener();

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

        toolbar.setTitle(getString(R.string.addComic));
    }


    @Override
    public void onUploadImageSuccess(String url) {
        onImageUploaded(url);
    }

    @Override
    public void onUploadImageError() {

    }
}


