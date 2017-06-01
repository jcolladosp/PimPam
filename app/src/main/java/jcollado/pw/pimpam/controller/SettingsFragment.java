package jcollado.pw.pimpam.controller;

import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import jcollado.pw.pimpam.R;
import jcollado.pw.pimpam.model.Database;
import jcollado.pw.pimpam.utils.BaseFragment;
import jcollado.pw.pimpam.utils.FirebaseModule;


public class SettingsFragment  extends BaseFragment {

    private static View view;
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


    public SettingsFragment() {
        // Required empty public constructor
    }

    public static SettingsFragment newInstance() {
        SettingsFragment fragment = new SettingsFragment();

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


        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

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
    public void onDetach() {
        super.onDetach();
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
                Log.i("xd","xd");
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

}


