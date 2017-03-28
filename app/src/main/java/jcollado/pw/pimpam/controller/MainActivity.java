package jcollado.pw.pimpam.controller;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import jcollado.pw.pimpam.R;
import jcollado.pw.pimpam.utils.BaseFragment;

public class MainActivity extends AppCompatActivity   implements ViewComicFragment.OnFragmentInteractionListener,AddComicFragment.OnFragmentInteractionListener, CollectionFragment.OnFragmentInteractionListener
        {
    private Drawer result = null;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setNavigationDrawer();

    }

    private void setNavigationDrawer(){


        getSupportActionBar().setDisplayHomeAsUpEnabled(false);


        // Create the AccountHeader
        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.header)
                .withSelectionListEnabledForSingleProfile(false)
                .withOnlyMainProfileImageVisible(true)
                .addProfiles(
                        new ProfileDrawerItem().withName(getString(R.string.placeholderName)).withIcon(getResources().getDrawable(R.drawable.profile))
                )

                .build();
        PrimaryDrawerItem item1 = new PrimaryDrawerItem().withIdentifier(1).withName(getString(R.string.seeCollection)).withIcon(FontAwesome.Icon.faw_book);
        SecondaryDrawerItem item2 = new SecondaryDrawerItem().withIdentifier(2).withName(getString(R.string.addComic)).withIcon(FontAwesome.Icon.faw_plus_circle);
        SecondaryDrawerItem ajustes = new SecondaryDrawerItem().withIdentifier(3).withName(getString(R.string.settings)).withIcon(FontAwesome.Icon.faw_cog);



        //create the drawer and remember the `Drawer` result object
        result = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withAccountHeader(headerResult)
                .addDrawerItems(item1,item2, new DividerDrawerItem(),ajustes)
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {

                        BaseFragment fragment = ViewComicFragment.newInstance();
                        if (position == 1){
                            fragment = CollectionFragment.newInstance();
                            toolbar.setTitle(getString(R.string.seeCollection));
                            getSupportActionBar().hide();

                        }

                        if (position == 2){
                            fragment = AddComicFragment.newInstance();
                            toolbar.setTitle(getString(R.string.addComic));
                            getSupportActionBar().hide();

                        }
                        if (position == 4){
                            fragment = ViewComicFragment.newInstance();
                            getSupportActionBar().hide();
                        }

                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.container, fragment,"")
                                .commit();
                        result.closeDrawer();
                        return true;
                    }
                })
                .build();

    }

            @Override
            public void onFragmentInteraction(Uri uri) {

            }
        }
