package jcollado.pw.pimpam.controller;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jcollado.pw.pimpam.R;
import jcollado.pw.pimpam.utils.BaseFragment;
import jcollado.pw.pimpam.utils.Functions;
import jcollado.pw.pimpam.widgets.SquareImageView;


public class ViewComicFragment extends BaseFragment {

    private static final int GALLERY_PICK = 1;
    private static final int CAMERA_PICK = 2;
    private boolean imageChanged = false;

    @BindView(R.id.nameED)
    EditText nameED;
    @BindView(R.id.anyoED)
    EditText anyoED;
    @BindView(R.id.editorialED)
    TextView editorialED;
    @BindView(R.id.numeroED)
    EditText numeroED;


    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.comicIV)
    SquareImageView comicIV;

    private OnFragmentInteractionListener mListener;

    public ViewComicFragment() {
        // Required empty public constructor
    }


    public static ViewComicFragment newInstance() {
        ViewComicFragment fragment = new ViewComicFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_viewcomic, container, false);
        ButterKnife.bind(this, view);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(true);
        toolbar.setTitle("Hola");



        return view;
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
                Bitmap imageBitmap = (Bitmap) extras.get("data");
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

    @OnClick(R.id.doneFab)
    public void onDone(){
        MainActivity.openDrawer();
    }
}
