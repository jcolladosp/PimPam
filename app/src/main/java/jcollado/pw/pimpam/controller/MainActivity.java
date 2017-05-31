package jcollado.pw.pimpam.controller;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import com.mikepenz.materialdrawer.Drawer;

import butterknife.BindView;
import butterknife.ButterKnife;
import jcollado.pw.pimpam.R;
import jcollado.pw.pimpam.model.Database;
import jcollado.pw.pimpam.utils.BaseActivity;
import jcollado.pw.pimpam.utils.BaseFragment;
import jcollado.pw.pimpam.utils.FirebaseModule;
import jcollado.pw.pimpam.utils.NavigationDrawer;


public class MainActivity extends BaseActivity implements ViewComicFragment.OnFragmentInteractionListener,AddComicFragment.OnFragmentInteractionListener {
    @BindView(R.id.toolbar)
    Toolbar toolbar;


    private static Drawer navigationDrawer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        setNavigationDrawer();
        FirebaseModule.getInstance().setConnectionDatabase();

        CollectionFragment collectionFragment = CollectionFragment.newInstance();
        Database.getInstance().setFragment(collectionFragment);
        toolbar.setTitle(getString(R.string.seeCollection));
        getSupportActionBar().hide();
        openFragment(collectionFragment,"");


    }

    public static void openDrawer() {
        navigationDrawer.openDrawer();
    }


    private void setNavigationDrawer() {
        navigationDrawer = NavigationDrawer.getInstance(this, toolbar).getDrawerBuilder();

    }


    @Override
    public void onFragmentInteraction(Uri uri) {
    }

    public void openFragment(BaseFragment fragment,String tag) {

        getSupportFragmentManager()
                .beginTransaction()
                .addToBackStack(tag)
                .replace(R.id.container, fragment, "")
                .commit();
        if (navigationDrawer != null) {
            navigationDrawer.closeDrawer();
        }

    }

    @Override
    public void onBackPressed() {

        if (navigationDrawer.getCurrentSelectedPosition() != 1) {
            FragmentManager fm = getSupportFragmentManager();
            fm.popBackStack ("add", FragmentManager.POP_BACK_STACK_INCLUSIVE);
            fm.popBackStack ("settings", FragmentManager.POP_BACK_STACK_INCLUSIVE);

            //openDrawer();
        } else {
            Intent a = new Intent(Intent.ACTION_MAIN);
            a.addCategory(Intent.CATEGORY_HOME);
            a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(a);
        }

    }

    public void mostrarCargando() {
        onPreStartConnection(getString(R.string.loading));
    }

    public static Drawer getResult() {
        return navigationDrawer;
    }





}
