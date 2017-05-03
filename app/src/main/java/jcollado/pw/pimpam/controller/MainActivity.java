package jcollado.pw.pimpam.controller;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.util.AbstractDrawerImageLoader;
import com.mikepenz.materialdrawer.util.DrawerImageLoader;
import com.mikepenz.materialdrawer.util.DrawerUIUtils;

import butterknife.OnClick;
import jcollado.pw.pimpam.R;
import jcollado.pw.pimpam.utils.BaseActivity;
import jcollado.pw.pimpam.utils.BaseFragment;
import jcollado.pw.pimpam.utils.Functions;
import jcollado.pw.pimpam.utils.Singleton;

public class MainActivity extends BaseActivity implements ViewComicFragment.OnFragmentInteractionListener,AddComicFragment.OnFragmentInteractionListener, CollectionFragment.OnFragmentInteractionListener , Barcode_Fragment.OnFragmentInteractionListener
        {
    public static Drawer result = null;
    Toolbar toolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setNavigationDrawer();
        Singleton.getInstance().getFirebaseModule().setConnectionDatabase();



    }
    public static void openDrawer(){
        result.openDrawer();




    }

    private void setNavigationDrawer(){

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        String displayName = Singleton.getInstance().getFirebaseModule().getmAuth().getCurrentUser().getDisplayName();
        String userEmail = Singleton.getInstance().getFirebaseModule().getmAuth().getCurrentUser().getEmail();
        Uri icon = Singleton.getInstance().getFirebaseModule().getmAuth().getCurrentUser().getPhotoUrl();


        // Create the AccountHeader
        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.header)
                .withSelectionListEnabledForSingleProfile(false)
                .withOnlyMainProfileImageVisible(true)
                .addProfiles(
                        new ProfileDrawerItem().withName(displayName).withEmail(userEmail).withIcon(icon)
                )

                .build();
        PrimaryDrawerItem item1 = new PrimaryDrawerItem().withIdentifier(1).withName(getString(R.string.seeCollection)).withIcon(FontAwesome.Icon.faw_book);
        SecondaryDrawerItem item2 = new SecondaryDrawerItem().withIdentifier(2).withName(getString(R.string.addComic)).withIcon(FontAwesome.Icon.faw_plus_circle);
        SecondaryDrawerItem barcode = new SecondaryDrawerItem().withIdentifier(4).withName("Barcode").withIcon(FontAwesome.Icon.faw_barcode);
        SecondaryDrawerItem ajustes = new SecondaryDrawerItem().withIdentifier(3).withName(getString(R.string.settings)).withIcon(FontAwesome.Icon.faw_cog);



        //create the drawer and remember the `Drawer` result object
        result = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withAccountHeader(headerResult)
                .addDrawerItems(item1,item2,barcode, new DividerDrawerItem(),ajustes)
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
                            getSupportActionBar().hide();


                        }

                        if (position == 3) {
                            fragment = Barcode_Fragment.newInstance();
                            getSupportActionBar().hide();
                        }

                        if (position == 5){
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



            @Override
            public void onBackPressed() {
                Intent a = new Intent(Intent.ACTION_MAIN);
                a.addCategory(Intent.CATEGORY_HOME);
                a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(a);

            }


        }
