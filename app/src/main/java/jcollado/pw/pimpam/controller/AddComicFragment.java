package jcollado.pw.pimpam.controller;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jcollado.pw.pimpam.R;
import jcollado.pw.pimpam.model.Factory;
import jcollado.pw.pimpam.utils.BaseFragment;
import jcollado.pw.pimpam.utils.Functions;
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
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.serieSpinner)
    Spinner serieSpinner;
    FirebaseStorage storage;
    private Uri uri;

    private OnFragmentInteractionListener mListener;

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
        Singleton.getInstance().getDatabase().getSeries();
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(true);
        toolbar.setTitle("Añadir comic");
        storage = FirebaseStorage.getInstance();
        ArrayList<String> spinnerList = new ArrayList<>();
        spinnerList.add(getString(R.string.newSerie));
        for(String s :  Singleton.getDatabase().getAllSeriesName());
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, spinnerList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        serieSpinner.setAdapter(dataAdapter);
        // Inflate the layout for this fragment
        return view ;

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


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @OnClick(R.id.addFab) void submit() {
        hideKeyboard();
        if (nameED.getText().toString().equals("")) {
            nameED.setError(getString(R.string.empty_name_comic));

        }
        if (editorialED.getText().toString().equals("")) {
            editorialED.setError(getString(R.string.empty_editorial_comic));

        } else {
            onPreStartConnection(getString(R.string.loading));

            StorageReference storageRef = storage.getReferenceFromUrl("gs://pim-pam-comics-ff8d4.appspot.com");
            StorageReference mountainImagesRef = storageRef.child("images/"+nameED.getText().toString());


            comicIV.setDrawingCacheEnabled(true);
            comicIV.buildDrawingCache();
            Bitmap bitmap = comicIV.getDrawingCache();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] data = baos.toByteArray();


            UploadTask uploadTask = mountainImagesRef.putBytes(data);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Log.e("fire", exception.toString());
                    // Handle unsuccessful uploads
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                @SuppressWarnings("VisibleForTests")
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                    uri = taskSnapshot.getDownloadUrl();

                    Factory.createComic(nameED.getText().toString(), editorialED.getText().toString(), uri.toString(), 0, 0, /*serie*/ null);
                    stopRefreshing();
                    Toast.makeText(getActivity(), "Comic añadido correctamente", Toast.LENGTH_SHORT).show();                }
            });


            // Singleton.getInstance().getDatabase().addNewComic(Factory.createComic(nameField.getText().toString(), descriptionText.getText().toString(), ""));
            //Singleton.getFirebaseModule().uploadFile(bitmapToFile(imageBitmap));
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


}
