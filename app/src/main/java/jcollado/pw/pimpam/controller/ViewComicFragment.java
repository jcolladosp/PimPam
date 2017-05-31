package jcollado.pw.pimpam.controller;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.app.AlertDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.varunest.sparkbutton.SparkButton;
import com.varunest.sparkbutton.SparkEventListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jcollado.pw.pimpam.R;
import jcollado.pw.pimpam.model.Comic;
import jcollado.pw.pimpam.model.Database;
import jcollado.pw.pimpam.model.FactoryComic;
import jcollado.pw.pimpam.model.Serie;
import jcollado.pw.pimpam.utils.BaseFragment;
import jcollado.pw.pimpam.utils.FirebaseModule;
import jcollado.pw.pimpam.utils.Functions;
import jcollado.pw.pimpam.widgets.SquareImageView;


public class ViewComicFragment extends BaseFragment {

    private static final int GALLERY_PICK = 1;
    private static final int CAMERA_PICK = 2;
    private boolean imageChanged = false;
    private     MenuItem actionDelete;

    @BindView(R.id.nameED)
    EditText nameED;
    @BindView(R.id.anyoED)
    EditText anyoED;
    @BindView(R.id.editorialED)
    TextView editorialED;
    @BindView(R.id.editFab)
    FloatingActionButton editFab;
    @BindView(R.id.showLayout)
    RelativeLayout showLayout;
    @BindView(R.id.editLayout)
    RelativeLayout editLayout;
    @BindView(R.id.tvEditorial)
    TextView tvEditorial;
    @BindView(R.id.tvFav)
    TextView tvFab;
    @BindView(R.id.tvYear)
    TextView tvYear;
    @BindView(R.id.tvName)
    TextView tvName;
    @BindView(R.id.star_button1)
    SparkButton starButton;

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.comicIV)
    SquareImageView comicIV;
    private boolean editingComic = false;
    private boolean isFavChange = false;

    private OnFragmentInteractionListener mListener;
    private static Comic comic;
    private static ViewComicFragment fragment;

    public ViewComicFragment() {
        // Required empty public constructor
    }


    public static ViewComicFragment newInstance() {
        fragment = new ViewComicFragment();
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
        prepareToolbar();
        setHasOptionsMenu(true);
        listenerFavButton();
        showMode();

        toolbar.setTitle(comic.getDisplayName());
        Glide.with(getContext()).load(comic.getImageURL()).placeholder(R.drawable.placeholder).into(comicIV);


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
           if(editingComic){
               Functions.changeImage(getActivity(),getContext(),fragment);

           }
    }
    public void setComic(Comic comic){
        this.comic = comic;

    }
    @OnClick(R.id.editFab)
    public void onDone(){
        if(!editingComic) {
            editingComic = true;
            //ditMode(comic.getFavourite());
            editMode();
        }
        else{
            editingComic = false;
            actionDelete.setVisible(false);
            editFab.setImageResource(R.drawable.ic_edit);
            if(imageChanged){
                comicIV.setDrawingCacheEnabled(true);
                comicIV.buildDrawingCache();
                Bitmap bitmap = comicIV.getDrawingCache();
                String imageURL = FirebaseModule.getInstance().uploadBitmap(bitmap,java.util.UUID.randomUUID().toString(),this,null);

            }
            else{
                uploadComicToDatabase(null);

            }


            showMode();

        }
    }

    @Override
    public void onImageUploaded(String imageURL){
        uploadComicToDatabase(imageURL);

    }

    private void uploadComicToDatabase(String imageurl){
        comic.setYear(anyoED.getText().toString());
        comic.setName(nameED.getText().toString());
        comic.setEditorial(editorialED.getText().toString());
        if(imageurl != null){
            comic.setImageURL(imageurl);
        }


        Database.getInstance().comicToDatabase(comic,this);
    }

    private void editMode(){
        editingComic = true;
        actionDelete.setVisible(true);
        editFab.setImageResource(R.drawable.ic_done);
        editLayout.setVisibility(View.VISIBLE);
        showLayout.setVisibility(View.GONE);

        toolbar.setTitle("Editando "+comic.getDisplayName());

        nameED.setText(comic.getName());
        anyoED.setText(comic.getYear());
        editorialED.setText(comic.getEditorial());


    }
    private void showMode(){
        editLayout.setVisibility(View.GONE);
        showLayout.setVisibility(View.VISIBLE);
        toolbar.setTitle(comic.getDisplayName());
        tvEditorial.setText(comic.getEditorial());
        tvYear.setText(comic.getYear());
        tvName.setText(comic.getName());
        isFavChange = comic.getFavourite();
        if(comic.getFavourite()){
            starButton.setChecked(true);
            tvFab.setText(getString(R.string.comicInFav));

        }
        else{
            starButton.setChecked(false);
            tvFab.setText(getString(R.string.comicNotInFav));
        }

    }
    @Override
    public void comicUploaded(){
        onConnectionFinished();
        new Toast(getContext()).makeText(getContext(),getString(R.string.comicUpdated), Toast.LENGTH_SHORT).show();
       showMode();
    }


    private void prepareToolbar(){
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(false);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container, CollectionFragment.newInstance(),"")
                        .commit();
            }
        });

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        getActivity().getMenuInflater().inflate( R.menu.viewcomic_menu, menu);
        super.onCreateOptionsMenu(menu,inflater);
        actionDelete = menu.findItem(R.id.action_delete);
        actionDelete.setVisible(false);

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
               case R.id.action_delete:

                   mostrarCargando();
                   Database.getInstance().deleteComicFromDatabase(comic,this);
                   return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public void mostrarCargando(){
        onPreStartConnection(getString(R.string.loading));
    }

    public void comicDeleted(){
        onConnectionFinished();
        new Toast(getContext()).makeText(getContext(),getString(R.string.comicDeleted), Toast.LENGTH_SHORT).show();
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, CollectionFragment.newInstance(),"")
                .commit();
    }



    private void listenerFavButton(){
        final BaseFragment fragment = this;
        starButton.setEventListener(new SparkEventListener(){
            @Override
            public void onEvent(ImageView button, boolean buttonState) {

                if (buttonState) {
                    comic.setFavourite(true);
                    tvFab.setText(getString(R.string.comicInFav));
                    Database.getInstance().comicToDatabase(comic,fragment);
                } else {
                    comic.setFavourite(false);
                    tvFab.setText(getString(R.string.comicNotInFav));
                    Database.getInstance().comicToDatabase(comic,fragment);
                }
            }

            @Override
            public void onEventAnimationEnd(ImageView button, boolean buttonState) {

            }

            @Override
            public void onEventAnimationStart(ImageView button, boolean buttonState) {

            }
        });

    }
}
