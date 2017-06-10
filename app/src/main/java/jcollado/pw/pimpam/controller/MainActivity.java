package jcollado.pw.pimpam.controller;

import android.os.Bundle;

import com.mikepenz.materialdrawer.Drawer;

import butterknife.ButterKnife;
import jcollado.pw.pimpam.R;
import jcollado.pw.pimpam.model.Database;
import jcollado.pw.pimpam.utils.BaseActivity;
import jcollado.pw.pimpam.utils.BaseFragment;
import jcollado.pw.pimpam.utils.FirebaseModule;
import jcollado.pw.pimpam.utils.Functions;
import jcollado.pw.pimpam.utils.NavigationDrawer;


public class MainActivity extends BaseActivity {



    private static Drawer navigationDrawer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);


        setNavigationDrawer();
        FirebaseModule.getInstance().setConnectionDatabase();

        CollectionFragment collectionFragment = CollectionFragment.newInstance();
        openFragment(collectionFragment,"");

        Database.getInstance().setFragment(collectionFragment);


    }

    public static void openDrawer() {
        navigationDrawer.openDrawer();
    }


    private void setNavigationDrawer() {
        navigationDrawer = NavigationDrawer.getInstance(this).getDrawerBuilder();

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
          getSupportFragmentManager().popBackStack();

            //openDrawer();
        } else {
            Functions.closeApp(this);
        }

    }


    public static Drawer getResult() {
        return navigationDrawer;
    }



}
