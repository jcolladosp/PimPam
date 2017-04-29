package jcollado.pw.pimpam.controller;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jcollado.pw.pimpam.R;
import jcollado.pw.pimpam.model.Comic;
import jcollado.pw.pimpam.model.Factory;
import jcollado.pw.pimpam.model.Serie;
import jcollado.pw.pimpam.utils.BaseFragment;
import jcollado.pw.pimpam.utils.Functions;
import jcollado.pw.pimpam.utils.Singleton;
import jcollado.pw.pimpam.widgets.SquareImageView;


public class AddComicFragment extends BaseFragment {

    private static final int GALLERY_PICK = 1;
    private static final int CAMERA_PICK = 2;
    private boolean imageChanged = false;
    private Bitmap imageBitmap;

    @BindView(R.id.nameED)
    EditText nameED;

    @BindView(R.id.editorialED)
    TextView editorialED;

    @BindView(R.id.comicIV)
    SquareImageView comicIV;
    @BindView(R.id.toolbar2)
    Toolbar toolbar;
    @BindView(R.id.serieAC)
    AutoCompleteTextView serieAC;

    FirebaseStorage storage;
    private Uri uri;
    ArrayList<String> spinnerList;
    private OnFragmentInteractionListener mListener;
    String[] languages={"Android ","java","IOS","SQL","JDBC","Web services"};

    public AddComicFragment() {
        // Required empty public constructor
    }


    public static AddComicFragment newInstance() {
        AddComicFragment fragment = new AddComicFragment();

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


        //Singleton.getInstance().getDatabase().getSeries();
        // for(String s :  Singleton.getDatabase().getAllSeriesName());

        storage = FirebaseStorage.getInstance();
        return view ;

    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            imageChanged = true;
            if (requestCode == CAMERA_PICK) {
                Bundle extras = data.getExtras();
                imageBitmap = (Bitmap) extras.get("data");
                comicIV.setImageBitmap(imageBitmap);
            } else {
                comicIV.setImageURI(data.getData());

            }
        }
    }



    @OnClick(R.id.addFab) void submit() {
        hideKeyboard();
        if (nameED.getText().toString().equals("")) {
            nameED.setError(getString(R.string.empty_name_comic));

        }
        if (editorialED.getText().toString().equals("")) {
            editorialED.setError(getString(R.string.empty_editorial_comic));

        } else {

            comicIV.setDrawingCacheEnabled(true);
            comicIV.buildDrawingCache();
            Bitmap bitmap = comicIV.getDrawingCache();

            Comic comicToAdd = Factory.createComic(nameED.getText().toString(),editorialED.getText().toString(),"",1,1,null);

            Serie serie = Singleton.getInstance().getDatabase().getSerieByName(serieAC.getText().toString());
            nameED.setText(Integer.toString(Singleton.getInstance().getDatabase().getSeries().get(0).getVolumenesName().size()));
            if(serie == null) {
                serie = Factory.createSerie(serieAC.getText().toString(), 0, 0);
            }
            serie.addComicToSerie(comicToAdd);
            Singleton.getInstance().getDatabase().serieToDatabase(serie);

            comicToAdd.setSerie(serie);

            Singleton.getInstance().getDatabase().comicToDatabase(comicToAdd);


            //Singleton.getFirebaseModule().uploadBitmap(bitmap,nameED.getText().toString(),this);
            //Factory.createComic(nameED.getText().toString(), editorialED.getText().toString(), uri.toString(), 0, 0, /*serie*/ null);

        }
    }

    @OnClick(R.id.comicIV)
    public void changeImage() {

        AlertDialog.Builder builder = Functions.getModal(R.string.warning_modal_title,R.string.news_image_message, getActivity());
        builder.setPositiveButton(R.string.news_image_camera, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_PICK);
            }
        });
        builder.setNeutralButton(R.string.news_image_gallery, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto, GALLERY_PICK);
            }
        });
        builder.show();

    }
    private void stopRefreshing() {

        onConnectionFinished();
    }
    private void prepareSpinner(){

        ArrayAdapter adapter = new ArrayAdapter(getActivity(),android.R.layout.simple_list_item_1,languages);

        serieAC.setAdapter(adapter);
        serieAC.setThreshold(1);
    }
    private void prepareToolbar(){
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(false);

        toolbar.setTitle(getString(R.string.addComic));
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

}
